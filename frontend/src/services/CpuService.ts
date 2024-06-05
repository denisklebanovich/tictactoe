import axios from "axios";

const cpuClient = axios.create({
    baseURL: '/api/cpu',
    withCredentials: true,
});

export async function loadCPU() {
    return cpuClient.get('/load');
}