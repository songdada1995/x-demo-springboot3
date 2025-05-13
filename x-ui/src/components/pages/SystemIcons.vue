<template>
  <div class="system-icons">
    <div class="page-header">
      <h2>业务系统</h2>
    </div>

    <a-row :gutter="[24, 24]">
      <a-col :span="6" v-for="system in systems" :key="system.id">
        <a-card
          hoverable
          class="system-card"
          @click="handleSystemClick(system)"
        >
          <template #cover>
            <div class="system-icon">
              <img :src="system.icon" :alt="system.name" />
            </div>
          </template>
          <a-card-meta :title="system.name">
            <template #description>
              <p class="system-desc">{{ system.description }}</p>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

// 系统数据
const systems = ref([
  {
    id: 1,
    name: '用户中心',
    description: '用户管理、权限控制、角色分配',
    icon: '/icons/user-center.png',
    url: '/system/user',
  },
  {
    id: 2,
    name: '订单管理',
    description: '订单处理、支付管理、物流跟踪',
    icon: '/icons/order.png',
    url: '/business/order',
  },
  {
    id: 3,
    name: '库存管理',
    description: '库存查询、入库出库、库存预警',
    icon: '/icons/inventory.png',
    url: '/business/inventory',
  },
  {
    id: 4,
    name: '报表中心',
    description: '数据统计、报表生成、数据分析',
    icon: '/icons/report.png',
    url: '/business/report',
  },
  {
    id: 5,
    name: '系统设置',
    description: '系统配置、参数设置、日志管理',
    icon: '/icons/settings.png',
    url: '/system/settings',
  },
  {
    id: 6,
    name: '消息中心',
    description: '消息通知、站内信、邮件管理',
    icon: '/icons/message.png',
    url: '/system/message',
  },
])

// 处理系统点击
const handleSystemClick = (system: any) => {
  // 实现系统跳转逻辑
  console.log('跳转到系统：', system)
  // window.location.href = system.url
}
</script>

<style scoped>
.system-icons {
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
}

.system-card {
  height: 100%;
  transition: all 0.3s;
}

.system-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.system-icon {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  padding: 20px;
}

.system-icon img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.system-desc {
  color: rgba(0, 0, 0, 0.45);
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
  height: 42px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

:deep(.ant-card-meta-title) {
  margin-bottom: 8px;
  font-size: 16px;
  font-weight: 500;
}
</style> 