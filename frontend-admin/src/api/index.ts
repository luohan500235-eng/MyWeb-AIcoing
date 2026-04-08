import adminClient from './client';
import type { ApiResponse, AuthUser, Category, CommentItem, DashboardStats, PageResponse, Post, SiteConfig, Tag } from '../types';

async function unwrap<T>(promise: Promise<{ data: ApiResponse<T> }>): Promise<T> {
  const response = await promise;
  return response.data.data;
}

export const authApi = {
  login: (payload: { username: string; password: string }) => unwrap(adminClient.post<ApiResponse<{ token: string; username: string; nickname: string }>>('/api/auth/login', payload)),
  me: () => unwrap(adminClient.get<ApiResponse<AuthUser>>('/api/auth/me')),
};

export const adminApi = {
  getDashboardStats: () => unwrap(adminClient.get<ApiResponse<DashboardStats>>('/api/admin/dashboard/stats')),
  getPosts: (params: { page?: number; size?: number; keyword?: string; status?: number | '' }) => unwrap(adminClient.get<ApiResponse<PageResponse<Post>>>('/api/admin/posts', { params })),
  getPost: (id: string) => unwrap(adminClient.get<ApiResponse<Post>>(`/api/admin/posts/${id}`)),
  savePost: (payload: Partial<Post>) => payload.id
    ? unwrap(adminClient.put<ApiResponse<Post>>(`/api/admin/posts/${payload.id}`, payload))
    : unwrap(adminClient.post<ApiResponse<Post>>('/api/admin/posts', payload)),
  deletePost: (id: number) => unwrap(adminClient.delete<ApiResponse<null>>(`/api/admin/posts/${id}`)),
  uploadFile: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return unwrap(adminClient.post<ApiResponse<{ id: number; name: string; url: string }>>('/api/admin/files/upload', formData));
  },
  getCategories: () => unwrap(adminClient.get<ApiResponse<Category[]>>('/api/admin/categories')),
  saveCategory: (payload: Partial<Category>) => payload.id
    ? unwrap(adminClient.put<ApiResponse<Category>>(`/api/admin/categories/${payload.id}`, payload))
    : unwrap(adminClient.post<ApiResponse<Category>>('/api/admin/categories', payload)),
  deleteCategory: (id: number) => unwrap(adminClient.delete<ApiResponse<null>>(`/api/admin/categories/${id}`)),
  getTags: () => unwrap(adminClient.get<ApiResponse<Tag[]>>('/api/admin/tags')),
  saveTag: (payload: Partial<Tag>) => payload.id
    ? unwrap(adminClient.put<ApiResponse<Tag>>(`/api/admin/tags/${payload.id}`, payload))
    : unwrap(adminClient.post<ApiResponse<Tag>>('/api/admin/tags', payload)),
  deleteTag: (id: number) => unwrap(adminClient.delete<ApiResponse<null>>(`/api/admin/tags/${id}`)),
  getComments: (params: { page?: number; size?: number; status?: number | '' }) => unwrap(adminClient.get<ApiResponse<PageResponse<CommentItem>>>('/api/admin/comments', { params })),
  updateCommentStatus: (id: number, status: number) => unwrap(adminClient.put<ApiResponse<null>>(`/api/admin/comments/${id}/status`, null, { params: { status } })),
  getSiteConfig: () => unwrap(adminClient.get<ApiResponse<SiteConfig>>('/api/admin/site-config')),
  updateSiteConfig: (payload: Partial<SiteConfig>) => unwrap(adminClient.put<ApiResponse<SiteConfig>>('/api/admin/site-config', payload)),
};