"use client";
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import styles from '../styles/Roulette.module.css';
import checkToken from "@/app/utils/checkToken";

interface BetResponse {
    bet: number;
    newBalance: number;
}

const Page: React.FC = () => {
    const [result, setResult] = useState<number | null>(null);
    const [betType, setBetType] = useState<string>('number');
    const [betValue, setBetValue] = useState<string>('');
    const [betAmount, setBetAmount] = useState<number>(0);
    const [isSpinning, setIsSpinning] = useState<boolean>(false);
    const [balance, setBalance] = useState<number>(0);
    const router = useRouter();

    // Проверка токена
    const validateToken = async () => {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            alert('Токен не найден, пожалуйста, войдите в систему.');
            router.push('/login');
            return false;
        }

        try {
            const response = await axios.get('http://localhost:9120/auth/validate', {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                withCredentials: true
            });

            if (response.status !== 200) {
                throw new Error('Токен недействителен');
            }

            return true;
        } catch (error) {
            alert('Недействительный токен, пожалуйста, войдите в систему снова.');
            console.log(error);
            router.push('/login');
            return false;
        }
    };

    useEffect(() => {
        const fetchBalanceFromDTO = async () => {
            if (!(await validateToken())) return;

            try {
                const token = localStorage.getItem('jwtToken');
                if(checkToken()) {
                    const response = await axios.get<BetResponse>('http://localhost:9120/user/balance', {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        },
                        withCredentials: true
                    });
                    setBalance(response.data.newBalance);
                }
            } catch (error) {
                console.error('Error fetching balance:', error);
            }
        };

        fetchBalanceFromDTO();
    }, []);

    // Функция для вращения колеса
    const spinWheel = async () => {
        if (!(await validateToken())) return;

        setIsSpinning(true);
        const outcome = Math.floor(Math.random() * 5) + 1; // Результат от 1 до 5
        setResult(outcome);

        // Остановить анимацию после 3 секунд
        setTimeout(() => {
            setIsSpinning(false);
            sendBetData();
        }, 3000); // 3 секунды — время вращения
    };

    // Функция для отправки данных о ставке
    const sendBetData = async () => {
        if (!(await validateToken())) return;

        try {
            const token = localStorage.getItem('jwtToken');
            const params = new URLSearchParams();
            params.append('bet', betAmount.toString());

            if (betType === 'number') {
                params.append('number', betValue);
            } else if (betType === 'color') {
                params.append('color', betValue);
            }

            const response = await axios.post<BetResponse>(`http://localhost:9120/roulette/play?${params.toString()}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                withCredentials: true
            });

            if (response.data) {
                console.log('Response:', response.data);
                setBalance(response.data.newBalance);
                console.log('Новый баланс:', response.data.newBalance);
            } else {
                console.error('Response data is undefined');
            }
        } catch (error) {
            console.error('Error sending bet data:', error);
        }
    };

    // Обработчик изменения типа ставки
    const handleBetTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setBetType(e.target.value);
        setBetValue('');
    };

    // Навигация
    const navigateToHome = () => {
        router.push('/');
    };

    const navigateToAccount = () => {
        router.push('/account');
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <button className={styles.button} onClick={navigateToHome}>На главную</button>
                <button className={styles.button} onClick={navigateToAccount}>Личный кабинет</button>
                <div className={styles.balance}>Баланс: ${balance}</div>
            </header>
            <h1>Казино - Рулетка</h1>

            {/* Колесо рулетки с анимацией */}
            <div className={`${styles.component} ${isSpinning ? styles.spinning : ''}`}>
                <div className={styles.option} style={{transform: 'rotate(72deg)', background: 'red'}}><div className={styles.row}>1</div></div>
                <div className={styles.option} style={{transform: 'rotate(144deg)', background: 'black'}}><div className={styles.row}>2</div></div>
                <div className={styles.option} style={{transform: 'rotate(-72deg)', background: 'red'}}><div className={styles.row}>3</div></div>
                <div className={styles.option} style={{transform: 'rotate(-144deg)', background: 'black'}}><div className={styles.row}>4</div></div>
                <div className={styles.option} style={{transform: 'rotate(0deg)', background: 'red'}}><div className={styles.row}>5</div></div>
            </div>

            <div className={styles.controls}>
                <label className={styles.label}>
                    Тип ставки:
                    <select value={betType} onChange={handleBetTypeChange} className={styles.select}>
                        <option value="number">Номер</option>
                        <option value="color">Цвет</option>
                    </select>
                </label>

                {betType === 'number' ? (
                    <label className={styles.label}>
                        Номер:
                        <input
                            type="number"
                            value={betValue}
                            onChange={(e) => setBetValue(e.target.value)}
                            min="0"
                            max="36"
                            className={styles.input}
                        />
                    </label>
                ) : (
                    <label className={styles.label}>
                        Цвет:
                        <select value={betValue} onChange={(e) => setBetValue(e.target.value)} className={styles.select}>
                            <option value="">Выберите цвет</option>
                            <option value="red">Красный</option>
                            <option value="black">Черный</option>
                        </select>
                    </label>
                )}

                <label className={styles.label}>
                    Ставка:
                    <input
                        type="number"
                        value={betAmount}
                        onChange={(e) => setBetAmount(Number(e.target.value))}
                        className={styles.input}
                    />
                </label>

                <button onClick={spinWheel} className={styles.button}>Крутить</button>
            </div>

            {/* Результат игры */}
            {result !== null && <div className={styles.result}>Результат: {result}</div>}
        </div>
    );
};

export default Page;
