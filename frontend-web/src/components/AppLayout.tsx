import { Link, NavLink, Outlet } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { publicApi } from '../api/public';

export function AppLayout() {
  const { data: siteConfig } = useQuery({ queryKey: ['site-config'], queryFn: publicApi.getSiteConfig });
  const { data: categories = [] } = useQuery({ queryKey: ['categories'], queryFn: publicApi.getCategories });

  return (
    <div className="site-shell">
      <header className="site-header">
        <div>
          <Link to="/" className="brand">{siteConfig?.siteName ?? 'MyWeb Blog'}</Link>
          <p className="brand-subtitle">{siteConfig?.siteSubtitle ?? '把想法和代码留在这里'}</p>
        </div>
        <nav className="site-nav">
          <NavLink to="/">首页</NavLink>
          <NavLink to="/posts">文章</NavLink>
          <NavLink to="/archives">归档</NavLink>
          <NavLink to="/about">关于</NavLink>
        </nav>
      </header>

      <main className="page-shell">
        <Outlet />
      </main>

      <footer className="site-footer">
        <div>
          <strong>分类</strong>
          <div className="footer-links">
            {categories.map((category) => (
              <Link key={category.id} to={`/categories/${category.id}`}>{category.name}</Link>
            ))}
          </div>
        </div>
        <p>{siteConfig?.siteDescription}</p>
      </footer>
    </div>
  );
}