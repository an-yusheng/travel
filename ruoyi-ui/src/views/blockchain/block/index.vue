<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" >
      <el-form-item label="区块Hash" prop="ipaddr" >
        <el-input
          v-model="queryParams.hash"
          placeholder="请输入区块hash"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>


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
      <el-table-column label="区块高度" align="center" prop="index" :show-overflow-tooltip="true" />
      <el-table-column label="区块hash" align="center" prop="hash"  :show-overflow-tooltip="true"/>
      <el-table-column label="上一个区块hash" align="center" prop="previousHash" :show-overflow-tooltip="true"/>
      <el-table-column label="工作量证明" align="center" prop="nonce" :show-overflow-tooltip="true" />
      <el-table-column label="上传人" align="center" prop="uploadedBy" :show-overflow-tooltip="true"/>
      <el-table-column label="归属人" align="center" prop="belongingPerson" :show-overflow-tooltip="true" />
      <el-table-column label="接收人" align="center" prop="recipient" :show-overflow-tooltip="true" />
      <el-table-column label="文件hash" align="center" prop="fileHash" :show-overflow-tooltip="true" />
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
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" />

      
  </div>
</template>

<script>
import { blockList } from "@/api/blockchain/encryption";

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
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      blockList(this.queryParams).then(response => {
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
      this.queryParams = {};
      this.handleQuery();
    }
  }
};
</script>

