const express = require('express');
const bodyParser = require('body-parser');

const app = express();
const http = require('http').Server(app);
const io = require('socket.io')(http);

const config = require('../config.json');
const localAddress = require('./utils')();

const corsMidleware = (req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
}

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(corsMidleware);

app.use((err, req, res, next) => {
    if (err.status !== 404) {
        return next();
    }
    res.status(404);
    res.send(err.message || 'ups!');
});

const context = {
    localAddress,
    port : process.env.PORT || 3001,
    http: http,
}

const route = require('./route')(context);
app.use('/', route);

http.listen(context.port, () => {
    console.log('Express server listening on port ' + context.port);
});

module.exports = app;