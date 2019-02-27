import React, { Component } from 'react';
import { styles } from './styles';
import * as Components from './components';
import * as helpers from './helpers';
import axios from 'axios';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      showMessage: false,
      message: '',
    };
  }
  async componentDidMount() {
    const { data } = await axios.get('http://localhost:7000/rest/v1/invoices');
    const invoices = await helpers.formatData(data);
    this.setState({ data: invoices });
  }

  sortTable = (e, key) => {
    const sorted = this.state.data.sort((a, b) => {
      if (a[key] < b[key]) {
        return -1;
      }
      if (a[key] > b[key]) {
        return 1;
      }
      return 0;
    });
    this.setState({ data: sorted });
  };

  charge = async e => {
    const { data } = await axios.get('http://localhost:7000/rest/v1/billing');
    if (data.statusCode === 500) {
      this.setState({
        showMessage: true,
        message: data.message,
      });
    } else {
      const invoices = await helpers.formatData(data.data);
      this.setState({ data: invoices });
    }
  };

  renderData = () => {
    const { data } = this.state;
    if (data.length === 0) {
      return <div>Loading...</div>;
    }
    return data.map(invoice => {
      return <Components.TableBody invoice={invoice} />;
    });
  };

  render() {
    return (
      <div className="App">
        <div style={styles.container}>
          <Components.Header onClick={e => this.charge()} />
          <Components.Floater
            show={this.state.showMessage}
            message={this.state.message}
          />
          <div style={styles.table}>
            <div style={styles.table.thead}>
              <div
                onClick={e => this.sortTable(e, 'id')}
                style={styles.table.thead.element}
              >
                Customer
              </div>
              <div
                onClick={e => this.sortTable(e, 'currency')}
                style={styles.table.thead.element}
              >
                Currency
              </div>
              <div
                onClick={e => this.sortTable(e, 'amount')}
                style={styles.table.thead.element}
              >
                Amount
              </div>
              <div
                onClick={e => this.sortTable(e, 'status')}
                style={styles.table.thead.element}
              >
                Status
              </div>
            </div>
            <div style={styles.table.tbody}>{this.renderData()}</div>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
