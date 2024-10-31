<template>
  <!-- header -->
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="javascript:window.location='/user'">实名认证 </span>
      </div>
      <div class="nav-item">
        <span class="v-link selected dark" onclick="javascript:window.location='/order'"> 挂号订单 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="javascript:window.location='/patient'"> 就诊人管理 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark"> 修改账号信息 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark"> 意见反馈 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->
    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div>
        <div class="title"> 实名认证</div>
        <div class="status-bar">
          <div class="status-wrapper"><span class="iconfont"></span>{{ userInfo.param.authStatusString }}</div>
        </div>
        <div class="tips"><span class="iconfont"></span>
          完成实名认证后才能添加就诊人，正常进行挂号，为了不影响后续步骤，建议提前实名认证。
        </div>
        <div class="form-wrapper" v-if="userInfo.authStatus == 0">
          <div>
            <el-form :model="userAuah" label-width="110px" label-position="left">
              <el-form-item prop="name" label="姓名：" class="form-normal">
                <div class="name-input">
                  <el-input v-model="userAuah.name" placeholder="请输入联系人姓名全称" class="input v-input" />
                </div>
              </el-form-item>
              <el-form-item prop="certificatesType" label="证件类型：">
                <el-select v-model="userAuah.certificatesType" placeholder="请选择证件类型"
                           class="v-select patient-select">
                  <el-option
                    v-for="item in certificatesTypeList"
                    :key="item.value"
                    :label="item.name"
                    :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="certificatesNo" label="证件号码：">
                <el-input v-model="userAuah.certificatesNo" placeholder="请输入联系人证件号码" class="input v-input" />
              </el-form-item>
              <el-form-item prop="name" label="上传证件：">
                <div class="upload-wrapper">
                  <div class="avatar-uploader">
                    <el-upload
                      class="avatar-uploader"
                      :class="{ disabled: uploadDisabled }"
                      :auto-upload="false"
                      ref="upload"
                      action="string"
                      :show-file-list="true"
                      :http-request="UploadImage"
                      :on-change="ChangeImage"
                      :on-remove="RemoveImage"
                      list-type="picture-card"
                      :limit="3"
                      :on-success="onUploadSuccess">
                      <div class="upload-inner-wrapper">
                        <!--                        <img v-if="userAuah.certificatesUrl" :src="userAuah.certificatesUrl" class="avatar">-->
                        <!--                        <i v-else class="el-icon-upload"></i>-->
                        <!--                        <i class="el-icon-upload"></i>-->
                        <!--                        <i class="el-icon-upload"></i>-->
                        <div v-if="!userAuah.certificatesUrl" class="text">上传证件照</div>
                      </div>
                    </el-upload>

                  </div>
                  <img src="@/assets/images/user/auth_example.png" class="example">
                </div>
              </el-form-item>
            </el-form>
            <div class="bottom-wrapper">
              <div class="button-wrapper">
                <div class="v-button" @click="saveUserAuah()">{{ submitBnt }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="context-container" v-if="userInfo.authStatus != 0">
          <div>
            <el-form :model="formData" label-width="110px" label-position="right">
              <el-form-item prop="name" label="姓名：" class="form-normal">
                <div class="name-input">
                  {{ userInfo.name }}
                </div>
              </el-form-item>
              <el-form-item prop="certificatesType" label="证件类型：">
                {{ userInfo.certificatesType }}
              </el-form-item>
              <el-form-item prop="certificatesNo" label="证件号码：">
                {{ userInfo.certificatesNo }}
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div><!-- 右侧内容 #end -->
    <!-- 登录弹出框 -->
  </div>
  <!-- footer -->
</template>

<script>
import "~/assets/css/hospital_personal.css";
import "~/assets/css/hospital.css";
import "~/assets/css/personal.css";
import { getUserInfo, userAuth } from "@/api/user";
import { findByDictCode } from "@/api/dict";
import { upload } from "@/api/file";

const defaultForm = {
  name: "",
  certificatesType: "",
  certificatesNo: "",
  certificatesUrl: ""
};
export default {
  data() {
    return {
      userAuah: defaultForm,
      uploadDisabled: false,
      certificatesTypeList: [],
      fileUrl: null, //"https://yygh-1302582151.cos.ap-beijing.myqcloud.com",
      userInfo: {
        param: {}
      },
      formData: {
        name: null,
        certificatesType: null,
        certificatesNo: null
      },
      submitBnt: "提交"
    };
  },
  created() {
    this.init();
  },
  computed: {},
  methods: {
    ChangeImage() {
      this.uploadDisabled = true;
    },
    RemoveImage() {
      this.uploadDisabled = false;
    },
    async UploadImage(param) {
      console.log(param);
      const formData = new FormData();
      formData.append("multipartFile", param.file);
      // console.log("param.file", param.file);
      // console.log("multipartFile", formData.get("multipartFile"));
      await upload(formData).then(response => {
        this.fileUrl = response.data;
        console.log("上传图片成功");
        this.userAuah.certificatesUrl = this.fileUrl;
        
        userAuth(this.userAuah).then(response => {
          this.$message.success("提交成功");
          window.location.reload();
        }).catch(e => {
          this.submitBnt = "提交";
        });

        param.onSuccess();  // 上传成功的图片会显示绿色的对勾
        // 但是我们上传成功了图片， fileList 里面的值却没有改变，还好有on-change指令可以使用
      }).catch(response => {
        console.log("图片上传失败");
        param.onError();
      });
    },
    init() {
      this.getUserInfo();
      this.getDict();
    },
    getUserInfo() {
      getUserInfo().then(response => {
        this.userInfo = response.data;
      });
    },
    saveUserAuah() {
      if (this.submitBnt == "正在提交...") {
        this.$message.info("重复提交");
        return;
      }
      this.submitBnt = "正在提交...";
      // 上传文件
      this.$refs.upload.submit();
    },
    getDict() {
      findByDictCode("CertificatesType").then(response => {
        this.certificatesTypeList = response.data;
      });
    },
    onUploadSuccess(response, file) {
      if (response.code !== 200) {
        this.$message.error("上传失败");
        return;
      }
      // 填充上传文件列表
      this.userAuah.certificatesUrl = file.response.data;
    }
  }
};
</script>
<style>
.header-wrapper .title {
  font-size: 16px;
  margin-top: 0;
}

.content-wrapper {
  margin-left: 0;
}

.patient-card .el-card__header .detail {
  font-size: 14px;
}

.page-container .title {
  letter-spacing: 1px;
  font-weight: 700;
  color: #333;
  font-size: 16px;
  margin-top: 0;
  margin-bottom: 20px;
}

.page-container .tips {
  width: 100%;
  padding-left: 0;
}

.page-container .form-wrapper {
  padding-left: 92px;
  width: 580px;
}

.form-normal {
  height: 40px;
}

.bottom-wrapper {
  width: 100%;
  padding: 0;
  margin-top: 0;
}
</style>
<style>

.disabled .el-upload--picture-card {
  display: none !important;
}
</style>
