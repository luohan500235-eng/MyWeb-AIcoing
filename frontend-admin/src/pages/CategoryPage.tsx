import { FormEvent, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { adminApi } from '../api';
import type { Category } from '../types';

const emptyCategory = { name: '', slug: '', description: '', sort: 1, status: 1 };

export function CategoryPage() {
  const queryClient = useQueryClient();
  const [editing, setEditing] = useState<Partial<Category>>(emptyCategory);
  const { data = [] } = useQuery({ queryKey: ['admin-categories'], queryFn: adminApi.getCategories });
  const saveMutation = useMutation({ mutationFn: () => adminApi.saveCategory(editing), onSuccess: async () => { setEditing(emptyCategory); await queryClient.invalidateQueries({ queryKey: ['admin-categories'] }); } });
  const deleteMutation = useMutation({ mutationFn: adminApi.deleteCategory, onSuccess: async () => { await queryClient.invalidateQueries({ queryKey: ['admin-categories'] }); } });

  function handleSubmit(event: FormEvent<HTMLFormElement>) { event.preventDefault(); saveMutation.mutate(); }

  return (
    <div className="stack-24">
      <form className="panel form-grid" onSubmit={handleSubmit}>
        <h2>分类管理</h2>
        <input value={editing.name ?? ''} onChange={(event) => setEditing({ ...editing, name: event.target.value })} placeholder="分类名称" required />
        <input value={editing.slug ?? ''} onChange={(event) => setEditing({ ...editing, slug: event.target.value })} placeholder="slug" required />
        <input value={editing.description ?? ''} onChange={(event) => setEditing({ ...editing, description: event.target.value })} placeholder="描述" />
        <div className="two-column">
          <input type="number" value={editing.sort ?? 1} onChange={(event) => setEditing({ ...editing, sort: Number(event.target.value) })} placeholder="排序" />
          <select value={editing.status ?? 1} onChange={(event) => setEditing({ ...editing, status: Number(event.target.value) })}><option value={1}>启用</option><option value={0}>禁用</option></select>
        </div>
        <button className="primary-button" type="submit">{editing.id ? '更新分类' : '新增分类'}</button>
      </form>
      <section className="panel"><table className="table"><thead><tr><th>名称</th><th>slug</th><th>排序</th><th>状态</th><th>操作</th></tr></thead><tbody>{data.map((item) => <tr key={item.id}><td>{item.name}</td><td>{item.slug}</td><td>{item.sort}</td><td>{item.status === 1 ? '启用' : '禁用'}</td><td className="action-row"><button type="button" onClick={() => setEditing(item)}>编辑</button><button type="button" onClick={() => deleteMutation.mutate(item.id)}>删除</button></td></tr>)}</tbody></table></section>
    </div>
  );
}