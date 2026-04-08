import { FormEvent, useEffect, useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import ReactMarkdown from 'react-markdown';
import { useParams } from 'react-router-dom';
import rehypeHighlight from 'rehype-highlight';
import remarkGfm from 'remark-gfm';
import { publicApi } from '../api/public';

export function PostDetailPage() {
  const { slug = '' } = useParams();
  const queryClient = useQueryClient();
  const [formState, setFormState] = useState({ nickname: '', email: '', website: '', content: '' });
  const { data: post, isLoading, error } = useQuery({ queryKey: ['post', slug], queryFn: () => publicApi.getPostDetail(slug) });
  const { data: comments = [] } = useQuery({
    queryKey: ['comments', post?.id],
    queryFn: () => publicApi.getComments(post!.id),
    enabled: Boolean(post?.id),
  });

  const submitComment = useMutation({
    mutationFn: () => publicApi.submitComment({ postId: post!.id, ...formState }),
    onSuccess: async () => {
      setFormState({ nickname: '', email: '', website: '', content: '' });
      await queryClient.invalidateQueries({ queryKey: ['comments', post?.id] });
      window.alert('评论已提交，等待审核');
    },
    onError: (mutationError) => {
      window.alert((mutationError as Error).message);
    },
  });

  useEffect(() => {
    if (post) {
      document.title = `${post.title} | MyWeb Blog`;
    }
  }, [post]);

  const createdLabel = useMemo(() => (post?.publishedAt ? new Date(post.publishedAt).toLocaleString() : '未发布'), [post]);

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    submitComment.mutate();
  }

  if (isLoading) return <p>文章加载中...</p>;
  if (error) return <p className="error-text">{(error as Error).message}</p>;
  if (!post) return <p>文章不存在。</p>;

  return (
    <div className="stack-24">
      <article className="markdown-panel">
        <div className="post-card-meta">
          <span>{post.categoryName ?? '未分类'}</span>
          <span>{createdLabel}</span>
          <span>{post.viewCount} 次阅读</span>
        </div>
        <h1>{post.title}</h1>
        <p className="article-summary">{post.summary}</p>
        <div className="tag-row">
          {post.tags.map((tag) => <span key={tag} className="tag-chip">{tag}</span>)}
        </div>
        <ReactMarkdown remarkPlugins={[remarkGfm]} rehypePlugins={[rehypeHighlight]}>
          {post.contentMd}
        </ReactMarkdown>
      </article>

      <section className="comment-panel">
        <div className="section-head"><h2>评论</h2><span>{comments.length} 条已展示</span></div>
        <div className="comment-list">
          {comments.map((comment) => (
            <div key={comment.id} className="comment-item">
              <strong>{comment.nickname}</strong>
              <span>{new Date(comment.createdAt).toLocaleString()}</span>
              <p>{comment.content}</p>
            </div>
          ))}
        </div>

        {post.allowComment === 1 ? (
          <form className="comment-form" onSubmit={handleSubmit}>
            <input placeholder="昵称" value={formState.nickname} onChange={(event) => setFormState({ ...formState, nickname: event.target.value })} required />
            <input placeholder="邮箱（选填）" value={formState.email} onChange={(event) => setFormState({ ...formState, email: event.target.value })} />
            <input placeholder="网站（选填）" value={formState.website} onChange={(event) => setFormState({ ...formState, website: event.target.value })} />
            <textarea placeholder="写下你的看法..." rows={5} value={formState.content} onChange={(event) => setFormState({ ...formState, content: event.target.value })} required />
            <button type="submit" disabled={submitComment.isPending}>{submitComment.isPending ? '提交中...' : '提交评论'}</button>
          </form>
        ) : (
          <p>作者已关闭此文章评论。</p>
        )}
      </section>
    </div>
  );
}