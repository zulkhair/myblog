import React from 'react';
import {useRouter} from 'next/router';
import {Breadcrumb, Layout, Spin} from 'antd';
import {WrapperHead} from '@/components/application/WrapperHead';
import HeaderComponent from '../Header/index.view';
import SideBarComponent from '../SideBar/index.view';
import {breadCrumbWrapper, contentPageWrapper, contentWrapper, titlePage,} from './index.style';
import type {MainLayoutPropsTypes} from './index.types';
import {checkSession} from "@/hooks/useSessions";

const {Content} = Layout;

const LayoutPage = ({
                        children,
                        title,
                        path,
                        showTitlePage = false,
                    }: MainLayoutPropsTypes) => {
    const router = useRouter();
    const {isLoading, isError} = checkSession();

    // error handling
    if (isError) {
        router.push('/login');
    }

    if (isLoading) {
        return (
            <div className="grid place-items-center">
                <Spin/>
            </div>
        );
    }

    return (
        <>
            <WrapperHead title={`MY BLOG - ${title}`}/>
            <Layout>
                <HeaderComponent/>
                <Layout hasSider>
                    <SideBarComponent/>
                    <Content className={contentWrapper}>
                        <Breadcrumb className={breadCrumbWrapper}>
                            <Breadcrumb.Item>MY BLOG</Breadcrumb.Item>
                            {path.map((item, index) => (
                                <Breadcrumb.Item key={index}>{item}</Breadcrumb.Item>
                            ))}
                        </Breadcrumb>
                        <div className={contentPageWrapper}>
                            {showTitlePage && <h1 className={titlePage}>{title}</h1>}
                            {children}
                        </div>
                    </Content>
                </Layout>
            </Layout>
        </>
    );
};

export default LayoutPage;
