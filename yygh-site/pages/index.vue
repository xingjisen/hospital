<template>
  <div class="home page-component">
    <el-carousel indicator-position="outside">
      <el-carousel-item v-for="item in 2" :key="item">
        <img src="~assets/images/web-banner1.png" alt="">
      </el-carousel-item>
    </el-carousel>
    <!-- 搜索 -->
    <div class="search-container">
      <div class="search-wrapper">
        <div class="hospital-search">
          <el-autocomplete class="search-input" prefix-icon="el-icon-search" v-model="state"
                           :fetch-suggestions="querySearch" placeholder="点击输入医院名称" @select="handleSelect">
            <span slot="suffix" class="search-btn v-link highlight clickable selected">搜索</span>
          </el-autocomplete>
        </div>
      </div>
    </div>
    <!-- bottom -->
    <div class="bottom">
      <div class="left">
        <div class="home-filter-wrapper">
          <div class="title">医院</div>
          <div>
            <div class="filter-wrapper">
              <span class="label">等级：</span>
              <div class="condition-wrapper">
                <span class="item v-link clickable highlight"
                      :class="hostypeActiveIndex === index ? 'selected':''"
                      v-for="(item,index) in hostypeList"
                      :key="index" @click="hostypeClick(item.value,index)">{{ item.name }}</span>
              </div>
            </div>
            <div class="filter-wrapper">
              <span class="label">地区：</span>
              <div class="condition-wrapper">
                <span class="item v-link clickable highlight"
                      :class="provinceActiveIndex === index ? 'selected':''"
                      v-for="(item,index) in districrList"
                      :key="index" @click="distClick(item.value,index)">{{ item.name }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="v-scroll-list hospital-list">
          <div class="v-card clickable list-item" v-for="(item,index) in list" :key="index" @click="show(item.hoscode)">
            <div>
              <div class="hospital-list-item hos-item" index="0">
                <div class="wrapper">
                  <div class="hospital-title">{{ item.hosname }}</div>
                  <div class="bottom-container">
                    <div class="icon-wrapper"><span class="iconfont"></span>
                      {{ item.param.hostype }}
                    </div>
                    <div class="icon-wrapper"><span class="iconfont"></span>
                      每天{{ item.bookingRule.releaseTime }}放号
                    </div>
                  </div>
                </div>
                <img :src="'data:image/jpeg;base64,'+item.logoData" :alt="item.hosname" class="hospital-img">
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="right">
        <div class="common-dept">
          <div class="header-wrapper">
            <div class="title"> 常见科室</div>
            <div class="all-wrapper"><span>全部</span>
              <span class="iconfont icon"></span>
            </div>
          </div>
          <div class="content-wrapper">
            <span class="item v-link clickable dark">神经内科 </span>
            <span class="item v-link clickable dark">消化内科 </span>
            <span class="item v-link clickable dark">呼吸内科 </span>
            <span class="item v-link clickable dark">内科 </span>
            <span class="item v-link clickable dark">神经外科 </span>
            <span class="item v-link clickable dark">妇科 </span>
            <span class="item v-link clickable dark"> 产科 </span>
            <span class="item v-link clickable dark">儿科 </span>
          </div>
        </div>
        <div class="space">
          <div class="header-wrapper">
            <div class="title-wrapper">
              <div class="icon-wrapper"><span class="iconfont title-icon"></span>
              </div>
              <span class="title">平台公告</span>
            </div>
            <div class="all-wrapper">
              <span>全部</span>
              <span class="iconfont icon"></span>
            </div>
          </div>
          <div class="content-wrapper">
            <div class="notice-wrapper">
              <div class="point"></div>
              <span class="notice v-link clickable dark">关于延长北京大学国际医院放假的通知 </span>
            </div>
            <div class="notice-wrapper">
              <div class="point"></div>
              <span class="notice v-link clickable dark">北京中医药大学东方医院部分科室医生门诊医 </span>
            </div>
            <div class="notice-wrapper">
              <div class="point"></div>
              <span class="notice v-link clickable dark"> 武警总医院号源暂停更新通知 </span>
            </div>
          </div>
        </div>
        <div class="suspend-notice-list space">
          <div class="header-wrapper">
            <div class="title-wrapper">
              <div class="icon-wrapper">
                <span class="iconfont title-icon"></span>
              </div>
              <span class="title">停诊公告</span>
            </div>
            <div class="all-wrapper">
              <span>全部</span>
              <span class="iconfont icon"></span>
            </div>
          </div>
          <div class="content-wrapper">
            <div class="notice-wrapper">
              <div class="point"></div>
              <span
                class="notice v-link clickable dark"> 中国人民解放军总医院第六医学中心(原海军总医院)呼吸内科门诊停诊公告 </span>
            </div>
            <div class="notice-wrapper">
              <div class="point"></div>
              <span class="notice v-link clickable dark"> 首都医科大学附属北京潞河医院老年医学科门诊停诊公告 </span>
            </div>
            <div class="notice-wrapper">
              <div class="point"></div>
              <span class="notice v-link clickable dark">中日友好医院中西医结合心内科门诊停诊公告 </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import "~/assets/css/app.css";
import "~/assets/css/chunk.css";
import "~/assets/css/iconfont.css";
import "~/assets/css/main.css";
import { findByDictCode } from "@/api/dict";
import { getByHospname, getList } from "@/api/hosp";

export default {
  // layout: 'default'// 指定使用 default 布局
  // layout(context) {
  //   return 'default'
  // }
  // asyncData({ isDev, route, store, env, params, query, req, res, redirect, error }) {
  // 服务端异步渲染，显示医院列表
  asyncData({ params, error }) {
    return getList({ pageNum: 1, pageSize: 10 }).then(resp => {
      return {
        list: resp.data.data.content,
        limit: resp.data.data.totalPages
      };
    });
  },
  data() {
    return {
      state: "",
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        hostype: null,
        districtCode: null
      },
      hosname: "", // 医院名称
      hostypeList: [],//医院等级集合
      districrList: [],//地区集合
      hostypeActiveIndex: 0,//选中状态
      provinceActiveIndex: 0//选中状态
    };
  },
  created() {
    this.getDict();
  },
  methods: {
    // 输入框输入建议
    querySearch(queryString, callback) {
      this.clear();
      if (!!queryString) {
        getByHospname(queryString).then(resp => {
          console.log(resp);
          // resp.data.map(item => {
          //   item.value = item.hosname;
          //   return item;
          // });
          const data = resp.data;
          callback(data.map(item => {
            item.value = item.hosname;
            return item;
          }));
        });
      } else {
        return callback([]);
      }
    },
    // 下拉框选择内容
    handleSelect(item) {
      console.log("下拉框选择");
      window.location.href = "/hosp/" + item.hoscode;
    },
    show(hoscode) {
      window.location.href = "/hosp/" + hoscode;
    },
    //根据医院等级查询
    hostypeClick(hostype, index) {
      this.hostypeActiveIndex = index;
      this.queryParam.hostype = hostype;
      this.getList();
    },
    //根据地区查询医院
    distClick(distCode, index) {
      this.provinceActiveIndex = index;
      this.queryParam.districtCode = distCode;
      this.getList();
    },
    getList() {
      getList(this.queryParam).then(resp => {
        this.list = resp.data.content;
        this.limit = resp.data.totalPages;
      });
    },
    clear() {
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        hostype: null,
        districtCode: null
      };
    },
    getDict() {
      // 查询医院等级列表和所有地区列表
      findByDictCode("Hostype").then(response => {
        this.hostypeList = [];
        this.hostypeList = response.data;
        this.hostypeList.unshift({
          name: "全部",
          value: ""
        });
      });
      // 查询地区数据
      findByDictCode("Beijing").then(response => {
        this.districrList = [];
        this.districrList = response.data;
        this.districrList.unshift({
          name: "全部",
          value: ""
        });
      });
    }
  }
};
</script>
