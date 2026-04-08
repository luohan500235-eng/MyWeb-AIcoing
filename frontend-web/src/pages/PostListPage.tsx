import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link, useParams, useSearchParams } from 'react-router-dom';
import { publicApi } from '../api/public';
import { PostCard } from '../components/PostCard';

export function PostListPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { categoryId: routeCategoryId, tagId: routeTagId } = useParams();
  const keyword = searchParams.get('keyword') ?? '';
  const page = Number(searchParams.get('page') ?? '1');
  const size = 10;
  const categoryId = routeCategoryId ?? searchParams.get('categoryId') ?? undefined;
  const tagId = routeTagId ?? searchParams.get('tagId') ?? undefined;

  const { data, isLoading, error } = useQuery({
    queryKey: ['posts', page, keyword, categoryId, tagId],
    queryFn: () => publicApi.getPosts({ page, size, keyword, categoryId, tagId }),
  });

  useEffect(() => {
    document.title = '文章列表 | MyWeb Blog';
  }, []);

  return (
    <div className="stack-24">
      <section className="toolbar-panel">
        <div>
          <h1>文章列表</h1>
          <p>按关键词、分类或标签浏览内容。</p>
        </div>
        <div className="search-row">
          <input value={keyword} onChange={(event) => setSearchParams({ keyword: event.target.value, page: '1' })} placeholder="搜索标题或摘要" />
        </div>
      </section>

      {isLoading ? <p>文章加载中...</p> : null}
      {error ? <p className="error-text">{(error as Error).message}</p> : null}

      <div className="post-grid">
        {data?.records.map((post) => <PostCard key={post.id} post={post} />)}
      </div>

      <div className="pager-row">
        <button disabled={page <= 1} onClick={() => setSearchParams({ keyword, page: String(page - 1) })}>上一页</button>
        <span>第 {data?.page ?? 1} / {Math.max(data?.totalPages ?? 1, 1)} 页</span>
        <button disabled={page >= (data?.totalPages ?? 1)} onClick={() => setSearchParams({ keyword, page: String(page + 1) })}>下一页</button>
      </div>

      <div>
        <Link to="/archives">去归档页看看更早的文章</Link>
      </div>
    </div>
  );
}