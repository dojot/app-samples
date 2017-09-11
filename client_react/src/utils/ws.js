import io from 'socket.io-client';
import config from '../config/server.json';

export default class WS {
   static connect() {        
        this.socket = io.connect(`${config.SERVER_SOCKET_URL}`);
        window.socket = this.socket;
    }
};
