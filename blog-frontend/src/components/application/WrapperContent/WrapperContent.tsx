import {Layout, Breadcrumb} from 'antd';

const {Content} = Layout;

interface WrapperContentProps {
    path: string[];
    children: React.ReactNode;
}

export function WrapperContent({path = [], children}: WrapperContentProps) {
    return (
        <>
            <Breadcrumb style={{margin: '16px 0'}}>
                {path.map((item, index) => (
                    <Breadcrumb.Item key={index}>{item}</Breadcrumb.Item>
                ))}
            </Breadcrumb>
            <Content
                className="site-layout-background"
                style={{
                    padding: 24,
                    margin: 0,
                    minHeight: 280,
                }}
            >
                {children}
            </Content>
        </>
    );
}
