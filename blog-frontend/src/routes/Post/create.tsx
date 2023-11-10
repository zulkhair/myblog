import {Card, Col, Row} from 'antd';
import {CreatePostForm} from './components/Form';

export function AddPost() {
    return (
        <>
            <Row gutter={[16, 24]}>
                <Col span={24}>
                    <Card title="Create Post">
                        <CreatePostForm/>
                    </Card>
                </Col>
            </Row>
        </>
    );
}
