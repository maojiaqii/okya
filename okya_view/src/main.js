// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './vuex'
import api from './http/index'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// fade/zoom 等
import 'element-ui/lib/theme-chalk/base.css';
import './assets/icon/iconfont.css'

import autoTableHeight from './components/table/autoTableHeight'

// 引入自定义组件
import CusTable from './components/table/cus/CusTable'
import CusReport from './components/table/cus/CusReport'
import CusEditTable from './components/table/cusEdit/CusEditTable'
import CusButton from './components/button/cusButton/CusButton'
import CusCircleButton from './components/button/cusCircleButton/CusCircleButton'
import PopupTreeInput from './components/tree/PopupTreeInput'
import IconSelecter from './components/icon/IconSelecter'
import BpmnDesign from './components/bpmn/BpmnDesign'
import scroll from 'vue-seamless-scroll'
import Print from 'vue-print-nb'
import  './css/common.css';
import './assets/font/font.css';
/*左边工具栏以及编辑节点的样式*/
import 'bpmn-js/dist/assets/diagram-js.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';
import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css';

Vue.config.productionTip = false

Vue.prototype.autoTableHeight = autoTableHeight;

Vue.use(ElementUI)
Vue.use(api)
Vue.use(scroll)
Vue.use(Print);

Vue.component('CusTable', CusTable);
Vue.component('CusReport', CusReport);
Vue.component('CusEditTable', CusEditTable);
Vue.component('CusButton', CusButton);
Vue.component('CusCircleButton', CusCircleButton);
Vue.component('PopupTreeInput', PopupTreeInput);
Vue.component('IconSelecter', IconSelecter);
Vue.component('BpmnDesign', BpmnDesign);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
