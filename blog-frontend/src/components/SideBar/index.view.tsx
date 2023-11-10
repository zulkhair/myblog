import React from 'react';
import Link from 'next/link';
import {Layout, Menu} from 'antd';
import {useRouter} from 'next/router';

import {FormOutlined,} from '@ant-design/icons';

import {menuStyle, mrgnRight9, sideBar} from './index.style';

const {Sider} = Layout;

const getSelectedKey = (pathName: string) => {
    switch (pathName) {
        case '/post':
            return 'post';
        default:
            return '';
    }
};

const SideBarComponent = () => {
    const router = useRouter();

    return (
        <Sider width="256" className={sideBar}>
            <Menu
                mode="inline"
                className={menuStyle}
                defaultSelectedKeys={[getSelectedKey(router.pathname)]}
            >
                <Menu.Item key="post">
                    <FormOutlined className={mrgnRight9}/>
                    <Link href="/post" passHref>
                        Post
                    </Link>
                </Menu.Item>
            </Menu>
        </Sider>
    );
};

export default SideBarComponent;
