<template>   
    <Wm />
</template>

<script>
import Wm from "./components/WM.vue"
export default{
  components:{
    Wm,
},

  // mounted() {
  //   // 使用媒体查询检测设备类型
  //   if (window.matchMedia('(max-width: 768px)').matches) {
  //     // 如果屏幕宽度小于等于768px，认为是移动设备，跳转到移动页面
  //     this.$router.push('/phone'); // 替换成你的移动页面路由
  //   } else {
  //     // 否则，认为是桌面设备，跳转到桌面页面
  //     this.$router.push('/menu'); // 替换成你的桌面页面路由
  //   }
  // },

}
</script>
