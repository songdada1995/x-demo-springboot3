import type { TablePaginationConfig } from 'ant-design-vue'

// 分页本地化配置
export const paginationLocale = {
  items_per_page: '条/页',
  jump_to: '跳至',
  jump_to_confirm: '确定',
  page: '页',
  prev_page: '上一页',
  next_page: '下一页',
  prev_5: '向前 5 页',
  next_5: '向后 5 页',
  prev_3: '向前 3 页',
  next_3: '向后 3 页',
}

/**
 * 创建默认分页配置
 * @param pageSize 默认每页条数，默认为10
 * @returns TablePaginationConfig 分页配置对象
 */
export const createDefaultPagination = (pageSize = 10): TablePaginationConfig => {
  return {
    total: 0,
    current: 1,
    pageSize,
    showSizeChanger: true,
    showQuickJumper: true,
    pageSizeOptions: ['10', '20', '50', '100'],
    locale: paginationLocale,
    size: 'default',
    showTotal: (total) => `共 ${total} 条记录`,
  }
}

/**
 * 全局分页样式，可以在全局样式文件中引入
 */
export const paginationStyles = `
/* 分页组件样式优化 */
.ant-pagination {
  margin-top: 16px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.ant-pagination-options {
  margin-left: 16px;
}

.ant-pagination-options-size-changer {
  margin-right: 8px;
  width: 110px;
}

.ant-pagination-options-size-changer .ant-select-selector {
  padding-right: 8px !important;
}

.ant-pagination-options-quick-jumper {
  margin-right: 0;
}

.ant-pagination-options-quick-jumper input {
  width: 50px;
  margin: 0 8px;
}

.ant-select-dropdown {
  min-width: 100px !important;
  width: auto !important;
  max-width: 120px;
}

.ant-select-item-option-content {
  white-space: nowrap;
}

.ant-select-dropdown .ant-select-item {
  padding: 5px 12px;
  min-height: 32px;
}
` 