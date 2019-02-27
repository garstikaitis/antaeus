import React from 'react';
import { styles } from '../styles';

export const TableBody = ({ invoice }) => {
  return (
    <div style={styles.table.tbody.row} key={invoice.id}>
      <div style={styles.table.tbody.row.element}>
        <img
          style={styles.table.tbody.row.element.img}
          src={invoice.customer.picture}
        />
        {invoice.customer.name}
      </div>
      <div style={styles.table.tbody.row.element}>{invoice.currency}</div>
      <div style={styles.table.tbody.row.element}>{invoice.amount}</div>

      <div style={styles.table.tbody.row.element}>{invoice.status}</div>
    </div>
  );
};
