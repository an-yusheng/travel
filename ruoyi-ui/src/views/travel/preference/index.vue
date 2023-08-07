<template>
  <div class="app-container">
        <el-card>
    <el-form ref="form" :model="form" label-width="130px">
        <el-col :span="24">
          <el-form-item
            label="自然与冒险偏好："
          >
          <el-radio v-model="form.preference1" label="0.2 " border>不感兴趣</el-radio>
          <el-radio v-model="form.preference1" label="0.4" border>有点兴趣</el-radio>
          <el-radio v-model="form.preference1" label="0.6" border>一般</el-radio>
          <el-radio v-model="form.preference1" label="0.8" border>很有兴趣</el-radio>
          <el-radio v-model="form.preference1" label="1.0" border>非去不可</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="文化与历史偏好："
          >
          <el-radio v-model="form.preference2" label="0.2 " border>不感兴趣</el-radio>
          <el-radio v-model="form.preference2" label="0.4" border>有点兴趣</el-radio>
          <el-radio v-model="form.preference2" label="0.6" border>一般</el-radio>
          <el-radio v-model="form.preference2" label="0.8" border>很有兴趣</el-radio>
          <el-radio v-model="form.preference2" label="1.0" border>非去不可</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="美食与品酒偏好："
          >
          <el-radio v-model="form.preference3" label="0.2 " border>不感兴趣</el-radio>
          <el-radio v-model="form.preference3" label="0.4" border>有点兴趣</el-radio>
          <el-radio v-model="form.preference3" label="0.6" border>一般</el-radio>
          <el-radio v-model="form.preference3" label="0.8" border>很有兴趣</el-radio>
          <el-radio v-model="form.preference3" label="1.0" border>非去不可</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="休闲与度假偏好："
          >
          <el-radio v-model="form.preference4" label="0.2 " border>不感兴趣</el-radio>
          <el-radio v-model="form.preference4" label="0.4" border>有点兴趣</el-radio>
          <el-radio v-model="form.preference4" label="0.6" border>一般</el-radio>
          <el-radio v-model="form.preference4" label="0.8" border>很有兴趣</el-radio>
          <el-radio v-model="form.preference4" label="1.0" border>非去不可</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="娱乐与购物偏好："
          >
          <el-radio v-model="form.preference5" label="0.2" border>不感兴趣</el-radio>
          <el-radio v-model="form.preference5" label="0.4" border>有点兴趣</el-radio>
          <el-radio v-model="form.preference5" label="0.6" border>一般</el-radio>
          <el-radio v-model="form.preference5" label="0.8" border>很有兴趣</el-radio>
          <el-radio v-model="form.preference5" label="1.0" border>非去不可</el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="家庭与亲子偏好："
          >
          <el-radio v-model="form.preference6" label="0.2 " border>不感兴趣</el-radio>
          <el-radio v-model="form.preference6" label="0.4" border>有点兴趣</el-radio>
          <el-radio v-model="form.preference6" label="0.6" border>一般</el-radio>
          <el-radio v-model="form.preference6" label="0.8" border>很有兴趣</el-radio>
          <el-radio v-model="form.preference6" label="1.0" border>非去不可</el-radio>
          </el-form-item>
        </el-col>
      </el-form>            
      <el-button type="primary" @click="submitForm">立即提交</el-button>
      </el-card>
  </div>
</template>

<script>
import { userPreference, updatePreference } from "@/api/trave/trave";

export default {
  name: "Online",
  data() {
    return {
      // 遮罩层
      loading: true,
      title: "",
      // 是否显示弹出层
      open: false,
      form: {
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      userPreference(this.queryParams).then(response => {
        this.form = response.data;
        this.loading = false;
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
            updatePreference(this.form).then(response => {
              if(response.code === 200){
                this.$modal.msgSuccess("提交成功");
                this.open = false;
                this.getList();
              }else{
                this.$modal.msgError("提交失败");
              }
            });
        }
      });
    },
  }
};
</script>

