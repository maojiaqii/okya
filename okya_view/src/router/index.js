import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/views/Home'
import HomePage from '../components/views/HomePage'
import Login from "../components/views/login/Login";
import Error from "../components/views/error/Error";
import store from '../vuex'
import http from '../http/interface'

Vue.use(Router)

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
// 解决切换页签时，最新页签不切换和无法重新加载bug
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    if (store.state.app.isRefreshTab !== undefined) {
      store.state.app.openTab[store.state.app.isRefreshTab].reLoad = true;
      store.state.app.isRefreshTab = undefined;
    }
  })
}

const router = new Router({
  routes: [
    {
      path: '/home',
      name: '主页',
      component: Home,
      meta: {requireAuth: true},
      children: []
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {requireAuth: false}
    },
    {
      path: '/',
      name: 'Login',
      component: Login,
      meta: {requireAuth: false}
    },
    {
      path: '/error/Error',
      name: 'Error',
      component: Error,
      meta: {requireAuth: false}
    }
  ]
})

export default router

Vue.mixin({
  beforeRouteEnter: function (to, from, next) {
    next(vm => {
      if (vm.$vnode) {
        if (vm.$vnode.parent && vm.$vnode.parent.componentInstance && vm.$vnode.parent.componentInstance.cache) {
          if (vm.$vnode.componentOptions) {
            var key = vm.$vnode.key == null
              ? vm.$vnode.componentOptions.Ctor.cid + (vm.$vnode.componentOptions.tag ? `::${vm.$vnode.componentOptions.tag}` : '')
              : vm.$vnode.key;
            var keys = vm.$vnode.parent.componentInstance.keys;
            store.commit('add_cachePath', {path: to.path, name: to.name, key: keys[keys.length - 1]});
          }
        }
      }
    })
  },
  beforeRouteLeave: function (to, from, next) {
    if (store.state.app.deletePath.length > 0) {
      if (this.$vnode) {
        if (this.$vnode.parent && this.$vnode.parent.componentInstance && this.$vnode.parent.componentInstance.cache) {
          if (this.$vnode.componentOptions) {
            var cache = this.$vnode.parent.componentInstance.cache;
            var keys = this.$vnode.parent.componentInstance.keys;
            let old_key = [];
            let index = 0;
            for (let option1 of store.state.app.deletePath) {
              for (let option of store.state.app.cachePath) {
                if (option && option.path === option1) {
                  old_key.push(option.key);
                  delete store.state.app.deletePath[index]
                  break;
                }
                index++;
              }
            }
            if (old_key.length > 0) {
              for (let oldKey of old_key) {
                if (cache[oldKey]) {
                  cache[oldKey].componentInstance.$destroy();
                  if (keys.length) {
                    var indes = keys.indexOf(oldKey);
                    if (indes > -1) {
                      keys.splice(indes, 1);
                    }
                  }
                  delete cache[oldKey];
                }
              }
            }
          }
        }
      }
    }
    next()
  }
})

router.beforeEach((to, from, next) => {
  // 登录界面登录成功之后，会把用户信息保存在会话
  // 存在时间为会话生命周期，页面关闭即失效。
  let user = sessionStorage.getItem('user');
  store.commit("set_deletePath", [])
  // 如果需要验证
  if (to.meta.requireAuth == false) {
    next()
  } else {
    if (!user) {
      // 如果用户会话信息不存在，代表未登录，则跳转到登录界面
      next({path: '/'})
    } else {
      if (router.options.routes[0].children && router.options.routes[0].children.length >= 1) {
        console.log("已加载路由")
      } else {
        // 加载动态菜单和路由
        console.log("未加载路由")
        addDynamicMenuAndRoutes()
      }
      if (store.state.app.viewOpenStyle == 'tabs') {
        if (to.meta.isTab) {
          // 判断是否刷新页面
          if (store.state.app.isRefresh) {
            store.commit('clear_tabs');
            store.commit('setRefresh', false);
            store.commit('clear_cachePath');
            next({path: '/home'})
          } else {
            // 是否重新载入页签
            if (!store.state.app.isRefreshTab) {
              //判断路由是否已经打开
              //已经打开的 ，将其置为active
              //未打开的，将其放入队列里
              let flag = false;
              for (let item of store.state.app.openTab) {
                if (item.route === to.path) {
                  store.commit('set_active_index', to.path)
                  flag = true;
                  break;
                }
              }
              if (!flag) {
                store.commit('add_tabs', {
                  route: to.path,
                  name: to.name,
                  closable: to.meta.tabClosable,
                  compo: to.matched[to.matched.length - 1].components.default,
                  query: to.query,
                  icon: to.meta.icon
                });
                store.commit('set_active_index', to.path);
                to.meta.keepAlive = true
              }
              next()
            } else {
              store.state.app.openTab[store.state.app.isRefreshTab].reLoad = true;
              store.state.app.isRefreshTab = undefined;
            }
          }
        } else {
          to.meta.keepAlive = false
          next()
        }
      } else {
        to.meta.keepAlive = false
        next()
      }
    }
  }
})

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function addDynamicRoutes(menuList = [], routes = []) {
  let temp = []
  for (let i = 0; i < menuList.length; i++) {
    if (menuList[i].children && menuList[i].children.length >= 1) {
      temp = temp.concat(menuList[i].children)
    } else if (menuList[i].url) {
      //&& /\S/.test(menuList[i].url)
      // 创建路由配置
      let route = {
        path: menuList[i].url,
        component: null,
        name: menuList[i].compoName,
        meta: {
          requireAuth: true,
          menuId: menuList[i].menuId,
          title: menuList[i].compoName,
          isDynamic: menuList[i].url.split('$')[1] !== undefined,
          isTab: true,
          tabClosable: true,
          iframeUrl: '',
          icon: menuList[i].icon
        }
      }
      // url以http[s]://开头, 通过iframe展示
      if (/^http[s]?:\/\/.*/.test(menuList[i].url)) {
        route['path'] = menuList[i].url
        route['name'] = menuList[i].compoName
        route['meta']['iframeUrl'] = menuList[i].url
      } else {
        try {
          // 根据菜单URL动态加载vue组件，这里要求vue组件须按照url路径存储
          // 如url="sys/user"，则组件路径应是"@/views/sys/user.vue",否则组件加载不到
          let array = menuList[i].url.split('$')[0].split('/')
          let url = array[1] + '/' + array[2].substring(0, 1).toUpperCase() + array[2].substring(1)
          route['component'] = resolve => require([`@/components/views/${url}` + '.vue'], resolve)
        } catch (e) {
        }
      }
      routes.push(route)
    }
  }
  if (temp.length >= 1) {
    addDynamicRoutes(temp, routes)
  } else {
  }
  return routes
}

/**
 * 加载动态菜单和路由
 */
function addDynamicMenuAndRoutes() {
  // 添加动态路由
  http.menu.getMenu({"roleCode": JSON.parse(sessionStorage.getItem('user')).roleCode})
    .then((res) => {
      store.commit('setMenuTree', res.message);
      store.commit('setButtons', res.extend);
      let dynamicRoutes = addDynamicRoutes(JSON.parse(store.state.app.menuTree))
      dynamicRoutes.push({
        path: '/homePage',
        name: '首页',
        component: HomePage,
        meta: {requireAuth: true, tabClosable: false, isTab: true, keepAlive: true, icon: 'icon iconfont iconzhuye'}
      })
      for (let i = 0; i < dynamicRoutes.length; i++) {
        router.options.routes[0].children.push(dynamicRoutes[i])
      }
      router.addRoutes(router.options.routes);
    })
}


