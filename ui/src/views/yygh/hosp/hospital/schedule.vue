<template>
    <h5>当前选择: {{ hospName }}</h5>
    <el-container>
        <el-aside style="height: 81vh">
            <el-tree
                    style="max-width: 100%"
                    :data="data"
                    :props="defaultProps"
                    node-key="depcode"
                    :default-expanded-keys="[defaultExpandKey]"
                    :default-checked-keys="[defaultExpandKey]"
                    :current-node-key="defaultSelectKey"
                    @node-click="handleNodeClick"
                    :highlight-current="true"
            />
        </el-aside>
        <el-container v-if="!!ruleData.bookingScheduleRuleVoList">
            <el-header height="auto">
                <el-row :gutter="10">
                    <el-col :span="24">
                        <div class="flex gap-2" v-loading="scheduleLoading">
                            <el-check-tag v-for="(rule,index) in ruleData.bookingScheduleRuleVoList" :index="index"
                                          type="primary" :checked="rule.checked"
                                          size="size"
                                          @change="tagChange(rule)">
                                <div style="margin: 10px 0;font-size: 15px">{{
                                    rule.workDate + ' ' + rule.dayOfWeek
                                    }}
                                </div>
                                <div style="margin: 10px 0">{{
                                    rule.availableNumber + ' / ' + rule.reservedNumber
                                    }}
                                </div>
                            </el-check-tag>
                        </div>
                    </el-col>
                </el-row>
                <el-pagination class="mt-5 mb-5"
                               size="small"
                               v-model:current-page="queryParam.pageNum"
                               v-model:page-size="queryParam.pageSize"
                               :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
                               background
                               layout="->, total, prev, pager, next"
                               :hide-on-single-page="total===0?true:false"
                               :total="total"
                               @current-change="currentChange"
                />
            </el-header>
            <el-footer>
                <el-table v-loading="detailLoading" v-if="detailScheduleData.length" :data="detailScheduleData" border
                          style="width: 100%">
                    <el-table-column type="index" label="序号"/>
                    <el-table-column prop="title" label="职称">
                        <template v-slot="scope">
                            {{ scope.row.title }} | {{ scope.row.docname }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="workTime" label="号源时间">
                        <template v-slot="scope">
                            {{ scope.row.workTime == 0 ? '上午' : '下午' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="reservedNumber" label="可预约数"/>
                    <el-table-column prop="availableNumber" label="剩余预约数"/>
                    <el-table-column prop="amount" label="挂号费(元)"/>
                    <el-table-column prop="skill" label="擅长技能"/>
                </el-table>
                <el-empty v-else description="暂无数据" :image-size="200"/>
            </el-footer>
        </el-container>
        <el-container v-else>
            <el-main>
                <el-empty description="暂无数据" :image-size="200"/>
            </el-main>
        </el-container>
    </el-container>

</template>

<script setup lang="ts">
import {nextTick, onMounted, ref} from "vue";
import {getDeptTree, getDetailSchedule, getScheduleRule} from "@/api/yygh/hosp/hospital";

/** 父组件传递数据 */
const props = defineProps({
    hospital: Object
});
/** 查询树形结构数据 */
onMounted(() => {
    getList();
})

const data = ref([]);
const defaultExpandKey = ref(null);
const defaultSelectKey = ref(null);

async function getList() {
    const resp = await getDeptTree(props.hospital.hoscode)
    data.value = resp.data;
    console.log("data", data.value)

    if (ruleData.value != null) {
        queryParam.value = {
            pageNum: 1,
            pageSize: 7,
            depcode: data.value[0].children[0].depcode,
            hoscode: props.hospital.hoscode
        }
        getRule();
    }
    nextTick(() => {
        defaultExpandKey.value = data.value[0].children[0].depcode
        defaultSelectKey.value = data.value[0].children[0];
        hospName.value = data.value[0].children[0].depname
    })
}

const defaultProps = {
    children: 'children',
    label: 'depname'
}

/** 节点点击事件 */
const hospName = ref('')

function handleNodeClick(val) {
    hospName.value = val.depname
    queryParam.value = {
        pageNum: 1,
        pageSize: 7,
        depcode: val.depcode,
        hoscode: props.hospital.hoscode
    }
    getRule();
}

/** 查询排班信息 */
const ruleData = ref([]);
const queryParam = ref({
    pageNum: 1,
    pageSize: 7,
    depcode: null,
    hoscode: null
});
const total = ref(10)

const scheduleLoading = ref(false);

function getRule() {
    scheduleLoading.value = true;
    getScheduleRule(queryParam.value).then(schedule => {
        ruleData.value = schedule.data
        ruleData.value.bookingScheduleRuleVoList = ruleData.value.bookingScheduleRuleVoList.map(res => {
            res.checked = false;
            return res;
        })
        if (ruleData.value.bookingScheduleRuleVoList.length != 0) {
            tagChange(ruleData.value.bookingScheduleRuleVoList[0])
        } else {
            detailScheduleData.value = [];
        }
        total.value = schedule.data.total;
        scheduleLoading.value = false;
    })
}

/** 排班详情信息 */
const detailScheduleData = ref([]);
const detailLoading = ref(false);

function tagChange(val) {
    detailLoading.value = true;
    getDetailSchedule(queryParam.value.hoscode, queryParam.value.depcode, val.workDate).then(detail => {
        detailScheduleData.value = detail.data
        detailLoading.value = false;
    })
    ruleData.value.bookingScheduleRuleVoList = ruleData.value.bookingScheduleRuleVoList.map(rule => {
        rule.checked = false
        return rule;
    });
    return val.checked = !val.checked;
}


/** 分页页码变化 */
function currentChange(val) {
    queryParam.value.pageNum = val;
    getRule();
}
</script>

<style lang="less" scoped>
.el-col .flex .el-check-tag {
  font-weight: 200 !important;
  font-size: 1rem;
}

</style>
