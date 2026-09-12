<template>
  <div class="page">
    <el-card shadow="never" class="page-card">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-input
          v-model="query.keyword"
          placeholder="按标题搜索"
          clearable
          style="width: 240px"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        >
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon> 搜索
        </el-button>
        <el-button type="success" @click="openCreate">
          <el-icon><Plus /></el-icon> 新建文章
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="list" v-loading="loading" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
        <el-table-column prop="summary" label="摘要" min-width="200" show-overflow-tooltip />
        <el-table-column prop="views" label="阅读量" width="90" sortable />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="pager"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :current-page="query.page"
        :page-size="query.size"
        :page-sizes="[10, 20, 50]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </el-card>

    <!-- 新建 / 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑文章' : '新建文章'" width="720px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="form.summary" type="textarea" :rows="2" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="封面图">
          <el-input v-model="form.coverImage" placeholder="封面图 URL（可空）" />
        </el-form-item>
        <el-form-item label="分类ID">
          <el-input-number v-model="form.categoryId" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">发布</el-radio>
            <el-radio :value="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="正文">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="支持 Markdown / HTML" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  pageArticles,
  createArticle,
  updateArticle,
  deleteArticle
} from '@/api/admin'

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const total = ref(0)

const query = reactive({ page: 1, size: 10, keyword: '' })

const dialogVisible = ref(false)
const emptyForm = () => ({
  id: null,
  title: '',
  summary: '',
  coverImage: '',
  categoryId: 0,
  status: 1,
  content: ''
})
const form = reactive(emptyForm())

async function loadData() {
  loading.value = true
  try {
    const res = await pageArticles(query)
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.page = 1
  loadData()
}

function handlePageChange(p) {
  query.page = p
  loadData()
}

function handleSizeChange(s) {
  query.size = s
  query.page = 1
  loadData()
}

function openCreate() {
  Object.assign(form, emptyForm())
  dialogVisible.value = true
}

function openEdit(row) {
  Object.assign(form, emptyForm(), row)
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.title) {
    ElMessage.warning('请填写标题')
    return
  }
  saving.value = true
  try {
    if (form.id) {
      await updateArticle(form.id, form)
    } else {
      await createArticle(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } finally {
    saving.value = false
  }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定删除文章「${row.title}」吗？`, '提示', {
    type: 'warning'
  })
    .then(async () => {
      await deleteArticle(row.id)
      ElMessage.success('删除成功')
      loadData()
    })
    .catch(() => {})
}

onMounted(loadData)
</script>

<style scoped>
.page-card {
  border-radius: 12px;
}
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
