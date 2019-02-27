import React from 'react';
import { styles } from '../styles';
export const Floater = ({ show, message }) => {
  return (
    <div
      style={{
        ...styles.floater,
        display: show ? 'flex' : 'none',
      }}
    >
      {message}
    </div>
  );
};
