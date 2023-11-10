import React from 'react';
import {CreateComment, UrlPathPage} from '@/types/post';
import 'react-quill/dist/quill.snow.css';
import {content, details, innerLayout, mainLayout} from "@/pages/style";
import {usePostDetail} from "@/hooks/usePost";
import {Button, Divider, Form, Input, List, message, Popconfirm, Space, Spin} from "antd";
import TextArea from "antd/lib/input/TextArea";
import {getSessionToken} from "@/helpers/token";

export function MainPostDetail({urlPath}: UrlPathPage) {
    const [form] = Form.useForm();
    const {post, isLoading, isError, mutate} = usePostDetail(urlPath);

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

    if (post === undefined) {
        return <div>Post not found</div>
    }


    const onFinish = (values: CreateComment) => {
        values.postId = post.postId;
        fetch(process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/comment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(values),
        }).then(res => {
            if (res.ok) {
                form.resetFields();
                message.info("Comment has been posted");
            } else {
                message.error("Failed to post comment");
            }
        }).then(() => mutate());
    };

    const deleteComment = (commentId: string) => {
        const requestOptions = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${getSessionToken()}`,
            },
        };
        fetch(
            process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/comment/delete/' + commentId,
            requestOptions
        )
            .then((response) => response.json())
            .then(() => mutate());
    };

    function deleteButton(commentId: string) {
        console.log(commentId);
        if (post?.deleteComment) {
            return <Popconfirm
                placement="top"
                title="Delete this comment ?"
                okText="Yes"
                cancelText="No"
                onConfirm={() => deleteComment(commentId)}
            >
                <Button
                    type="primary"
                    size="small"
                    danger
                    title="Delete Event"
                >Delete</Button>
            </Popconfirm>;
        }
    }

    return (
        <>
            <div className={mainLayout}>
                <div className={innerLayout}>
                    <h1>{post.title}</h1>
                    <p className={details}>By {post.author}</p>
                    <p className={details}>Posted
                        in {post.postTime} {post.updateTime ? "(Last Update : " + post.updateTime + ")" : ""}</p>
                    <p className={details}>Viewed {post.viewCount} times</p>
                    <div className={content} dangerouslySetInnerHTML={{__html: post.content}}></div>
                </div>
                <Divider/>
                <h2>Comments</h2>
                <List
                    bordered
                    dataSource={post.comments}
                    renderItem={comment => (
                        <div className={innerLayout}>
                            <h3>{comment.name}</h3>
                            <p className={details}>{comment.postTime}</p>
                            <div className={content}>{comment.content}</div>
                            {deleteButton(comment.commentId)}
                            <Divider/>
                        </div>
                    )}
                />
                <Divider/>
                <Form labelAlign="left" form={form} onFinish={onFinish} labelCol={{span: 3}} wrapperCol={{span: 24}}>
                    <Form.Item
                        name="name"
                        label="Name"
                        rules={[
                            {
                                required: true,
                            },
                        ]}
                    >
                        <Input/>
                    </Form.Item>
                    <Form.Item
                        name="comment"
                        label="Comment"
                        rules={[
                            {
                                required: true,
                            },
                        ]}
                    >
                        <TextArea rows={4}/>
                    </Form.Item>
                    <Form.Item wrapperCol={{offset: 3}}>
                        <Space>
                            <Button type="primary" htmlType="submit">
                                Submit
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </div>


        </>
    );
}
