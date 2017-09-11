import React, { Component } from 'react';

import { Col, Row } from 'react-materialize';

import config from '../../config/server.json';

export default class EntityRealTimeChart extends Component {

    constructor(props) {
        super(props);

        this.topic = `/device/${this.props.deviceId}`;
        this.state = {
            data: {},
            subscriptionId: null
        }
        
        const options = {
            method: 'POST',
            headers: {                
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: this.props.deviceId,
                attributes: [this.props.sensor]                
            })
        }        
        
        fetch(`${config.SERVER_URL}/subscribe`, options)
            .then(response => response.json())
            .then(data => {
                this.setState({
                    subscriptionId: data.subscribeResponse.subscriptionId
                });
            });      

        window.socket.on(this.topic, (msg) => {
            this.setState({
                data: JSON.parse(msg.data)
            });
        });
    }

    componentWillUnmount(){
        fetch(`${config.SERVER_URL}/unsubscribe/${this.state.subscriptionId}`)
            .then(response => response.json())
            .then(data => console.log(data));
    }

    render() {
        const sensorValue = this.state.data.value || '';
        return (
            <div className="card">
                <div className="card-content">
                    <span className="card-title activator grey-text text-darken-4">{this.props.deviceId}<i className="material-icons right">more_vert</i></span>
                    <Row className="containerCard">
                        <Col className="cardElement temp">
                            <div className="inner">
                                <div className="icon"></div>
                                <div className="title">
                                    <div className="text">{this.props.sensor}</div>
                                </div>
                                <div className="number">{sensorValue}</div>
                            </div>
                        </Col>
                    </Row>
                </div>
            </div>);
    }

}
