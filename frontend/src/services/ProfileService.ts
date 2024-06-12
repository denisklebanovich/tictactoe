import axios from "axios";
import {getAuthToken} from "./AuthService";

const profileClient = axios.create({
    baseURL: '/api/profile',
    withCredentials: true,
});

export default class ProfileService {
    static async getProfileImage(username: string) {
        const token = getAuthToken();
        try {
            const response = await profileClient.get(`${username}/image`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response?.status === 404) {
                return null;
            }
            throw error;
        }
    }

    static async setProfileImage(image: File) {
        const token = getAuthToken();
        const formData = new FormData();
        formData.append('file', image);
        return profileClient.post('/image', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${token}`
            }
        });
    }
}