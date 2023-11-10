import React from 'react';
import {UrlPathPage} from '@/types/post';
import 'react-quill/dist/quill.snow.css';
import {innerLayout, mainLayout, overflowVisible} from "@/pages/style";
import {usePostWithUrlPath} from "@/hooks/usePost";
import {Divider, List, Spin} from "antd";
import Link from "next/link";

export function MainPostView({urlPath}: UrlPathPage) {

    const {posts, isLoading, isError} = usePostWithUrlPath(urlPath);

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
            <div className={mainLayout}>
                <List
                    bordered
                    dataSource={posts}
                    renderItem={post => (
                        <div className={innerLayout}>
                            <Link href={"/post/" + post.postId}>
                                <a>
                                    <h1>{post.title}</h1>
                                </a>
                            </Link>
                            <p>Author : {post.author}</p>
                            <p>{post.postTime} {post.updateTime ? "(Last Update : " + post.updateTime + ")" : ""}</p>
                            <div className={overflowVisible} dangerouslySetInnerHTML={{__html: post.content}}></div>
                            <Link href={"" + post.postId}>
                                <a>
                                    Read more..
                                </a>
                            </Link>
                            <Divider/>
                        </div>
                    )}
                />
            </div>
        </>
    );
}
