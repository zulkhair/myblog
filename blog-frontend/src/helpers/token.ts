import {deleteCookie, getCookie, setCookie} from "cookies-next";

export const getSessionToken = (): string => {
    const token = getCookie("token");
    return String(token);
}

export const setSessionToken = (token: string) => {
    setCookie("token", token);
}

export const deleteSessionToken = () => {
    deleteCookie("token");
}