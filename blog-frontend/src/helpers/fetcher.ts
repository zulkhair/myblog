export default async function fetcher<JSON = any>(
    url: RequestInfo,
    token: string,
    init?: RequestInit
): Promise<JSON> {

    const res = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        ...init,
    });

    // handle error and get error object
    if (!res.ok) {
        const data = await res.json();
        const error = new Error(data.error);
        throw error;
    }

    // handle success and get json data
    const resp = await res.json();
    return resp;
}
