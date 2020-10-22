import React from 'react';
import TargetUrlInput from './target-url-input';
import AliasInput from './alias-input';

export default class Form extends React.Component<FormProps, FormState> {
  constructor(props: FormProps) {
    super(props);
    this.state = ({
      targetUrl: null,
      alias: null,
      selfDomain: props.selfDomain,
      submitDomain: props.submitDomain,
      resultMessage: null,
      errors: {
        targetUrl: [],
        alias: [],
      },
    });
  }

  /* handleChange(this: Form, e: React.ChangeEvent<HTMLInputElement>): void { */
  /*   console.log(e); */
  /*   /1* this.setState({ value: event.currentTarget.value }); *1/ */
  /* } */
  setTargetUrl(this: Form, targetUrl: string) {
    this.setState({ targetUrl });
  }

  setAlias(this: Form, alias: string) {
    this.setState({ alias: alias === '' ? null : alias });
  }

  setErrors(this: Form, formErrors: ApiError[]): void {
    const targetUrl = [];
    const alias = [];
    formErrors.forEach((err) => {
      if (err.field === 'alias') {
        alias.push(err.message);
      } else {
        targetUrl.push(err.message);
      }
    });
    this.setState({ errors: { targetUrl, alias } });
  }

  showSuccessMessage(shouldShow: boolean, alias?: string): void {
    if (!shouldShow) {
      this.setState({ resultMessage: null });
    } else {
      const { selfDomain } = this.state;
      this.setState({ resultMessage: `URL created successfull! You can find it at ${selfDomain}/${alias}` });
    }
  }

  async handleSubmit(this: Form, e): Promise<boolean> {
    e.preventDefault();
    this.showSuccessMessage(false);
    const { targetUrl, alias, submitDomain } = this.state;
    const data = {
      target: targetUrl,
      alias,
    };
    const response: ApiResponse = await fetch(submitDomain, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })
      .then((resp) => {
        if (resp.status === 500) throw new Error('Something went wrong with our servers, please try again later');
        return resp;
      })
      .then((resp) => resp.json())
      .catch((err) => this.setState({ resultMessage: err.message }));
    if (response === undefined) {
      return false;
    }

    if (response.errors) {
      this.setErrors(response.errors);
    } else {
      this.showSuccessMessage(true, response.redirect.alias);
    }
    return false;
  }

  render(this: Form): JSX.Element {
    const { resultMessage: successMessage, errors } = this.state;
    const { targetUrl, alias } = errors;
    return (
      <form onSubmit={this.handleSubmit.bind(this)}>
        <TargetUrlInput
          errors={targetUrl}
          onChange={(e): void => this.setTargetUrl(e.target.value)}
        />
        <br />
        <AliasInput
          errors={alias}
          onChange={(e): void => this.setAlias(e.target.value)}
        />
        <input
          type="submit"
          value="Create shortened URL"
        />
        {successMessage}
      </form>
    );
  }
}

interface FormProps {
  selfDomain: string;
  submitDomain: string;
}

interface FormState {
  targetUrl: string;
  alias: string;
  selfDomain: string;
  submitDomain: string;
  resultMessage: string;
  errors: {
    targetUrl: string[];
    alias: string[];
  }
}

// TODO: Check what the java api actually returns
interface ApiResponse extends Response {
  errors?: ApiError[];
  redirect?: ApiSuccess;
}

interface ApiError {
  resource: string;
  field: string;
  code: string;
  message: string;
}

interface ApiSuccess {
  alias: string;
  target: string;
}
