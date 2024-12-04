"use client";

import React, { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import checkAuth from './utils/checkAuth';
import checkToken from './utils/checkToken';
import styles from './styles/Home.module.css';

const Home: React.FC = () => {
    const router = useRouter();

    useEffect(() => {
        const removeAnnouncer = () => {
            const announcer = document.getElementById('__next-route-announcer__');
            if (announcer) {
                announcer.remove();
            }
        };

        // Удаляем элемент после полной загрузки страницы
        window.addEventListener('load', removeAnnouncer);

        // Чистим слушатель при размонтировании компонента
        return () => {
            window.removeEventListener('load', removeAnnouncer);
        };
    }, []);

    const navigateToAccount = () => {
        if (checkAuth()) {
            router.push('/account'); // Перенаправление на личный кабинет
        } else {
            router.push('/login'); // Перенаправление на страницу логина
        }
    };

    const navigateToGames = () => {
        localStorage.setItem("redirectUrl", window.location.pathname);
        if (checkAuth()) {
            if (checkToken()) {
                router.push('/games'); // Перенаправление на страницу игр
            } else {
                localStorage.setItem("redirectUrl", window.location.pathname);
                router.push('/login'); // Перенаправление на страницу логина при недействительном токене
            }
        } else {
            router.push('/register'); // Перенаправление на страницу регистрации
        }
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <div className={styles.leftButton}>
                    <button className={styles.button} onClick={navigateToGames}>Игры</button>
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
};

export default Home;
