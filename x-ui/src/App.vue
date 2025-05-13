<script lang="ts" setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN'
</script>

<template>
  <a-config-provider :locale="zhCN">
    <router-view></router-view>
  </a-config-provider>
</template>

<style>
#app {
  width: 100%;
  height: 100%;
}
</style>
