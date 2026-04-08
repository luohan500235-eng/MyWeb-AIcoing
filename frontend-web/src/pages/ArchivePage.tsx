import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { publicApi } from '../api/public';

export function ArchivePage() {
  const { data = [] } = useQuery({ queryKey: ['archives'], queryFn: publicApi.getArchives });

  useEffect(() => {
    document.title = '归档 | MyWeb Blog';
  }, []);

  return (
    <div className="stack-24">
      <div className="section-head"><h1>归档</h1><p>按照月份整理所有已发布文章。</p></div>
      <div className="archive-list">
        {data.map((item, index) => (
          <div className="archive-item" key={`${item.yearMonth}-${item.post.id}-${index}`}>
            <span>{item.yearMonth}</span>
            <Link to={`/posts/${item.post.slug}`}>{item.post.title}</Link>
          </div>
        ))}
      </div>
    </div>
  );
}