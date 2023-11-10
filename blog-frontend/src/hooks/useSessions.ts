import useSWR from 'swr';
import fetcher from '@/helpers/fetcher';
import type {PostResponse} from '@/types/post';
import {getSessionToken} from "@/helpers/token";

interface Data<Type> {
    data: Type;
}

export function checkSession() {
    const key = process.env.NEXT_PUBLIC_BACKEND_API_URL + '/session';
    const {data, error, mutate} = useSWR<Data<PostResponse[]>>([key, getSessionToken()], fetcher);

    return {
        isLoading: !error && !data,
        isError: error,
        mutate: mutate,
    };
}
