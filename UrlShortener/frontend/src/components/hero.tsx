import React from 'react';
import style from '../styles/Hero.module.css';

export default function Hero({ href, title, subtitle }: HeroProps): JSX.Element {
  return (
    <div className={style['hero-image']} style={{ backgroundImage: `url(${href})` }}>
      <h1 className={style['hero-title']}>{title}</h1>
      <h2 className={style['hero-subtitle']}>{subtitle}</h2>
    </div>
  );
}

interface HeroProps {
  href: string;
  title: string;
  subtitle: string;
}
