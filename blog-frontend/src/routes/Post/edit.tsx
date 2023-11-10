import {Card, Col, Row} from 'antd';
import {EditPostForm} from './components/Form';
import {PostIdPage} from "@/types/post";

export function EditPost({postId}: PostIdPage) {
    return (
        <>
            <Row gutter={[16, 24]}>
                <Col span={24}>
                    <Card title="Edit Post">
                        <EditPostForm postId={postId}/>
                    </Card>
                </Col>
            </Row>
        </>
    );
}
