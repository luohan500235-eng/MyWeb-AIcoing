import { Link } from 'react-router-dom';
import type { PostSummary } from '../types';

export function PostCard({ post }: { post: PostSummary }) {
  return (
    <article className="post-card">
      <div className="post-card-meta">
        <span>{post.categoryName ?? '未分类'}</span>
        <span>{post.publishedAt ? new Date(post.publishedAt).toLocaleDateString() : '未发布'}</span>
        <span>{post.viewCount} 次阅读</span>
      </div>
      <h3>
        <Link to={`/posts/${post.slug}`}>{post.title}</Link>
      </h3>
      <p>{post.summary}</p>
      <div className="tag-row">
        {post.tags.map((tag) => <span key={tag} className="tag-chip">{tag}</span>)}
      </div>
    </article>
  );
}