<template>
  <div>
    <div style="margin:100px auto;text-align:center;">
      <img src="../../../assets/error.svg"/>
      <h2>噢！访问失败啦...</h2>
      <el-button type="primary" @click="goBack" round>返回上一页</el-button>
      <el-button type="danger" @click="reLogin" round>重新登录</el-button>
    </div>
  </div>
</template>

<script>
    export default {
        name: "error",
        methods: {
            goBack: function () {
                this.$router.back(-1);
            },
            reLogin: function () {
                this.$store.commit('clearStates'); // 清空全局属性
                sessionStorage.clear(); // 清空session
                this.$router.options.routes[0].children = []; // 清空动态路由
                this.$router.replace('/');
            }
        }
    }
</script>
