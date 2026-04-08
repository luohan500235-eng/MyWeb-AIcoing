import { ChangeEvent, FormEvent, useEffect, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router-dom';
import { adminApi } from '../api';

const defaultState = {
  title: '',
  slug: '',
  summary: '',
  coverImage: '',
  contentMd: '# 新文章\n\n从这里开始写作。',
  categoryId: undefined as number | undefined,
  status: 0,
  isTop: 0,
  allowComment: 1,
  seoTitle: '',
  seoDescription: '',
  tagIds: [] as number[],
};

export function PostEditorPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { id } = useParams();
  const [formState, setFormState] = useState(defaultState);
  const { data: categories = [] } = useQuery({ queryKey: ['admin-categories'], queryFn: adminApi.getCategories });
  const { data: tags = [] } = useQuery({ queryKey: ['admin-tags'], queryFn: adminApi.getTags });
  const postQuery = useQuery({ queryKey: ['admin-post', id], queryFn: () => adminApi.getPost(id!), enabled: Boolean(id) });

  useEffect(() => {
    document.title = `${id ? '编辑文章' : '新建文章'} | MyWeb Admin`;
  }, [id]);

  useEffect(() => {
    if (postQuery.data) {
      setFormState({
        title: postQuery.data.title,
        slug: postQuery.data.slug,
        summary: postQuery.data.summary,
        coverImage: postQuery.data.coverImage ?? '',
        contentMd: postQuery.data.contentMd,
        categoryId: postQuery.data.categoryId,
        status: postQuery.data.status,
        isTop: postQuery.data.isTop,
        allowComment: postQuery.data.allowComment,
        seoTitle: postQuery.data.seoTitle ?? '',
        seoDescription: postQuery.data.seoDescription ?? '',
        tagIds: postQuery.data.tagIds ?? [],
      });
    }
  }, [postQuery.data]);

  const saveMutation = useMutation({
    mutationFn: () => adminApi.savePost({ ...formState, id: id ? Number(id) : undefined }),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['admin-posts'] });
      navigate('/posts');
    },
    onError: (error) => window.alert((error as Error).message),
  });

  const uploadMutation = useMutation({
    mutationFn: adminApi.uploadFile,
    onSuccess: (data) => setFormState((current) => ({ ...current, coverImage: data.url })),
    onError: (error) => window.alert((error as Error).message),
  });

  function handleUpload(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    if (file) uploadMutation.mutate(file);
  }

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    saveMutation.mutate();
  }

  return (
    <form className="stack-24" onSubmit={handleSubmit}>
      <header className="panel toolbar"><h2>{id ? '编辑文章' : '新建文章'}</h2><button className="primary-button" type="submit">保存文章</button></header>
      <section className="panel form-grid">
        <input value={formState.title} onChange={(event) => setFormState({ ...formState, title: event.target.value })} placeholder="文章标题" required />
        <input value={formState.slug} onChange={(event) => setFormState({ ...formState, slug: event.target.value })} placeholder="slug，例如 hello-world" required />
        <textarea value={formState.summary} onChange={(event) => setFormState({ ...formState, summary: event.target.value })} placeholder="文章摘要" rows={3} />
        <textarea value={formState.contentMd} onChange={(event) => setFormState({ ...formState, contentMd: event.target.value })} placeholder="Markdown 内容" rows={18} required />
      </section>
      <section className="panel two-column">
        <div className="stack-16">
          <label>分类
            <select value={formState.categoryId ?? ''} onChange={(event) => setFormState({ ...formState, categoryId: event.target.value ? Number(event.target.value) : undefined })}>
              <option value="">未分类</option>
              {categories.map((category) => <option value={category.id} key={category.id}>{category.name}</option>)}
            </select>
          </label>
          <label>状态
            <select value={formState.status} onChange={(event) => setFormState({ ...formState, status: Number(event.target.value) })}>
              <option value={0}>草稿</option>
              <option value={1}>已发布</option>
              <option value={2}>已下线</option>
            </select>
          </label>
          <label>封面图 URL
            <input value={formState.coverImage} onChange={(event) => setFormState({ ...formState, coverImage: event.target.value })} placeholder="封面图地址" />
          </label>
          <label>上传封面图
            <input type="file" onChange={handleUpload} />
          </label>
        </div>
        <div className="stack-16">
          <label>SEO 标题
            <input value={formState.seoTitle} onChange={(event) => setFormState({ ...formState, seoTitle: event.target.value })} />
          </label>
          <label>SEO 描述
            <textarea value={formState.seoDescription} onChange={(event) => setFormState({ ...formState, seoDescription: event.target.value })} rows={4} />
          </label>
          <label className="checkbox-row"><input type="checkbox" checked={formState.isTop === 1} onChange={(event) => setFormState({ ...formState, isTop: event.target.checked ? 1 : 0 })} />置顶文章</label>
          <label className="checkbox-row"><input type="checkbox" checked={formState.allowComment === 1} onChange={(event) => setFormState({ ...formState, allowComment: event.target.checked ? 1 : 0 })} />允许评论</label>
        </div>
      </section>
      <section className="panel stack-16">
        <h3>标签</h3>
        <div className="tag-selector">
          {tags.map((tag) => {
            const active = formState.tagIds.includes(tag.id);
            return (
              <button type="button" key={tag.id} className={active ? 'tag-toggle active' : 'tag-toggle'} onClick={() => setFormState({ ...formState, tagIds: active ? formState.tagIds.filter((value) => value !== tag.id) : [...formState.tagIds, tag.id] })}>
                {tag.name}
              </button>
            );
          })}
        </div>
      </section>
    </form>
  );
}