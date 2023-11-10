export interface PostResponse {
    postId: string,
    title: string;
    content: string;
    postTime: string;
    updateTime: string;
    author: string;
    comments: Array<CommentResponse>;
    viewCount: number;
    deleteComment: boolean;
}

export interface CommentResponse {
    commentId: string;
    content: string;
    postTime: string;
    name: string;

}

export interface CreatePost {
    title: string;
    content: string;
}

export interface CreateComment {
    postId: string;
    name: string;
    content: string;
}

export interface PostIdPage {
    postId: string;
}

export interface UrlPathPage {
    urlPath: string;
}