import {Button, Card, Form, Input, message} from 'antd'
import Password from 'antd/lib/input/Password'
import Link from "next/link";
import {useRouter} from "next/router";

export default function Regiser() {
    const [form] = Form.useForm();
    const router = useRouter()

    const register = (values: any) => {
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: values.username,
                password: values.password,
                name: values.name,
                email: values.email,
                urlPath: values.url,
            }),
        };

        fetch(
            process.env.NEXT_PUBLIC_BACKEND_API_URL + '/user/register', requestOptions
        ).then((res) => {
            if (res.status == 200) {
                message.success('Registration Success');
                form.resetFields();
                router.push("/login");
            } else {
                res.json().then(content => {
                    message.warn(content.message);
                }).catch(() => {
                    message.warn("Registration Failed");
                })
            }
        });
    };

    return (
        <>
            <Card
                style={{
                    width: 600,
                    height: 600,
                    borderRadius: '8px',
                    margin: '120px auto',
                    textAlign: 'center',
                    borderWidth: '3px',
                }}
            >
                <h1 className="title">MY BLOG</h1>
                <p>Registration</p>
                {
                    <Form
                        form={form}
                        name="basic"
                        labelCol={{span: 8}}
                        wrapperCol={{span: 16}}
                        initialValues={{remember: true}}
                        autoComplete="off"
                        onFinish={register}
                    >
                        <Form.Item wrapperCol={{span: 24}}
                                   name="username"
                                   rules={[{required: true, message: 'Please input your username'}]}
                        >
                            <Input placeholder="Username"/>
                        </Form.Item>

                        <Form.Item wrapperCol={{span: 24}}
                                   name="password"
                                   rules={[{required: true, message: 'Please input your password'}]}
                        >
                            <Password placeholder="Password"/>
                        </Form.Item>

                        <Form.Item wrapperCol={{span: 24}}
                                   name="name"
                                   rules={[{required: true, message: 'Please input your full name'}]}
                        >
                            <Input placeholder="Full Name"/>
                        </Form.Item>

                        <Form.Item wrapperCol={{span: 24}}
                                   name="email"
                                   rules={[{required: true, message: 'Please input your email'}]}
                        >
                            <Input placeholder="Email"/>
                        </Form.Item>

                        <Form.Item wrapperCol={{span: 24}}
                                   name="url"
                                   rules={[{required: true, message: 'Please input your url'}]}
                        >
                            <Input placeholder="Blog URL path (e.g 'daim')"/>
                        </Form.Item>

                        <Form.Item wrapperCol={{span: 24}}>
                            <Button
                                type="primary"
                                htmlType="submit"
                            >
                                Register
                            </Button>
                        </Form.Item>
                    </Form>
                }
                <p>Already have an account?</p>
                <Link href="/login">Sign In here</Link>
            </Card>

        </>
    )
}
