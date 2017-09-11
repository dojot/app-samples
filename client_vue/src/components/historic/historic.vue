<template>
    <div>
        <canvas id="historic-chart" style="display: block; width: 974px; height: 350px;" width="974" height="350"></canvas>
    </div>
</template>
<script>
import moment from 'moment';
import Chart from 'chart.js';

export default {
    name: 'history',
    props: {
        historicDTO: Object
    },
    data() {
        return {
            data: null
        }
    },
    watch: {
        data: function (newData) {
            this.createGraphic(newData);
        }
    },
    created() {
        this.fetchData();
    },
    methods: {
        fetchData: function () {
            const url = `${process.env.SERVER_URL}/sth/type/${this.historicDTO.sensor.type}/id/${this.historicDTO.sensor.id}/attributes/${this.historicDTO.sensor.name}?lastN=0&dateFrom=${this.historicDTO.startDate}&dateTo=${this.historicDTO.endDate}`;
            fetch(url)
                .then(response => response.json())
                .then(response => {
                    const values = response.contextResponses[0].contextElement.attributes[0].values;
                    const attrName = response.contextResponses[0].contextElement.attributes[0].name;
                    const data = {
                        labels: [],
                        datasets: [
                            {
                                label: attrName,
                                data: [],

                                borderColor: '#EC6F69',
                                backgroundColor: '#F3B5B2',
                                fill: false
                            }
                        ]
                    };
                    this.parseValues(values).forEach((value) => {
                        data.labels.push(value.label);
                        data.datasets[0].data.push(value.value);
                    });
                    this.data = data;
                });
        },
        parseValues: function (values) {
            return values.map((point) => {
                return {
                    label: moment(new Date(point.recvTime)).format('YYYY-MM-DD HH:mm:ss'),
                    value: point.attrValue
                }
            });
        },
        createGraphic: function (data) {
            if (data) {
                const ctx = document.getElementById("historic-chart").getContext('2d');
                const config = {
                    type: 'line',
                    data,
                    options: {
                        responsive: true,                       
                        tooltips: {
                            mode: 'index',
                            intersect: false,
                        },
                        hover: {
                            mode: 'nearest',
                            intersect: true
                        },
                        scales: {
                            xAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Date'
                                }
                            }],
                            yAxes: [{
                                display: true,
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Value'
                                }
                            }]
                        }
                    }
                };
                const chart = new Chart(ctx, config);
            }
        }
    }
}
</script>