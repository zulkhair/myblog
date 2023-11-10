import {Button, Popconfirm, Spin, Table} from 'antd';
import {usePost} from '@/hooks/usePost';
import {getSessionToken} from "@/helpers/token";
import {PostResponse} from "@/types/post";
import Link from "next/link";
import {DeleteOutlined, EditOutlined, EyeOutlined} from "@ant-design/icons";

const {Column} = Table;

export function PostTableComponent() {
    const token = getSessionToken();
    const {posts, isLoading, isError, mutate} = usePost(String(token));

    // error handling
    if (isError) {
        return <div className="grid place-items-center">{isError.message}</div>;
    }

    if (isLoading) {
        return (
            <div className="grid place-items-center">
                <Spin/>
            </div>
        );
    }

    const deleteEvent = (postId: string) => {
        const requestOptions = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${getSessionToken()}`,
            },
        };
        fetch(
            process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/delete/' + postId,
            requestOptions
        )
            .then((response) => response.json())
            .then(() => mutate());
    };

    return (
        <>
            <Table dataSource={posts} rowKey={(record) => record.postId}>
                <Column title="Title" dataIndex="title" key="title"/>
                <Column title="Posted Time" dataIndex="postTime" key="postTime"/>
                <Column title="Last Updated" dataIndex="updateTime" key="updateTime"/>
                <Column title="Action" key="action" render={(record: PostResponse) => (
                    <div>
                        <Link
                            href={{
                                pathname: '/post/[postId]',
                                query: {postId: record.postId},
                            }}
                        >
                            <Button
                                type="primary"
                                size="small"
                                title="View"
                                shape="circle"
                                icon={<EyeOutlined/>}
                            />
                        </Link>
                        <Link
                            href={{
                                pathname: '/post/edit/[postId]',
                                query: {postId: record.postId},
                            }}
                        >
                            <Button
                                type="primary"
                                size="small"
                                title="View"
                                shape="circle"
                                icon={<EditOutlined/>}
                            />
                        </Link>
                        <Popconfirm
                            placement="top"
                            title="Delete this post ?"
                            okText="Yes"
                            cancelText="No"
                            onConfirm={() => deleteEvent(record.postId)}
                        >
                            <Button
                                type="primary"
                                size="small"
                                danger
                                title="Delete Event"
                                shape="circle"
                                icon={<DeleteOutlined/>}
                            />
                        </Popconfirm>{' '}
                    </div>
                )}/>
            </Table>
        </>
    );
}

export default PostTableComponent;
