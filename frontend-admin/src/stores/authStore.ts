import { create } from 'zustand';
import type { AuthUser } from '../types';

interface AuthState {
  token: string;
  user?: AuthUser;
  setToken: (token: string) => void;
  setUser: (user?: AuthUser) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  token: localStorage.getItem('admin_token') ?? '',
  user: undefined,
  setToken: (token) => {
    localStorage.setItem('admin_token', token);
    set({ token });
  },
  setUser: (user) => set({ user }),
  logout: () => {
    localStorage.removeItem('admin_token');
    set({ token: '', user: undefined });
  },
}));