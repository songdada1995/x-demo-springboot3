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
            <span class="number">{{ stats.userCount }}</span>
            <span class="label">人</span>
          </div>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <template #title>今日访问</template>
          <div class="card-content">
            <span class="number">{{ stats.todayVisits }}</span>
            <span class="label">次</span>
          </div>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <template #title>系统消息</template>
          <div class="card-content">
            <span class="number">{{ stats.messageCount }}</span>
            <span class="label">条</span>
          </div>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card>
          <template #title>待办事项</template>
          <div class="card-content">
            <span class="number">{{ stats.todoCount }}</span>
            <span class="label">项</span>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="[16, 16]" style="margin-top: 16px">
      <a-col :span="12">
        <a-card title="最近活动">
          <a-timeline>
            <a-timeline-item v-for="(activity, index) in activities" :key="index">
              {{ activity.content }} {{ activity.time }}
            </a-timeline-item>
            <a-timeline-item v-if="activities.length === 0">暂无活动记录</a-timeline-item>
          </a-timeline>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="系统公告">
          <a-list :data-source="notices" :pagination="false">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta :title="item.noticeTitle">
                  <template #description>{{ item.createTime }}</template>
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
import { ref, reactive, onMounted } from 'vue'
import { dashboardApi } from '../../api/dashboard'

const stats = reactive({ userCount: 0, todayVisits: 0, messageCount: 0, todoCount: 0 })
const notices = ref<any[]>([])
const activities = ref<any[]>([])

const loadData = async () => {
  try {
    const [statsRes, noticesRes, activityRes] = await Promise.all([
      dashboardApi.stats(),
      dashboardApi.notices(),
      dashboardApi.recentActivity(),
    ])
    Object.assign(stats, (statsRes as any).data)
    notices.value = (noticesRes as any).data || []
    activities.value = (activityRes as any).data || []
  } catch {
    // fallback
  }
}

onMounted(() => loadData())
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