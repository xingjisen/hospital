<template>
  <div class="app-container">
    <div id="main">
      <!-- 公共头 -->
      <no-ssr>
        <myHeader />
      </no-ssr>
      <div class="main-container">
        <no-ssr>
          <el-scrollbar class="page-component__scroll">
            <!-- 内容区域 -->
            <nuxt />
          </el-scrollbar>
        </no-ssr>
      </div>
      <!-- 公共底 -->
      <myFooter />
    </div>
  </div>
</template>
<script>
import "~/assets/css/app.css";
import "~/assets/css/chunk.css";
import "~/assets/css/iconfont.css";
import "~/assets/css/main.css";

import myHeader from "~/layouts/myheader.vue";
import myFooter from "~/layouts/myfooter.vue";

export default {
  name: "default",
  components: {
    myHeader, myFooter
  }
};
</script>
