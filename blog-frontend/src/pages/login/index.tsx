import {Button, Card, Form, Input, message} from 'antd'
import {useRouter} from 'next/router'
import Password from 'antd/lib/input/Password'
import Link from "next/link";
import {setSessionToken} from "@/helpers/token";

export default function Login() {
    const router = useRouter()
    const [form] = Form.useForm();

    const login = (values: any) => {
        fetch(process.env.NEXT_PUBLIC_BACKEND_API_URL + "/login", {
            method: "POST",
            body: JSON.stringify({
                username: values.username,
                password: values.password,
            }),
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(res => {
            if (res.ok) {
                res.json().then(value => {
                    setSessionToken(value.data.token);
                    router.push("/post")
                })
            } else {
                res.json().then(value => {
                    message.warn("Please check your username or password");
                })
            }
        })
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
                <p>Sign in</p>
                {
                    <Form
                        form={form}
                        name="basic"
                        labelCol={{span: 8}}
                        wrapperCol={{span: 16}}
                        initialValues={{remember: true}}
                        autoComplete="off"
                        onFinish={login}
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

                        <Form.Item wrapperCol={{span: 24}}>
                            <Button
                                type="primary"
                                htmlType="submit"
                            >
                                Sign in
                            </Button>
                        </Form.Item>
                    </Form>
                }
                <p>You don't have an account?</p>
                <Link href="/register">Register here</Link>
            </Card>

        </>
    )
}
