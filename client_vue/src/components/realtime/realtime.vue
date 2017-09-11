<template>
  <div class="realtime">
    <md-card>
      <md-card-header>
        <md-card-header-text>
          <div class="md-title">{{this.sensorDTO.name}}</div>
        </md-card-header-text>
        <md-card-media md-small>
          <img src='../../assets/temperatura.png'></img>
        </md-card-media>
      </md-card-header>
      <md-card-content class="value">
        <span class="md-display-1">{{lastValue}}</span>
      </md-card-content>
    </md-card>

  </div>
</template>
<script>
export default {
  name: 'realtime',
  props: {
    sensorDTO: Object
  },
  data() {
    const topic = `/device/${this.sensorDTO.id}`;
    return {
      lastValue: 0,
      topic,
      subscriptionId: null
    }
  },
  created() {
    this.connect();
    this.subscribe();
  },
  methods: {
    connect: function() {
      const options = {
        method: 'POST',
        headers: {                
                'Content-Type': 'application/json'
        },
        mode: 'cors',
        body: JSON.stringify({          
            id: this.sensorDTO.id,
            attributes: [this.sensorDTO.name]          
        })
      }
      const url = `${process.env.SERVER_URL}/subscribe`;
      fetch(url, options)
        .then(response => response.json())
        .then(data => {
            this.subscriptionId = data.subscribeResponse.subscriptionId;
        });
    },
    subscribe: function() {
      this.$options.sockets[this.topic] = (msg) => {
        const data = JSON.parse(msg.data);
        this.lastValue = data.value;
      }
    }
  },
  sockets: {
    
  },
}
</script>
<style scoped>
.realtime {
  width: 15%;
}

.value {
  text-align: center;
}
</style>
