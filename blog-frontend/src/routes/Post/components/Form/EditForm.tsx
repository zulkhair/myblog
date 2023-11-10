import React from 'react';
import {Button, Form, Input, message, Space, Spin,} from 'antd';
import {CreatePost, PostIdPage} from '@/types/post';
import {getSessionToken} from "@/helpers/token";
import 'react-quill/dist/quill.snow.css';
import dynamic from "next/dynamic";
import {usePostWithId} from "@/hooks/usePost";
import {router} from "next/client";

const layout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16},
};

export function EditPostForm({postId}: PostIdPage) {
    const QuillNoSSRWrapper = dynamic(import('react-quill'), {
        ssr: false,
        loading: () => <p>Loading ...</p>,
    })

    const modules = {
        toolbar: [
            [{header: '1'}, {header: '2'}, {font: []}],
            [{size: []}],
            ['bold', 'italic', 'underline', 'strike', 'blockquote'],
            [
                {list: 'ordered'},
                {list: 'bullet'},
                {indent: '-1'},
                {indent: '+1'},
            ],
            ['link', 'image', 'video'],
            ['clean'],
        ],
        clipboard: {
            // toggle to add extra line breaks when pasting HTML:
            matchVisual: false,
        },
    }
    /*
     * Quill editor formats
     * See https://quilljs.com/docs/formats/
     */
    const formats = [
        'header',
        'font',
        'size',
        'bold',
        'italic',
        'underline',
        'strike',
        'blockquote',
        'list',
        'bullet',
        'indent',
        'link',
        'image',
        'video',
    ]

    const token = getSessionToken();
    const [form] = Form.useForm();

    const onFinish = (values: CreatePost) => {
        fetch(process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/edit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                postId: postId,
                title: values.title,
                content: values.content,
            }),
        }).then(res => {
            if (res.ok) {
                router.push("/post")
                message.info("Article has been edited");
            } else {
                message.error("Failed to edit article");
            }
        });
    };

    const {post, isLoading, isError} = usePostWithId(String(token), postId);

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

    return (
        <>
            <Form {...layout} labelAlign="left" form={form} onFinish={onFinish} labelCol={{span: 3}}
                  wrapperCol={{span: 24}}>
                <Form.Item
                    name="title"
                    label="Title"
                    rules={[
                        {
                            required: true,
                        },
                    ]}
                    initialValue={post?.title}
                >
                    <Input/>
                </Form.Item>
                <Form.Item
                    name="content"
                    label="Content"
                    rules={[
                        {
                            required: true,
                        },
                    ]}
                    initialValue={post?.content}
                ><QuillNoSSRWrapper modules={modules} formats={formats} theme="snow"/>
                </Form.Item>
                <Form.Item wrapperCol={{offset: 3}}>
                    <Space>
                        <Button type="primary" htmlType="submit">
                            Edit
                        </Button>
                    </Space>
                </Form.Item>
            </Form>
        </>
    );
}
