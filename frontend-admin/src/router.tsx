import { createBrowserRouter } from 'react-router-dom';
import { AdminLayout } from './components/AdminLayout';
import { CategoryPage } from './pages/CategoryPage';
import { CommentPage } from './pages/CommentPage';
import { DashboardPage } from './pages/DashboardPage';
import { LoginPage } from './pages/LoginPage';
import { PostEditorPage } from './pages/PostEditorPage';
import { PostListPage } from './pages/PostListPage';
import { SiteConfigPage } from './pages/SiteConfigPage';
import { TagPage } from './pages/TagPage';

export const router = createBrowserRouter([
  { path: '/login', element: <LoginPage /> },
  {
    path: '/',
    element: <AdminLayout />,
    children: [
      { index: true, element: <DashboardPage /> },
      { path: 'posts', element: <PostListPage /> },
      { path: 'posts/new', element: <PostEditorPage /> },
      { path: 'posts/:id/edit', element: <PostEditorPage /> },
      { path: 'categories', element: <CategoryPage /> },
      { path: 'tags', element: <TagPage /> },
      { path: 'comments', element: <CommentPage /> },
      { path: 'site', element: <SiteConfigPage /> }
    ]
  }
]);