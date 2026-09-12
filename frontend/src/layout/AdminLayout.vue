<template>
  <el-container class="admin-layout">
    <!-- 左侧导航栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
      <div class="logo">
        <el-icon :size="22"><Notebook /></el-icon>
        <span v-show="!isCollapse" class="logo-text">博客管理系统</span>
      </div>
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :collapse-transition="false"
          background-color="#001529"
          text-color="#c0c4cc"
          active-text-color="#ffffff"
          router
        >
          <el-menu-item
            v-for="item in menuItems"
            :key="item.path"
            :index="item.path"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <!-- 顶部栏 -->
      <el-header class="topbar">
        <div class="topbar-left">
          <el-icon class="collapse-btn" :size="20" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="topbar-right">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="30" :src="userAvatar">
                <el-icon><UserFilled /></el-icon>
              </el-avatar>
              <span class="username">{{ userName }}</span>
              <el-icon><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)

// 菜单项（与路由 children 对应）
const menuItems = [
  { path: '/dashboard', title: '仪表盘', icon: 'Odometer' },
  { path: '/articles', title: '文章管理', icon: 'Document' },
  { path: '/categories', title: '分类/标签', icon: 'CollectionTag' },
  { path: '/comments', title: '评论管理', icon: 'ChatDotRound' },
  { path: '/messages', title: '留言管理', icon: 'Message' },
  { path: '/links', title: '友链管理', icon: 'Link' },
  { path: '/music', title: '音乐管理', icon: 'Headset' },
  { path: '/rss', title: 'RSS订阅', icon: 'Rss' },
  { path: '/visitors', title: '访客管理', icon: 'User' },
  { path: '/view-records', title: '浏览记录', icon: 'View' },
  { path: '/logs', title: '操作日志', icon: 'Memo' },
  { path: '/profile', title: '个人资料', icon: 'Avatar' },
  { path: '/settings', title: '系统设置', icon: 'Setting' }
]

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '')

const user = JSON.parse(localStorage.getItem('user') || '{}')
const userName = computed(() => user.nickname || user.username || '管理员')
const userAvatar = computed(() => user.avatar || '')

// 响应式：小屏自动折叠
function handleResize() {
  isCollapse.value = window.innerWidth < 768
}

onMounted(() => {
  handleResize()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})

function handleCommand(command) {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        router.push('/login')
      })
      .catch(() => {})
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.sidebar {
  background: #001529;
  transition: width 0.28s;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #fff;
  background: #002140;
  font-weight: 600;
  white-space: nowrap;
}

.logo-text {
  font-size: 16px;
}

.sidebar :deep(.el-menu) {
  border-right: none;
}

.sidebar :deep(.el-menu-item.is-active) {
  background: #1890ff !important;
}

.topbar {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
}

.topbar-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  cursor: pointer;
  color: #606266;
}

.topbar-right .user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #303133;
  outline: none;
}

.username {
  font-size: 14px;
}

.main-content {
  background: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
