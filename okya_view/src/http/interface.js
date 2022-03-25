// 请求接口汇总模块，聚合模块 API
import * as login from './modules/login'
import * as menu from './modules/menu'
import * as user from './modules/user'
import * as role from './modules/role'
import * as company from './modules/company'
import * as form from './modules/form'
import * as finance from './modules/finance'
import * as common from './modules/common'
import * as eChart from './modules/eChart'
import * as sysSetup from './modules/sysSetup'
import * as report from './modules/report'
import * as notice from './modules/notice'

// 默认全部导出
export default {
  common,
  login,
  menu,
  user,
  role,
  company,
  form,
  finance,
  eChart,
  sysSetup,
  report,
  notice
}
