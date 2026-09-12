<template>
  <div class="dashboard">
    <!-- 统计卡片区：一排 7 个 -->
    <el-row :gutter="16" class="stat-row">
      <el-col v-for="card in statCards" :key="card.key" :xs="12" :sm="8" :md="6" :lg="3">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-body">
            <div class="stat-icon" :style="{ background: card.color }">
              <el-icon><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区：2×2 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="chart-title">浏览量趋势（近7天）</div>
          <div ref="visitTrendRef" class="chart-box"></div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="chart-title">访客数趋势（近7天）</div>
          <div ref="visitorTrendRef" class="chart-box"></div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="chart-title">阅读量 TOP 10 文章</div>
          <div ref="topArticlesRef" class="chart-box"></div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="chart-title">访客省份分布</div>
          <div ref="provinceRef" class="chart-box"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 底部：运行天数 -->
    <div class="run-days">
      <el-icon><Clock /></el-icon>
      <span>系统已稳定运行 <b>{{ runDays }}</b> 天</span>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, reactive, ref, nextTick } from 'vue'
import * as echarts from 'echarts'
import {
  getDashboardStats,
  getVisitTrend,
  getVisitorTrend,
  getTopArticles,
  getProvinceDistribution,
  getRunDays
} from '@/api/stats'

const statCards = reactive([
  { key: 'totalViews', label: '总浏览量', value: 0, icon: 'View', color: '#1890ff' },
  { key: 'totalVisitors', label: '总访客数', value: 0, icon: 'User', color: '#52c41a' },
  { key: 'todayViews', label: '今日浏览', value: 0, icon: 'DataLine', color: '#13c2c2' },
  { key: 'todayNewVisitors', label: '今日新访客', value: 0, icon: 'UserFilled', color: '#722ed1' },
  { key: 'totalArticles', label: '文章总数', value: 0, icon: 'Document', color: '#fa8c16' },
  { key: 'totalComments', label: '评论总数', value: 0, icon: 'ChatDotRound', color: '#eb2f96' },
  { key: 'pendingComments', label: '待审评论', value: 0, icon: 'Warning', color: '#f5222d' }
])

const runDays = ref(0)

const visitTrendRef = ref(null)
const visitorTrendRef = ref(null)
const topArticlesRef = ref(null)
const provinceRef = ref(null)

let visitChart = null
let visitorChart = null
let topChart = null
let provinceChart = null

const chartInstances = []

function initChart(el, option) {
  const chart = echarts.init(el)
  chart.setOption(option)
  chartInstances.push(chart)
  return chart
}

// 折线图通用配置
function lineOption(dates, values, color) {
  return {
    tooltip: { trigger: 'axis' },
    grid: { left: 40, right: 20, top: 30, bottom: 30 },
    xAxis: { type: 'category', boundaryGap: false, data: dates },
    yAxis: { type: 'value', minInterval: 1 },
    series: [
      {
        type: 'line',
        smooth: true,
        data: values,
        itemStyle: { color },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: color + '66' },
            { offset: 1, color: color + '05' }
          ])
        }
      }
    ]
  }
}

async function loadStats() {
  const res = await getDashboardStats()
  statCards.forEach((c) => {
    c.value = res.data[c.key] ?? 0
  })
}

async function loadRunDays() {
  const res = await getRunDays()
  runDays.value = res.data
}

async function loadCharts() {
  const [visitRes, visitorRes, topRes, provinceRes] = await Promise.all([
    getVisitTrend(7),
    getVisitorTrend(7),
    getTopArticles(10),
    getProvinceDistribution()
  ])

  const visitDates = visitRes.data.map((i) => i.date)
  const visitValues = visitRes.data.map((i) => i.count)
  visitChart = initChart(visitTrendRef.value, lineOption(visitDates, visitValues, '#1890ff'))

  const visitorDates = visitorRes.data.map((i) => i.date)
  const visitorValues = visitorRes.data.map((i) => i.count)
  visitorChart = initChart(visitorTrendRef.value, lineOption(visitorDates, visitorValues, '#52c41a'))

  // TOP10 水平柱状图（倒序，让第一名在最上）
  const topData = [...topRes.data].reverse()
  topChart = initChart(topArticlesRef.value, {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: 10, right: 30, top: 20, bottom: 20, containLabel: true },
    xAxis: { type: 'value', minInterval: 1 },
    yAxis: {
      type: 'category',
      data: topData.map((i) => i.name),
      axisLabel: { width: 120, overflow: 'truncate' }
    },
    series: [
      {
        type: 'bar',
        data: topData.map((i) => i.value),
        barWidth: '55%',
        itemStyle: {
          borderRadius: [0, 4, 4, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#69c0ff' },
            { offset: 1, color: '#1890ff' }
          ])
        }
      }
    ]
  })

  // 省份分布环形饼图
  provinceChart = initChart(provinceRef.value, {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', right: 10, top: 'center', type: 'scroll' },
    series: [
      {
        type: 'pie',
        radius: ['40%', '68%'],
        center: ['40%', '50%'],
        avoidLabelOverlap: true,
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
        data: provinceRes.data
      }
    ]
  })
}

function handleResize() {
  chartInstances.forEach((c) => c && c.resize())
}

onMounted(async () => {
  await nextTick()
  try {
    await Promise.all([loadStats(), loadRunDays(), loadCharts()])
  } catch (e) {
    // 错误已由拦截器统一提示
  }
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstances.forEach((c) => c && c.dispose())
})
</script>

<style scoped>
.stat-row {
  margin-bottom: 16px;
}

.stat-card {
  border-radius: 12px;
  margin-bottom: 16px;
}

.stat-body {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-info {
  overflow: hidden;
}

.stat-value {
  font-size: 22px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  white-space: nowrap;
}

.chart-row .el-col {
  margin-bottom: 16px;
}

.run-days {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
  color: #606266;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.run-days b {
  color: #1890ff;
  font-size: 18px;
  margin: 0 4px;
}
</style>
