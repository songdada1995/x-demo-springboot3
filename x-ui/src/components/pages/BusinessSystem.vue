<template>
  <div class="business-system">
    <div class="page-header">
      <h2>{{ title }}</h2>
    </div>

    <a-card>
      <a-tabs v-model:activeKey="activeTab">
        <a-tab-pane key="overview" tab="概览">
          <a-row :gutter="[16, 16]">
            <a-col :span="8">
              <a-statistic
                title="总订单数"
                :value="1128"
                style="margin-right: 50px"
              >
                <template #suffix>
                  <span>个</span>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic
                title="本月订单"
                :value="93"
                style="margin-right: 50px"
              >
                <template #suffix>
                  <span>个</span>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic
                title="待处理"
                :value="5"
                style="margin-right: 50px"
              >
                <template #suffix>
                  <span>个</span>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="orders" tab="订单管理">
          <a-table :columns="columns" :data-source="data" :pagination="false">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'status'">
                <a-tag :color="record.status === 'success' ? 'green' : 'orange'">
                  {{ record.status === 'success' ? '已完成' : '处理中' }}
                </a-tag>
              </template>
              <template v-if="column.key === 'action'">
                <a-space>
                  <a>查看</a>
                  <a-divider type="vertical" />
                  <a>编辑</a>
                </a-space>
              </template>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="settings" tab="系统设置">
          <a-form :model="formState" layout="vertical">
            <a-form-item label="系统名称">
              <a-input v-model:value="formState.name" />
            </a-form-item>
            <a-form-item label="系统描述">
              <a-textarea v-model:value="formState.description" :rows="4" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary">保存设置</a-button>
            </a-form-item>
          </a-form>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeTab = ref('overview')

// 根据路由参数设置标题
const title = computed(() => {
  const systemId = route.params.id
  return `业务系统${systemId}`
})

// 表格列定义
const columns = [
  {
    title: '订单编号',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '客户名称',
    dataIndex: 'customer',
    key: 'customer',
  },
  {
    title: '订单金额',
    dataIndex: 'amount',
    key: 'amount',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

// 表格数据
const data = ref([
  {
    key: '1',
    id: 'ORD001',
    customer: '张三',
    amount: '¥1,234.00',
    status: 'success',
    createTime: '2024-01-01 12:00:00',
  },
  {
    key: '2',
    id: 'ORD002',
    customer: '李四',
    amount: '¥2,345.00',
    status: 'processing',
    createTime: '2024-01-02 13:00:00',
  },
])

// 表单数据
const formState = reactive({
  name: '',
  description: '',
})
</script>

<style scoped>
.business-system {
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
</style> 