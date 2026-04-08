import { Link } from 'react-router-dom';

export function NotFoundPage() {
  return (
    <div className="empty-panel">
      <h1>页面走丢了</h1>
      <p>你访问的内容不存在，或者已经被移动。</p>
      <Link to="/">回到首页</Link>
    </div>
  );
}