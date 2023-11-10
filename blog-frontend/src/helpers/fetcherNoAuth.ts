export default async function fetcherNoAuth<JSON = any>(
  url: RequestInfo,
  init?: RequestInit
): Promise<JSON> {

  const res = await fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
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
