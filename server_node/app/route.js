const router = require('express').Router();
const request = require('request-promise-native');

const config = require('../config.json');
const localAdress = require('./utils')();

_getDeviceUrl = originalUrl => {
    return `${config.platformUrl}/device${originalUrl.replace('/device', '')}`;
};

_getSthUrl = originalUrl => {
    return `${config.platformUrl}/history/STH/v1/contextEntities${originalUrl.replace('/sth', '')}`;
}

_createRequestOptions = (url, headers, authKey) => {
    return {
        url,
        headers: {
            'fiware-service': headers['fiware-service'] || config.headers['fiware-service'],
            'fiware-servicepath': headers['fiware-servicepath'] || config.headers['fiware-servicepath'],
            'authorization': `Bearer ${authKey}`
        },
        json: true
    }
}

_getResponse = (req, res, url, authKey) => {
    const requestOptions = _createRequestOptions(url, req.headers, authKey);
    request(requestOptions)
        .then(response => res.json(response))
        .catch(error => {
            res.status(500).send(error);
        });
}

async function authorization(req, res, next) {
    if (!jwtKey) {
        const options = {
            method: 'POST',
            url: `${config.platformUrl}/auth`,
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
            },
            body: {
                username: config.security.username,
                passwd: config.security.passwd
            },
            json: true
        };
        await request(options).then(response => {
            jwtKey = response.jwt;
            next();
        });
    } else {
        next();
    }
}

let jwtKey = null;

module.exports = (context) => {

    const io = require('socket.io')(context.http);

    router.use(authorization);

    router.get('/device?/?*', (req, res) => {
        _getResponse(req, res, _getDeviceUrl(req.originalUrl), jwtKey);
    });

    router.get('/sth/*', (req, res) => {
        _getResponse(req, res, _getSthUrl(req.originalUrl), jwtKey);
    });

    router.post('/notifications', (req, res) => {
        const element = req.body.contextResponses[0].contextElement;
        const topic = `/device/${element.id}`
        const data = element.attributes.map(attr => {
            return { name: attr.name, value: attr.value }
        })[0];
        io.emit(topic, {
            data: JSON.stringify(data)
        });
        res.send('ok');
    });

    router.post('/subscribe', (req, res) => {

        const requestOptions = _createRequestOptions(`${config.platformUrl}/metric/v1/subscribeContext`, req.headers, jwtKey);
        requestOptions.method = 'POST';        
        requestOptions.body = {
                entities: [
                    {
                        type: 'device',
                        isPattern: 'false',
                        id: req.body.id
                    }
                ],
                attributes: req.body.attributes,
                reference: `http://${context.localAddress}:${context.port}/notifications`,
                duration: 'P1Y',
                notifyConditions: [
                    {
                        type: 'ONCHANGE',
                        condValues: req.body.attributes
                    }
                ]
            };
        

        request(requestOptions)
            .then(response => res.json(response))
            .catch(error => res.status(500).send(error));

    });

    router.post('/unsubscribe/:subscribeId', (req, res) => {
        const requestOptions = _createRequestOptions(`${config.platformUrl}/metric/v1/unsubscribeContext/`, req.headers, jwtKey);
        requestOptions.method = 'POST'; 
        requestOptions.body = {
            subscriptionId : req.params.subscribeId
        }
        request(requestOptions)
        .then(response => res.json(response))
        .catch(error => res.status(500).send(error));        
    });

    return router;
}