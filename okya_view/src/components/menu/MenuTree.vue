<template>
  <el-submenu v-if="menu.children && menu.children.length >= 1" :index="menu.compoId + ''">
    <template slot="title">
      <i :class="menu.icon"></i>
      <span slot="title" style="font-family: '新宋体'">{{menu.compoName}}</span>
    </template>
    <MenuTree v-for="item in menu.children" :key="item.compoId" :menu="item"></MenuTree>
  </el-submenu>
  <el-menu-item v-else :index="menu.compoId + ''" @click="handleRoute(menu)">
    <i :class="menu.icon"></i>
    <span slot="title" style="font-family: '新宋体'">{{menu.compoName}}</span>
  </el-menu-item>
</template>

<script>
    export default {
        name: 'MenuTree',
        props: {
            menu: {
                type: Object,
                required: true
            }
        },
        methods: {
            handleRoute(menu) {
                if(menu.url.split('$').length > 1){
                    // 通过菜单URL跳转至指定路由
                    this.$router.push({
                        path: menu.url,
                        query: JSON.parse(menu.url.split('$')[1]),
                        icon: menu.icon
                    })
                } else {
                    // 通过菜单URL跳转至指定路由
                    this.$router.push({
                        path: menu.url,
                        icon: menu.icon
                    })
                }
            }
        }
    }
</script>
