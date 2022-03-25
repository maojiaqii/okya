<template>
  <div class="menu-bar-container">
    <!-- logo -->
    <div class="logo" :class="$store.state.app.collapse?'menu-bar-collapse-width':'menu-bar-width'"
         :style="'background :'+ $store.state.app.viewColor + ';'">
      <el-avatar :size="50" :src="$store.state.app.appIcon" style="margin-top: 6px;position: absolute;z-index: 100"/>
      <div style="margin-left:50px;">
        <vue-seamless-scroll v-if="$store.state.app.collapse ? false : true" :data="newsList" :class-option="classOption" class="seamless-warp2">
          <ul class="item">
            <li>{{$store.state.app.appName}}</li>
          </ul>
        </vue-seamless-scroll>
      </div>
    </div>
    <!-- 导航菜单 -->
    <div class="menu-div">
      <el-scrollbar style="height: 100%">
        <el-menu collapse-transition unique-opened
                 :background-color="$store.state.app.viewColor"
                 text-color="#fff"
                 active-text-color="#ffd04b"
                 class="el-menu-vertical-demo" @open="handleopen"
                 @close="handleclose" @select="handleselect" :collapse="$store.state.app.collapse">
          <el-menu-item index="homePage" @click="$router.push('/homePage')">
            <i class="icon iconfont iconzhuye"></i>
            <span slot="title" style="font-family: '新宋体'">首页</span>
          </el-menu-item>
          <!-- 动态加载菜单 -->
          <MenuTree v-for="item in JSON.parse($store.state.app.menuTree)" :key="item.compoId" :menu="item"></MenuTree>
        </el-menu>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
    import MenuTree from "../menu/MenuTree"

    export default {
        components: {
            MenuTree
        },
        data() {
            return {
                logo: "",
                classOption: {
                    direction: 2,
                    limitMoveNum: 2
                },
                newsList: [{}, {}]
            };
        },
        methods: {
            handleopen() {
            },
            handleclose() {
            },
            handleselect() {
            }
        },
        mounted() {
        }
    };
</script>

<style scoped lang="scss">
  .menu-bar-container {
    .el-menu {
      text-align: left;
    }

    .logo {
      top: 0px;
      height: 65px;
      line-height: 65px;
      background: #4b5f6e;
      font-size: 22px;
      color: white;
      text-align: left;

      .el-avatar {
        background: #ffffff00;
      }

      img {
        width: 65px;
        height: 65px;
        border-radius: 0px;
        margin: 10px 10px 10px 10px;
        float: left;
      }

    }

    .menu-div{
      position: fixed;
      overflow: hidden;
      top: 65px;;
      width: 210px;
      bottom: 0;
      left: 0;
    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
      width: 210px;
      height: 100%;
    }

    .el-menu--collapse {
      width: 68px
    }

    .menu-bar-width {
      width: 210px;
      opacity: 1;
    }

  }

  .seamless-warp2 {
    ul.item {
      li {
        width: 210px;
        margin-top: -22px;
        list-style-type: none;
      }
    }
  }
</style>
