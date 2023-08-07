<template>
  <div class="app-container"  style="height: 700px;">
    <el-container style="height: 100%; border: 1px solid #eee">
      <el-card style="width: 200px;padding: auto;">
        <div style="text-align:center;font-weight: bold;">心愿单</div>
        <el-card style="height: 50px;margin-top: 10px;background-color: #fef0f0;border-color: #fde2e2;color: #f56c6c;font-size: 10px;" 
        v-for="o in 4" :key="o" >
            {{'列表内容 ' + o }}
            <i class="el-icon-delete" style="float: right;cursor: pointer;"></i>
        </el-card>
      </el-card>
    <el-card class="box-card" style="margin-left: 20px;width: 100%;">
      <el-main>
        <el-calendar>
          <template #dateCell="{data}">
            <div style="margin:0px" @click="calendarOnClick(data)">
              {{ data.day.split('-').slice(2).join() }} 
              <div v-for="(i, index) in dayTime" :key="index">
                <div v-if="data.day==i.courseTime" style="color: rgb(88 132 211);">{{i.courseName}}</div>
              </div>
            </div>
          </template>
        </el-calendar>
      </el-main>
    </el-card>
    </el-container>
  </div>
</template>

<script>
import { userPreference, updatePreference } from "@/api/trave/trave";

export default {
  name: "Online",
  data() {
    return {
			nowdate: new Date(),
      dayTime: ["2023-05-09"],
			dataTime: null,
			dataTimeY: null,
			dataTimeM: null,
      // 遮罩层
      loading: true,
      total: 0,
      // 表格数据
      list: [],
      title: "",
      // 是否显示弹出层
      open: false,
      form: {
      },
    };
  },
  created() {
    this.getList();
		// 日历上个月按钮
            this.$nextTick(()=>{
                let prevBtn = document.querySelector(".el-calendar__button-group .el-button-group>button:nth-child(1)")
                prevBtn.addEventListener('click',()=>{
                    if(this.dataTime === null){
                        const date = new Date(this.nowdate);
                        const Y = date.getFullYear();
                        const M = date.getMonth() + 1;
                        this.dataTimeM = Number(M-Number('1'))
                        this.dataTimeY = Y 
                        this.dataTime = this.dataTimeY + '-' + (this.dataTimeM < 10 ? '0' + this.dataTimeM : this.dataTimeM);
                    }else if(this.dataTimeM !== null && this.dataTimeM === 1){
                        this.dataTimeM = 12 
                        this.dataTime = (this.dataTimeY - Number('1')) + '-' + String(this.dataTimeM < 10 ? '0' + this.dataTimeM : this.dataTimeM);
                        this.dataTimeY = this.dataTimeY - Number('1')
                    }else{
                        this.dataTimeM = Number(this.dataTimeM-Number('1')) 
                        this.dataTime = this.dataTimeY + '-' + (this.dataTimeM < 10 ? '0' + this.dataTimeM : this.dataTimeM);
                    }
                    console.log(this.dataTime)
                })
            })
 
            // 日历按钮今天
            this.$nextTick(()=>{
                let prevBtn = document.querySelector(".el-calendar__button-group .el-button-group>button:nth-child(2)")
                prevBtn.addEventListener('click',()=>{
                    const date = new Date(this.nowdate);
                    const Y = date.getFullYear();
                    const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                    const D = date.getDate() + ' ';
                    this.dataTime = Y + '-' + M + D;
                    this.dataTimeY = Y;
                    this.dataTimeM = date.getMonth() + 1;
                    console.log(this.dataTime)
                })
            })
 
            // 日历下个月按钮
            this.$nextTick(()=>{
                let prevBtn = document.querySelector(".el-calendar__button-group .el-button-group>button:nth-child(3)")
                prevBtn.addEventListener('click',()=>{
                    if(this.dataTime === null){
                        const date = new Date(this.nowdate);
                        const Y = date.getFullYear();
                        const M = date.getMonth() + 1;
                        this.dataTimeM = Number(M+Number('1'))
                        this.dataTimeY = Y 
                        this.dataTime = this.dataTimeY + '-' + (this.dataTimeM < 10 ? '0' + this.dataTimeM : this.dataTimeM);
                    }else if(this.dataTimeM !== null && this.dataTimeM === 12){
                        this.dataTimeM = 1 
                        this.dataTime = (this.dataTimeY + Number('1')) + '-' + String(this.dataTimeM < 10 ? '0' + this.dataTimeM : this.dataTimeM);
                        this.dataTimeY = this.dataTimeY + Number('1')
                    }else{
                        this.dataTimeM = Number(this.dataTimeM+Number('1')) 
                        this.dataTime = this.dataTimeY + '-' + (this.dataTimeM < 10 ? '0' + this.dataTimeM : this.dataTimeM);
                    }
                    console.log(this.dataTime)
                })
            })
  },
  methods: {
    // 获取点击日期
		calendarOnClick(data){
			console.log(data.day)
		},
    getList(){
      listCourse().then(response => {
        this.dayTime = response.data;
        console.info(this.dayTime)
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

