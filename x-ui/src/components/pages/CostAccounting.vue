<template>
  <div class="cost-accounting">
    <div class="page-header">
      <h2>成本核算</h2>
    </div>

    <a-card>
      <a-tabs v-model:activeKey="activeTab">
        <a-tab-pane key="overview" tab="概览">
          <a-row :gutter="[16, 16]">
            <a-col :span="8">
              <a-statistic
                title="本月成本"
                :value="256789"
                :precision="2"
                prefix="¥"
              />
            </a-col>
            <a-col :span="8">
              <a-statistic
                title="上月成本"
                :value="245678"
                :precision="2"
                prefix="¥"
              />
            </a-col>
            <a-col :span="8">
              <a-statistic
                title="成本增长率"
                :value="4.5"
                :precision="2"
                suffix="%"
                :value-style="{ color: '#cf1322' }"
              />
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="list" tab="成本明细">
          <div class="table-operations">
            <a-space>
              <a-button type="primary">新增成本</a-button>
              <a-button>导出数据</a-button>
            </a-space>
          </div>
          <a-table :columns="columns" :data-source="data" :pagination="{ pageSize: 10 }">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'status'">
                <a-tag :color="record.status === 'approved' ? 'green' : 'orange'">
                  {{ record.status === 'approved' ? '已审核' : '待审核' }}
                </a-tag>
              </template>
              <template v-if="column.key === 'action'">
                <a-space>
                  <a>查看</a>
                  <a-divider type="vertical" />
                  <a>编辑</a>
                  <a-divider type="vertical" />
                  <a>删除</a>
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
    title: '成本编号',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '成本类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '金额',
    dataIndex: 'amount',
    key: 'amount',
  },
  {
    title: '发生日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark',
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
    id: 'COST001',
    type: '人工成本',
    amount: '¥12,345.00',
    date: '2024-01-01',
    status: 'approved',
    remark: '1月工资支出',
  },
  {
    key: '2',
    id: 'COST002',
    type: '材料成本',
    amount: '¥23,456.00',
    date: '2024-01-02',
    status: 'pending',
    remark: '原材料采购',
  },
  {
    key: '3',
    id: 'COST003',
    type: '制造费用',
    amount: '¥8,765.00',
    date: '2024-01-03',
    status: 'approved',
    remark: '设备维护',
  },
])
</script>

<style scoped>
.cost-accounting {
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