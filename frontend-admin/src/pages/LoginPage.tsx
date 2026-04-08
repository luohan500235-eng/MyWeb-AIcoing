import { FormEvent, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../api';
import { useAuthStore } from '../stores/authStore';

export function LoginPage() {
  const navigate = useNavigate();
  const { setToken } = useAuthStore();
  const [formState, setFormState] = useState({ username: 'admin', password: 'Admin123456' });
  const loginMutation = useMutation({
    mutationFn: () => authApi.login(formState),
    onSuccess: (data) => {
      setToken(data.token);
      navigate('/');
    },
    onError: (error) => window.alert((error as Error).message),
  });

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    loginMutation.mutate();
  }

  return (
    <div className="login-shell">
      <form className="login-card" onSubmit={handleSubmit}>
        <p className="eyebrow">Blog Admin</p>
        <h1>欢迎回来</h1>
        <p>默认账号已经帮你准备好了，登录后可以直接管理文章与站点内容。</p>
        <input value={formState.username} onChange={(event) => setFormState({ ...formState, username: event.target.value })} placeholder="用户名" required />
        <input type="password" value={formState.password} onChange={(event) => setFormState({ ...formState, password: event.target.value })} placeholder="密码" required />
        <button type="submit" disabled={loginMutation.isPending}>{loginMutation.isPending ? '登录中...' : '进入后台'}</button>
      </form>
    </div>
  );
}