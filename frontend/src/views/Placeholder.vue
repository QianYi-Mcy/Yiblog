<template>
  <el-card class="placeholder-card">
    <el-empty :description="`「${title}」功能模块开发中`" />
  </el-card>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const title = computed(() => route.meta.title || '功能')
</script>

<style scoped>
.placeholder-card {
  border-radius: 12px;
}
</style>
