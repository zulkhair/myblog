import React from 'react';
import {Button, Form, Input, message, Space,} from 'antd';
import {CreatePost} from '@/types/post';
import {getSessionToken} from "@/helpers/token";
import 'react-quill/dist/quill.snow.css';
import dynamic from "next/dynamic";
import {useRouter} from "next/router";

export function CreatePostForm() {
    const router = useRouter();
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
        fetch(process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(values),
        }).then(res => {
            if (res.ok) {
                message.info("Article has been posted");
                form.resetFields();
                router.push("/post")
            } else {
                message.error("Failed to post article");
            }
        });
    };

    return (
        <>
            <Form labelAlign="left" form={form} onFinish={onFinish} labelCol={{span: 3}} wrapperCol={{span: 24}}>
                <Form.Item
                    name="title"
                    label="Title"
                    rules={[
                        {
                            required: true,
                        },
                    ]}
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
                ><QuillNoSSRWrapper modules={modules} formats={formats} theme="snow"/>
                </Form.Item>
                <Form.Item wrapperCol={{offset: 3}}>
                    <Space>
                        <Button type="primary" htmlType="submit">
                            Submit
                        </Button>
                    </Space>
                </Form.Item>
            </Form>
        </>
    );
}
