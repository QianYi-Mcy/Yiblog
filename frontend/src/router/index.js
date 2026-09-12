import { createRouter, createWebHistory } from 'vue-router'

const Layout = () => import('@/layout/AdminLayout.vue')

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'Odometer' }
      },
      {
        path: 'articles',
        name: 'Articles',
        component: () => import('@/views/ArticleManage.vue'),
        meta: { title: '文章管理', icon: 'Document' }
      },
      {
        path: 'categories',
        name: 'Categories',
        component: () => import('@/views/CategoryManage.vue'),
        meta: { title: '分类/标签', icon: 'CollectionTag' }
      },
      {
        path: 'comments',
        name: 'Comments',
        component: () => import('@/views/CommentManage.vue'),
        meta: { title: '评论管理', icon: 'ChatDotRound' }
      },
      {
        path: 'messages',
        name: 'Messages',
        component: () => import('@/views/MessageManage.vue'),
        meta: { title: '留言管理', icon: 'Message' }
      },
      {
        path: 'links',
        name: 'Links',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: '友链管理', icon: 'Link' }
      },
      {
        path: 'music',
        name: 'Music',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: '音乐管理', icon: 'Headset' }
      },
      {
        path: 'rss',
        name: 'Rss',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: 'RSS订阅', icon: 'Rss' }
      },
      {
        path: 'visitors',
        name: 'Visitors',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: '访客管理', icon: 'User' }
      },
      {
        path: 'view-records',
        name: 'ViewRecords',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: '浏览记录', icon: 'View' }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: '操作日志', icon: 'Memo' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: '个人资料', icon: 'Avatar' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Placeholder.vue'),
        meta: { title: '系统设置', icon: 'Setting' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫：未登录跳转登录页
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 博客管理系统` : '博客管理系统'
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router

