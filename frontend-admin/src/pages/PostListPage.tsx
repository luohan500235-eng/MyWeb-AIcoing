import { useEffect, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { adminApi } from '../api';

export function PostListPage() {
  const queryClient = useQueryClient();
  const [keyword, setKeyword] = useState('');
  const [status, setStatus] = useState<number | ''>('');
  const { data, isLoading } = useQuery({ queryKey: ['admin-posts', keyword, status], queryFn: () => adminApi.getPosts({ page: 1, size: 20, keyword, status }) });
  const deleteMutation = useMutation({
    mutationFn: adminApi.deletePost,
    onSuccess: async () => { await queryClient.invalidateQueries({ queryKey: ['admin-posts'] }); },
    onError: (error) => window.alert((error as Error).message),
  });

  useEffect(() => { document.title = '文章管理 | MyWeb Admin'; }, []);

  return (
    <div className="stack-24">
      <header className="panel toolbar">
        <div>
          <h2>文章管理</h2>
          <p>支持草稿、发布、SEO 字段和封面图。</p>
        </div>
        <Link className="primary-button" to="/posts/new">写新文章</Link>
      </header>
      <section className="panel toolbar compact">
        <input value={keyword} onChange={(event) => setKeyword(event.target.value)} placeholder="搜索标题或摘要" />
        <select value={status} onChange={(event) => setStatus(event.target.value === '' ? '' : Number(event.target.value))}>
          <option value="">全部状态</option>
          <option value="0">草稿</option>
          <option value="1">已发布</option>
          <option value="2">已下线</option>
        </select>
      </section>
      <section className="panel">
        {isLoading ? <p>加载中...</p> : null}
        <table className="table">
          <thead>
            <tr><th>标题</th><th>分类</th><th>状态</th><th>标签</th><th>更新时间</th><th>操作</th></tr>
          </thead>
          <tbody>
            {data?.records.map((post) => (
              <tr key={post.id}>
                <td>{post.title}</td>
                <td>{post.categoryName ?? '未分类'}</td>
                <td>{post.status === 1 ? '已发布' : post.status === 0 ? '草稿' : '已下线'}</td>
                <td>{post.tags.join(' / ')}</td>
                <td>{post.updatedAt ? new Date(post.updatedAt).toLocaleString() : '-'}</td>
                <td className="action-row">
                  <Link to={`/posts/${post.id}/edit`}>编辑</Link>
                  <button type="button" onClick={() => deleteMutation.mutate(post.id)}>删除</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
}