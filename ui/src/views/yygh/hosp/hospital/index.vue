<template>
    <el-form ref="queryRef" :inline="true" :model="queryParam" label-width="auto">
        <el-form-item label="医院编号">
            <el-input clearable v-model="queryParam.hoscode" placeholder="请输入医院编号"/>
        </el-form-item>
        <el-form-item label="医院名称">
            <el-input clearable v-model="queryParam.hosname" placeholder="请输入医院名称"/>
        </el-form-item>
        <el-form-item label="医院类型">
            <el-input clearable v-model="queryParam.hostype" placeholder="请输入医院类型"/>
        </el-form-item>
        <el-form-item label="省">
            <el-select
                    v-model="queryParam.provinceCode"
                    placeholder="请选择省"
                    filterable
                    @change="provinceChange"
                    style="width: 240px">
                <el-option
                        v-for="item in provinceList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="市">
            <el-select
                    v-model="queryParam.cityCode"
                    placeholder="请选择市"
                    no-data-text="请先选择省"
                    filterable
                    @change="cityChange"
                    style="width: 240px">
                <el-option
                        v-for="item in cityList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="区/县">
            <el-select
                    v-model="queryParam.districtCode"
                    placeholder="请选择区/县"
                    no-data-text="请先选择市"
                    filterable
                    style="width: 240px">
                <el-option
                        v-for="item in districtList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="状态">
            <el-select
                    v-model="queryParam.status"
                    placeholder="是否上线"
                    filterable
                    style="width: 240px">
                <el-option
                        v-for="item in statusList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.value"
                />
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="handleQuery">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
        </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableList" stripe style="width: 100%"
              @selection-change="tableChange">
        <el-table-column type="index" label="序号" width="60" :show-overflow-tooltip="true"/>
        <el-table-column prop="logoData" label="医院Logo" :show-overflow-tooltip="false">
            <template v-slot="scope">
                <el-image
                        style="width: 100px; height: 100px"
                        :src="'data:image/png;base64,' + scope.row.logoData"
                />
            </template>
        </el-table-column>
        <el-table-column prop="hosname" label="医院名称" :show-overflow-tooltip="true"/>
        <el-table-column prop="param.hostype" label=等级 :show-overflow-tooltip="true"/>
        <el-table-column prop="param.area" label="详情地址" :show-overflow-tooltip="true"/>
        <el-table-column prop="status" label="状态">
            <template v-slot="scope">
                <el-switch
                        v-model="scope.row.status"
                        inline-prompt
                        active-text="已上线"
                        inactive-text="未上线"
                        :active-value="1"
                        :inactive-value="0"
                        @change="statusChange(scope.row)"
                        style="--el-switch-on-color: #559eff; --el-switch-off-color: rgba(255,73,73,0.75)"
                />
            </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建日期" :show-overflow-tooltip="true"/>
        <el-table-column fixed="right" label="操作">
            <template #default="scope">
                <el-button link type="primary" size="small" :icon="View" @click="handleShowDetail(scope.row)">详情
                </el-button>
                <el-button link type="primary" size="small" :icon="Operation" @click="handleDeptTree(scope.row)">排班
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

    <el-dialog v-model="dialogShowVisible" :title="hospDetail.hosname+' 详细信息'" width="80%" fullscreen>
        <h3 style="margin: 10px 0">基本信息</h3>
        <el-row>
            <el-col :span="3">
                <div class="el-tabs--border-card odd title">
                    医院名称
                </div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card odd">
                    <span><b>{{ hospDetail.hosname }}</b></span> <span style="padding: 0 10px">|</span>
                    <span>{{ hospDetail.param.hostype }}</span>
                </div>
            </el-col>
            <el-col :span="5">
                <div class="el-tabs--border-card odd title">医院logo</div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card odd">
                    <el-image
                            style="width: 100px; height: 100px"
                            :src="'data:image/png;base64,' + hospDetail.logoData"
                    />
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="3">
                <div class="el-tabs--border-card even title">
                    医院编码
                </div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card even">
                    {{ hospDetail.hoscode }}
                </div>
            </el-col>
            <el-col :span="5">
                <div class="el-tabs--border-card even title">地址</div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card even">
                    {{ hospDetail.param.area }}
                </div>
            </el-col>
        </el-row>

        <el-row>
            <el-col :span="3">
                <div class="el-tabs--border-card odd title">
                    坐车路线
                </div>
            </el-col>
            <el-col :span="21">
                <div class="el-tabs--border-card odd">
                    {{ hospDetail.route }}
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="3">
                <div class="el-tabs--border-card even title">
                    医院简介
                </div>
            </el-col>
            <el-col :span="21">
                <div class="el-tabs--border-card even">
                    {{ hospDetail.intro }}
                </div>
            </el-col>
        </el-row>
        <h3 style="margin: 10px 0">预约规则信息</h3>
        <el-row>
            <el-col :span="3">
                <div class="el-tabs--border-card odd title">
                    预约周期
                </div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card odd">
                    {{ hospDetail.bookingRule.cycle }}天
                </div>
            </el-col>
            <el-col :span="5">
                <div class="el-tabs--border-card odd title">放号时间</div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card odd">
                    {{ hospDetail.bookingRule.releaseTime }}
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="3">
                <div class="el-tabs--border-card even title">
                    挂停时间
                </div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card even">
                    {{ hospDetail.bookingRule.stopTime }}
                </div>
            </el-col>
            <el-col :span="5">
                <div class="el-tabs--border-card even title">退号时间</div>
            </el-col>
            <el-col :span="8">
                <div class="el-tabs--border-card even">
                    {{ hospDetail.bookingRule.quitTime }}
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="3">
                <div class="el-tabs--border-card odd title">
                    预约规则
                </div>
            </el-col>
            <el-col :span="21">
                <div class="el-tabs--border-card odd">
                    <div style="padding-left: 30px" v-for="(item,index) in hospDetail.bookingRule.rule">
                        {{ index + 1 }}.{{ item }}
                    </div>
                </div>
            </el-col>
        </el-row>
    </el-dialog>

    <el-dialog
            :title="scheduleTitle+' 排班信息'"
            v-model="dialogScheduleVisible"
            fullscreen>
        <schedule :hospital="scheduleData"/>
    </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {findByDictCode, getHospitalList, listByParentId, showDetail, updateStatus} from "@/api/yygh/hosp/hospital";
import {Operation, View} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import Schedule from "@/views/yygh/hosp/hospital/schedule.vue";

defineOptions({
    name: 'HospList'
})
// ref引用
const queryRef = ref();
const loading = ref(false);


onMounted(() => {
    getList();
    getProvince();
    getStatus();
})

const total = ref(20);
const tableList = ref([]);
const getList = async () => {
    loading.value = true;
    const res = await getHospitalList(queryParam.value);
    tableList.value = res.data.content;
    loading.value = false;
    total.value = res.data.totalElements
}

const statusList = ref([]);

/**获取状态字典*/
function getStatus() {
    findByDictCode("Status").then(prov => {
        statusList.value = prov.data;
    })
}

const provinceList = ref([]);

/** 获取省份信息 */
function getProvince() {
    findByDictCode("Province").then(prov => {
        provinceList.value = prov.data;
    })
}

const cityList = ref([]);

function provinceChange(id) {
    cityList.value = [];
    districtList.value = [];
    queryParam.value.cityCode = null;
    queryParam.value.districtCode = null;
    listByParentId(id).then(resp => {
        cityList.value = resp.data
    })
}


const districtList = ref([]);

function cityChange(id) {
    districtList.value = [];
    queryParam.value.districtCode = null;
    listByParentId(id).then(resp => {
        districtList.value = resp.data
    })
}


/** 状态启停 */
function statusChange(row) {
    updateStatus(row.id, row.status).then(() => {
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
    hoscode: null,
    hosname: null,
    hostype: null,
    provinceCode: null,
    cityCode: null,
    districtCode: null,
    status: null
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
        hoscode: null,
        hosname: null,
        hostype: null,
        provinceCode: null,
        cityCode: null,
        districtCode: null,
        status: null
    }
    getList();
}

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


const ids = ref([]);

/** 查看医院设置详细信息 */
const hospDetail = ref({});

const dialogShowVisible = ref(false)

async function handleShowDetail(row) {
    const resp = await showDetail(row.id);
    hospDetail.value = resp.data;
    dialogShowVisible.value = true;
}

/** 排班信息 */
const dialogScheduleVisible = ref(false)
const scheduleTitle = ref('');
const scheduleData = ref('');

function handleDeptTree(row) {
    scheduleData.value = row;
    scheduleTitle.value = row.hosname;
    dialogScheduleVisible.value = true
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


</script>


<style scoped lang="less">

.title {
  font-weight: bold;
}

.even {
  padding: 10px;
  height: 100%;
}

.odd {
  height: 100%;
  background: #F9F9F9;
  padding: 10px;
}
</style>
