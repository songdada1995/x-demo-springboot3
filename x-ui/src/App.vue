<script lang="ts" setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'

// 设置 dayjs 的语言为中文
dayjs.locale('zh-cn')
</script>

<template>
  <a-config-provider
    :locale="zhCN"
    :locale-data="{
      locale: 'zh_CN',
      Calendar: zhCN.Calendar,
      DatePicker: zhCN.DatePicker,
      TimePicker: zhCN.TimePicker,
      Table: zhCN.Table,
      Modal: zhCN.Modal,
      Pagination: zhCN.Pagination,
      Popconfirm: zhCN.Popconfirm,
      Form: zhCN.Form,
      Select: zhCN.Select
    }"
    :theme="{
      token: {
        colorPrimary: '#27c2ad',
        colorPrimaryHover: '#2ed3bd',
        colorPrimaryActive: '#1fb19d',
      }
    }"
  >
    <router-view></router-view>
  </a-config-provider>
</template>

<style>
#app {
  height: 100vh;
  width: 100vw;
  max-width: 100vw;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}

html, body {
  margin: 0;
  padding: 0;
  width: 100vw;
  max-width: 100vw;
  overflow-x: hidden;
}
</style>
