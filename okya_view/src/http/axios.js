// 二次封装 axios 模块，包含拦截器等信息
import Axios from 'axios';
import config from './config';
import qs from 'qs';
import Cookies from "js-cookie";
import router from '../router'
import { Message } from 'element-ui'
import { Loading } from 'element-ui';
// 使用vuex做全局loading时使用
import store from '../vuex'

export default function $axios(options) {
  var that = this;
  return new Promise((resolve, reject) => {
    let loadingInstance = Loading.service({
      text: '请稍等',
      target: document.querySelector('.loadingArea')
    });
    const instance = Axios.create({
      baseURL: config.baseURL,
      headers: {},
      withCredentials: config.withCredentials
    })

    // request 拦截器
    instance.interceptors.request.use(
      config => {
        let token = Cookies.get('token')
        // 1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
        // console.log(store.state.loading)
        // console.log('准备发送请求...')
        // 2. 带上token
        if (token) {
          config.headers.Authorization = token
        }
        // 3. 根据请求方法，序列化传来的参数，根据后端需求是否序列化
        if (config.method === 'post') {
          config.data.setYear = store.state.app.setYear
          config.data = qs.stringify(config.data)
        }
        return config
      },

      error => {
        // 请求错误时
       /* console.log('request:', error)
        // 2. 需要重定向到错误页面
        const errorInfo = error.response
        console.log(errorInfo)
        if (errorInfo) {
          error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
          const errorStatus = errorInfo.status; // 404 403 500 ...
          router.push({
            path: `/error/${errorStatus}`
          }).then(r => {
          })
        }*/
        return Promise.reject(error) // 在调用的那边可以拿到(catch)你想返回的错误信息
      }
    )

    // response 拦截器
    instance.interceptors.response.use(
      response => {
        let data;
        // IE9时response.data是undefined，因此需要使用response.request.responseText(Stringify后的字符串)
        if (response.data == undefined) {
          data = JSON.parse(response.request.responseText)
        } else {
          data = response.data
        }
        loadingInstance.close();
        // 若不是正确的返回code，且已经登录，就抛出错误
        // const err = new Error(data.desc)
        // err.data = data
        // err.response = response
        // throw err

        return data
      },
      err => {
        loadingInstance.close();
        if (err && err.response) {
          switch (err.response.status) {
            case 400:
              Message({
                type: 'error',
                message: '请求错误',
                showClose: true
              })
              break
            case 401:
              Message({
                type: 'error',
                message: '未授权，请登录',
                showClose: true
              })
              break
            case 403:
              Message({
                type: 'error',
                message: '拒绝访问',
                showClose: true
              })
              break
            case 404:
              Message({
                type: 'error',
                message: `请求地址出错: ${err.response.request.responseURL}`,
                showClose: true
              })
              break
            case 408:
              Message({
                type: 'error',
                message: '请求超时',
                showClose: true
              })
              break
            case 500:
              Message({
                type: 'error',
                message: '服务器内部错误',
                showClose: true
              })
              break
            case 501:
              Message({
                type: 'error',
                message: '服务不存在',
                showClose: true
              })
              break
            case 502:
              Message({
                type: 'error',
                message: '网关错误',
                showClose: true
              })
              break
            case 503:
              Message({
                type: 'error',
                message: '服务不可用',
                showClose: true
              })
              break
            case 504:
              Message({
                type: 'error',
                message: '网关超时',
                showClose: true
              })
              break
            case 505:
              Message({
                type: 'error',
                message: 'HTTP版本不受支持',
                showClose: true
              })
              break
            default:
              Message({
                type: 'error',
                message: err,
                showClose: true
              })
              break
          }
          router.push({
            path: '/error/Error'
          })
        }
        return Promise.reject(err) // 返回接口返回的错误信息
      }
    )

    // 请求处理
    instance(options).then(res => {
      loadingInstance.close();
      resolve(res)
      return false
    }).catch(error => {
      loadingInstance.close();
      console.log(error.message)
      // 1. 判断请求超时
      if (error.message === 'Network Error') {
        Message({
          type: 'error',
          message: error.message,
          showClose: true
        })
        router.push({
          path: '/error/Error'
        }).then(r => {
        })
      }
      reject(error)
    })
  })
}
