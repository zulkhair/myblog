import useSWR from 'swr';
import fetcher from '@/helpers/fetcher';
import fetcherNoAuth from '@/helpers/fetcherNoAuth';
import type {PostResponse} from '@/types/post';
import {getSessionToken} from "@/helpers/token";

interface Data<Type> {
    data: Type;
}

export function usePost(token: string) {
    const key = process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/admin';
    const {data, error, mutate} = useSWR<Data<PostResponse[]>>([key, token], fetcher);

    return {
        posts: data?.data,
        isLoading: !error && !data,
        isError: error,
        mutate: mutate,
    };
}

export function usePostWithId(token: string, postId: string) {
    const key = process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/view/' + postId;
    const {data, error, mutate} = useSWR<Data<PostResponse>>([key, token], fetcher);

    return {
        post: data?.data,
        isLoading: !error && !data,
        isError: error,
        mutate: mutate,
    };
}

export function usePostWithUrlPath(urlPath: string) {
    const key = process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/view?urlPath=' + urlPath;
    const {data, error, mutate} = useSWR<Data<PostResponse[]>>([key], fetcherNoAuth);

    return {
        posts: data?.data,
        isLoading: !error && !data,
        isError: error,
        mutate: mutate,
    };
}

export function usePostAll() {
    const key = process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/view';
    const {data, error, mutate} = useSWR<Data<PostResponse[]>>([key], fetcherNoAuth);

    return {
        posts: data?.data,
        isLoading: !error && !data,
        isError: error,
        mutate: mutate,
    };
}


export function usePostDetail(postId: string) {
    const key = process.env.NEXT_PUBLIC_BACKEND_API_URL + '/post/view/' + postId;
    const {data, error, mutate} = useSWR<Data<PostResponse>>([key, getSessionToken()], fetcher);

    return {
        post: data?.data,
        isLoading: !error && !data,
        isError: error,
        mutate: mutate,
    };
}

