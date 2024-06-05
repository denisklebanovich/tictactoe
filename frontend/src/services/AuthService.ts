import axios, {AxiosResponse} from "axios";

const authClient = axios.create({
    baseURL: '/api/auth',
    withCredentials: true,
});

export interface AuthResponse {
    accessToken: string;
    refreshToken: string;
}

export async function register(email: string, username: string, password: string) {
    return authClient.post('/register', {email, username, password});
}

//@ts-ignore
export async function login(username: string, password: string): Promise<void> {
    const response: AxiosResponse<AuthResponse> = await authClient.post('/login', {username, password});
    const {accessToken, refreshToken} = response.data;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    localStorage.setItem('username', username);
}

//@ts-ignore
export const logout = async () => {
    const accessToken = localStorage.getItem('accessToken');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('username');
    if (!accessToken) return;
    await authClient.post('/logout', {accessToken});
};

//@ts-ignore
export const refreshToken = async () => {
    const refreshTokenSaved = localStorage.getItem('refreshToken');
    if (!refreshTokenSaved) {
        return;
    }

    try {
        const response = await authClient.post('/refresh', {refreshToken: refreshTokenSaved});
        const {accessToken, refreshToken} = response.data;
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
    } catch (error) {
        console.error('Token refresh failed:', error);
    }
};