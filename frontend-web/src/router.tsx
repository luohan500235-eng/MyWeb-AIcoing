import { createBrowserRouter } from 'react-router-dom';
import { AppLayout } from './components/AppLayout';
import { AboutPage } from './pages/AboutPage';
import { ArchivePage } from './pages/ArchivePage';
import { HomePage } from './pages/HomePage';
import { NotFoundPage } from './pages/NotFoundPage';
import { PostDetailPage } from './pages/PostDetailPage';
import { PostListPage } from './pages/PostListPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <AppLayout />,
    errorElement: <NotFoundPage />,
    children: [
      { index: true, element: <HomePage /> },
      { path: 'posts', element: <PostListPage /> },
      { path: 'posts/:slug', element: <PostDetailPage /> },
      { path: 'categories/:categoryId', element: <PostListPage /> },
      { path: 'tags/:tagId', element: <PostListPage /> },
      { path: 'archives', element: <ArchivePage /> },
      { path: 'about', element: <AboutPage /> },
    ],
  },
]);