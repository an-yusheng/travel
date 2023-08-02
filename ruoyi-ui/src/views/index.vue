<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" >
      <el-form-item label="景点名称" prop="title" >
        <el-input
          v-model="queryParams.title"
          placeholder="请输入景点名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>


    <div>
      <el-row>
        <el-col :span="4" v-for="item in this.list" :key="item" style="margin: 20px;">
          <el-card :body-style="{ padding: '10px' }">
            <img :src="item.image" class="image">
            <div style="padding: 14px;">
              <span>{{ item.name }}</span>
              <div class="bottom clearfix">
              <el-rate v-model="item.recommended" disabled></el-rate>
              </div>
              <div class="bottom clearfix">
                <time class="time">{{ item.desc }}</time>
                <!-- <el-button type="text" class="button">操作按钮</el-button> -->
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px">
        <el-col :span="24">
          <el-form-item
            label="案件名称"
            prop="title"
          >
          <el-input v-model="form.title" placeholder="请输入案件名称" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="取证人"
            prop="forensic"
          >
          <el-input v-model="form.forensic" placeholder="请输入取证人" />
          </el-form-item>
        </el-col>
        
        <el-col :span="24">
          <el-form-item
            label="取证工具"
            prop="forensictool"
          >
          <el-input v-model="form.forensictool" placeholder="请输入取证工具" />
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
      
  </div>
</template>

<script>
import { list, upload, saveAttractions, removeAttractions, editAttractions } from "@/api/trave/trave";

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
        uploadedBy: undefined,
        belongingPerson: undefined,
        recipient: undefined
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
      list(this.queryParams).then(response => {
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
    /** 编辑 */
    editCase(row){
      this.reset();
      this.open = true;
      this.title = "修改案件";
      this.form = row;
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
    color: #999;
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