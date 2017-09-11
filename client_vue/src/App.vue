<template>
  <div>
    <div class="title">
      <span class="md-title">IoT Vue Project</span>      
    </div>
    <md-tabs>     
      <md-tab id="realtime" md-label="Realtime">
        <realtime :sensorDTO="sensorDTO"></realtime>
      </md-tab>
      <md-tab id="historic" md-label="Historic">
        <historic :historicDTO="historicDTO"></historic>
      </md-tab>
    </md-tabs>
  </div>
</template>

<script>
import moment from 'moment';

import Realtime from './components/realtime/realtime'
import Historic from './components/historic/historic'


export default {
  name: 'app',
  data() {
    const sensorDTO = {
        id: '9670',
        type: 'device',
        name: 'temperature'
    }
    const historicDTO = {
        sensor: sensorDTO,
        startDate: moment().subtract(20, 'days'),
        endDate: moment()
    }    
    return {
      sensorDTO,
      historicDTO     
    }
  },
  components: {
    Realtime,
    Historic
  }
}
</script>

<style>
.title {
  text-align: center;
}
</style>

