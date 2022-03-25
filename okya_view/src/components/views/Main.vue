<template>
  <div class="container" :class="$store.state.app.collapse?'position-collapse-left':'position-left'">
    <div ref="pr" style="width: calc(100%);overflow: hidden;">
      <el-breadcrumb v-if="$store.state.app.viewOpenStyle == 'breadcrumb'" separator="/" class="breadcrumb">
        <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
          {{ item.name }}
        </el-breadcrumb-item>
      </el-breadcrumb>
      <div style="white-space: nowrap;border-bottom:1px solid #000;"
           v-if="$store.state.app.viewOpenStyle == 'tabs'" @wheel.prevent="scrollBarWheel">
        <el-tag
          style="display: inline-block"
          :key="item.route"
          v-for="(item, index) in tabsList"
          :closable="item.closable"
          size="medium"
          :type="$store.state.app.activeIndex == item.route ? 'success' : index == 0 ? '' :'info'"
          :effect="$store.state.app.activeIndex == item.route ? 'dark' : 'plain'"
          :disable-transitions="false"
          @click='tabClick(item)'
          @close="tabRemove(item)"
          @contextmenu.prevent.native="openContextMenu($event, item, index)">
          <i :class="item.icon"></i> {{item.name}}
        </el-tag>
      </div>
    </div>
    <div class="loadingArea">
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive" :key="$route.path"></router-view>
      </keep-alive>
      <transition name="fade" mode="out-in">
        <router-view v-if="!$route.meta.keepAlive"></router-view>
      </transition>
    </div>
    <!--右键弹出的菜单内容-->
    <!--动态计算菜单出现的位置-->
    <el-dropdown ref="messageDrop" :style="styleD" v-if="$store.state.app.viewOpenStyle == 'tabs'">
      <el-dropdown-menu>
        <!--<el-dropdown-item icon="icon iconfont iconmimabukejian" @click.native="reloadTab">重新加载
        </el-dropdown-item>-->
        <el-dropdown-item @click.native="closeAllTabs">关闭所有
        </el-dropdown-item>
        <el-dropdown-item v-show="closeLeftFlag" @click.native="closeLeftTabs">
          关闭左边
        </el-dropdown-item>
        <el-dropdown-item v-show="closeRightFlag" @click.native="closeRightTabs">关闭右边
        </el-dropdown-item>
        <el-dropdown-item @click.native="closeOtherTabs">关闭其他</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                styleD: '', // 显示右键菜单位置
                closeLeftFlag: false, // 关闭左边
                closeRightFlag: false, // 关闭右边
                rightSelectTab: undefined
            };
        },
        methods: {
            //tab标签点击时，切换相应的路由
            tabClick(tab) {
                this.$router.push({
                    path: tab.route,
                    query: tab.query
                })
            },
            //移除tab标签
            tabRemove(routers) {
                this.$store.commit('delete_tabs', {route: routers.route, name: routers.name});
                this.$store.commit('set_deletePath', [routers.route]);
                if (this.$store.state.app.activeIndex === routers.route) {
                    // 设置当前激活的路由
                    this.$router.push({
                        path: this.$store.state.app.openTab[this.$store.state.app.openTab.length - 1].route,
                        query: this.$store.state.app.openTab[this.$store.state.app.openTab.length - 1].query
                    })
                } else {
                    let query = undefined
                    this.$store.state.app.openTab.forEach(v => {
                        if (v.route == this.$store.state.app.activeIndex) {
                            query = v.query
                        }
                    })
                    this.$router.push({
                        path: this.$store.state.app.activeIndex,
                        query: query
                    })
                }
            },
            // 右击事件
            openContextMenu(e, item, index) {
                e.preventDefault(); //防止默认菜单弹出
                this.$refs.messageDrop.hide();
                this.rightSelectTab = index
                if (item) {
                    if (this.$store.state.app.openTab.length > 2) {
                        // 当前右击【首页】标签或者后一个标签
                        if (index <= 1) {
                            this.closeLeftFlag = false;
                            this.closeRightFlag = true;
                        } else if (index == this.$store.state.app.openTab.length - 1) {
                            this.closeLeftFlag = true;
                            this.closeRightFlag = false;
                        } else {
                            this.closeLeftFlag = true;
                            this.closeRightFlag = true;
                        }
                    } else {
                        // 当前右击【首页】标签或者后一个标签
                        this.closeLeftFlag = false;
                        this.closeRightFlag = false;
                    }
                    this.$nextTick(() => {
                        this.styleD = "top:" + e.clientY + "px;" + "left:" + e.clientX + "px;position: fixed;"
                        this.$refs.messageDrop.show();
                    })
                }
            },
            // 重新加载
            reloadTab: function () {
                this.$confirm("确认重载当前页签吗?", "提示", {
                    type: "warning"
                })
                    .then(() => {
                        this.$store.commit('setRefreshTab', this.rightSelectTab);
                        this.$store.commit('set_deletePath', [this.$store.state.app.openTab[this.rightSelectTab].route]);
                        this.$router.push('/')
                    }).catch(() => {
                });
            },
            closeAllTabs: function () {
                this.$confirm("确认关闭所有页签吗?", "提示", {
                    type: "warning"
                })
                    .then(() => {
                        let deletePaths = []
                        for (let c = 1; c < this.$store.state.app.openTab.length; c++) {
                            deletePaths.push(this.$store.state.app.openTab[c].route)
                        }
                        this.$store.commit('delete_Otabs', {
                            index: 1,
                            count: this.$store.state.app.openTab.length - 1
                        });
                        this.$store.commit('set_deletePath', deletePaths);
                        this.$router.push({
                            path: this.$store.state.app.openTab[0].route,
                            query: this.$store.state.app.openTab[0].query
                        })
                    }).catch(() => {
                });
            },
            closeRightTabs: function () {
                this.$confirm("确认关闭右侧所有页签吗?", "提示", {
                    type: "warning"
                })
                    .then(() => {
                        let path = this.$store.state.app.openTab[this.rightSelectTab].route;
                        let query = this.$store.state.app.openTab[this.rightSelectTab].query;
                        let deletePaths = []
                        for (let c = this.rightSelectTab + 1; c < this.$store.state.app.openTab.length; c++) {
                            deletePaths.push(this.$store.state.app.openTab[c].route)
                        }
                        this.$store.commit('delete_Otabs', {
                            index: this.rightSelectTab + 1,
                            count: this.$store.state.app.openTab.length - this.rightSelectTab - 1
                        });
                        this.$store.commit('set_deletePath', deletePaths);
                        this.$router.push({
                            path: path,
                            query: query
                        })
                    }).catch(() => {
                });
            },
            closeLeftTabs: function () {
                this.$confirm("确认关闭左侧所有页签吗?", "提示", {
                    type: "warning"
                })
                    .then(() => {
                        let path = this.$store.state.app.openTab[this.rightSelectTab].route;
                        let query = this.$store.state.app.openTab[this.rightSelectTab].query;
                        let deletePaths = []
                        for (let c = 1; c < this.rightSelectTab; c++) {
                            deletePaths.push(this.$store.state.app.openTab[c].route)
                        }
                        this.$store.commit('delete_Otabs', {index: 1, count: this.rightSelectTab - 1});
                        this.$store.commit('set_deletePath', deletePaths);
                        this.$router.push({
                            path: path,
                            query: query
                        })
                    }).catch(() => {
                });
            },
            closeOtherTabs: function () {
                this.$confirm("确认关闭其他页签吗?", "提示", {
                    type: "warning"
                })
                    .then(() => {
                        let path = this.$store.state.app.openTab[this.rightSelectTab].route;
                        let query = this.$store.state.app.openTab[this.rightSelectTab].query;
                        let deletePaths = []
                        for (let c = 1; c < this.rightSelectTab; c++) {
                            deletePaths.push(this.$store.state.app.openTab[c].route)
                        }
                        for (let c = this.rightSelectTab + 1; c < this.$store.state.app.openTab.length; c++) {
                            deletePaths.push(this.$store.state.app.openTab[c].route)
                        }
                        this.$store.commit('delete_Otabs', {index: 1, count: this.rightSelectTab - 1});
                        this.$store.commit('delete_Otabs', {
                            index: 2,
                            count: this.$store.state.app.openTab.length - 2
                        });
                        this.$store.commit('set_deletePath', deletePaths);
                        this.$router.push({
                            path: path,
                            query: query
                        })
                    }).catch(() => {
                });
            },
            scrollBarWheel(e) {
                e = e || window.event;
                if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件
                    if (e.wheelDelta > 0) { //当滑轮向上滚动时
                        this.$refs.pr.scrollLeft += -30;
                    }
                    if (e.wheelDelta < 0) {
                        this.$refs.pr.scrollLeft += 30;
                    }
                } else if (e.detail) {  //Firefox滑轮事件
                    if (e.detail > 0) { //当滑轮向上滚动时
                        this.$refs.pr.scrollLeft += -30;
                    }
                    if (e.detail < 0) {
                        this.$refs.pr.scrollLeft += 30;
                    }
                }
            }
        },
        computed: {
            tabsList() {
                return this.$store.state.app.openTab
            },
            cachePath() {
                return this.$store.state.app.cachePath
            },
            reLoad() {
                return function (path) {
                    console.log(path)
                    for (let v of this.$store.state.app.openTab) {
                        if (v.route == path) {
                            return v.reLoad
                        }
                    }
                    return false
                }
            }
        }
    };
</script>

<style scoped lang="scss">
  .container {
    position: absolute;
    top: 65px;
    bottom: 0px;
    right: 0px;

    .el-tag {
      margin-left: 8px;
      margin-top: 10px;
      margin-bottom: 10px;
    }

    .breadcrumb {
      height: 20px;
      padding: 10px;
      font-size: medium;
      border-color: rgba(38, 86, 114, 0.2);
      border-bottom-width: 1px;
      border-bottom-style: solid;
    }
  }

  .position-left {
    left: 210px
  }

  .position-collapse-left {
    left: 68px;
  }

  // 都不显示重载按钮
  .el-tabs--card > .el-tabs__header .el-tabs__item .el-icon-refresh-right {
    opacity: 0;
    transition: all 0.5s ease-in-out;
    -webkit-transition: all 0.5s ease-in-out;
    -moz-transition: all 0.5s ease-in-out;
    -o-transition: all 0.5s ease-in-out;
  }

  // 选中的显示重载按钮
  .el-tabs--card > .el-tabs__header .el-tabs__item.is-active .el-icon-refresh-right {
    opacity: 1 !important;
  }

  // 如果鼠标放在未选中的tab上，则：显示关闭按钮
  .el-tabs--card > .el-tabs__header .el-tabs__item:hover .el-icon-refresh-right {
    opacity: 1;
  }

  // 如果鼠标放在未选中的tab上，则：显示关闭按钮
  .el-tabs--card > .el-tabs__header .el-tabs__item .el-icon-refresh-right:hover {
    transform: rotate(360deg);
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
  }

</style>
