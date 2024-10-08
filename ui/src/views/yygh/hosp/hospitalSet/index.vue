<template>
    <el-form ref="queryRef" :inline="true" :model="queryParam" label-width="auto">
        <el-form-item label="医院名称">
            <el-input clearable v-model="queryParam.hosname" placeholder="请输入医院名称"/>
        </el-form-item>
        <el-form-item label="医院编号">
            <el-input clearable v-model="queryParam.hoscode" placeholder="请输入医院编号"/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="handleQuery">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
        </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
            <el-button
                    type="danger"
                    :icon="Delete"
                    :disabled="!ids.length"
                    @click="handleDel">批量删除
            </el-button>
        </el-col>
        <el-col :span="1.5">
            <el-button
                    type="primary"
                    :icon="Plus"
                    @click="handleAdd">新增
            </el-button>
        </el-col>
    </el-row>
    <el-table v-loading="loading" :data="tableList" stripe style="width: 100%"
              @selection-change="tableChange">
        <el-table-column type="selection" label="选择"/>
        <el-table-column type="index" label="序号" width="60" :show-overflow-tooltip="true"/>
        <el-table-column prop="hosname" label="医院名称" :show-overflow-tooltip="true"/>
        <el-table-column prop="hoscode" label="医院编号" :show-overflow-tooltip="true"/>
        <el-table-column prop="apiUrl" label="api基础路径" :show-overflow-tooltip="true"/>
        <el-table-column prop="contactsName" label="联系人姓名"/>
        <el-table-column prop="contactsPhone" label="联系人联系方式" :show-overflow-tooltip="true"/>
        <el-table-column prop="createTime" label="创建日期" :show-overflow-tooltip="true"/>
        <el-table-column prop="status" label="状态">
            <template v-slot="scope">
                <el-switch
                        v-model="scope.row.status"
                        inline-prompt
                        active-text="启用"
                        inactive-text="锁定"
                        :active-value="1"
                        :inactive-value="0"
                        @change="statusChange(scope.row)"
                        style="--el-switch-on-color: #559eff; --el-switch-off-color: rgba(255,73,73,0.75)"
                />
            </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
            <template #default="scope">
                <el-button link type="primary" size="small" :icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button link type="primary" size="small" :icon="Delete" @click="handleDel(scope.row)">删除
                </el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination class="mt-10"
                   v-model:current-page="queryParam.pageNum"
                   v-model:page-size="queryParam.pageSize"
                   :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
                   background
                   layout="->, sizes, total, prev, pager, next, jumper"
                   :total="total"
                   @size-change="sizeChange"
                   @current-change="currentChange"
    />

    <el-dialog v-model="open" :title="title" width="40%"
               @close="dialogClose" append-to-body>
        <el-form :model="form" :rules="formRules" ref="refForm">
            <el-row :gutter="10">
                <el-col :span="12">
                    <el-form-item label="医院名称" prop="hosname">
                        <el-input v-model="form.hosname" autocomplete="off"/>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="医院编码" prop="hoscode">
                        <el-input v-model="form.hoscode" :disabled="isEdit" autocomplete="off"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="10">
                <el-col :span="12">
                    <el-form-item label="联系人" prop="contactsName">
                        <el-input v-model="form.contactsName" autocomplete="off"/>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="联系人联系方式" prop="contactsPhone">
                        <el-input v-model="form.contactsPhone" autocomplete="off"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :span="10">
                <el-col :span="24">
                    <el-form-item label="api基础路径" prop="apiUrl">
                        <el-input v-model="form.apiUrl" autocomplete="off"/>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogCancel">取消</el-button>
                <el-button type="primary" @click="dialogVerify">确认</el-button>
            </div>
        </template>
    </el-dialog>
</template>
<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {addHosp, delHospList, detailHosp, getHospList, lockHosp, putHosp} from "@/api/yygh/hosp/hospitalSet";
import {Delete, Edit, Plus} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";

defineOptions({
    name: 'HospList'
})
// ref引用
const refForm = ref();
const queryRef = ref();
const loading = ref(false);

onMounted(() => {
    getList();
})

const total = ref(20);
const tableList = ref([]);
const getList = async () => {
    loading.value = true
    const res = await getHospList(queryParam.value);
    tableList.value = res.data.records;
    loading.value = false
    total.value = res.data.total
}

/** 状态启停 */
function statusChange(row) {
    lockHosp(row.id, row.status).then(() => {
        ElMessage.success('操作成功');
        getList()
    }).catch(err => {
        ElMessage.error('操作失败' + err);
    })
}

/** 复选框选择 */
const tableChange = (row) => {
    ids.value = row.map(res => res.id);
    console.log(ids.value);
}


const queryParam = ref({
    pageNum: 1,
    pageSize: 5,
    hosname: null,
    hoscode: null
});

/** 搜索 */
function handleQuery() {
    queryParam.value.pageNum = 1;
    getList();
}

/** 重置 */
function handleReset() {
    queryParam.value = {
        pageNum: 1,
        pageSize: 5,
        hosname: null,
        hoscode: null
    }
    getList();
}

const formCopy = ref({});
const form = ref({
    hosname: null,
    hoscode: null,
    apiUrl: null,
    contactsName: null,
    contactsPhone: null,
    status: 0,
    isDeleted: 0,
});
const formRules = reactive({
    hosname: [
        {required: true, message: '请输入医院名称', trigger: 'blur'},
    ],
    hoscode: [
        {required: true, message: '请输入医院编码', trigger: 'blur'},
    ],
    contactsName: [
        {required: true, message: '请输入联系人姓名', trigger: 'blur'},
        {min: 2, max: 5, message: '姓名不合规范', trigger: 'blur'},
    ],
    contactsPhone: [
        {required: true, message: '请输入联系人联系方式', trigger: 'blur'},
        {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur',},
    ],
});

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

/** 新增医院设置 */
function handleAdd() {
    open.value = true
    title.value = '新增医院设置'
}

const ids = ref([]);

/** 删除医院设置*/
function handleDel(row) {
    const _ids = row.id || ids.value;
    ElMessageBox.confirm(
        '是否要删除数据?',
        '删除数据',
        {
            confirmButtonText: '删除',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
        // console.log("数据List", !Array.isArray(_ids) ? [_ids] : [..._ids])
        delHospList(!Array.isArray(_ids) ? [_ids] : [..._ids]).then(() => {
            getList();
            ElMessage({
                type: 'success',
                message: '删除成功!',
            })
        }).catch(err => {
            ElMessage({
                type: 'error',
                message: '删除失败!' + err,
            })
        })
    }).catch(() => {
    })
}

const isEdit = ref(false);

/** 编辑医院设置 */
function handleEdit(row) {
    detailHosp(row.id).then(res => {
        form.value = res.data
        formCopy.value = {...res.data}
    })
    open.value = true
    isEdit.value = true
    title.value = '编辑医院设置'
}

/** 条目数 */
function sizeChange(val) {
    queryParam.value.pageSize = val;
    getList();
}

/** 分页页码变化 */
function currentChange(val) {
    queryParam.value.pageNum = val;
    getList();
}

const open = ref(false);
const title = ref('');

/** dialog关闭 */
function dialogClose() {
    isEdit.value = false;
    reset();
}

/** 新增编辑取消 */
function dialogCancel() {
    console.log("取消")
    open.value = false;
}

/** 新增编辑确认*/
function dialogVerify() {
    refForm.value.validate(valid => {
        // console.log("校验", valid)
        if (valid) {
            if (form.value.id != null) {
                if (JSON.stringify(formCopy.value) == JSON.stringify(form.value)) {
                    ElMessage.warning('修改失败,与原内容相等');
                } else {
                    putHosp(form.value).then(() => {
                        ElMessage.success('修改成功');
                        open.value = false;
                        reset();
                        getList()
                    }).catch(err => {
                        ElMessage.error('修改失败' + err);
                    })
                }
            } else {
                addHosp(form.value).then(() => {
                    ElMessage.success('新增成功');
                    open.value = false;
                    reset();
                    getList()
                }).catch(err => {
                    ElMessage.error('新增失败' + err);
                })
            }
        }
    })
}
</script>


<style scoped lang="less">
</style>
