<template>
    <el-form ref="queryRef" :inline="true" :model="queryParam" label-width="auto">
        <el-form-item label="姓名/手机">
            <el-input clearable v-model="queryParam.keyword" placeholder="请输入姓名/手机"/>
        </el-form-item>
        <el-form-item label="创建时间">
            <el-date-picker
                    v-model="dateRange"
                    type="daterange"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    range-separator="至"
                    start-placeholder="请选择开始时间"
                    end-placeholder="请选择结束时间"
            />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="handleQuery">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
        </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableList" stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="60" :show-overflow-tooltip="true"/>
        <el-table-column prop="phone" label="手机号" :show-overflow-tooltip="true"/>
        <el-table-column prop="nickName" label="昵称" :show-overflow-tooltip="true"/>
        <el-table-column prop="name" label="姓名" :show-overflow-tooltip="true"/>
        <el-table-column prop="param.authStatusName" label="认证状态" :show-overflow-tooltip="true"/>
        <el-table-column prop="createTime" label="创建日期" :show-overflow-tooltip="true"/>
        <el-table-column prop="status" label="状态">
            <template v-slot="scope">
                <el-switch
                        v-model="scope.row.status"
                        inline-prompt
                        active-text="正常"
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
                <el-button link type="primary" size="small" :icon="View" @click="handleView(scope.row)">详情</el-button>
                <el-button link type="primary" size="small" v-if="scope.row.authStatus === 1" :icon="Bell"
                           @click="handleApproval(scope.row)">审批
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
    <el-dialog
            v-model="dialogVisible"
            :title="title"
            @close="dialogClose"
            custom-class="el-dialog"
            :fullscreen="true">
        <el-row>
            <el-col :span="24">
                <el-descriptions
                        title="用户信息"
                        :column="2"
                        border>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">手机号</div>
                        </template>
                        {{ userInfo.phone }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">用户姓名</div>
                        </template>
                        {{ userInfo.nickName }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">状态</div>
                        </template>
                        {{ userInfo.param?.statusName }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">注册时间</div>
                        </template>
                        {{ userInfo.createTime }}
                    </el-descriptions-item>
                </el-descriptions>
            </el-col>
        </el-row>
        <el-divider/>
        <el-row>
            <el-col :span="24">
                <el-descriptions
                        title="认证信息"
                        :column="2"
                        border>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">姓名</div>
                        </template>
                        {{ userInfo.name }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">证件类型</div>
                        </template>
                        {{ userInfo.certificatesType }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">证件号</div>
                        </template>
                        {{ userInfo.certificatesNo }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">证件图片</div>
                        </template>
                        <el-image style="width: 100px; height: 100px" :src="userInfo.certificatesUrl"
                                  :preview-src-list="[userInfo.certificatesUrl]"/>
                    </el-descriptions-item>
                </el-descriptions>
            </el-col>
        </el-row>
        <el-divider/>
        <el-row>
            <el-col :span="24">
                <el-table :data="patientList">
                    <el-table-column type="index" label="序号" width="60" :show-overflow-tooltip="true"/>
                    <el-table-column prop="name" label="姓名" :show-overflow-tooltip="true"/>
                    <el-table-column prop="param.certificatesTypeString" label="证件类型"
                                     :show-overflow-tooltip="true"/>
                    <el-table-column prop="certificatesNo" label="证件编号" :show-overflow-tooltip="true"/>
                    <el-table-column prop="sex" label="性别" :show-overflow-tooltip="true">
                        <template v-slot="scope">
                            {{ scope.row.sex === 0 ? '男' : '女' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="birthdate" label="出生年月" :show-overflow-tooltip="true"/>
                    <el-table-column prop="phone" label="手机号" :show-overflow-tooltip="true"/>
                    <el-table-column prop="isMarry" label="是否结婚" :show-overflow-tooltip="true">
                        <template v-slot="scope">
                            {{ scope.row.isMarry === 0 ? '未婚' : '已婚' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="param.fullAddress" label="地址" :show-overflow-tooltip="true"/>
                    <el-table-column prop="createTime" label="注册时间" :show-overflow-tooltip="true"/>
                </el-table>
            </el-col>
        </el-row>
    </el-dialog>

</template>
<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {Bell, View} from "@element-plus/icons-vue";
import {Action, ElLoading, ElMessage, ElMessageBox} from "element-plus";
import {approval, detail, getUserList, lockUser} from "@/api/yygh/user";

defineOptions({
    name: 'User'
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
    if (dateRange.value != null) {
        queryParam.value.createTimeBegin = dateRange.value[0]
        queryParam.value.createTimeEnd = dateRange.value[1]
    }
    loading.value = true
    const res = await getUserList(queryParam.value);
    tableList.value = res.data.records;
    loading.value = false
    total.value = res.data.total
}

/** 状态启停 */
function statusChange(row) {
    lockUser(row.id, row.status).then(() => {
        ElMessage.success('操作成功');
        getList()
    }).catch(err => {
        ElMessage.error('操作失败' + err);
    })
}

const dateRange = ref();
const queryParam = ref({
    pageNum: 1,
    pageSize: 5,
    keyword: null,
    createTimeBegin: "",
    createTimeEnd: ""
});

/** 搜索 */
function handleQuery() {
    queryParam.value.pageNum = 1;
    getList();
}

/** 重置 */
function handleReset() {
    dateRange.value = null
    queryParam.value = {
        pageNum: 1,
        pageSize: 5,
        keyword: null,
        createTimeBegin: "",
        createTimeEnd: ""
    }
    getList();
}

const dialogVisible = ref(false);

/** 详情信息*/
const userInfo = ref({})
const patientList = ref([])

function handleView(row) {
    dialogVisible.value = true
    const loading = ElLoading.service({
        target: '.el-dialog',
        fullscreen: true,
        background: 'rgba(0, 0, 0, 0.7)'
    })
    title.value = "用户信息"
    detail(row.id).then(resp => {
        userInfo.value = resp.data.userInfo
        patientList.value = resp.data.patientList
        loading.close()
    })
}

function handleApproval(row) {
    ElMessageBox.confirm(
        '是否通过审批？',
        {
            title: '用户审批',
            confirmButtonText: '通过',
            cancelButtonText: '不通过',
            closeOnClickModal: false,
            closeOnPressEscape: false,
            distinguishCancelAndClose: true
        })
        .then(() => {
            approval(row.id, 2)
        })
        .catch((action: Action) => {
            if (action === 'cancel') {
                approval(row.id, -1)
            }
        }).finally(() => {
        getList()
    })
}

const form = ref({
    hosname: null,
    hoscode: null,
    apiUrl: null,
    contactsName: null,
    contactsPhone: null,
    status: 0,
    isDeleted: 0,
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
    getList()
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
const title = ref('用户信息');

/** dialog关闭 */
function dialogClose() {
    // dialogVisible.value = false;
    reset();
}

</script>


<style scoped lang="less">
.title {
  margin: 10px 0;
}
</style>
