import 'antd/dist/antd.css';
import '@/styles/tailwind.css';
import '@/styles/variables.less';
import '@/styles/antd.less';
import type {AppProps} from 'next/app';

export default function MyApp({
  Component,
  pageProps: { ...pageProps },
}: AppProps) {
  return (
      <Component {...pageProps} />
  );
}
