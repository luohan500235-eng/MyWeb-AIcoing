import { FormEvent, useEffect, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { adminApi } from '../api';
import type { SiteConfig } from '../types';

const emptySiteConfig: Partial<SiteConfig> = {
  siteName: '',
  siteSubtitle: '',
  siteDescription: '',
  siteKeywords: '',
  logo: '',
  favicon: '',
  authorName: '',
  authorIntro: '',
  githubUrl: '',
  email: '',
  aboutMd: '',
  icp: '',
};

export function SiteConfigPage() {
  const queryClient = useQueryClient();
  const [formState, setFormState] = useState<Partial<SiteConfig>>(emptySiteConfig);
  const siteConfigQuery = useQuery({ queryKey: ['admin-site-config'], queryFn: adminApi.getSiteConfig });

  useEffect(() => {
    if (siteConfigQuery.data) {
      setFormState(siteConfigQuery.data);
    }
  }, [siteConfigQuery.data]);

  const saveMutation = useMutation({
    mutationFn: () => adminApi.updateSiteConfig(formState),
    onSuccess: async () => { await queryClient.invalidateQueries({ queryKey: ['admin-site-config'] }); window.alert('站点配置已更新'); },
  });

  function handleSubmit(event: FormEvent<HTMLFormElement>) { event.preventDefault(); saveMutation.mutate(); }

  return (
    <form className="stack-24" onSubmit={handleSubmit}>
      <header className="panel toolbar"><h2>站点配置</h2><button className="primary-button" type="submit">保存配置</button></header>
      <section className="panel form-grid">
        <input value={formState.siteName ?? ''} onChange={(event) => setFormState({ ...formState, siteName: event.target.value })} placeholder="站点名称" />
        <input value={formState.siteSubtitle ?? ''} onChange={(event) => setFormState({ ...formState, siteSubtitle: event.target.value })} placeholder="站点副标题" />
        <textarea value={formState.siteDescription ?? ''} onChange={(event) => setFormState({ ...formState, siteDescription: event.target.value })} rows={3} placeholder="站点描述" />
        <input value={formState.siteKeywords ?? ''} onChange={(event) => setFormState({ ...formState, siteKeywords: event.target.value })} placeholder="SEO 关键词" />
        <input value={formState.authorName ?? ''} onChange={(event) => setFormState({ ...formState, authorName: event.target.value })} placeholder="作者名" />
        <input value={formState.authorIntro ?? ''} onChange={(event) => setFormState({ ...formState, authorIntro: event.target.value })} placeholder="作者简介" />
        <input value={formState.githubUrl ?? ''} onChange={(event) => setFormState({ ...formState, githubUrl: event.target.value })} placeholder="GitHub 链接" />
        <input value={formState.email ?? ''} onChange={(event) => setFormState({ ...formState, email: event.target.value })} placeholder="联系邮箱" />
        <textarea value={formState.aboutMd ?? ''} onChange={(event) => setFormState({ ...formState, aboutMd: event.target.value })} rows={12} placeholder="关于页 Markdown 内容" />
      </section>
    </form>
  );
}