import React from 'react';
import Layout from 'components/layout';
import Form from 'components/form/form';
import styles from '../styles/Home.module.css';

const selfDomain = process.env.NEXT_PUBLIC_DOMAIN_NAME;
const submitDomain = process.env.NEXT_PUBLIC_SUBMIT_DOMAIN;

export default function Home(): JSX.Element {
  return (
    <Layout>
      <Form selfDomain={selfDomain} submitDomain={submitDomain} />
    </Layout>
  );
}
