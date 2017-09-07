var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  SERVER_URL: '"http://localhost:3001"',
  SERVER_SOCKET_URL: '"http://localhost:9092"',  
})
