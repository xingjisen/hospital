<template>
    <el-form>
        <el-form-item>
            <el-button type="primary" @click="handleAdd">新增</el-button>
            <el-button type="warning" @click="handleExport">导出</el-button>
            <el-button type="success" @click="HandleUpload">导入</el-button>
            <el-button type="info" @click="HandleClearCache">清除缓存并刷新数据</el-button>
            <el-button type="primary" @click="getList(1)">查询</el-button>
        </el-form-item>
    </el-form>

    <el-table
            :data="dictList"
            style="width: 100%"
            row-key="id"
            ref="dictListRef"
            border
            lazy
            v-loading="loading"
            :load="load"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="dictCode" label="编码"/>
        <el-table-column prop="value" label="值"/>
        <el-table-column prop="createTime" label="创建时间"/>
        <el-table-column prop="updateTime" label="修改时间"/>
        <el-table-column label="操作">
            <template #default="scope">
                <el-button type="primary" link @click="handleAdd(scope.row)">新增下级</el-button>
                <el-button type="warning" link @click="handleEdit(scope.row)">修改</el-button>
                <el-button type="danger" link @click="handleDict(scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-dialog v-model="uploadVisible" title="导入文件" width="40%">
        <el-upload
                class="upload-demo"
                drag
                ref="uploadRef"
                accept="xls,xlsx"
                action="http://localhost:8090/admin/cmn/dict/upload"
                :on-success="uploadSuccess"
                :on-error="uploadErrer"
                multiple>
            <el-icon class="el-icon--upload">
                <UploadFilled/>
            </el-icon>
            <div class="el-upload__text">将文件拖放到此处或<em>单击上传</em></div>
            <template #tip>
                <div class="el-upload__tip">请上传 xls 或 xlsx 格式文件</div>
            </template>
        </el-upload>
    </el-dialog>
    <el-dialog v-model="addVisible" :title="title" width="40%"
               @close="dialogClose" append-to-body>
        <el-form ref="refForm" :model="form" :rules="formRules"
                 label-width="80px">
            <!--            <el-form-item label="id" prop="id">-->
            <!--                <el-input v-model="form.id"/>-->
            <!--            </el-form-item>-->
            <el-form-item label="上级" prop="parentName">
                <el-input v-model="form.parentName" disabled/>
            </el-form-item>
            <el-form-item label="名称" prop="name">
                <el-input v-model="form.name"/>
            </el-form-item>
            <el-form-item label="编码" prop="dictCode">
                <el-input v-model="form.dictCode"/>
            </el-form-item>
            <el-form-item label="值" prop="value">
                <el-input v-model="form.value"/>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="cancel">取消</el-button>
                <el-button type="primary" @click="confirm">确定</el-button>
            </div>
        </template>
    </el-dialog>

</template>

<script setup lang="ts">
import {addDict, clearCache, delDict, detailDict, getDictList, putDict} from "@/api/yygh/dict";
import {onMounted, reactive, ref} from "vue";
import {UploadFilled} from "@element-plus/icons-vue";
import {download} from "@/utils/download";
import {ElMessage, ElMessageBox} from "element-plus";

const dictList = ref([]);

const loading = ref(false);
const form = ref({});
const dictListRef = ref();
const formRules = reactive({
    // id: [{required: true, message: '必填', trigger: 'blur'}],
    parentName: [{required: true, message: '必填', trigger: 'blur'}],
    parentId: [{required: true, message: '必填', trigger: 'blur'}],
    name: [{required: true, message: '必填', trigger: 'blur'}],
    // dictCode: [{required: true, message: '必填', trigger: 'blur'}],
    // value: [{required: true, message: '必填', trigger: 'blur'}],
});

/** 获取字典数据 */
function getList(id = 1) {
    loading.value = true
    getDictList(id).then(res => {
        // console.log(res)
        dictList.value = res.data
        loading.value = false;
    })
    dictListRef.value.store.states.lazyTreeNodeMap.value = {}
    dictListRef.value.store.states.treeData.value = {}
}

onMounted(() => {
    getList();
})

/** 节点下数据 */
const load = (row, treeNode, resolve) => {
    // console.log(row)
    getDictList(row.id).then(res => {
        resolve(res.data)
    })
    // console.log(dictListRef.value.store.states.lazyTreeNodeMap.value)
    // console.log(dictListRef.value.store.states.treeData.value)
}

/** 导出 */
function handleExport() {
    console.log("导出")
    download('/admin/cmn/dict/export', '字典数据')
}

const uploadVisible = ref(false);

/** 导入 */
function HandleUpload() {
    console.log("上传")
    uploadVisible.value = true;
}

/** 清除缓存刷新数据 */
async function HandleClearCache() {
    await clearCache()
    getList();
    ElMessage.success("缓存清除成功!")
}

// ref引用
const refForm = ref();
const uploadRef = ref();

/** 文件上传钩子函数 */
function uploadSuccess(succ) {
    console.log("文件上传succ", succ)
    ElMessage.success("文件上传成功")
    uploadVisible.value = false;
    uploadRef.value!.clearFiles();
    getList();
}

function uploadErrer(err) {
    ElMessage.error("文件上传失败" + err)
    console.log("文件上传err", err)
}

/** 删除 */
function handleDict(row) {
    ElMessageBox.confirm(
        "该操作会删除该节点以及子节点下全部数据，是否继续？",
        "删除数据",
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        })
        .then(() => {
            delDict(row.id).then(() => {
                ElMessage.success("删除成功")
                getList()
            }).catch(err => {
                ElMessage.error("删除失败", err)
            })
        })
        .catch(err => {
            console.log(err)
        })
}

const addVisible = ref(false);
const title = ref('');

/** 新增 */
function handleAdd(row) {
    if (!row.id) {
        title.value = '新增一级菜单';
        console.log('新增一级菜单')
        form.value.parentId = 1
        form.value.parentName = '全部分类'
    } else {
        title.value = '新增下级菜单';
        console.log('新增下级菜单')
        form.value.parentId = row.id
        form.value.parentName = row.name
    }
    addVisible.value = true;

    console.log(dictListRef.value.store.states.lazyTreeNodeMap.value)
    console.log(dictListRef.value.store.states.treeData.value)
}


/** 修改 */
function handleEdit(row) {
    title.value = '修改字典';
    form.value = row
    form.value.parentId = row.parentId
    detailDict(row.parentId).then(res => {
        form.value.parentName = res.data.name
    })
    addVisible.value = true;
}

/** 重置属性 */
function reset() {
    form.value = {
        hosname: null,
        hoscode: null,
        apiUrl: null,
        contactsName: null,
        contactsPhone: null,
        status: 0,
        isDeleted: 0,
    }
    refForm.value.clearValidate();
}

/** dialog关闭 */
function dialogClose() {
    addVisible.value = false;
    reset();
}

/** 取消 */
function cancel() {
    addVisible.value = false;
}

/** 确定 */
function confirm() {
    refForm.value.validate(valid => {
        if (valid) {
            if (!form.value.id) {
                addDict(form.value).then(() => {
                    ElMessage.success("新增成功")
                    addVisible.value = false;
                    reset();
                    getList();
                }).catch(err => {
                    ElMessage.error("新增失败", err)
                })
            } else {
                putDict(form.value).then(() => {
                    ElMessage.success("修改成功")
                    addVisible.value = false;
                    reset();
                    getList();
                }).catch(err => {
                    ElMessage.error("修改失败", err)
                })
            }
        }
    });
}
</script>

<style scoped lang="scss">

</style>
