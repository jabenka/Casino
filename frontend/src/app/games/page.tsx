"use client";
import React from 'react';
import { useRouter } from 'next/navigation';
import styles from '../styles/GamesMenu.module.css';

const GamesMenu: React.FC = () => {
    const router = useRouter();

    const navigateToRoulette = () => {
        router.push('/GameOne'); // Перенаправление на страницу GameOne
    };

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Выбор игры</h1>
            <div className={styles.gamesList}>
                <div className={styles.gameCard} onClick={navigateToRoulette}>
                    <h2>Рулетка</h2>
                    <p>Попробуйте свою удачу в нашей захватывающей рулетке!</p>
                </div>
                {/* Вы можете добавить другие игры здесь */}
            </div>
        </div>
    );
};

export default GamesMenu;
