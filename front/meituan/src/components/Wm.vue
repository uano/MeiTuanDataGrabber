<template>
    <!-- 标题 -->
    <div class="title">
    美团外卖数据
    </div>

    <!-- 搜索功能及下载按钮 -->
    <el-row class="demo-autocomplete">
    <el-col :span="12">
      时间段:
      <el-autocomplete
        v-model="state1"
        :fetch-suggestions="querySearch1"
        clearable
        class="inline-input w-50"
        placeholder="选择时间.."
        @select="handleSelect1"
      />
      门店:
      <el-autocomplete
        v-model="state2"
        :fetch-suggestions="querySearch2"
        clearable
        class="inline-input w-50"
        placeholder="选择门店..."
        @select="handleSelect2"
        @clear="handleClear"
      />
      <!-- 搜索按钮 -->
      <el-button type="primary" round @click="select">点击搜索</el-button>
      <!-- 下载按钮 -->
    <el-button type="success" round @click="download">点击下载</el-button>
    </el-col>
    
  </el-row>

    <!-- 表格内容 -->

    <el-table :data="tableData.filter(data => !search || data.wmShopName.toLowerCase().includes(search.toLowerCase()))" style="width: 100%">

        <el-table-column label="序号" type="index" />
        <el-table-column label="时间段" prop="wmTime" width="160px"/>
        <el-table-column label="id" prop="id" v-if="false" />
        <el-table-column label="门店" prop="wmShopName" width="250px"/>
        <el-table-column label="营业额" prop="wmTurnover" width="150px"/>
        <el-table-column label="客单量" prop="wmOrder"/>
        <el-table-column label="实付客单" prop="wmCusPay"/>
        <el-table-column label="实收客单" prop="wmCusIncome"/>
        <el-table-column label="曝光量" prop="wmExposure"/>
        <el-table-column label="商圈前10曝光量" prop="wmExposureTopten"/>
        <el-table-column label="入店转换率" prop="wmStoreRate" :formatter="formatPercentage"/>
        <el-table-column label="商圈前10入店转化率" prop="wmStoreRateTopten" :formatter="formatPercentage"/>
        <el-table-column label="下单转化率" prop="wmOrderRate" :formatter="formatPercentage"/>
        <el-table-column label="商圈前10下单转化率" prop="wmOrderRateTopten" :formatter="formatPercentage"/>
        <el-table-column label="复购率" prop="wmRepurchaseRate" :formatter="formatPercentage"/>
        <el-table-column label="商圈前10复购率" prop="wmRepurchaseRateTopten" :formatter="formatPercentage"/>
        <el-table-column label="评分" prop="wmScore"/>
    </el-table>
     <!-- //分页组件 -->
     <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 40]"
      :small="small"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalNum"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange" 
    /> 

 </template>


 <script>
 export default {
  
   data() {
     return {
       pageSize:10,
       pageNum:1,
       totalPage:500,
       totalNum:0,
       tableData: [],
       search: '',
       state1:"",
       state2:"",  //仅仅作为展示
       shopId:"",
       timeQuerys: [],
       shops:[]
     }
   },
   
   methods: {
    formatPercentage(row, column, cellValue) {
      if (cellValue === null) {
        return '';
      }
      const percentage = (cellValue * 100).toFixed(2);
      return `${percentage}%`;
    },
    querySearch1(queryString, cb) {
      const results = queryString
        ? this.timeQuerys.filter(this.createFilter1(queryString))
        : this.timeQuerys;
      cb(results);
    },
    querySearch2(queryString, cb) {
      const results = queryString
        ? this.shops.filter(this.createFilter2(queryString))
        : this.shops;
      cb(results);
    },
    createFilter1(queryString) {
      return (restaurant) => {
        return (
            restaurant.value.toLowerCase().includes(queryString.toLowerCase())
        );
      };
    },
    createFilter2(queryString) {
      return (restaurant) => {
        return (
            restaurant.value.toLowerCase().includes(queryString.toLowerCase())
        );
      };
    },
    loadTime() {
        let data =[];
        //获取时间查询条件集合
        this.$axios({
                headers:{
                    "Content-Type":"application/json"
                },
                method:"get",
                url:"/api/meituan/queryTimeList",
            }).then(res=>{
                // console.log(res.data)
                data=res.data
                 this.timeQuerys = data.map(item => ({ value: item.wmTime }));
            })
            
    },
    loadShops() {
        let data =[];
        //获取时间查询条件集合
        this.$axios({
                headers:{
                    "Content-Type":"application/json"
                },
                method:"get",
                url:"/api/meituan/wmShopList",
            }).then(res=>{
                // console.log(res.data)
                data=res.data
                this.shops = data.map(item => ({ value: item.wmShopName, shopId: item.wmShopId}));
            })
    },

    handleSelect1(item) {
      this.state1=item.value
      console.log(item);
    },
    handleSelect2(item) {
      this.state2=item.value
      this.shopId=item.shopId
    },
    handleClear() {
    this.shopId = ""; // 将绑定的字段设置为 null
  },
    select(){
      //  alert("搜索成功!!\n时间段为:"+this.state1+"\n门店为："+this.shopId)
       this.$axios({
                headers:{
                    "Content-Type":"application/json"
                },
                method:"post",
                url:"/api/meituan/select",
                data:JSON.stringify({
                    pageVO:{
                    pageNum:this.pageNum,
                    pageSize:this.pageSize
                        },
                    meituanWmTurnover:{
                      wmTime:this.state1,
                      wmShopId:this.shopId
                        }
                })
            }).then(res=>{
                //渲染表格
                this.tableData=res.data.data.records
                //设置总页数
                this.totalPage=res.data.data.pages
                //设置总数量
                this.totalNum=res.data.data.total
                console.log(res.data)
            })
    },

    download(){
      ElMessageBox.confirm(
        '是否下载时间段:' + this.state1+'门店:' + this.state2 + '的相关数据?',
    {
      confirmButtonText: '是',
      cancelButtonText: '否',
      type: 'warning',
    }
  )
    .then(() => {
        this.$axios({
  method: "get",
  url: "/api/meituan/exportExcel",
  responseType: "blob", // 设置响应数据类型为二进制
  params: {
    wmTime: this.state1,
    wmShopId: this.shopId
  }
}).then(response => {
  // 获取文件名，并进行URL解码
  const filename = decodeURIComponent(response.headers['content-disposition'].split('filename=')[1]);
  
  // 将返回的数据保存到Blob对象中
  const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });

  // 创建可下载的URL
  const url = URL.createObjectURL(blob);

  // 创建a标签并设置下载属性
  const link = document.createElement('a');
  link.href = url;

  // 判断是否为移动设备
  if (/Mobi/.test(navigator.userAgent)) {
    // 使用 window.open() 在移动设备上触发下载
    window.open(url);
  } else {
    // 设置下载属性和文件名
    link.download = filename;

    // 添加到DOM树并触发下载
    document.body.appendChild(link);
    link.click();
  }

  // 释放URL对象
  URL.revokeObjectURL(url);

  ElMessage({
                    type: 'success',
                    message: '下载成功!',
                 })
}).catch(error => {
  console.error('下载失败:', error);
  ElMessage({
                    type: 'error',
                    message: '下载失败!',
                 })
});













      
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消',
      })
    })

        






      
    },
    
    
    //页码发生改变时
   handleCurrentChange(paper){
        this.$axios({
                headers:{
                    "Content-Type":"application/json"
                },
                method:"post",
                url:"/api/meituan/select",
                data:JSON.stringify({
                    pageVO:{
                    pageNum:paper,
                    pageSize:this.pageSize
                        },
                    meituanWmTurnover:{
                      wmTime:this.state1,
                      wmShopId:this.shopId
                        }
                })
            }).then(res=>{
                //渲染表格
                this.tableData=res.data.data.records
                //设置总页数
                this.totalPage=res.data.data.pages
                //设置总数量
                this.totalNum=res.data.data.total
                console.log(res.data)
            })
     },
     //当页面大小发生变化时
     handleSizeChange(paper){
        this.$axios({
                headers:{
                    "Content-Type":"application/json"
                },
                method:"post",
                url:"/api/meituan/select",
                data:JSON.stringify({
                    pageVO:{
                    pageNum:"1",
                    pageSize:paper
                        },
                    meituanWmTurnover:{
                      wmTime:this.state1,
                      wmShopId:this.shopId
                        }
                })
            }).then(res=>{
                //渲染表格
                this.tableData=res.data.data.records
                //设置总页数
                this.totalPage=res.data.data.pages
                //设置总数量
                this.totalNum=res.data.data.total
                console.log(res.data)
            })
     },
   },


   created:function(){
         //初始化函数
         this.$axios({
                headers:{
                    "Content-Type":"application/json"
                },
                method:"post",
                url:"/api/meituan/select",
                data:JSON.stringify({
                    pageVO:{
                    pageNum:"1",
                    pageSize:this.pageSize
                        },
                    meituanWmTurnover:{
                      wmTime:"",
                      wmShopId:""
                        }
                })
            }).then(res=>{
                //渲染表格
                this.tableData=res.data.data.records
                //设置总页数
                this.totalPage=res.data.data.pages
                //设置总数量
                this.totalNum=res.data.data.total
                console.log(res.data)
            })
   },
   mounted() {
    //获取时间段集合
    this.loadTime();
    //获取门店集合
    this.loadShops();
  },
 }
</script>
<style scoped>
.my-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
.title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  padding: 20px;
  background-color: #f4f4f4;
  border-radius: 10px;
}
</style>
