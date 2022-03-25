export default {
  state: {
    appName: "",  // 应用名称
    appIcon: "",  // 应用名称
    setYear: "2021", // 系统年度
    collapse: false,  // 导航栏收缩状态
    menuTree: [], // 主页菜单树
    buttons: [], // 主页菜单树
    openTab: [], //所有打开的路由
    activeIndex: "", //激活状态
    isRefresh: false, // 是否刷新主页面
    isRefreshTab: undefined, // 是否刷新页签
    viewOpenStyle: 'tabs', // 页签风格
    viewColor: "rgba(84, 92, 100, 1)", // 主题颜色
    cachePath: [],
    deletePath: [],
    websocket: null,
    unCheckCount: 0,
    nodeVisible: false,
    nodeInfo: {}
  },
  getters: {},
  mutations: {
    onCollapse(state) {  // 改变收缩状态
      state.collapse = !state.collapse;
    },
    setAppName(state, data) {
      state.appName = data;
    },
    setAppIcon(state, data) {
      state.appIcon = data;
    },
    setViewColor(state, data) {
      state.viewColor = data;
    },
    setViewOpenStyle(state, data) {
      state.viewOpenStyle = data;
    },
    setMenuTree(state, data) {  // 修改menuTree
      state.menuTree = data;
    },
    setButtons(state, data) {
      state.buttons = data;
    },
    setSetYear(state, data) {
      state.setYear = data;
    },
    setRefresh(state, data) {
      state.isRefresh = data;
    },
    setRefreshTab(state, data) {
      state.isRefreshTab = data;
    },
    set_deletePath(state, data) {
      state.deletePath = data;
    },
    set_websocket(state, data) {
      state.websocket = data;
    },
    set_unCheckCount(state, data) {
      state.unCheckCount = data;
    },
    set_nodeVisible(state, data) {
      state.nodeVisible = data;
    },
    set_nodeInfo(state, data) {
      state.nodeInfo = data;
    },
    clearStates(state) {
      state.menuTree = [];
      state.isRefresh = false;
      state.buttons = [];
      state.setYear = '';
      state.openTab = [];
      state.activeIndex = '';
      state.isRefreshTab = undefined;
      state.deletePath = [];
      state.cachePath = [];
      state.websocket = null;
      state.unCheckCount = 0;
      state.nodeVisible = false;
      state.nodeInfo = {};
    },
    // 清空tabs
    clear_tabs(state) {
      state.openTab = [];
      state.activeIndex = '';
    },
    // 添加tabs
    add_tabs(state, data) {
      state.openTab.push(data);
    },
    // 删除tabs
    delete_tabs(state, obj) {
      let index = 0;
      for (let option of state.openTab) {
        if (option.route === obj.route && option.name === obj.name) {
          break;
        }
        index++;
      }
      state.openTab.splice(index, 1);
    },
    // 删除除首页以外tabs
    delete_Otabs(state, obj) {
      state.openTab.splice(obj.index, obj.count);
    },
    // 设置当前激活的tab
    set_active_index(state, index) {
      state.activeIndex = index;
    },
    add_cachePath(state, key) {
      state.cachePath.push(key)
    },
    clear_cachePath(state) {
      state.cachePath = [];
    },
    add_unCheckCount(state){
      state.unCheckCount++
    },
    reduce_unCheckCount(state, count){
      state.unCheckCount -= count
    }
  },
  actions: {}
}
