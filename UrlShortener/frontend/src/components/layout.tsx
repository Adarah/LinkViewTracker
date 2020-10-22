import React from 'react';
import Header from 'next/head';
/* import Footer from 'components/footer'; */
import Hero from 'components/hero';

export default function Layout({ children }: LayoutProps): JSX.Element {
  return (
    <div>
      <Header>
        <title>My title</title>
      </Header>
      <Hero title="Title" subtitle="subtitle" href="https://source.unsplash.com/random/" />
      {children}
    </div>
  );
}

interface LayoutProps {
  children: React.ReactNode;
}
