import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { adminApi } from '../api';

export function DashboardPage() {
  const { data } = useQuery({ queryKey: ['dashboard-stats'], queryFn: adminApi.getDashboardStats });

  useEffect(() => {
    document.title = '仪表盘 | MyWeb Admin';
  }, []);

  return (
    <div className="stack-24">
      <header className="panel">
        <h2>项目概览</h2>
        <p>第一阶段的核心模块都在这里收口：内容、评论、结构和站点信息。</p>
      </header>
      <section className="stat-grid">
        <article className="stat-card"><strong>{data?.postCount ?? 0}</strong><span>文章总数</span></article>
        <article className="stat-card"><strong>{data?.categoryCount ?? 0}</strong><span>分类数</span></article>
        <article className="stat-card"><strong>{data?.tagCount ?? 0}</strong><span>标签数</span></article>
        <article className="stat-card"><strong>{data?.pendingCommentCount ?? 0}</strong><span>待审核评论</span></article>
      </section>
    </div>
  );
}