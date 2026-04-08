import { FormEvent, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { adminApi } from '../api';
import type { Tag } from '../types';

const emptyTag = { name: '', slug: '', color: '#1f7ae0' };

export function TagPage() {
  const queryClient = useQueryClient();
  const [editing, setEditing] = useState<Partial<Tag>>(emptyTag);
  const { data = [] } = useQuery({ queryKey: ['admin-tags'], queryFn: adminApi.getTags });
  const saveMutation = useMutation({ mutationFn: () => adminApi.saveTag(editing), onSuccess: async () => { setEditing(emptyTag); await queryClient.invalidateQueries({ queryKey: ['admin-tags'] }); } });
  const deleteMutation = useMutation({ mutationFn: adminApi.deleteTag, onSuccess: async () => { await queryClient.invalidateQueries({ queryKey: ['admin-tags'] }); } });

  function handleSubmit(event: FormEvent<HTMLFormElement>) { event.preventDefault(); saveMutation.mutate(); }

  return (
    <div className="stack-24">
      <form className="panel form-grid" onSubmit={handleSubmit}>
        <h2>标签管理</h2>
        <input value={editing.name ?? ''} onChange={(event) => setEditing({ ...editing, name: event.target.value })} placeholder="标签名称" required />
        <input value={editing.slug ?? ''} onChange={(event) => setEditing({ ...editing, slug: event.target.value })} placeholder="slug" required />
        <input value={editing.color ?? ''} onChange={(event) => setEditing({ ...editing, color: event.target.value })} placeholder="颜色值" />
        <button className="primary-button" type="submit">{editing.id ? '更新标签' : '新增标签'}</button>
      </form>
      <section className="panel"><table className="table"><thead><tr><th>名称</th><th>slug</th><th>颜色</th><th>操作</th></tr></thead><tbody>{data.map((item) => <tr key={item.id}><td>{item.name}</td><td>{item.slug}</td><td><span className="color-dot" style={{ backgroundColor: item.color ?? '#183153' }} />{item.color}</td><td className="action-row"><button type="button" onClick={() => setEditing(item)}>编辑</button><button type="button" onClick={() => deleteMutation.mutate(item.id)}>删除</button></td></tr>)}</tbody></table></section>
    </div>
  );
}