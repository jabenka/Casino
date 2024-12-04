"use client";
import React, { useRef, useState, useEffect } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";
import styles from "../styles/Roulette.module.css";

interface BetResponse {
    bet: number;
    newBalance: number;
}

const RoulettePage: React.FC = () => {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);
    const [isSpinning, setIsSpinning] = useState(false);
    const [rotationAngle] = useState(0);
    const [result, setResult] = useState<string | null>(null); // Результат
    const [betType, setBetType] = useState<string>("number");
    const [betValue, setBetValue] = useState<string>(""); // Значение ставки
    const [betAmount, setBetAmount] = useState<number>(0); // Сумма ставки
    const [balance, setBalance] = useState<number>(0); // Баланс
    const [errorMessage, setErrorMessage] = useState<string | null>(null); // Сообщение об ошибке
    const router = useRouter();

    let Color: string | null = null;

    let color:string="null";

    let resultNumber:number=0;

    // Функция для получения результата рулетки
    const getRouletteResult = (angle: number): number => {
        const segmentAngle = 360 / 36;
        const normalizedAngle = angle % 360;
        const segmentNumber = Math.floor((360 - normalizedAngle) / segmentAngle);
        return segmentNumber === 0 ? 36 : segmentNumber; // Нулевой сегмент соответствует номеру 36
    };

    // Функция для рисования колеса
    const drawWheel = (ctx: CanvasRenderingContext2D, width: number, height: number) => {
        const numSegments = 36;
        const radius = Math.min(width, height) / 2;
        const angle = (2 * Math.PI) / numSegments;

        for (let i = 0; i < numSegments; i++) {
            ctx.beginPath();
            ctx.moveTo(width / 2, height / 2);
            ctx.arc(width / 2, height / 2, radius, angle * i, angle * (i + 1));
            ctx.lineTo(width / 2, height / 2);
            ctx.fillStyle = i % 2 === 1 ? "red" : "black";
            ctx.fill();
        }

        for (let i = 0; i < numSegments; i++) {
            const angleOffset = angle * i + angle / 2;
            const x = width / 2 + Math.cos(angleOffset) * (radius - 30);
            const y = height / 2 + Math.sin(angleOffset) * (radius - 30);
            ctx.fillStyle = "white";
            ctx.font = "bold 16px Arial";
            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            ctx.fillText(i === 0 ? "1" : (i + 1).toString(), x, y);
        }
    };

    // Функция для анимации вращения колеса
    const spinWheel = async () => {
        if (isSpinning) return;
        setIsSpinning(true);

        let angle = rotationAngle;
        let velocity = Math.random() * 10 + 5;
        const friction = 0.99;
        const spinDuration = 3000;

        const startTime = Date.now();

        const rotate = () => {
            const elapsedTime = Date.now() - startTime;

            if (elapsedTime >= spinDuration) {
                setIsSpinning(false);
                resultNumber = getRouletteResult(angle) - 8;
                const mapping = {
                    '0': 36,
                    '-1': 35,
                    '-2': 34,
                    '-3': 33,
                    '-4': 32,
                    '-5': 31,
                    '-6': 30,
                    '-7': 29,
                    '-8': 28
                };

                if (mapping.hasOwnProperty(resultNumber)) {
                    resultNumber = mapping[resultNumber];
                }
                color = resultNumber % 2 === 0 ? "Red" : "Black";

                Color = color === "Red" ? "Красный" : "Черный";

                setResult(betType === "color" ? Color : resultNumber.toString());
                checkWin(resultNumber, Color); // Проверяем, победил ли игрок
                return;
            }

            angle += velocity;
            velocity *= friction;

            if (canvasRef.current) {
                const ctx = canvasRef.current.getContext("2d");
                if (ctx) {
                    ctx.save();
                    ctx.translate(canvasRef.current.width / 2, canvasRef.current.height / 2);
                    ctx.rotate((angle * Math.PI) / 180);
                    ctx.translate(-canvasRef.current.width / 2, -canvasRef.current.height / 2);
                    drawWheel(ctx, canvasRef.current.width, canvasRef.current.height);
                    ctx.restore();
                }
            }

            if (elapsedTime < spinDuration) {
                requestAnimationFrame(rotate);
            }
        };

        rotate();
    };

    // Функция отправки данных о ставке на сервер
    const sendBetData = async (resultType: number) => {
        const token = localStorage.getItem("jwtToken");
        if (!token) {
            alert("Токен не найден, пожалуйста, войдите в систему.");
            router.push("/login");
            return;
        }

        try {
            const params = new URLSearchParams();
            params.append("bet", betAmount.toString());
            params.append("number", resultType.toString()); // Тип результата: 1 (цвет), 2 (число), 0 (проигрыш)
            params.append("color", color);
            params.append("rouletteNumber", resultNumber.toString());

            const response = await axios.post<BetResponse>(
                `http://localhost:9120/roulette/play?${params.toString()}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                    withCredentials: true,
                }
            );

            if (response.data) {
                setBalance(response.data.newBalance); // Обновляем баланс на клиенте, если это нужно
            }
        } catch (error) {
            console.error("Error sending bet data:", error);
        }
    };

    // Функция проверки, победил ли пользователь
    const checkWin = (resultNumber: number, color: string | null) => {
        let resultType = 0; // 0 — проиграл, 1 — выиграл по цвету, 2 — выиграл по числу

        if (betType === "number") {
            if (betValue === resultNumber.toString()) {
                resultType = 2; // Выиграл по числу
            }
        } else if (betType === "color") {
            if ((color === "Красный" && betValue === "red") || (color === "Черный" && betValue === "black")) {
                resultType = 1; // Выиграл по цвету
            }
        }
        sendBetData(resultType); // Отправляем результат на сервер
    };

    // Загрузка баланса при старте
    const fetchBalanceFromDTO = async () => {
        const token = localStorage.getItem("jwtToken");
        if (!token) {
            alert("Токен не найден, пожалуйста, войдите в систему.");
            router.push("/login");
            return;
        }

        try {
            const response = await axios.get<number>("http://localhost:9120/user/balance", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                withCredentials: true,
            });
            console.log("Ответ от сервера:", response);
            console.log("Получен баланс:", response.data);

            // Используем только `newBalance` из объекта `BetResponse`
            setBalance(response.data); // Устанавливаем только новый баланс
        } catch (error) {
            console.error("Error fetching balance:", error);
        }
    };

    useEffect(() => {
        const loadBalance = async () => {
            await fetchBalanceFromDTO();
        };
        loadBalance().then();

        // Нарисовать колесо при загрузке
        if (canvasRef.current) {
            const ctx = canvasRef.current.getContext("2d");
            if (ctx) {
                drawWheel(ctx, canvasRef.current.width, canvasRef.current.height);
            }
        }
    }, []);

    const handleBetTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setBetType(e.target.value);
        setBetValue("");
    };

    const navigateToHome = () => {
        router.push("/");
    };

    const navigateToAccount = () => {
        router.push("/account");
    };

    // Проверка, можно ли сделать ставку
    const canSpin = betAmount > 0 && (betType === "number" ? betValue !== "" : betValue !== "");

    // Проверка пустого поля
    const validateBet = () => {
        if (betAmount <= 0) {
            setErrorMessage("Ставка не может быть 0.");
            return false;
        }

        if (betType === "number" && betValue === "") {
            setErrorMessage("Пожалуйста, выберите номер для ставки.");
            return false;
        }

        if (betType === "color" && betValue === "") {
            setErrorMessage("Пожалуйста, выберите цвет для ставки.");
            return false;
        }

        setErrorMessage(null);
        return true;
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <button className={styles.button} onClick={navigateToHome}>На главную</button>
                <button className={styles.button} onClick={navigateToAccount}>Личный кабинет</button>
                <div className={styles.balance}>Баланс: ${balance}</div>
            </header>
            <h1>Казино - Рулетка</h1>

            <div className={styles.rouletteContainer}>
                <canvas ref={canvasRef} width={400} height={400} className={styles.canvas} />
                <div className={styles.arrow}></div>
            </div>

            <div className={styles.controls}>
                <label className={styles.label}>
                    Тип ставки:
                    <select value={betType} onChange={handleBetTypeChange} className={styles.select}>
                        <option value="number">Номер</option>
                        <option value="color">Цвет</option>
                    </select>
                </label>

                {betType === "number" ? (
                    <label className={styles.label}>
                        Номер:
                        <input
                            type="text"
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
                        type='text'
                        value={betAmount}
                        onChange={(e) => setBetAmount(Number(e.target.value))}
                        className={styles.input}
                    />
                </label>

                {errorMessage && <div className={styles.errorMessage}>{errorMessage}</div>}

                <button
                    onClick={() => {
                        if (validateBet()) {
                            spinWheel();
                        }
                    }}
                    className={styles.button}
                    disabled={!canSpin}
                >
                    Крутить
                </button>
            </div>

            {result !== null && (
                <div className={styles.result}>
                    Результат: {betType === "color" ? result : `Номер ${result}`}
                </div>
            )}
        </div>
    );
};

export default RoulettePage;
