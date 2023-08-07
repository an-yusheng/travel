<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" >
      <el-form-item label="景点名称" prop="name" >
        <el-input
          v-model="queryParams.name"
          placeholder="请输入景点名称"
          clearable
          @keyup.enter.native="handleQuery"
          v-loading.fullscreen.lock="loading"
        />
      </el-form-item>
      <el-form-item label="计划开始时间" prop="biginDate" >
        <el-date-picker
          v-model="queryParams.biginDate"
          type="date"
          placeholder="选择日期"
          @keyup.enter.native="handleQuery">
        </el-date-picker>
      </el-form-item>
      
      <el-form-item label="计划结束时间" prop="endDate" >
        <el-date-picker
          v-model="queryParams.endDate"
          type="date"
          placeholder="选择日期"
          @keyup.enter.native="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>


    <div>
      <el-row>
        <el-col :span="4" v-for="item in this.list" :key="item" style="width: 250px;height: 400px; margin-left: 30px;">
          <el-card :body-style="{ padding: '10px',height: '350px',background: '#2b2f3a' }"> 
            <div @click="openTravel(item)" style="cursor: pointer;">
              <img :src="item.image" class="image" style="height: 150px;">
              <div style="padding: 14px;color: #ffffff;">
                <span>{{ item.name }}</span>
                <div class="bottom clearfix">
                <el-rate v-model="item.recommended" disabled></el-rate>
                </div>
                <div class="bottom clearfix">
                  <time class="time">{{ item.desc }}</time>
                  <!-- <el-button type="text" class="button">操作按钮</el-button> -->
                </div>
                
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" /> -->

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-image :src="this.form.image" style="cursor: pointer;"></el-image>
      <el-descriptions class="margin-top" :column="1" :size="size" border>
        <template slot="extra" >
            <div v-if="this.form.wish === false" @click="onwish(true)" style="margin-top: 10px;">
              <el-button type="warning" icon="el-icon-star-off" circle></el-button>
              <span style="margin-left: 5px;font-size: 2px;">加入心愿单</span>
            </div>
            <div v-if="this.form.wish" @click="onwish(false)" style="margin-top: 10px;">
              <el-button type="danger" icon="el-icon-delete" circle></el-button>
              <span style="margin-left: 5px;font-size: 2px;">取消心愿单</span>
            </div>
          </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-camera-solid"></i>
            景点名称：
          </template>
          {{ this.form.name }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-star-off"></i>
            推荐级别
          </template>
          <el-rate
            v-model="this.form.recommended"
            disabled
            show-text
            text-color="#ff9900"
            score-template="{value}">
          </el-rate>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-comment"></i>
            景点描述
          </template>
          {{ this.form.desc }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-ticket"></i>
            景点标签
          </template>
            <el-tag  v-for="item in this.form.typeName" :key="item" type="danger" style="margin: 2px;">{{ item }}</el-tag>
      
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            景点地址
          </template>
          {{ this.form.address }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
      
  </div>
</template>

<script>
import { cpmputList, saveAttractionsUser,removeAttractionsUser } from "@/api/trave/trave";

export default {
  name: "Online",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      pageNum: 1,
      pageSize: 10,
      // 查询参数
      queryParams: {
        biginDate:undefined,
        endDate:undefined
      },
      title: "",
      open: false,
      form: {},
      currentDate: new Date()
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams)
      cpmputList(this.queryParams).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    reset(){
      this.form = {};
    },
    openTravel(row){
      this.reset();
      this.open = true;
      this.title = "景点信息";
      this.form = row;
    },
    onwish(wish){
        let params = {"attractionsId":this.form.id}
      if(wish){
        saveAttractionsUser(params).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("添加心愿成功");
              this.form.wish = wish
            }else{
              this.$modal.msgError(response.msg);
            }
          });
      }else{
        removeAttractionsUser(params).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("取消心愿成功");
              this.form.wish = wish
            }else{
              this.$modal.msgError(response.msg);
            }
          });
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {};
      this.handleQuery();
    },
    /** 提交按钮 */
    submitForm: function() {
      updateCase(this.form).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("提交成功");
              this.open = false;
              this.getList();
            }else{
              this.$modal.msgError(response.msg);
            }
          });
    },
  }
};
</script>

<style>
  .time {
    font-size: 13px;
    color: #ffffff;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }
  
</style>