"use client";

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import styles from '../styles/Account.module.css';

interface UserDto {
    id: number;
    name: string;
    surname: string;
    email: string;
    password: string;
    balance: number;
}

interface JwtPayload {
    exp: number;
    [key: string]: unknown;
}

const base64UrlDecode = (str: string): string => {
    try {
        return decodeURIComponent(atob(str.replace(/-/g, '+').replace(/_/g, '/'))
            .split('')
            .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
            .join(''));
    } catch (error) {
        console.error('Invalid token format', error);
        return '';
    }
};

const decodeJwt = (token: string): JwtPayload | null => {
    const parts = token.split('.');
    if (parts.length !== 3) {
        console.error('Invalid token format');
        return null;
    }

    const payload = parts[1];
    const decodedPayload = base64UrlDecode(payload);
    try {
        return JSON.parse(decodedPayload);
    } catch (error) {
        console.error('Error parsing JWT payload', error);
        return null;
    }
};

const Account: React.FC = () => {
    const [user, setUser] = useState<UserDto | null>(null);
    const router = useRouter();

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('jwtToken');
            if (!token) {
                console.error('Token is null. Please log in again.');
                router.push('/login');
                return;
            }

            const decoded = decodeJwt(token);
            if (decoded && typeof decoded.exp === 'number') {
                const now = Math.floor(Date.now() / 1000);
                if (decoded.exp < now) {
                    console.error('Token has expired. Please log in again.');
                    localStorage.removeItem('jwtToken');
                    router.push('/login');
                    return;
                }
            } else {
                console.error('Invalid token. Please log in again.');
                localStorage.removeItem('jwtToken');
                router.push('/login');
                return;
            }

            axios.get<UserDto>('http://localhost:9120/user/me', {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                withCredentials: true
            })
                .then(response => setUser(response.data))
                .catch(error => console.error('Ошибка при загрузке данных пользователя:', error));
        };

        fetchData();
    }, [router]);

    const navigateToHome = () => {
        router.push('/');
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <h1 className={styles.title}>Личный кабинет</h1>

                <button className={styles.button} onClick={navigateToHome}>На главную</button>
            </header>

            {user && (
                <div className={styles.infoBox}>
                    <p>Имя: {user.name}</p>
                    <p>Фамилия: {user.surname}</p>
                    <p>Email: {user.email}</p>
                    <p>Баланс: ${user.balance}</p>
                </div>
            )}
        </div>
    );
};

export default Account;
