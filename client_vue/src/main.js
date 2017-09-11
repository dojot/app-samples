// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';

import VueMaterial from 'vue-material';
import 'vue-material/dist/vue-material.css';
import VueSocketio from 'vue-socket.io';

Vue.config.productionTip = false;
Vue.use(VueMaterial);
Vue.use(VueSocketio, process.env.SERVER_SOCKET_URL);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<App/>',
  components: { App }  
})
