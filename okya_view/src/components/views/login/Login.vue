<template>
  <div id="poster">
    <p style="font-size: 280%; margin: calc(5vh) calc(5vh);">
      {{$store.state.app.appName}}
    </p>
    <el-form class="login-container" ref="loginForm" :model="user" :rules="rules" label-position="left"
             label-width="0px">
      <div style="text-align: center;">
        <el-avatar :size="100" :src="$store.state.app.appIcon"></el-avatar>
      </div>
      <el-form-item prop="username">
        <el-input type="text" v-model="user.username" auto-complete="off" placeholder="账号" @keyup.enter.native="login()"
                  prefix-icon="icon iconfont iconrenbudaiwaiquan"/>
      </el-form-item>

      <el-form-item prop="password">
        <el-input v-model="user.password" auto-complete="off" placeholder="密码" show-password
                  @keyup.enter.native="login()" prefix-icon="icon iconfont iconmima"/>
      </el-form-item>

      <div style="display: block;white-space: nowrap;margin-top: 30px">
        <el-button type="primary" style="display: inline-block; width: 48%;background: #505458;border: none"
                   v-on:click="reset">重置
        </el-button>
        <el-button type="primary" style="display: inline-block; width: 48%;background: orangered;border: none"
                   v-on:click="login">登录
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
    import md5 from 'js-md5';
    import Cookies from "js-cookie";

    export default {
        name: "Login",
        methods: {
            initSysSetup() {
                this.$api.sysSetup.getSetup({}).then(res => {
                    if (res.success) {
                        let js = JSON.parse(res.message);
                        this.$store.commit('setAppName', js['SYS_TITEL'])
                        this.$store.commit('setAppIcon', location.protocol + '//' + location.host + js['SYS_ICON'])
                    } else {
                        this.$message({
                            type: 'error',
                            message: res.message
                        })
                    }
                });
            },
            login() {
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        this.$store.commit('clearStates'); // 清空全局属性
                        // 暂时写死，后期登录页面增加年度选择
                        this.$store.commit('setSetYear', '2021');
                        this.$api.login.login({
                            "userCode": this.user.username,
                            "password": md5(this.user.password)
                        }).then(res => {
                            if (res.success) {
                                Cookies.set('token', res.extend) // 放置token到Cookie
                                sessionStorage.setItem('user', res.message) // 保存用户到本地会话
                                this.$notify({
                                    type: 'success',
                                    message: '欢迎你,' + JSON.parse(sessionStorage.getItem('user')).userName + '!',
                                    duration: 3000
                                })
                                this.$router.push('/home')
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: res.message,
                                    showClose: true
                                })
                            }
                        });
                    } else {
                        return false
                    }
                })
            },
            reset() {
                this.$refs.loginForm.resetFields();
            }
        },
        data() {
            return {
                user: {},
                rules: {
                    username: [
                        {required: true, message: '用户名不能为空', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            this.initSysSetup()
        }
    }
</script>

<style lang="scss" scoped>
  #poster {
    background-image: url("../../../assets/bg.svg");
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  body {
    margin: 0px;
    padding: 0;
  }

  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin-left: auto;
    margin-right: auto;
    margin-top: 5%;
    width: 20%;
    padding: 35px 35px 35px 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;

    .el-avatar {
      background: #ffffff00;
    }
  }

</style>
