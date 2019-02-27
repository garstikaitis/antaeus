import React from 'react';
import { styles } from '../styles';

export const Header = ({ onClick }) => {
  return (
    <div style={styles.row}>
      <h1>All invoices</h1>
      <button onClick={onClick} style={styles.row.button}>
        CHARGE
      </button>
    </div>
  );
};
