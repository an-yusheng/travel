<template>
  <div class="app-container"  style="height: 700px;">
    <el-container style="height: 100%; border: 1px solid #eee">
      <el-card style="width: 200px;padding: auto;">
        <div style="text-align:center;font-weight: bold;">心愿单</div>
        <div v-if="wish.length <= 0" style="margin-top: 100%;font-size: 10px;">
          您的心愿单为空，快去添加景点吧
          <div style="margin-left: 20%;">
            <el-button type="primary" size="mini" @click="toIndex" style="margin-top: 20px;">添加</el-button>
          </div>
        </div>
        <div v-if="wish.length > 0">
          <div v-for="(item,index) in wish" :key="index">
                <h3>{{  item.city }}</h3>
            <el-card style="height: 50px;margin-top: 10px;background-color: #fef0f0;border-color: #fde2e2;color: #f56c6c;font-size: 10px;" 
            v-for="(item2,index2) in item.data" :key="index2">
                  {{ item2.name }}
                  <i class="el-icon-delete" style="float: right;cursor: pointer; margin-left: 5px;" @click="removeWith(item2)"></i>
                  <i class="el-icon-edit" style="float: right;cursor: pointer;" @click="addWish(item2)"></i>
            </el-card>
          </div>
        </div>
      </el-card>
    <el-card class="box-card" style="margin-left: 20px;width: 100%;">
      <el-main>
        <el-calendar>
          <template #dateCell="{data}">
            <div style="margin:0px" @click="calendarOnClick(data)">
              {{ data.day.split('-').slice(2).join() }} 
              <div v-for="(i, index) in dayData" :key="index">
                <div v-if="data.day==i.wishDate" style="color: rgb(88 132 211);" @click="wishDt">
                  <el-card style="height: 48px;background-color: #fef0f0;border-color: #fde2e2;color: #f56c6c;font-size: 12px;margin-top: 5px;" >
                    {{ i.name  }}
                  </el-card>
                </div>
              </div>
            </div>
          </template>
        </el-calendar>
      </el-main>
    </el-card>
    </el-container>

    
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-col :span="24">
          <el-form-item
            label="计划日期："
            prop="wishDate"
          >
            <el-date-picker
              v-model="form.wishDate"
              type="date"
              placeholder="选择日期">
            </el-date-picker>
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
import { wishList, updatePreference,removeAttractionsUser } from "@/api/trave/trave";

export default {
  name: "Online",
  data() {
    return {
			nowdate: new Date(),
      dayData: [],
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
      wish:[],
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
      wishList({}).then(response => {
        this.wish = response.data
      });
    },
    wishDt(){
      alert(1)
    },
    addWish(item){
      this.form = item
      console.log(this.form)
      this.open = true
      this.title = "选择计划时间"
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.form = {};
    },
    toIndex(){
      this.$router.push({ path: '/index'});
    },
    removeWith(item){
      let params = {"attractionsId":item.id}
      removeAttractionsUser(params).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("取消心愿成功");
              this.getList()
            }else{
              this.$modal.msgError(response.msg);
            }
          });
    },
    /** 提交按钮 */
    submitForm: function() {
      let params = Object.assign({}, this.form);
      console.log(params)
      if(params.wishDate === undefined){
        this.$modal.msgError("请选择计划日期");
        return;
      }
      var dateobj_toString = params.wishDate.toISOString().slice(0, 10)
      params.wishDate = dateobj_toString
      this.dayData.push(params)
      this.cancel()
    },
  }
};
</script>

