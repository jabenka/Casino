"use client";

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';
import styles from '../styles/Register.module.css';

const Register: React.FC = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();

    const validateEmail = (email: string) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    };
    const clearForm = () => {
        setFirstName('');
        setLastName('');
        setEmail('');
        setPassword('');
        setConfirmPassword('');
    };
    const handleRegister = async () => {
        if (!validateEmail(email)) {
            setError('Некорректный email');
            clearForm();
            return;
        }
        if (password.length < 8) {
            setError('Пароль должен содержать минимум 8 символов');
            clearForm();
            return;
        }
        if (password !== confirmPassword) {
            setError('Пароли не совпадают');
            clearForm();
            return;
        }
        console.log({ firstName, lastName, email, password });
        try {
            await axios.post('http://localhost:9120/auth/signup', {
                name: firstName,
                surname: lastName,
                email: email,
                password: password,
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            router.push('/login');
        }catch (error){
            setError('Ошибка при регистрации');
            clearForm();
            console.error('Ошибка при регистрации:', error);
        }
    };



    return (
        <div className={styles.container}>
            <h2>Регистрация</h2>
            {error && <p className={styles.error}>{error}</p>}
            <input
                className={styles.input}
                type="text"
                placeholder="Имя"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
            />
            <input
                className={styles.input}
                type="text"
                placeholder="Фамилия"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
            />
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
            <input
                className={styles.input}
                type="password"
                placeholder="Повторите пароль"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
            />
            <button className={styles.button} onClick={handleRegister}>Регистрация</button>
        </div>
    );
}

export default Register;
