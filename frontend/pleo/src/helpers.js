import axios from 'axios';
export const formatData = async data => {
  const response = await axios.get('https://randomuser.me/api/?results=1000');
  return data.map(invoice => {
    return {
      ...invoice,
      currency: invoice.amount.currency,
      amount: invoice.amount.value,
      customer: {
        name: `${response.data.results[
          invoice.id - 1
        ].name.first.toUpperCase()} ${response.data.results[
          invoice.id - 1
        ].name.last.toUpperCase()}`,
        picture: response.data.results[invoice.id - 1].picture.medium,
      },
    };
  });
};
