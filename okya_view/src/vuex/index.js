import Vue from 'vue'
import Vuex from 'vuex'
import AppStore from "./AppStore";
import persistedState from 'vuex-persistedstate'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app: AppStore
    // 其他
  },
  plugins: [persistedState ()] // 解决刷新页面store数据清空
})

export default store
