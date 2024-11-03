"use client";

import React from 'react';
import { useRouter } from 'next/navigation';
import checkAuth from './utils/checkAuth';
import styles from './styles/Home.module.css';

const Home: React.FC = () => {
    const router = useRouter();

    const navigateToAccount = () => {
        if (checkAuth()) {
            router.push('/account'); // Перенаправление на личный кабинет
        } else {
            router.push('/login'); // Перенаправление на страницу логина
        }
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <div className={styles.leftButton}>
                    <button className={styles.button}>Игры</button>
                </div>
                <div className={styles.rightButton}>
                    <button className={styles.button} onClick={navigateToAccount}>Личный кабинет</button>
                </div>
            </header>
            <main className={styles.main}>
                <h1 className={styles.title}>История нашей Компании</h1>
                <p className={styles.description}>
                    Наше казино было основано в 2024 году с целью предоставления лучших онлайн-игр и невероятных выигрышей для всех наших игроков. Мы гордимся нашей историей и стремимся к совершенству каждый день.
                </p>
            </main>
            <footer className={styles.footer}>
                <p>© 2024 Наше Казино. Все права защищены.</p>
            </footer>
        </div>
    );
}

export default Home;
