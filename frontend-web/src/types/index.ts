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

export interface PostSummary {
  id: number;
  title: string;
  slug: string;
  summary: string;
  coverImage?: string;
  isTop: number;
  viewCount: number;
  publishedAt?: string;
  categoryName?: string;
  categoryId?: number;
  tags: string[];
}

export interface PostDetail extends PostSummary {
  contentMd: string;
  allowComment: number;
  seoTitle?: string;
  seoDescription?: string;
}

export interface ArchiveItem {
  yearMonth: string;
  post: PostSummary;
}

export interface CommentItem {
  id: number;
  postId: number;
  parentId: number;
  replyToId?: number;
  nickname: string;
  website?: string;
  content: string;
  createdAt: string;
}