import React from 'react';
import Image from 'next/image';
import Link from 'next/link';
import {Button, Layout as LayoutAntd} from 'antd';

import logo from '@/assets/Logo.png';

import {headerContentWrapper, headerTitleWrapper, headerWrapper, styleTitle1, styleTitle2,} from './index.styles';
import {deleteSessionToken} from "@/helpers/token";
import {useRouter} from "next/router";

const {Header} = LayoutAntd;

const HeaderComponent = () => {
    const router = useRouter();

    const signOut = () => {
        deleteSessionToken()
        router.push("/login");
    }

    return (
        <Header className={headerWrapper}>
            <div className={headerContentWrapper}>
                <div className={headerTitleWrapper}>
                    <Link href="/" passHref>
                        <div className={styleTitle1}>
                            <Image src={logo} alt="my blog"/>
                        </div>
                    </Link>
                    <div className={styleTitle2}>MY BLOG</div>
                </div>
                <div className="ProfileWrapper">
                    <Button type="primary" danger onClick={() => signOut()}>
                        Sign Out
                    </Button>
                </div>
            </div>
        </Header>
    );
};

export default HeaderComponent;
