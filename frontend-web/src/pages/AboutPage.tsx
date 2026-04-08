import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import rehypeHighlight from 'rehype-highlight';
import { publicApi } from '../api/public';

export function AboutPage() {
  const { data } = useQuery({ queryKey: ['site-config'], queryFn: publicApi.getSiteConfig });

  useEffect(() => {
    document.title = '关于我的博客 | MyWeb Blog';
  }, []);

  return (
    <section className="markdown-panel">
      <h1>关于</h1>
      <ReactMarkdown remarkPlugins={[remarkGfm]} rehypePlugins={[rehypeHighlight]}>
        {data?.aboutMd ?? '# 关于我'}
      </ReactMarkdown>
    </section>
  );
}