import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { adminApi } from '../api';

export function CommentPage() {
  const queryClient = useQueryClient();
  const [status, setStatus] = useState<number | ''>('');
  const { data } = useQuery({ queryKey: ['admin-comments', status], queryFn: () => adminApi.getComments({ page: 1, size: 20, status }) });
  const updateMutation = useMutation({ mutationFn: ({ id, nextStatus }: { id: number; nextStatus: number }) => adminApi.updateCommentStatus(id, nextStatus), onSuccess: async () => { await queryClient.invalidateQueries({ queryKey: ['admin-comments'] }); } });

  return (
    <div className="stack-24">
      <header className="panel toolbar compact">
        <h2>评论审核</h2>
        <select value={status} onChange={(event) => setStatus(event.target.value === '' ? '' : Number(event.target.value))}><option value="">全部</option><option value={0}>待审核</option><option value={1}>已通过</option><option value={2}>已拒绝</option></select>
      </header>
      <section className="panel"><table className="table"><thead><tr><th>文章</th><th>用户</th><th>内容</th><th>状态</th><th>操作</th></tr></thead><tbody>{data?.records.map((item) => <tr key={item.id}><td>{item.postTitle}</td><td>{item.nickname}</td><td>{item.content}</td><td>{item.status === 0 ? '待审核' : item.status === 1 ? '已通过' : '已拒绝'}</td><td className="action-row"><button type="button" onClick={() => updateMutation.mutate({ id: item.id, nextStatus: 1 })}>通过</button><button type="button" onClick={() => updateMutation.mutate({ id: item.id, nextStatus: 2 })}>拒绝</button></td></tr>)}</tbody></table></section>
    </div>
  );
}