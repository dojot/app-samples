import config from '../config/server.json';

export default class STHRepository {

  getHistory(sthSearchDTO) {
    const url = `${config.SERVER_URL}/sth/type/${sthSearchDTO.type}/id/${sthSearchDTO.deviceId}/attributes/${sthSearchDTO.sensor}?lastN=0&dateFrom=${sthSearchDTO.startDate}&dateTo=${sthSearchDTO.endDate}`;
    return new Promise((resolve, reject) => {
      fetch(url)
        .then(response => response.json())
        .then(result => {
          resolve(result);
        })
        .catch(err => {
          reject(err)
        });
    });
  }
}
