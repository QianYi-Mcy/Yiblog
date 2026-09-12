<template>
  <div class="page">
    <el-card shadow="never" class="page-card">
      <div class="toolbar">
        <el-button type="success" @click="openCreate">
          <el-icon><Plus /></el-icon> 新建分类
        </el-button>
      </div>

      <el-table :data="list" v-loading="loading" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" min-width="180" />
        <el-table-column prop="sort" label="排序值" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑分类' : '新建分类'" width="420px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序值">
          <el-input-number v-model="form.sort" :min="0" />
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
  listCategories,
  createCategory,
  updateCategory,
  deleteCategory
} from '@/api/admin'

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const dialogVisible = ref(false)
const emptyForm = () => ({ id: null, name: '', sort: 0 })
const form = reactive(emptyForm())

async function loadData() {
  loading.value = true
  try {
    const res = await listCategories()
    list.value = res.data
  } finally {
    loading.value = false
  }
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
  if (!form.name) {
    ElMessage.warning('请填写分类名称')
    return
  }
  saving.value = true
  try {
    if (form.id) {
      await updateCategory(form.id, form)
    } else {
      await createCategory(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } finally {
    saving.value = false
  }
}

function remove(row) {
  ElMessageBox.confirm(`确定删除分类「${row.name}」吗？`, '提示', { type: 'warning' })
    .then(async () => {
      await deleteCategory(row.id)
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
  margin-bottom: 16px;
}
</style>
