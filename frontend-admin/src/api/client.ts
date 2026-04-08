import axios from 'axios';

const adminClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080',
  timeout: 15000,
});

adminClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

adminClient.interceptors.response.use((response) => response, (error) => {
  const message = error.response?.data?.message ?? error.message ?? '请求失败';
  if (error.response?.status === 401) {
    localStorage.removeItem('admin_token');
  }
  return Promise.reject(new Error(message));
});

export default adminClient;