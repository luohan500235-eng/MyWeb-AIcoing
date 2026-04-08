import apiClient from './client';
import type { ApiResponse, ArchiveItem, Category, CommentItem, PageResponse, PostDetail, PostSummary, SiteConfig, Tag } from '../types';

async function unwrap<T>(promise: Promise<{ data: ApiResponse<T> }>): Promise<T> {
  const response = await promise;
  return response.data.data;
}

export const publicApi = {
  getSiteConfig: () => unwrap(apiClient.get<ApiResponse<SiteConfig>>('/api/public/site-config')),
  getCategories: () => unwrap(apiClient.get<ApiResponse<Category[]>>('/api/public/categories')),
  getTags: () => unwrap(apiClient.get<ApiResponse<Tag[]>>('/api/public/tags')),
  getLatestPosts: (limit = 6) => unwrap(apiClient.get<ApiResponse<PostSummary[]>>('/api/public/posts/latest', { params: { limit } })),
  getPosts: (params: { page?: number; size?: number; keyword?: string; categoryId?: string; tagId?: string }) =>
    unwrap(apiClient.get<ApiResponse<PageResponse<PostSummary>>>('/api/public/posts', { params })),
  getPostDetail: (slug: string) => unwrap(apiClient.get<ApiResponse<PostDetail>>(`/api/public/posts/${slug}`)),
  getArchives: () => unwrap(apiClient.get<ApiResponse<ArchiveItem[]>>('/api/public/archives')),
  getComments: (postId: number) => unwrap(apiClient.get<ApiResponse<CommentItem[]>>('/api/public/comments', { params: { postId } })),
  submitComment: (payload: { postId: number; nickname: string; email?: string; website?: string; content: string }) =>
    unwrap(apiClient.post<ApiResponse<null>>('/api/public/comments', payload)),
};