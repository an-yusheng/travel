<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="上传人" prop="ipaddr">
        <el-input
          v-model="queryParams.uploadedBy"
          placeholder="请输入上传人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="归属人" prop="userName">
        <el-input
          v-model="queryParams.belongingPerson"
          placeholder="请输入归属人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收人" prop="userName">
        <el-input
          v-model="queryParams.recipient"
          placeholder="请输入接收人"
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
        >上传</el-button>
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
      <el-table-column label="文件名称" align="center" prop="fileName" :show-overflow-tooltip="true" />
      <el-table-column label="文件地址" align="center" prop="localAddress" :show-overflow-tooltip="true" />
      <el-table-column label="文件hash" align="center" prop="fileHash" :show-overflow-tooltip="true" />
      <el-table-column label="上传人" align="center" prop="uploadedBy" :show-overflow-tooltip="true"/>
      <el-table-column label="归属人" align="center" prop="belongingPerson" :show-overflow-tooltip="true" />
      <el-table-column label="接收人" align="center" prop="recipient" :show-overflow-tooltip="true" />
      <el-table-column label="接受状态" align="center" prop="acceptState" >
        <template slot-scope="scope">
          <span v-if="scope.row.acceptState == 0">待接受</span>
          <span v-if="scope.row.acceptState == 1">已接受</span>
        </template>
      </el-table-column>
      <el-table-column label="文件状态" align="center" prop="fileStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.fileStatus == 0">有效</span>
          <span v-if="scope.row.fileStatus == 1">无效</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="douloadFile(scope.row)"
          >下载</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="transferBelonging(scope.row)"
          >转移归属权</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-key"
            @click="upperChain(scope.row)"
          >上链</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />

      
    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-col :span="24">
          <el-form-item
            label="接受人公钥"
            prop="recipient"
            :rules="[
              { required: true, message: '接受人公钥不能为空'}
            ]"
          >
          <el-input v-model="form.recipient" placeholder="请输入接受人公钥" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="加密私钥"
            prop="privateKey"
            :rules="[
              { required: true, message: '加密私钥不能为空'}
            ]"
          >
          <el-input v-model="form.privateKey" placeholder="请输入加密私钥" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="文件" prop="file">
              <el-upload class="upload"
                 ref="upload"
                 action="string"
                 :file-list="fileList"	
                 :auto-upload="false"
                 :http-request="uploadFile"		
                 :on-change="handleChange"	
                 :on-preview="handlePreview"	
                 :on-remove="handleRemove"	
                 :multiple="multiple">	
                  <el-button slot="trigger"
                   size="small"
                   type="primary"
                   @click="delFile">选取文件</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <el-dialog :title="title" :visible.sync="downloadOpen" width="800px" append-to-body>
      <el-form ref="form" :model="downloadForm" :rules="downloadRules" label-width="120px">
        <el-col :span="24">
          <el-form-item
            label="私钥"
            prop="privateKey"
            :rules="[
              { required: true, message: '私钥不能为空'}
            ]"
          >
          <el-input v-model="downloadForm.privateKey" placeholder="请输入私钥" />
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="downloadSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    
    <el-dialog :title="title" :visible.sync="transferOpen" width="800px" append-to-body>
      <el-form ref="transferForm" :model="transferForm" :rules="transferRules" label-width="120px">
        <el-col :span="24">
          <el-form-item
            label="接受人公钥"
            prop="recipient"
            :rules="[
              { required: true, message: '接受人公钥不能为空'}
            ]"
          >
          <el-input v-model="transferForm.recipient" placeholder="请输入接受人公钥" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="私钥"
            prop="privateKey"
            :rules="[
              { required: true, message: '私钥'}
            ]"
          >
          <el-input v-model="transferForm.privateKey" placeholder="请输入私钥" />
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="transferSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, upload, saveEncryFile, downloadEncryFile, checkDownloadEncryFile, transferFile, upperChain } from "@/api/blockchain/encryption";

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
      downloadOpen: false,
      // 是否显示弹出层
      open: false,
      transferOpen: false,
      form: {},
      downloadForm: {},
      transferForm: {},
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
      downloadRow: {},
      transferRow: {}
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
    /** 下载文件操作 */
    douloadFile(row) {
      this.reset();
      this.downloadOpen = true;
      this.title = "下载加密文件";
      this.downloadRow = row;
    },
    openUpload(){
      this.reset();
      this.open = true;
      this.title = "添加任务";
    },
    reset(){
      this.form = {};
      this.downloadForm = {};
      this.transferForm = {};
      this.formData = "";
      this.fileList = [];
    },
    /** 转移归属 */
    transferBelonging(row){
      checkDownloadEncryFile(row).then(response => {
            if(response.code === 200){
              this.reset();
              this.transferOpen = true;
              this.title = "转移归属权";
              this.transferRow = row;
            }else{
              this.$modal.msgError(response.msg);
            }
      });
    },
    /** 上链 */
    upperChain(row){
      upperChain(row).then(response => {
            if(response.code === 200){
              this.reset();
              this.$modal.msgSuccess("上链成功");
            }else{
              this.$modal.msgError(response.msg);
            }
      });
    },
    /** 转移归属权提交 */
    transferSubmit: function() {
      this.$refs["transferForm"].validate(valid => {
        if (valid) {
          let row = this.transferRow;
          row.recipient = this.transferForm.recipient;
          row.privateKey = this.transferForm.privateKey;
          transferFile(row).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("转移成功");
              this.transferOpen = false;
              this.getList();
            }else{
              this.$modal.msgError("转移失败");
            }
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.fileAddress === null || this.form.fileAddress === undefined){
            this.$modal.msgError("请上传文件");
            return;
          }
          saveEncryFile(this.form).then(response => {
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
    downloadSubmit: function(){
      if(this.downloadForm.privateKey === null || this.downloadForm.privateKey === undefined){
        this.$modal.msgError("请输入文件私钥");
        return;
      }
      let row = this.downloadRow;
      row.privateKey = this.downloadForm.privateKey;
      downloadEncryFile(row).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("下载成功");
              this.downloadOpen = false;
              this.getList();
              window.open(response.msg);
            }else{
              this.$modal.msgError(response.msg);
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
          this.form.fileAddress = response.url;
          this.form.fileName = response.newFileName;
          this.form.localAddress = response.localFile;
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

