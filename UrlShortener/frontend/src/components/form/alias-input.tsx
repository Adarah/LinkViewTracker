import React from 'react';
import InputProps from './input-props';

export default function AliasInput(props: InputProps): JSX.Element {
  const { errors, onChange } = props;
  return (
    <label htmlFor="aliasInput">
      Alias:
      <br />
      {errors}
      <br />
      <input
        id="aliasInput"
        name="alias"
        type="text"
        minLength={3}
        onChange={onChange}
      />
    </label>
  );
}
