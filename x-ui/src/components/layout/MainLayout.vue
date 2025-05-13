<template>
  <a-layout class="main-layout">
    <!-- 顶部菜单 -->
    <a-layout-header class="header">
      <div class="logo-area">
        <span class="logo">X-UI</span>
      </div>
      <a-menu
        v-model:selectedKeys="selectedTopKeys"
        mode="horizontal"
        theme="light"
        class="top-menu"
        @select="handleTopMenuSelect"
      >
        <a-menu-item key="system">系统管理</a-menu-item>
        <a-menu-item key="business">业务平台</a-menu-item>
      </a-menu>
      <div class="header-right">
        <span class="username">admin</span>
      </div>
    </a-layout-header>

    <a-layout>
      <!-- 左侧菜单 -->
      <a-layout-sider
        v-model:collapsed="collapsed"
        :trigger="null"
        collapsible
        class="sider"
      >
        <a-menu
          v-model:selectedKeys="selectedKeys"
          v-model:openKeys="openKeys"
          mode="inline"
          theme="dark"
          @select="handleMenuSelect"
        >
          <template v-for="item in currentMenuItems" :key="item.key">
            <a-sub-menu v-if="item.children" :key="item.key">
              <template #title>
                <span>
                  <component :is="item.icon" />
                  <span>{{ item.title }}</span>
                </span>
              </template>
              <a-menu-item v-for="child in item.children" :key="child.key">
                {{ child.title }}
              </a-menu-item>
            </a-sub-menu>
            <a-menu-item v-else :key="item.key">
              <span>
                <component :is="item.icon" />
                <span>{{ item.title }}</span>
              </span>
            </a-menu-item>
          </template>
        </a-menu>
      </a-layout-sider>

      <!-- 内容区 -->
      <a-layout-content class="content">
        <div class="content-wrapper">
          <router-view></router-view>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  UserOutlined,
  SettingOutlined,
  DashboardOutlined,
  TeamOutlined,
  AppstoreOutlined,
} from '@ant-design/icons-vue'

const router = useRouter()
const collapsed = ref(false)
const selectedKeys = ref(['1'])
const openKeys = ref(['sub1'])
const selectedTopKeys = ref(['system'])

// 系统管理菜单
const systemMenuItems = [
  {
    key: 'dashboard',
    title: '仪表盘',
    icon: DashboardOutlined,
    path: '/system'
  },
  {
    key: 'sub1',
    title: '系统管理',
    icon: SettingOutlined,
    children: [
      {
        key: '1',
        title: '用户管理',
        path: '/system/users'
      },
      {
        key: '2',
        title: '角色管理',
        path: '/system/roles'
      },
      {
        key: '3',
        title: '权限管理',
        path: '/system/permissions'
      },
    ],
  },
]

// 业务平台菜单
const businessMenuItems = [
  {
    key: 'sub2',
    title: '业务管理',
    icon: AppstoreOutlined,
    children: [
      {
        key: '4',
        title: '成本核算',
        path: '/business/cost'
      },
      {
        key: '5',
        title: '财务报表',
        path: '/business/finance'
      },
    ],
  },
]

// 根据顶部菜单选择显示对应的左侧菜单
const currentMenuItems = computed(() => {
  return selectedTopKeys.value[0] === 'system' ? systemMenuItems : businessMenuItems
})

// 处理顶部菜单选择
const handleTopMenuSelect = ({ key }: { key: string }) => {
  // 重置左侧菜单选中状态
  selectedKeys.value = []
  openKeys.value = []
  
  // 获取新菜单的第一个可点击项
  const firstMenuItem = findFirstMenuItem(currentMenuItems.value)
  if (firstMenuItem) {
    selectedKeys.value = [firstMenuItem.key]
    // 如果是业务平台，展开业务管理菜单
    if (key === 'business') {
      openKeys.value = ['sub2']
    }
    if (firstMenuItem.path) {
      router.push(firstMenuItem.path)
    }
  }
}

// 处理左侧菜单选择
const handleMenuSelect = ({ key }: { key: string }) => {
  // 查找选中的菜单项
  const findMenuItem = (items: any[]): any => {
    for (const item of items) {
      if (item.key === key) {
        return item
      }
      if (item.children) {
        const found = findMenuItem(item.children)
        if (found) return found
      }
    }
    return null
  }

  const selectedItem = findMenuItem(currentMenuItems.value)
  if (selectedItem?.path) {
    router.push(selectedItem.path)
  }
}

// 查找第一个可点击的菜单项
const findFirstMenuItem = (items: any[]): any => {
  for (const item of items) {
    if (item.path) {
      return item
    }
    if (item.children) {
      const found = findFirstMenuItem(item.children)
      if (found) return found
    }
  }
  return null
}

// 初始化时选择第一个菜单项
const initFirstMenuItem = () => {
  const firstMenuItem = findFirstMenuItem(currentMenuItems.value)
  if (firstMenuItem) {
    selectedKeys.value = [firstMenuItem.key]
    if (firstMenuItem.path) {
      router.push(firstMenuItem.path)
    }
  }
}

// 组件挂载时初始化
initFirstMenuItem()
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  padding: 0;
  background: #001529;
  height: 64px;
  min-height: 64px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  position: relative;
  z-index: 10;
}

.logo-area {
  background: #001529;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 64px;
  min-height: 64px;
  width: 200px;
  min-width: 200px;
  max-width: 200px;
  flex-shrink: 0;
  box-sizing: border-box;
}

.logo {
  color: #27c2ad;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 2px;
  text-align: center;
}

.top-menu {
  flex: 1;
  line-height: 64px;
  height: 64px;
  background: #001529;
  color: #fff;
  min-width: 0;
  margin-left: 0;
  padding-left: 0;
}

.header-right {
  background: #001529;
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  color: #fff;
  border-radius: 0;
  margin-left: auto;
}

.username {
  color: #fff;
  font-size: 16px;
  font-weight: 500;
}

.sider {
  background: #001529;
}

.content {
  margin: 24px;
  padding: 24px;
  background: #fff;
  min-height: 280px;
}

.content-wrapper {
  background: #fff;
  padding: 24px;
  min-height: 360px;
}

:deep(.ant-layout-header) {
  height: 64px;
  padding: 0 24px;
  background: #fff;
}

:deep(.ant-menu-dark) {
  background: #001529;
}

:deep(.ant-menu-dark .ant-menu-item-selected) {
  background-color: #27c2ad;
}

:deep(.ant-menu-dark .ant-menu-item:hover) {
  background-color: #27c2ad;
  opacity: 0.8;
}

:deep(.ant-menu-horizontal) {
  background: #001529;
  color: #fff;
  border-bottom: none;
}

:deep(.ant-menu-horizontal > .ant-menu-item),
:deep(.ant-menu-horizontal > .ant-menu-submenu) {
  color: #fff;
}

:deep(.ant-menu-horizontal > .ant-menu-item-selected) {
  background-color: #27c2ad !important;
  color: #fff !important;
}

:deep(.ant-menu-horizontal > .ant-menu-item:hover) {
  background-color: #27c2ad !important;
  color: #fff !important;
}

:deep(.ant-dropdown-menu) {
  background: #001529;
  color: #fff;
}
:deep(.ant-dropdown-menu-item) {
  color: #fff;
}
:deep(.ant-dropdown-menu-item-active),
:deep(.ant-dropdown-menu-item-selected) {
  background-color: #27c2ad !important;
  color: #fff !important;
}
</style> 