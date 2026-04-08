export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface PageResponse<T> {
  records: T[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}

export interface AuthUser {
  id: number;
  username: string;
  nickname: string;
  avatar?: string;
  email?: string;
}

export interface DashboardStats {
  postCount: number;
  categoryCount: number;
  tagCount: number;
  pendingCommentCount: number;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  description?: string;
  sort: number;
  status: number;
}

export interface Tag {
  id: number;
  name: string;
  slug: string;
  color?: string;
}

export interface Post {
  id: number;
  title: string;
  slug: string;
  summary: string;
  coverImage?: string;
  contentMd: string;
  categoryId?: number;
  categoryName?: string;
  status: number;
  isTop: number;
  allowComment: number;
  viewCount: number;
  seoTitle?: string;
  seoDescription?: string;
  publishedAt?: string;
  createdAt?: string;
  updatedAt?: string;
  tagIds: number[];
  tags: string[];
}

export interface CommentItem {
  id: number;
  postId: number;
  postTitle: string;
  nickname: string;
  email?: string;
  website?: string;
  content: string;
  status: number;
  createdAt: string;
}

export interface SiteConfig {
  id: number;
  siteName: string;
  siteSubtitle: string;
  siteDescription: string;
  siteKeywords: string;
  logo?: string;
  favicon?: string;
  authorName: string;
  authorIntro: string;
  githubUrl?: string;
  email?: string;
  aboutMd: string;
  icp?: string;
}