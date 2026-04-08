import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { publicApi } from '../api/public';
import { PostCard } from '../components/PostCard';

export function HomePage() {
  const { data: latestPosts = [] } = useQuery({ queryKey: ['latest-posts'], queryFn: () => publicApi.getLatestPosts(6) });
  const { data: siteConfig } = useQuery({ queryKey: ['site-config'], queryFn: publicApi.getSiteConfig });

  useEffect(() => {
    document.title = `${siteConfig?.siteName ?? 'MyWeb Blog'} | 首页`;
  }, [siteConfig]);

  return (
    <div className="stack-24">
      <section className="hero-panel">
        <div>
          <p className="eyebrow">Personal Publishing System</p>
          <h1>{siteConfig?.siteName ?? 'MyWeb Blog'}</h1>
          <p className="hero-text">{siteConfig?.siteSubtitle}</p>
          <div className="hero-actions">
            <Link to="/posts" className="primary-link">开始阅读</Link>
            <Link to="/about" className="ghost-link">认识站长</Link>
          </div>
        </div>
      </section>

      <section className="stack-16">
        <div className="section-head">
          <h2>最近更新</h2>
          <Link to="/posts">查看全部</Link>
        </div>
        <div className="post-grid">
          {latestPosts.map((post) => <PostCard key={post.id} post={post} />)}
        </div>
      </section>
    </div>
  );
}