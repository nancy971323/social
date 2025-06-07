import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'

// Lazy-loaded views
const HomeView = () => import('../views/HomeView.vue')
const RegisterView = () => import('../views/RegisterView.vue')
const LoginView = () => import('../views/LoginView.vue')
const PostsView = () => import('../views/PostsView.vue')
const ProfileView = () => import('../views/ProfileView.vue')

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/posts',
    name: 'posts',
    component: PostsView,
    meta: { requiresAuth: true }
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
    meta: { guest: true }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { guest: true }
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const isAuthenticated = authStore.isAuthenticated
  
  // If route requires auth and user is not authenticated
  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'home' })
  } 
  // If route is for guests only and user is authenticated
  else if (to.meta.guest && isAuthenticated) {
    next({ name: 'home' })
  } 
  // Otherwise proceed as normal
  else {
    next()
  }
})

export default router 