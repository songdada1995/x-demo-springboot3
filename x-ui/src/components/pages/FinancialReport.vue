<template>
  <div class="financial-report">
    <div class="page-header">
      <h2>财务报表</h2>
    </div>

    <a-card>
      <a-tabs v-model:activeKey="activeTab">
        <a-tab-pane key="overview" tab="概览">
          <a-row :gutter="[16, 16]">
            <a-col :span="6">
              <a-statistic
                title="总资产"
                :value="1234567"
                :precision="2"
                prefix="¥"
              />
            </a-col>
            <a-col :span="6">
              <a-statistic
                title="总负债"
                :value="345678"
                :precision="2"
                prefix="¥"
              />
            </a-col>
            <a-col :span="6">
              <a-statistic
                title="净资产"
                :value="888889"
                :precision="2"
                prefix="¥"
              />
            </a-col>
            <a-col :span="6">
              <a-statistic
                title="资产负债率"
                :value="28"
                :precision="2"
                suffix="%"
              />
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="list" tab="报表列表">
          <div class="table-operations">
            <a-space>
              <a-button type="primary">生成报表</a-button>
              <a-button>导出数据</a-button>
            </a-space>
          </div>
          <a-table :columns="columns" :data-source="data" :pagination="{ pageSize: 10 }">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'type'">
                <a-tag :color="record.type === 'monthly' ? 'blue' : 'green'">
                  {{ record.type === 'monthly' ? '月度报表' : '季度报表' }}
                </a-tag>
              </template>
              <template v-if="column.key === 'action'">
                <a-space>
                  <a>查看</a>
                  <a-divider type="vertical" />
                  <a>下载</a>
                  <a-divider type="vertical" />
                  <a>打印</a>
                </a-space>
              </template>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const activeTab = ref('overview')

// 表格列定义
const columns = [
  {
    title: '报表编号',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '报表类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '报表期间',
    dataIndex: 'period',
    key: 'period',
  },
  {
    title: '生成时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
  {
    title: '生成人',
    dataIndex: 'creator',
    key: 'creator',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
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
    id: 'REP001',
    type: 'monthly',
    period: '2024-01',
    createTime: '2024-01-31 18:00:00',
    creator: '张三',
    status: '已生成',
  },
  {
    key: '2',
    id: 'REP002',
    type: 'quarterly',
    period: '2023-Q4',
    createTime: '2024-01-01 18:00:00',
    creator: '李四',
    status: '已生成',
  },
  {
    key: '3',
    id: 'REP003',
    type: 'monthly',
    period: '2023-12',
    createTime: '2023-12-31 18:00:00',
    creator: '张三',
    status: '已生成',
  },
])
</script>

<style scoped>
.financial-report {
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

.table-operations {
  margin-bottom: 16px;
}
</style> 