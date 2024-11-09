"use client";

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';
import styles from '../styles/Login.module.css';

type LoginResponse = {
    token: string;
};

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();

    const validateEmail = (email: string) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    };

    const handleLogin = async () => {
        if (!validateEmail(email)) {
            setError('Некорректный email');
            setEmail('');
            setPassword('');
            return;
        }
        if (password.length < 8) {
            setError('Пароль должен содержать минимум 8 символов');
            setEmail('');
            setPassword('');
            return;
        }

        try {
            const response = await axios.post<LoginResponse>('http://localhost:9120/auth/login', { email, password });
            const token = response.data.token;
            localStorage.setItem('jwtToken', token);
            router.push('/account');
        } catch (error) {
            setError('Ошибка при входе. Проверьте email и пароль');
            setEmail('');
            setPassword('');
            console.error('Ошибка при входе:', error);
        }
    };

    const navigateToRegister = () => {
        router.push('/register');
    };

    return (
        <div className={styles.container}>
            <h2>Войти</h2>
            {error && <p className={styles.error}>{error}</p>}
            <input
                className={styles.input}
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <input
                className={styles.input}
                type="password"
                placeholder="Пароль"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button className={styles.button} onClick={handleLogin}>Войти</button>
            <button className={styles.button} onClick={navigateToRegister}>Регистрация</button>
        </div>
    );
}

export default Login;
