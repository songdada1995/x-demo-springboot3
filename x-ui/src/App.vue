<script lang="ts" setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN'
</script>

<template>
  <a-config-provider
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
