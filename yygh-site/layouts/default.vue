<template>
  <div class="app-container">
    <div id="main">
      <!-- 公共头 -->
      <myHeader/>
      <div class="main-container">
        <el-scrollbar class='page-component__scroll'>
          <!-- 内容区域 -->
          <nuxt />
        </el-scrollbar>
      </div>
      <!-- 公共底 -->
      <myFooter/>
    </div>
  </div>
</template>
<script>
import '~/assets/css/app.css'
import '~/assets/css/chunk.css'
import '~/assets/css/iconfont.css'
import '~/assets/css/main.css'

import myHeader from "~/layouts/myheader.vue";
import myFooter from "~/layouts/myfooter.vue";

export default {
  name:'default',
  components: {
    myHeader, myFooter
  }
}
</script>
