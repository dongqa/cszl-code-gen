import axios from 'axios'
import {MessageBox, Message} from 'element-ui'
import store from '@/store'
import {getToken} from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    config.headers['Db-Url'] = localStorage.getItem('dbUrl')
    config.headers['Db-Username'] = localStorage.getItem('dbUsername')
    config.headers['Db-Password'] = localStorage.getItem('dbPassword')
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  res => {

    if (res.headers['content-disposition']) {
      const filename = decodeURI(res.headers['content-disposition'].split(';')[1]).replace('filename=', '')
      const blob = new Blob([res.data], {type: res.headers['content-type']})
      const a = document.createElement('a')
      a.download = filename.replace(/"/g, '')
      a.href = URL.createObjectURL(blob)
      a.click()
      URL.revokeObjectURL(a.href) // 释放URL 对象
      // document.body.removeChild(a);//dom元素找不到，报错
      return
    }

    const originData = res.data
    let data
    if (originData instanceof ArrayBuffer) {
      data = JSON.parse(String.fromCharCode.apply(null, new Uint8Array(originData)))
    } else {
      data = originData
    }

    // if the custom code is not 20000, it is judged as an error.
    if (data.code !== 20000) {
      Message({
        message: data.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (data.code === 50008 || data.code === 50012 || data.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(data.message || 'Error'))
    } else {
      return data
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
