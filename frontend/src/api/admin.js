import request from '@/utils/request'

/* ===================== 文章管理 ===================== */
export function pageArticles(params) {
  return request.get('/admin/articles', { params })
}
export function getArticle(id) {
  return request.get(`/admin/articles/${id}`)
}
export function createArticle(data) {
  return request.post('/admin/articles', data)
}
export function updateArticle(id, data) {
  return request.put(`/admin/articles/${id}`, data)
}
export function deleteArticle(id) {
  return request.delete(`/admin/articles/${id}`)
}

/* ===================== 评论管理 ===================== */
export function pageComments(params) {
  return request.get('/admin/comments', { params })
}
export function auditComment(id, status) {
  return request.put(`/admin/comments/${id}/status`, null, { params: { status } })
}
export function deleteComment(id) {
  return request.delete(`/admin/comments/${id}`)
}

/* ===================== 留言管理 ===================== */
export function pageMessages(params) {
  return request.get('/admin/messages', { params })
}
export function deleteMessage(id) {
  return request.delete(`/admin/messages/${id}`)
}

/* ===================== 分类管理 ===================== */
export function listCategories() {
  return request.get('/admin/categories')
}
export function createCategory(data) {
  return request.post('/admin/categories', data)
}
export function updateCategory(id, data) {
  return request.put(`/admin/categories/${id}`, data)
}
export function deleteCategory(id) {
  return request.delete(`/admin/categories/${id}`)
}

/* ===================== 用户 ===================== */
export function pageUsers(params) {
  return request.get('/admin/users', { params })
}
export function getUser(id) {
  return request.get(`/admin/users/${id}`)
}
export function updateUser(id, data) {
  return request.put(`/admin/users/${id}`, data)
}
