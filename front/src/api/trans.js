import request from '@/utils/request'

export function json2JavaBeanHtml(data) {
  return request({
    url: '/trans/json2JavaBeanHtml',
    method: 'post',
    data
  })
}
