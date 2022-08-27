<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-input
          resize="none"
          v-model="content"
          :rows="40"
          placeholder="请输入内容"
          type="textarea">
        </el-input>
      </el-col>
      <el-col :span="4" style="margin-left: 20px">
        <div class="btn-group horizon">
          <el-button type="primary" @click="toUnderline">驼峰转下划线</el-button>
          <el-button type="warning" @click="toCamel">下划线转驼峰</el-button>
        </div>
        <div class="btn-group horizon" style="margin-top: 100px;">
          <el-button type="primary" @click="toJavaBean">生成JavaBean</el-button>
          <div>
            <el-input placeholer="请输入包名" type="text" v-model="packageName"></el-input>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      :visible="javaCodeShow"
      @close="javaCodeShow = false"
      :close-on-click-modal="false"
    >
      <div v-html="javaCodeContent">
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {json2JavaBeanHtml} from '@/api/trans'

export default {
  name: "index",
  data() {
    return {
      content: '',
      packageName: 'com.cszl',
      javaCodeShow: false,
      javaCodeContent: '',
    }
  },
  methods: {
    toUnderline() {
      this.content = this.content.replace(/([A-Z])/g, "_$1").toLowerCase();
    },
    toCamel() {
      this.content = this.content.replace(/\_(\w)/g, function (all, letter) {
        return letter.toUpperCase();
      });
    },
    toJavaBean() {
      this.javaCodeContent = ''
      if (this.content.trim() === '') {
        this.$message.error('请输入json数据')
        return
      }
      json2JavaBeanHtml({json: this.content, packageName: this.packageName}).then(res => {
        if (res && res.data) {
          this.javaCodeShow = true
          this.javaCodeContent = res.data.data
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
