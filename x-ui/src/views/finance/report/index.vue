<template>
  <div class="report-container">
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="overview" tab="概览">
        <a-row :gutter="[16, 16]">
          <a-col :span="8">
            <a-statistic
              title="本月收入"
              :value="356789"
              :precision="2"
              prefix="¥"
            />
          </a-col>
          <a-col :span="8">
            <a-statistic
              title="本月支出"
              :value="256789"
              :precision="2"
              prefix="¥"
            />
          </a-col>
          <a-col :span="8">
            <a-statistic
              title="本月利润"
              :value="100000"
              :precision="2"
              prefix="¥"
              :value-style="{ color: '#3f8600' }"
            />
          </a-col>
        </a-row>
      </a-tab-pane>
      <a-tab-pane key="list" tab="报表列表">
        <div class="search-form">
          <a-form layout="inline" :model="searchForm" class="search-form-inline">
            <a-row :gutter="[16, 16]">
              <a-col :span="6">
                <a-form-item label="报表类型">
                  <a-select
                    v-model:value="searchForm.reportType"
                    placeholder="请选择报表类型"
                    style="width: 240px"
                  >
                    <a-select-option value="income">收入报表</a-select-option>
                    <a-select-option value="expense">支出报表</a-select-option>
                    <a-select-option value="profit">利润报表</a-select-option>
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
                    format="YYYY年MM月DD日"
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
                <a class="edit-link" @click="handleEdit(record)">编辑</a>
                <a-divider type="vertical" />
                <a-popconfirm
                  title="确定要删除这条记录吗？"
                  @confirm="handleDelete(record)"
                >
                  <a class="delete-link">删除</a>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
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
import { createDefaultPagination } from '../../../utils/pagination'

// 当前激活的标签页
const activeTab = ref<string>('overview')

// 日期选择器语言
const locale = {
  lang: {
    locale: 'zh_CN',
    placeholder: 'Select date',
    yearPlaceholder: 'Select year',
    quarterPlaceholder: 'Select quarter',
    monthPlaceholder: 'Select month',
    weekPlaceholder: 'Select week',
    rangePlaceholder: ['Start date', 'End date'],
    rangeYearPlaceholder: ['Start year', 'End year'],
    rangeMonthPlaceholder: ['Start month', 'End month'],
    rangeQuarterPlaceholder: ['Start quarter', 'End quarter'],
    rangeWeekPlaceholder: ['Start week', 'End week'],
    shortWeekDays: ['日', '一', '二', '三', '四', '五', '六'],
    shortMonths: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    today: '今天',
    now: '此刻',
    backToToday: '返回今天',
    ok: '确定',
    timeSelect: '选择时间',
    dateSelect: '选择日期',
    weekSelect: '选择周',
    clear: '清除',
    month: '月',
    year: '年',
    previousMonth: '上个月',
    nextMonth: '下个月',
    monthSelect: '选择月份',
    yearSelect: '选择年份',
    decadeSelect: '选择年代',
    yearFormat: 'YYYY年',
    monthFormat: 'M月',
    dateFormat: 'M月D日',
    dayFormat: 'D日',
    dateTimeFormat: 'YYYY年M月D日 HH时mm分ss秒',
    previousYear: '上一年',
    nextYear: '下一年',
    previousDecade: '上一年代',
    nextDecade: '下一年代',
    previousCentury: '上一世纪',
    nextCentury: '下一世纪',
  },
  timePickerLocale: {
    placeholder: '请选择时间',
    rangePlaceholder: ['开始时间', '结束时间'],
  },
}

// 搜索表单
const searchForm = reactive({
  reportType: undefined,
  dateRange: [],
  remark: '',
})

// 表格数据
const loading = ref(false)
const tableData = ref([
  {
    key: '1',
    reportType: '收入报表',
    amount: 356789,
    date: '2024-03-15',
    remark: '本月销售收入',
  },
  {
    key: '2',
    reportType: '支出报表',
    amount: 256789,
    date: '2024-03-15',
    remark: '本月成本支出',
  },
  {
    key: '3',
    reportType: '利润报表',
    amount: 100000,
    date: '2024-03-15',
    remark: '本月净利润',
  },
  {
    key: '4',
    reportType: '收入报表',
    amount: 320000,
    date: '2024-03-14',
    remark: '昨日销售收入',
  },
  {
    key: '5',
    reportType: '支出报表',
    amount: 230000,
    date: '2024-03-14',
    remark: '昨日成本支出',
  },
])
const selectedRowKeys = ref<string[]>([])

// 分页配置
const pagination = reactive<TablePaginationConfig>(createDefaultPagination())

// 表格列定义
const columns = [
  {
    title: '报表类型',
    dataIndex: 'reportType',
    key: 'reportType',
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
  searchForm.reportType = undefined
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
.report-container {
  padding: 24px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  :deep(.ant-tabs-content) {
    padding: 24px 0;
  }

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

.edit-link {
  color: #27c2ad;
  cursor: pointer;
  &:hover {
    color: #2ed3bd;
  }
}

.delete-link {
  color: #ff4d4f;
  cursor: pointer;
  &:hover {
    color: #ff7875;
  }
}
</style> 