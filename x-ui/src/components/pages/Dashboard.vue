<template>
  <div class="dashboard">
    <div class="page-header">
      <h2>仪表盘</h2>
    </div>

    <a-row :gutter="[16, 16]">
      <a-col :span="6">
        <a-card>
          <template #title>用户总数</template>
          <div class="card-content">
            <span class="number">1,234</span>
            <span class="label">人</span>
          </div>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <template #title>今日访问</template>
          <div class="card-content">
            <span class="number">256</span>
            <span class="label">次</span>
          </div>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <template #title>系统消息</template>
          <div class="card-content">
            <span class="number">12</span>
            <span class="label">条</span>
          </div>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <template #title>待办事项</template>
          <div class="card-content">
            <span class="number">5</span>
            <span class="label">项</span>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="[16, 16]" style="margin-top: 16px">
      <a-col :span="12">
        <a-card title="最近活动">
          <a-timeline>
            <a-timeline-item>创建项目 2024-01-01</a-timeline-item>
            <a-timeline-item>系统初始化 2024-01-02</a-timeline-item>
            <a-timeline-item>技术评审 2024-01-03</a-timeline-item>
            <a-timeline-item>设计评审 2024-01-04</a-timeline-item>
          </a-timeline>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="系统公告">
          <a-list :data-source="notices" :pagination="false">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta :title="item.title">
                  <template #description>{{ item.date }}</template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const notices = ref([
  {
    title: '系统升级通知',
    date: '2024-01-01'
  },
  {
    title: '新功能发布公告',
    date: '2024-01-02'
  },
  {
    title: '安全更新提醒',
    date: '2024-01-03'
  }
])
</script>

<style scoped>
.dashboard {
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

.card-content {
  display: flex;
  align-items: baseline;
}

.number {
  font-size: 24px;
  font-weight: 500;
  color: #27c2ad;
  margin-right: 8px;
}

.label {
  color: rgba(0, 0, 0, 0.45);
}
</style> 