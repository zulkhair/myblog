import Head from 'next/head';

interface WrapperHeadProps {
    title: string;
}

export function WrapperHead({title}: WrapperHeadProps) {
    return (
        <Head>
            <title>{title}</title>
            <meta name="description" content="My Blog"/>
            <link rel="icon" href="/favicon.ico"/>
        </Head>
    );
}
