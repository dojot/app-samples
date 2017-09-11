# server-node

>NodeJS Server project for IoT with CPqD ​IoT Platform

## Setup

Make sure to configure the config.json that contains the info about the platform.
```json
{
    "platformUrl":"https://your-platform-url",   
    "headers": {
        "fiware-service": "platform-user",
        "fiware-servicepath": "/"
    },
    "security": {        
        "username":"platform-user",
        "passwd":"platform-password"
    }
} 
```


``` bash
# install dependencies
npm install

# serve with hot reload at localhost:3001
npm start

# serve with hot reload at localhost:3001 avaliable to debug
npm run debug

```
References:
* Server created with [ExpressJS](http://expressjs.com/)
* Websocket created with [Socket.io](https://socket.io/)
