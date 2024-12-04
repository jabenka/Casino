import { useEffect } from 'react';
import type { AppProps } from 'next/app';
import '../styles/globals.css';

function MyApp({ Component, pageProps }: AppProps) {
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

    return <Component {...pageProps} />;
}

export default MyApp;
