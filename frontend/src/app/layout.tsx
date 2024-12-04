import React from 'react';
import Head from 'next/head';
import './globals.css';

export const metadata = {
    title: 'CASINO',
    description: 'Лучшее онлайн казино с увлекательными играми и большими выигрышами.',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="en">
        <Head>
            <title>CASINO</title>
            <link rel="icon" href="/favicon.ico" />
        </Head>
        <body>{children}</body>
        </html>
    );
}
