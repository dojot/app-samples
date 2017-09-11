import React, { Component } from 'react';

import moment from 'moment';
import { Line } from 'react-chartjs-2';

import STHRepository from '../../utils/sth-repository';

export default class EntityHistoricalChart extends Component {

    constructor(props) {
        super(props);
        this.sthRepository = new STHRepository();

        this.state = {
            startDate: moment().subtract(10, 'days'),
            endDate: moment(),
            sthData: []
        };
    };

    parseValues = (values) => {
        return values.map((point) => {
            return {
                label: moment(new Date(point.recvTime)).format('YYYY-MM-DD HH:mm:ss'),
                value: point.attrValue
            }
        });
    };

    componentWillMount() {

        const sthSearchDTO = {
            deviceId: this.props.deviceId,
            type: this.props.type,
            sensor: this.props.sensor,
            startDate: this.state.startDate.format(),
            endDate: this.state.endDate.format()
        }

        this.sthRepository.getHistory(sthSearchDTO).then(response => {
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

            this.setState({
                sthData: data
            });
        });
    }

    render() {        
        return (
            <div className="card">
                <div className="card-content">
                    <span className="card-title activator grey-text text-darken-4">{this.props.deviceId}<i className="material-icons right">more_vert</i></span>
                    {this.state.sthData.length !== 0 ?
                        <Line key={this.props.deviceId} data={this.state.sthData} />
                    : ''}
                </div>
            </div>
        );
    }

}