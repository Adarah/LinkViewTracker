import React from 'react';
import InputProps from './input-props';

export default function TargetUrlInput(props: InputProps): JSX.Element {
  const { errors, onChange } = props;
  return (
    <label htmlFor="targetUrlInput">
      Target URL:
      <br />
      {errors}
      <br />
      <input
        id="targetUrlInput"
        name="targetUrl"
        type="url"
        pattern="https?://.*"
        placeholder="http://www.example.com"
        title="The URL must start with http(s)://"
        onChange={onChange}
        required
      />
    </label>
  );
}
