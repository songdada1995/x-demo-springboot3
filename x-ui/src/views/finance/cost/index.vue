<template>
  <div class="cost-container">
    <a-card class="cost-card">
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
        <a-tab-pane key="detail" tab="成本明细">
          <div class="search-form">
            <a-form layout="inline" :model="searchForm" class="search-form-inline">
              <a-row :gutter="[16, 16]">
                <a-col :span="6">
                  <a-form-item label="成本类型">
                    <a-select
                      v-model:value="searchForm.costType"
                      placeholder="请选择成本类型"
                      style="width: 240px"
                    >
                      <a-select-option value="material">材料成本</a-select-option>
                      <a-select-option value="labor">人工成本</a-select-option>
                      <a-select-option value="overhead">制造费用</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item label="日期范围">
                    <a-range-picker
                      v-model:value="searchForm.dateRange"
                      style="width: 240px"
                      :locale="locale"
                      :placeholder="['开始日期', '结束日期']"
                    />
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item label="备注">
                    <a-input
                      v-model:value="searchForm.remark"
                      placeholder="请输入备注"
                      style="width: 240px"
                    />
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
            <div class="button-group">
              <a-button type="primary" class="theme-button" @click="handleSearch">
                <template #icon><search-outlined /></template>
                搜索
              </a-button>
              <a-button type="primary" class="theme-button" @click="handleExport">
                <template #icon><export-outlined /></template>
                导出
              </a-button>
              <a-button type="primary" class="theme-button" @click="handleAdd">
                <template #icon><plus-outlined /></template>
                新增
              </a-button>
              <a-button type="primary" class="theme-button" @click="handleBatchEdit">
                <template #icon><edit-outlined /></template>
                修改
              </a-button>
              <a-button danger @click="handleBatchDelete">
                <template #icon><delete-outlined /></template>
                删除
              </a-button>
              <a-button @click="handleReset">
                <template #icon><reload-outlined /></template>
                重置
              </a-button>
            </div>
          </div>

          <a-table
            :columns="columns"
            :data-source="tableData"
            :loading="loading"
            :pagination="pagination"
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            @change="handleTableChange"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <a-space>
                  <a @click="handleEdit(record)">编辑</a>
                  <a-divider type="vertical" />
                  <a-popconfirm
                    title="确定要删除这条记录吗？"
                    @confirm="handleDelete(record)"
                  >
                    <a>删除</a>
                  </a-popconfirm>
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
import { ref, reactive, onMounted } from 'vue'
import {
  SearchOutlined,
  ExportOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ReloadOutlined,
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import type { TablePaginationConfig } from 'ant-design-vue'
import zhCN from 'ant-design-vue/es/date-picker/locale/zh_CN'
import { createDefaultPagination } from '../../../utils/pagination'

// 日期选择器语言
const locale = zhCN

// 当前激活的标签页
const activeTab = ref('overview')

// 搜索表单
const searchForm = reactive({
  costType: undefined,
  dateRange: [],
  remark: '',
})

// 表格数据
const loading = ref(false)
const tableData = ref([
  {
    key: '1',
    costType: '材料成本',
    amount: 150000,
    date: '2024-03-15',
    remark: '原材料采购',
  },
  {
    key: '2',
    costType: '人工成本',
    amount: 80000,
    date: '2024-03-15',
    remark: '员工工资',
  },
  {
    key: '3',
    costType: '制造费用',
    amount: 26800,
    date: '2024-03-15',
    remark: '设备维护',
  },
  {
    key: '4',
    costType: '材料成本',
    amount: 120000,
    date: '2024-03-14',
    remark: '辅料采购',
  },
  {
    key: '5',
    costType: '人工成本',
    amount: 75000,
    date: '2024-03-14',
    remark: '加班费',
  },
])
const selectedRowKeys = ref<string[]>([])

// 分页配置
const pagination = reactive<TablePaginationConfig>(createDefaultPagination())

// 表格列定义
const columns = [
  {
    title: '成本类型',
    dataIndex: 'costType',
    key: 'costType',
  },
  {
    title: '金额',
    dataIndex: 'amount',
    key: 'amount',
  },
  {
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark',
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
  },
]

// 搜索
const handleSearch = () => {
  // TODO: 实现搜索逻辑
}

// 导出
const handleExport = () => {
  // TODO: 实现导出逻辑
}

// 新增
const handleAdd = () => {
  // TODO: 实现新增逻辑
}

// 修改
const handleBatchEdit = () => {
  // TODO: 实现批量修改逻辑
}

// 删除
const handleBatchDelete = () => {
  // TODO: 实现批量删除逻辑
}

// 重置
const handleReset = () => {
  searchForm.costType = undefined
  searchForm.dateRange = []
  searchForm.remark = ''
  handleSearch()
}

// 表格选择变化
const onSelectChange = (keys: string[]) => {
  selectedRowKeys.value = keys
}

// 表格变化
const handleTableChange = (pag: any) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  handleSearch()
}

// 编辑单条记录
const handleEdit = (record: any) => {
  // TODO: 实现编辑逻辑
}

// 删除单条记录
const handleDelete = (record: any) => {
  // TODO: 实现删除逻辑
}
</script>

<style lang="less" scoped>
.cost-container {
  padding: 24px;
  background-color: #f0f2f5;
  min-height: 100vh;

  .cost-card {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

    .search-form {
      margin-bottom: 24px;

      .search-form-inline {
        width: 100%;
      }

      .button-group {
        margin-top: 16px;
        display: flex;
        gap: 8px;
      }
    }
  }
}

:deep(.ant-form) {
  width: 100%;
}

:deep(.ant-form-inline) {
  display: block;
}

:deep(.ant-form-item) {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}

:deep(.ant-form-item-label) {
  padding-bottom: 0;
  width: 70px;
  text-align: right;
  padding-right: 8px;
  flex: 0 0 70px !important;
  max-width: 70px !important;
}

:deep(.ant-form-item-control) {
  width: 240px;
  margin-left: 0 !important;
  flex: 0 0 240px !important;
}

:deep(.ant-form-item-label > label) {
  height: 32px;
  line-height: 32px;
  padding-right: 0;
}

:deep(.ant-input),
:deep(.ant-select),
:deep(.ant-picker) {
  width: 240px;
}

:deep(.ant-picker-range) {
  width: 240px;
}

/* 覆盖 ant-design-vue 的默认样式 */
:deep(.ant-form-item-control-input) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-control-input-content) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-explain) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-extra) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-label-col) {
  flex: 0 0 70px !important;
  max-width: 70px !important;
}

:deep(.ant-form-item-control-col) {
  flex: 0 0 240px !important;
  max-width: 240px !important;
}

:deep(.ant-form-item-label-col > label) {
  padding-right: 0 !important;
  margin-right: 0 !important;
}

/* 日期选择器特殊处理 */
:deep(.ant-picker) {
  margin-left: 0 !important;
}

:deep(.ant-picker-range) {
  margin-left: 0 !important;
}

:deep(.ant-picker-input) {
  margin-left: 0 !important;
}

:deep(.ant-picker-separator) {
  margin: 0 4px !important;
}

:deep(.ant-picker-suffix) {
  margin-left: 4px !important;
}

/* 统一所有表单项的间距 */
:deep(.ant-form-item-label),
:deep(.ant-form-item-control),
:deep(.ant-form-item-label-col),
:deep(.ant-form-item-control-col) {
  margin-right: 0 !important;
  margin-left: 0 !important;
  padding-right: 0 !important;
  padding-left: 0 !important;
}

:deep(.ant-form-item-label) {
  margin-right: 8px !important;
}

:deep(.ant-form-item-control) {
  margin-left: 0 !important;
}

/* 确保所有输入框组件对齐 */
:deep(.ant-input),
:deep(.ant-select),
:deep(.ant-picker),
:deep(.ant-picker-range),
:deep(.ant-select-selector),
:deep(.ant-picker-input) {
  margin-left: 0 !important;
  padding-left: 0 !important;
}

/* 调整输入框内占位文字的边距 */
:deep(.ant-input) {
  padding-left: 8px !important;
}

:deep(.ant-select-selector) {
  padding-left: 8px !important;
}

:deep(.ant-picker-input) {
  padding-left: 8px !important;
}

:deep(.ant-picker-range .ant-picker-input) {
  padding-left: 8px !important;
}

:deep(.ant-select-selection-placeholder) {
  padding-left: 0 !important;
}

:deep(.ant-picker-input > input) {
  padding-left: 0 !important;
}
</style> 