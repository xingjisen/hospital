<template>
    <el-form ref="queryRef" :inline="true" :model="queryParam" label-width="auto">
        <el-form-item label="就诊人：">
            <el-select v-model="queryParam.patientId" placeholder="请选择就诊人" class="v-select patient-select"
                       style="width: 200px;">
                <el-option
                        v-for="item in patientList"
                        :key="item.id"
                        :label="item.name + '【' + item.certificatesNo + '】'+':'+item.id"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="订单状态：" style="margin-left: 80px">
            <el-select v-model="queryParam.orderStatus" placeholder="全部" class="v-select patient-select"
                       style="width: 200px;">
                <el-option
                        v-for="item in statusList"
                        :key="item.status"
                        :label="item.comment"
                        :value="item.status">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="handleQuery">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
        </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableList" stripe style="width: 100%">
        <el-table-column label="就诊时间" width="150" :show-overflow-tooltip="true">
            <template v-slot="scope">
                {{ scope.row.reserveDate }} {{ scope.row.reserveTime === 0 ? "上午" : "下午" }}
            </template>
        </el-table-column>
        <el-table-column prop="hosname" label="医院" :show-overflow-tooltip="true"/>
        <el-table-column prop="depname" label="科室" :show-overflow-tooltip="true"/>
        <el-table-column prop="title" label="医生" :show-overflow-tooltip="true"/>
        <el-table-column prop="amount" label="医师服务费" :show-overflow-tooltip="true"/>
        <el-table-column prop="patientName" label="就诊人" :show-overflow-tooltip="true"/>
        <el-table-column
                prop="param.orderStatusString"
                label="订单状态">
        </el-table-column>
        <el-table-column fixed="right" label="操作">
            <template #default="scope">
                <el-button link type="primary" size="small" :icon="View" @click="handleView(scope.row)">详情</el-button>
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
        <div class="page-container">
            <div class="order-detail">
                <div class="title"> 挂号详情</div>
                <div class="status-bar">
                    <div class="left-wrapper">
                        <div class="status-wrapper BOOKING_SUCCESS">
                            <span class="iconfont"></span> {{ orderInfo.param?.orderStatusString }}
                        </div>
                    </div>
                    <div class="right-wrapper">
                        <img src="//img.114yygh.com/static/web/code_order_detail.png" class="code-img">
                        <div class="content-wrapper">
                            <div> 微信<span class="iconfont"></span>关注“北京114预约挂号”</div>
                            <div class="watch-wrapper"> 快速挂号，轻松就医</div>
                        </div>
                    </div>
                </div>
                <div class="info-wrapper">
                    <div class="title-wrapper">
                        <div class="block"></div>
                        <div>挂号信息</div>
                    </div>
                    <div class="info-form">
                        <el-form ref="form">
                            <el-form-item label="就诊人信息：">
                                <div class="content"><span>{{ orderInfo.patientName }}</span></div>
                            </el-form-item>
                            <el-form-item label="就诊日期：">
                                <div class="content"><span>{{ orderInfo.reserveDate }} {{
                                    orderInfo.reserveTime == 0 ? "上午" : "下午"
                                    }}</span></div>
                            </el-form-item>
                            <el-form-item label="就诊医院：">
                                <div class="content"><span>{{ orderInfo.hosname }} </span></div>
                            </el-form-item>
                            <el-form-item label="就诊科室：">
                                <div class="content"><span>{{ orderInfo.depname }} </span></div>
                            </el-form-item>
                            <el-form-item label="医生职称：">
                                <div class="content"><span>{{ orderInfo.title }} </span></div>
                            </el-form-item>
                            <el-form-item label="医事服务费：">
                                <div class="content">
                                    <div class="fee">{{ orderInfo.amount }}元
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item label="挂号单号：">
                                <div class="content"><span>{{ orderInfo.outTradeNo }} </span></div>
                            </el-form-item>
                            <el-form-item label="挂号时间：">
                                <div class="content"><span>{{ orderInfo.createTime }}</span></div>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
                <div class="rule-wrapper mt40">
                    <div class="rule-title"> 注意事项</div>
                    <div>1、请确认就诊人信息是否准确，若填写错误将无法取号就诊，损失由本人承担；<br>
                        <span style="color:red">2、【取号】就诊当天需在{{
                            orderInfo.fetchTime
                            }}在医院取号，未取号视为爽约，该号不退不换；</span><br>
                        3、【退号】在{{ orderInfo.quitTime }}前可在线退号 ，逾期将不可办理退号退费；<br>
                        4、北京114预约挂号支持自费患者使用身份证预约，同时支持北京市医保患者使用北京社保卡在平台预约挂号。请于就诊当日，携带预约挂号所使用的有效身份证件到院取号；<br>
                        5、请注意北京市医保患者在住院期间不能使用社保卡在门诊取号。
                    </div>
                </div>
            </div>
        </div>
    </el-dialog>

</template>
<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {ElLoading, ElMessage} from "element-plus";
import {lockUser} from "@/api/yygh/user";
import {getOrderAllList, getOrders, getStatusList} from "@/api/yygh/order";
import {list} from "@/api/yygh/patient";
import {View} from "@element-plus/icons-vue";

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
    loading.value = true
    const res = await getOrderAllList(queryParam.value);
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

const queryParam = ref({
    pageNum: 1,
    pageSize: 5,
    patientId: null,
    orderStatus: null
});

/** 搜索 */
function handleQuery() {
    getList();
}

/** 重置 */
function handleReset() {
    queryParam.value = {
        pageNum: 1,
        pageSize: 5,
        patientId: null,
        orderStatus: null
    }
    getList();
}

const dialogVisible = ref(false);

/** 详情信息*/
const orderInfo = ref({})

function handleView(row) {
    dialogVisible.value = true
    const loading = ElLoading.service({
        target: '.el-dialog',
        fullscreen: true,
        background: 'rgba(0, 0, 0, 0.7)'
    })
    title.value = "详情信息"
    getOrders(row.id).then(response => {
        orderInfo.value = response.data;
        loading.close()
    });

}


/** 重置属性 */
function reset() {
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
    // reset();
}

/** 查询下拉数据 */
const patientList = ref([])
const statusList = ref([])

onMounted(() => {
    getDicts();
})

function getDicts() {
    list().then(response => {
        patientList.value = response.data;
    });
    getStatusList().then(response => {
        statusList.value = response.data;
    });
}


</script>


<style scoped lang="less">
@font-face {
  font-family: "iconfont";
  /*src: url('iconfont.eot?t=1590319216482'); !* IE9 *!*/
  /*src: url('iconfont.eot?t=1590319216482#iefix') format('embedded-opentype'), !* IE6-IE8 *!*/
  src: url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2');
  /*url('iconfont.woff?t=1590319216482') format('woff'),*/
  /*url('iconfont.ttf?t=1590319216482') format('truetype'), !* chrome, firefox, opera, Safari, Android, iOS 4.2+ *!*/
  /*url('iconfont.svg?t=1590319216482#iconfont') format('svg'); !* iOS 4.1- *!*/
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.title {
  margin: 10px 0;
}

.page-container {
  padding-top: 38px;
  min-height: 500px;
  width: calc(100% - 200px);
  margin-left: 200px
}

.title {
  letter-spacing: 1px;
  font-weight: 700;
  color: #333;
  font-size: 16px
}

.order-detail .status-bar {
  margin-top: 20px;
  background-color: #f4f9ff;
  width: 1000px;
  height: 80px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between
}

.order-detail .status-bar .status-wrapper {
  color: #4490f1;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 1px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center
}

.order-detail .status-bar .status-wrapper .iconfont {
  margin-right: 6px;
  font-size: 28px;
  font-weight: 400
}

.order-detail .status-bar .status-wrapper.CANCELLED, .order-detail .status-bar .status-wrapper.STOP_VISIT {
  color: #999
}

.order-detail .status-bar .left-wrapper {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  width: 636px;
  padding-left: 20px
}

.order-detail .status-bar .identify-wrapper {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  text-align: right
}

.order-detail .status-bar .identify-wrapper .identify-text {
  font-size: 12px;
  color: #333
}

.order-detail .status-bar .identify-wrapper .identify-code {
  font-size: 24px;
  color: #4490f1
}

.order-detail .status-bar .right-wrapper {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;
  padding-left: 40px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  color: #333
}

.order-detail .status-bar .right-wrapper .content-wrapper {
  margin-top: -3px;
  margin-left: 12px
}

.order-detail .status-bar .right-wrapper .content-wrapper .text {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center
}

.order-detail .status-bar .right-wrapper .content-wrapper .iconfont {
  color: #00c25f;
  font-size: 24px
}

.order-detail .status-bar .right-wrapper .watch-wrapper {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  margin-top: 3px
}

.order-detail .status-bar .right-wrapper .code-img {
  width: 61px;
  height: 61px
}

.order-detail .info-wrapper .title-wrapper {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  color: #999;
  margin-top: 40px
}

.order-detail .info-wrapper .info-form {
  margin: 40px 0 0 140px
}

.order-detail .info-wrapper .info-form .fee {
  color: #4490f1;
  font-weight: 700
}

.order-detail .info-wrapper .info-form .patient-info {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  font-size: 12px
}

.order-detail .info-wrapper .info-form .patient-info .patient-name {
  color: #333;
  font-weight: 700;
  font-size: 14px
}

.order-detail .info-wrapper .info-form .patient-info .split {
  margin: 0 20px;
  height: 14px
}

.order-detail .info-wrapper .info-form .patient-info .patient-no {
  margin-left: 10px
}

.order-detail .rule-wrapper {
  background-color: #fafafa;
  padding: 30px
}

.order-detail .rule-wrapper .rule-title {
  color: #333;
  font-weight: 700;
  margin-bottom: 10px
}

.order-detail .bottom-wrapper {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center
}

.order-detail .bottom-wrapper .button-wrapper {
  width: 140px
}

.order-detail .mt40 {
  margin-top: 40px
}

.order-detail .mt60 {
  margin-top: 60px
}

.order-detail .ml20 {
  margin-left: 20px
}

.order-detail .cancel-dialog .icon {
  font-size: 54px;
  color: #4490f1;
  display: block;
  text-align: center
}

.order-detail .cancel-dialog .dialog-footer {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -ms-flex-pack: distribute;
  justify-content: space-around
}

.order-detail .cancel-dialog .dialog-text {
  margin: 30px 0 28px;
  color: #333;
  font-size: 16px;
  letter-spacing: 1px;
  text-align: center
}

.order-detail .cancel-dialog .button-wrapper {
  width: 140px
}

</style>
