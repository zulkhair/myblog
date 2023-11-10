import React from 'react';
import 'react-quill/dist/quill.snow.css';
import {details, innerLayout, mainLayout, overflowVisible} from "@/pages/style";
import {usePostAll} from "@/hooks/usePost";
import {Divider, List, Spin} from "antd";
import Link from "next/link";

export function MainPostViewAll() {

    const {posts, isLoading, isError} = usePostAll();

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
                            <p className={details}>By {post.author}</p>
                            <p className={details}>Posted
                                in {post.postTime} {post.updateTime ? "(Last Update : " + post.updateTime + ")" : ""}</p>
                            <p className={details}>Viewed {post.viewCount} times</p>
                            <div className={overflowVisible} dangerouslySetInnerHTML={{__html: post.content}}></div>
                            <Link href={"/post/" + post.postId}>
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
