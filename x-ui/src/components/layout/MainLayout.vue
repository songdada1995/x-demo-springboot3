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
        <a-dropdown>
          <span class="username">
            admin
            <down-outlined />
          </span>
          <template #overlay>
            <a-menu>
              <a-menu-item key="profile">
                <user-outlined />
                个人信息
              </a-menu-item>
              <a-menu-item key="password">
                <lock-outlined />
                修改密码
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="logout">
                <logout-outlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
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
          @openChange="handleOpenChange"
        >
          <template v-for="item in currentMenuItems" :key="item.key">
            <a-sub-menu v-if="item.children" :key="item.key" :selectable="false" @click="handleSubMenuClick(item)">
              <template #title>
                <span>
                  <component :is="item.icon" />
                  <span>{{ item.title }}</span>
                </span>
              </template>
              <template v-for="child in item.children" :key="child.key">
                <a-sub-menu v-if="child.children" :key="`sub-${child.key}`" :selectable="false" @click="handleSubMenuClick(child)">
                  <template #title>
                    <span>{{ child.title }}</span>
                  </template>
                  <a-menu-item v-for="grandChild in child.children" :key="grandChild.key">
                    {{ grandChild.title }}
                  </a-menu-item>
                </a-sub-menu>
                <a-menu-item v-else :key="`item-${child.key}`">
                  {{ child.title }}
                </a-menu-item>
              </template>
            </a-sub-menu>
            <a-menu-item v-else :key="`item-${item.key}`">
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
  DownOutlined,
  LockOutlined,
  LogoutOutlined,
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

interface MenuItem {
  key: string
  title: string
  icon?: any
  path?: string
  children?: MenuItem[]
}

const router = useRouter()
const collapsed = ref(false)
const selectedKeys = ref(['1'])
const openKeys = ref(['systemManagement'])
const selectedTopKeys = ref(['system'])

// 系统管理菜单
const systemMenuItems: MenuItem[] = [
  {
    key: 'dashboard',
    title: '仪表盘',
    icon: DashboardOutlined,
    path: '/dashboard'
  },
  {
    key: 'systemManagement',
    title: '系统管理',
    icon: SettingOutlined,
    children: [
      {
        key: 'users',
        title: '用户管理',
        path: '/system/users'
      },
      {
        key: 'roles',
        title: '角色管理',
        path: '/system/roles'
      },
      {
        key: 'permissions',
        title: '权限管理',
        path: '/system/permissions'
      },
    ],
  },
]

// 业务平台菜单
const businessMenuItems: MenuItem[] = [
  {
    key: 'businessPlatform',
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

// 查找菜单项
const findMenuItem = (items: MenuItem[], key: string): MenuItem | null => {
  for (const item of items) {
    if (item.key === key) {
      return item
    }
    if (item.children) {
      const found = findMenuItem(item.children, key)
      if (found) return found
    }
  }
  return null
}

// 处理子菜单点击
const handleSubMenuClick = (item: MenuItem) => {
  // 如果当前选中的是没有子菜单的一级菜单项，则不清除选中状态
  if (selectedKeys.value.length > 0) {
    const currentSelectedKey = selectedKeys.value[0]
    const currentSelectedItem = findMenuItem(currentMenuItems.value, currentSelectedKey.replace(/^(item-|sub-)/, ''))
    if (currentSelectedItem && !currentSelectedItem.children) {
      // 如果当前选中的是没有子菜单的项，则保持选中状态
      return
    }
  }
  // 否则清除选中状态
  selectedKeys.value = []
}

// 处理菜单选择
const handleMenuSelect = ({ key }: { key: string }) => {
  // 移除 key 前缀以获取实际的菜单 key
  const actualKey = key.replace(/^(item-|sub-)/, '')
  const selectedItem = findMenuItem(currentMenuItems.value, actualKey)
  
  if (selectedItem && !selectedItem.children) {
    // 只有没有子菜单的项才能被选中
    selectedKeys.value = [key]
    if (selectedItem.path) {
      router.push(selectedItem.path)
    }
  } else {
    // 如果有子菜单，且当前没有选中的没有子菜单的项，则清除选中状态
    if (selectedKeys.value.length > 0) {
      const currentSelectedKey = selectedKeys.value[0]
      const currentSelectedItem = findMenuItem(currentMenuItems.value, currentSelectedKey.replace(/^(item-|sub-)/, ''))
      if (!currentSelectedItem || currentSelectedItem.children) {
        selectedKeys.value = []
      }
    }
  }
}

// 处理菜单展开/收起
const handleOpenChange = (keys: string[]) => {
  const lastKey = keys[keys.length - 1]
  const menuItem = findMenuItem(currentMenuItems.value, lastKey)
  
  if (menuItem && menuItem.children) {
    // 只处理有子菜单的菜单项
    openKeys.value = keys
    // 如果当前选中的是没有子菜单的项，则不清除选中状态
    if (selectedKeys.value.length > 0) {
      const currentSelectedKey = selectedKeys.value[0]
      const currentSelectedItem = findMenuItem(currentMenuItems.value, currentSelectedKey.replace(/^(item-|sub-)/, ''))
      if (!currentSelectedItem || currentSelectedItem.children) {
        selectedKeys.value = []
      }
    }
  }
}

// 查找第一个可点击的菜单项（没有子菜单的项）
const findFirstMenuItem = (items: MenuItem[]): MenuItem | null => {
  for (const item of items) {
    if (!item.children) {
      // 如果当前项没有子菜单，直接返回
      return item
    }
    // 如果有子菜单，递归查找子菜单中的第一个可点击项
    const found = findFirstMenuItem(item.children)
    if (found) return found
  }
  return null
}

// 处理顶部菜单选择
const handleTopMenuSelect = ({ key }: { key: string }) => {
  // 重置左侧菜单选中状态
  selectedKeys.value = []
  openKeys.value = []
  
  // 获取新菜单的第一个可点击项
  const firstMenuItem = findFirstMenuItem(currentMenuItems.value)
  if (firstMenuItem) {
    // 设置选中状态
    selectedKeys.value = [`item-${firstMenuItem.key}`]
    // 如果有路径，进行路由跳转
    if (firstMenuItem.path) {
      router.push(firstMenuItem.path)
    }
    
    // 如果第一个可点击项在子菜单中，需要展开其父菜单
    const findParentMenu = (items: MenuItem[], targetKey: string): string | null => {
      for (const item of items) {
        if (item.children) {
          if (item.children.some(child => child.key === targetKey)) {
            return item.key
          }
          const found = findParentMenu(item.children, targetKey)
          if (found) return found
        }
      }
      return null
    }
    
    const parentKey = findParentMenu(currentMenuItems.value, firstMenuItem.key)
    if (parentKey) {
      openKeys.value = [parentKey]
    }
  }
}

// 初始化时选择第一个菜单项
const initFirstMenuItem = () => {
  const firstMenuItem = findFirstMenuItem(currentMenuItems.value)
  if (firstMenuItem) {
    // 设置选中状态
    selectedKeys.value = [`item-${firstMenuItem.key}`]
    // 如果有路径，进行路由跳转
    if (firstMenuItem.path) {
      router.push(firstMenuItem.path)
    }
    
    // 如果第一个可点击项在子菜单中，需要展开其父菜单
    const findParentMenu = (items: MenuItem[], targetKey: string): string | null => {
      for (const item of items) {
        if (item.children) {
          if (item.children.some(child => child.key === targetKey)) {
            return item.key
          }
          const found = findParentMenu(item.children, targetKey)
          if (found) return found
        }
      }
      return null
    }
    
    const parentKey = findParentMenu(currentMenuItems.value, firstMenuItem.key)
    if (parentKey) {
      openKeys.value = [parentKey]
    }
  }
}

// 组件挂载时初始化
initFirstMenuItem()

// 处理用户菜单点击
const handleUserMenuClick = ({ key }: { key: string }) => {
  switch (key) {
    case 'profile':
      router.push('/system/profile')
      break
    case 'password':
      router.push('/system/change-password')
      break
    case 'logout':
      // 这里添加退出登录的逻辑
      message.success('退出成功')
      router.push('/login')
      break
  }
}
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  width: 100%;
  overflow-x: hidden;
}

.header {
  display: flex;
  align-items: center;
  padding: 0;
  background: #001529;
  height: 64px;
  min-height: 64px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
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
  transition: all 0.2s;
}

.logo {
  color: #27c2ad;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 2px;
  text-align: center;
  margin-left: 0;
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
  width: 256px;
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
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.username:hover {
  color: #27c2ad;
}

.sider {
  background: #001529;
  position: fixed;
  left: 0;
  top: 64px;
  bottom: 0;
  overflow-y: auto;
  overflow-x: hidden;
  transition: all 0.2s;
  z-index: 999;
}

.content {
  margin: 24px;
  background: #f0f2f5;
  min-height: calc(100vh - 48px);
  transition: all 0.2s;
  margin-left: 224px; /* 200px(菜单宽度) + 24px(边距) */
}

.content-wrapper {
  background: #fff;
  padding: 24px;
  min-height: calc(100vh - 96px);
  border-radius: 2px;
  width: 100%;
  box-sizing: border-box;
}

/* 顶部菜单样式 */
:deep(.ant-menu-horizontal) {
  background: #001529;
  color: #fff;
  border-bottom: none;
  width: 200px;
  transition: all 0.2s;
  margin-left: 0;
}

:deep(.ant-menu-horizontal > .ant-menu-item),
:deep(.ant-menu-horizontal > .ant-menu-submenu) {
  color: #fff;
  margin: 0;
  padding: 0 32px;
}

:deep(.ant-menu-horizontal > .ant-menu-item-selected) {
  background-color: #27c2ad !important;
  color: #fff !important;
}

:deep(.ant-menu-horizontal > .ant-menu-item:hover) {
  background-color: #27c2ad !important;
  color: #fff !important;
  opacity: 0.8;
}

/* 左侧菜单基础样式 */
:deep(.ant-menu-dark) {
  background: #001529;
}

/* 所有菜单项基础样式 */
:deep(.ant-menu-dark .ant-menu-item),
:deep(.ant-menu-dark .ant-menu-submenu-title) {
  display: flex !important;
  align-items: center !important;
  padding: 0 24px !important;
  text-align: left !important;
  margin: 0 !important;
  border: none !important;
  border-radius: 0 !important;
  height: 40px !important;
  line-height: 40px !important;
}

/* 一级菜单样式 */
:deep(.ant-menu-dark > .ant-menu-item),
:deep(.ant-menu-dark > .ant-menu-submenu > .ant-menu-submenu-title) {
  padding-left: 24px !important;
  height: 40px !important;
  line-height: 40px !important;
}

/* 一级菜单图标样式 */
:deep(.ant-menu-dark > .ant-menu-item .anticon),
:deep(.ant-menu-dark > .ant-menu-submenu > .ant-menu-submenu-title .anticon) {
  margin-right: 10px !important;
  font-size: 16px !important;
  min-width: 16px !important;
}

/* 菜单标题内容样式 */
:deep(.ant-menu-title-content) {
  display: flex !important;
  align-items: center !important;
  text-align: left !important;
}

/* 二级菜单样式 */
:deep(.ant-menu-dark .ant-menu-submenu > .ant-menu-item) {
  padding-left: 50px !important;  /* 24px + 16px + 10px */
  margin-top: 4px !important;
  margin-bottom: 4px !important;
  text-align: left !important;
  height: 32px !important;
  line-height: 32px !important;
}

/* 二级菜单标题内容样式 */
:deep(.ant-menu-dark .ant-menu-submenu > .ant-menu-item .ant-menu-title-content) {
  padding-left: 0 !important;
  margin-left: 0 !important;
  text-align: left !important;
}

/* 三级菜单样式 */
:deep(.ant-menu-dark .ant-menu-submenu .ant-menu-submenu > .ant-menu-item) {
  padding-left: 76px !important;  /* 50px + 16px + 10px */
  margin-top: 4px !important;
  margin-bottom: 4px !important;
  text-align: left !important;
  height: 32px !important;
  line-height: 32px !important;
}

/* 三级菜单标题内容样式 */
:deep(.ant-menu-dark .ant-menu-submenu .ant-menu-submenu > .ant-menu-item .ant-menu-title-content) {
  padding-left: 0 !important;
  margin-left: 0 !important;
  text-align: left !important;
}

/* 菜单项内容样式 */
:deep(.ant-menu-item span),
:deep(.ant-menu-submenu-title span) {
  display: inline-block !important;
  text-align: left !important;
  width: auto !important;
  height: 100% !important;
  line-height: inherit !important;
}

/* 确保菜单项内容左对齐 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu-title) {
  text-align: left !important;
  justify-content: flex-start !important;
}

/* 移除菜单项的居中对齐 */
:deep(.ant-menu-item-selected),
:deep(.ant-menu-submenu-selected) {
  text-align: left !important;
  justify-content: flex-start !important;
}

/* 确保只有叶子节点可以被选中 */
:deep(.ant-menu-item-selected) {
  background-color: #27c2ad !important;
  color: #fff !important;
  transition: none !important;
  border-radius: 0 !important;
}

/* 完全禁用子菜单的选中状态 */
:deep(.ant-menu-submenu) {
  background-color: transparent !important;
}

/* 一级菜单标题样式 */
:deep(.ant-menu-dark > .ant-menu-submenu > .ant-menu-submenu-title) {
  background-color: transparent !important;
  color: #fff !important;
  text-align: left !important;
  justify-content: flex-start !important;
  padding-left: 24px !important;
}

/* 二级菜单标题样式 */
:deep(.ant-menu-dark .ant-menu-submenu > .ant-menu-item) {
  background-color: transparent !important;
  color: #fff !important;
  text-align: left !important;
  justify-content: flex-start !important;
  padding-left: 50px !important;
}

:deep(.ant-menu-submenu-selected) {
  background-color: transparent !important;
  color: #fff !important;
}

:deep(.ant-menu-submenu-selected > .ant-menu-submenu-title) {
  background-color: transparent !important;
  color: #fff !important;
  text-align: left !important;
  justify-content: flex-start !important;
}

:deep(.ant-menu-submenu-open) {
  background-color: transparent !important;
}

:deep(.ant-menu-submenu-open > .ant-menu-submenu-title) {
  background-color: transparent !important;
  color: #fff !important;
  text-align: left !important;
  justify-content: flex-start !important;
}

/* 子菜单的hover效果 */
:deep(.ant-menu-submenu-title:hover) {
  background-color: #27c2ad !important;
  opacity: 0.8;
}

:deep(.ant-menu-submenu-title:active) {
  background-color: #27c2ad !important;
  opacity: 0.8;
}

/* 移除所有菜单的蓝色过渡效果 */
:deep(.ant-menu-item::after),
:deep(.ant-menu-submenu-title::after) {
  display: none !important;
}

:deep(.ant-menu-item-selected::after),
:deep(.ant-menu-submenu-selected::after) {
  display: none !important;
}

/* 移除所有默认的过渡动画 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu),
:deep(.ant-menu-submenu-title) {
  transition: none !important;
  border-radius: 0 !important;
}

/* 移除所有hover和active状态的默认样式 */
:deep(.ant-menu-item:hover),
:deep(.ant-menu-item:active),
:deep(.ant-menu-submenu-title:hover),
:deep(.ant-menu-submenu-title:active) {
  background-color: #27c2ad !important;
  color: #fff !important;
  opacity: 0.8;
  border-radius: 0 !important;
}

/* 移除所有focus状态的默认样式 */
:deep(.ant-menu-item:focus),
:deep(.ant-menu-submenu-title:focus) {
  background-color: #27c2ad !important;
  color: #fff !important;
  border-radius: 0 !important;
}

/* 移除所有默认的边框和轮廓 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu-title) {
  outline: none !important;
  border: none !important;
  box-shadow: none !important;
  border-radius: 0 !important;
}

/* 移除子菜单的圆角 */
:deep(.ant-menu-submenu-popup) {
  border-radius: 0 !important;
}

:deep(.ant-menu-submenu-popup .ant-menu-item) {
  border-radius: 0 !important;
}

/* 移除所有菜单项的圆角 */
:deep(.ant-menu-item),
:deep(.ant-menu-submenu-title),
:deep(.ant-menu-submenu),
:deep(.ant-menu-submenu-popup),
:deep(.ant-menu-submenu-popup .ant-menu-item) {
  border-radius: 0 !important;
}

/* 控制父级菜单的选中状态 */
:deep(.ant-menu-submenu-selected) {
  background-color: transparent !important;
  color: #fff !important;
}

:deep(.ant-menu-submenu-selected > .ant-menu-submenu-title) {
  background-color: transparent !important;
  color: #fff !important;
}

/* 当左侧菜单折叠时的顶部菜单样式 */
:deep(.ant-layout-sider-collapsed) ~ .ant-layout-header .top-menu {
  width: 80px;
}

:deep(.ant-layout-sider-collapsed) ~ .ant-layout-header :deep(.ant-menu-horizontal) {
  width: 80px;
}

/* 调整内容区域的边距，确保与顶部菜单对齐 */
:deep(.ant-layout-sider:not(.ant-layout-sider-collapsed)) + .ant-layout .content {
  margin-left: 200px;
}

:deep(.ant-layout-sider-collapsed) + .ant-layout .content {
  margin-left: 104px; /* 80px(折叠菜单宽度) + 24px(边距) */
}

:deep(.ant-layout-header) {
  height: 64px;
  padding: 0;
  background: #001529;
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

/* 当左侧菜单折叠时的样式 */
:deep(.ant-layout-sider-collapsed) + .logo-area {
  width: 80px;
  min-width: 80px;
  max-width: 80px;
}

:deep(.ant-layout-sider-collapsed) + .logo {
  margin-left: 0;
}

/* 调整顶部菜单项的样式 */
:deep(.ant-menu-horizontal > .ant-menu-item) {
  padding: 0 32px;
  font-size: 16px;
  margin: 0;
}

/* 确保布局容器占满整个视口 */
:deep(.ant-layout) {
  background: #f0f2f5;
  min-height: 100vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

:deep(.ant-layout-has-sider) {
  flex-direction: row;
  width: 100%;
  margin: 0;
  padding: 0;
}

:deep(.ant-layout-sider) {
  position: fixed;
  left: 0;
  top: 64px;
  bottom: 0;
  overflow-y: auto;
  overflow-x: hidden;
  transition: all 0.2s;
  z-index: 999;
  background: #001529;
}

:deep(.ant-layout-content) {
  margin-top: 64px;
  margin-left: 200px;
  background: #f0f2f5;
  min-height: calc(100vh - 64px);
  transition: all 0.2s;
  padding: 12px;
}

:deep(.ant-layout-sider-collapsed) + .ant-layout .ant-layout-content {
  margin-left: 80px;
}

/* 覆盖 ant-layout 的默认样式 */
:deep(.css-dev-only-do-not-override-1p3hq3p.ant-layout) {
  background: #f0f2f5;
  min-height: 100vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

:deep(.css-dev-only-do-not-override-1p3hq3p.ant-layout-has-sider) {
  flex-direction: row;
  width: 100%;
  margin: 0;
  padding: 0;
}

/* 下拉菜单样式 */
:deep(.ant-dropdown-menu) {
  background: #001529;
  border: 1px solid #27c2ad;
}

:deep(.ant-dropdown-menu-item) {
  color: #fff;
  padding: 8px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.ant-dropdown-menu-item:hover) {
  background-color: #27c2ad !important;
  color: #fff !important;
}

:deep(.ant-dropdown-menu-item .anticon) {
  font-size: 16px;
}

:deep(.ant-dropdown-menu-divider) {
  background-color: rgba(255, 255, 255, 0.1);
  margin: 4px 0;
}
</style> 