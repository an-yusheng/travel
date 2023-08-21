<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="景点名称" prop="name">
        <el-input
          v-model="queryParams.name"
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


    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="openUpload"
        >新建景点</el-button>
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="list.slice((pageNum-1)*pageSize,pageNum*pageSize)"
      style="width: 100%;"
    >
      <el-table-column label="序号" type="index" align="center">
        <template slot-scope="scope">
          <span>{{(pageNum - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="景点名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column prop="image" label="景点图片" width="100">
        <template v-slot:default="scope">
          <el-image :src="scope.row.image"/>
        </template>
      </el-table-column>
      <el-table-column label="景点描述" align="center" prop="desc" :show-overflow-tooltip="true" />
      <el-table-column label="推荐级别" align="center" prop="recommended" :show-overflow-tooltip="true" >
        <template v-slot:default="scope">
          <el-rate
            v-model="scope.row.recommended"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value}">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column label="景点城市" align="center" prop="city" :show-overflow-tooltip="true"/>
      <el-table-column label="景点地址" align="center" prop="address" :show-overflow-tooltip="true"/>
      <el-table-column label="标签" align="center" prop="typeName" :show-overflow-tooltip="true" >
        <template v-slot:default="scope">
          <div v-for="item in scope.row.typeName" :key="item">
            <el-tag type="danger" style="margin: 2px;">{{ item }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="春季评分" align="center" prop="seasonSpring" :show-overflow-tooltip="true" />
      <el-table-column label="夏季评分" align="center" prop="seasonSummer" :show-overflow-tooltip="true" />
      <el-table-column label="秋季评分" align="center" prop="seasonAutumn" :show-overflow-tooltip="true" />
      <el-table-column label="冬季评分" align="center" prop="seasonWinter" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true" />
      <el-table-column label="更新时间" align="center" prop="updateDate" :show-overflow-tooltip="true" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="edit(scope.row)"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="remove(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />

      
    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-col :span="24">
          <el-form-item
            label="景点名称："
            prop="name"
            :rules="[
              { required: true, message: '景点名称不能为空'}
            ]"
          >
          <el-input v-model="form.name" placeholder="请输入景点名称" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="景点图片：" prop="file">
              <el-upload class="upload-demo"
                 ref="upload"
                 action="string"
                 :file-list="fileList"	
                 :auto-upload="false"
                 :on-change="handleChange"
                 :multiple="multiple">	
                  <el-button slot="trigger"
                   size="small"
                   type="primary"
                   @click="delFile">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        <el-col :span="24">
          <el-form-item
            label="景点描述："
            prop="desc"
          >
          <el-input
            type="textarea"
            placeholder="请输入景点描述"
            v-model="form.desc"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="推荐级别："
            prop="recommended"
          >
          <el-rate
            v-model="form.recommended"
            show-text>
          </el-rate>
        </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="景点城市："
            prop="city"
          >
          <el-input v-model="form.city" placeholder="请输入景点城市" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="景点地址："
            prop="address"
          >
          <el-input v-model="form.address" placeholder="请输入景点地址" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="景点标签：">
            <el-checkbox-group v-model="form.type">
              <el-checkbox label="1" name="type">自然与冒险</el-checkbox>
              <el-checkbox label="2" name="type">文化与历史</el-checkbox>
              <el-checkbox label="3" name="type">美食与品酒</el-checkbox>
              <el-checkbox label="4" name="type">休闲与度假</el-checkbox>
              <el-checkbox label="5" name="type">娱乐与购物</el-checkbox>
              <el-checkbox label="6" name="type">家庭与亲子</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="春季评分：">
            <el-input-number v-model="form.seasonSpring" :min="1" :max="100" label="春季评分"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="夏季评分：">
            <el-input-number v-model="form.seasonSummer" :min="1" :max="100" label="夏季评分"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="秋季评分：">
            <el-input-number v-model="form.seasonAutumn" :min="1" :max="100" label="秋季评分"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="冬季评分：">
            <el-input-number v-model="form.seasonWinter" :min="1" :max="100" label="冬季评分"></el-input-number>
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
      // 是否显示弹出层
      open: false,
      form: {
        type: [],
        seasonSpring:1,
        seasonSummer:1,
        seasonAutumn:1,
        seasonWinter:1,
      },
      // el-upload组件绑定的属性—— :file-list=“fileList”,渲染后fileList[index]是Object类型,而不是后台所需的File类型,
      // 而这个组件已经把对应的File类型存储到了fileList[index].raw这个属性里,直接拿来用就好.
      fileList: [],
      // 不支持多选
      multiple: false,
      formData: "",
      title: "",
      rules: {
        recipient: [
          { required: true, message: "接受人公钥不能为空", trigger: "blur" }
        ],
        privateKey: [
          { required: true, message: "加密私钥不能为空", trigger: "blur" }
        ],
      },
      downloadRules: {
        privateKey: [
          { required: true, message: "私钥不能为空", trigger: "blur" }
        ]
      },
      transferRules: {
        recipient: [
          { required: true, message: "接受人公钥不能为空", trigger: "blur" }
        ]
      },
      isEdit: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    openUpload(){
      this.reset();
      this.isEdit = false;
      this.open = true;
      this.title = "添加景点";
    },
    edit(row){
      this.reset();
      this.form = row;
      this.isEdit = true;
      this.open = true;
      this.title = "编辑景点";
    },
    reset(){
      this.form = {
        type: [],
        seasonSpring:1,
        seasonSummer:1,
        seasonAutumn:1,
        seasonWinter:1,
      };
      this.downloadForm = {};
      this.transferForm = {};
      this.formData = "";
      this.fileList = [];
    },
    /** 提交按钮 */
    submitForm: function() {
      console.log(this.form)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.image === null || this.form.image === undefined){
            this.$modal.msgError("请上传景点图片");
            return;
          }
          if(this.isEdit){
            editAttractions(this.form).then(response => {
              if(response.code === 200){
                this.$modal.msgSuccess("提交成功");
                this.open = false;
                this.getList();
              }else{
                this.$modal.msgError("提交失败");
              }
            });
          }else{
            saveAttractions(this.form).then(response => {
              if(response.code === 200){
                this.$modal.msgSuccess("提交成功");
                this.open = false;
                this.getList();
              }else{
                this.$modal.msgError("提交失败");
              }
            });
          }
        }
      });
    },
    remove(row){
      removeAttractions(row).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("删除成功");
              this.open = false;
              this.getList();
            }else{
              this.$modal.msgError("删除失败");
            }
          });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.downloadOpen = false;
      this.transferOpen = false;
      this.reset();
    },
    //点击上传文件触发的额外事件,清空fileList
    delFile () {
      this.fileList = [];
    },
    handleChange (file, fileList) {
      //this.fileList = fileList;
      let formData = new FormData();
      formData.append("file", fileList[0].raw);//拿到存在fileList的文件存放到formData中
      // console.log(this.fileList, "sb");
      upload(formData).then(response => {
        if(response.code === 200){
          this.form.image = response.url;
          this.$modal.msgSuccess("上传成功");
        }else{
          this.$modal.msgError("上传失败");
        }
      });
    },
    //自定义上传文件
    uploadFile (file) {
      
      this.formData.append("file", file.file);
      // console.log(file.file, "sb2");
    },
    //删除文件
    handleRemove (file, fileList) {
      console.log(file, fileList);
    },
    // 点击文件
    handlePreview (file) {
      console.log(file);
    }
  }
};
</script>

