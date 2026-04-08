import { useEffect } from 'react';
import { NavLink, Navigate, Outlet, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { authApi } from '../api';
import { useAuthStore } from '../stores/authStore';

export function AdminLayout() {
  const navigate = useNavigate();
  const { token, user, setUser, logout } = useAuthStore();
  const profileQuery = useQuery({
    queryKey: ['admin-me'],
    queryFn: authApi.me,
    enabled: Boolean(token),
    retry: false,
  });

  useEffect(() => {
    if (profileQuery.data) {
      setUser(profileQuery.data);
    }
    if (profileQuery.error) {
      logout();
      navigate('/login');
    }
  }, [profileQuery.data, profileQuery.error, setUser, logout, navigate]);

  if (!token) return <Navigate to="/login" replace />;

  return (
    <div className="admin-shell">
      <aside className="sidebar">
        <div>
          <h1>MyWeb Admin</h1>
          <p>{user?.nickname ?? '管理员'}</p>
        </div>
        <nav>
          <NavLink to="/">仪表盘</NavLink>
          <NavLink to="/posts">文章管理</NavLink>
          <NavLink to="/categories">分类管理</NavLink>
          <NavLink to="/tags">标签管理</NavLink>
          <NavLink to="/comments">评论审核</NavLink>
          <NavLink to="/site">站点配置</NavLink>
        </nav>
        <button type="button" className="ghost-button" onClick={() => { logout(); navigate('/login'); }}>退出登录</button>
      </aside>
      <main className="admin-main">
        <Outlet />
      </main>
    </div>
  );
}