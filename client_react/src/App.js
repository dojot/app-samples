import React, {
  Component
} from 'react';

import WS from './utils/ws'

import './App.css';

import EntityHistoricalChart from './components/charts/entity-sth-chart';
import EntityRealTimeChart from './components/charts/entity-rt-chart';

import { Row, Col } from 'react-materialize';

class App extends Component {

  constructor(props) {
    super(props);
    WS.connect();
  }

  render() {
    const deviceId = "9670";
    const type = "device";
    const sensor = "temperature";    
    
    return (
      <Row key={`chartsOf${deviceId}`}>
          <Col s={5}>
              <EntityHistoricalChart deviceId={deviceId} type={type} sensor={sensor} />
          </Col>
          <Col s={2}>
              <EntityRealTimeChart deviceId={deviceId} type={type} sensor={sensor} />
          </Col>
      </Row>
    );
  }
}

export default App;
