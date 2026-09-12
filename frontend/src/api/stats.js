import request from '@/utils/request'

// 仪表盘统计数字
export function getDashboardStats() {
  return request.get('/admin/stats/dashboard')
}

// 浏览量趋势
export function getVisitTrend(days = 7) {
  return request.get('/admin/stats/visitTrend', { params: { days } })
}

// 访客趋势
export function getVisitorTrend(days = 7) {
  return request.get('/admin/stats/visitorTrend', { params: { days } })
}

// TOP10 文章
export function getTopArticles(limit = 10) {
  return request.get('/admin/stats/topArticles', { params: { limit } })
}

// 省份分布
export function getProvinceDistribution() {
  return request.get('/admin/stats/provinceDistribution')
}

// 系统运行天数
export function getRunDays() {
  return request.get('/admin/stats/runDays')
}
