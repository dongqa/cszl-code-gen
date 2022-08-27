import request from '@/utils/request'

export function listQueryRels() {
  return request({
    url: '/gen/listQueryRels',
    method: 'get'
  })
}

export function listQueryConditionRels() {
  return request({
    url: '/gen/listQueryConditionRels',
    method: 'get'
  })
}

export function listGenMethodName() {
  return request({
    url: '/gen/listGenMethodName',
    method: 'get'
  })
}

export function getTableList(params) {
  return request({
    url: '/gen/listTables',
    method: 'get',
    params
  })
}

export function listColumnByTableName(tableName) {
  return request({
    url: '/gen/listColumnByTableName',
    method: 'get',
    params: {tableName: tableName}
  })
}

export function genCode(data) {
  return request({
    url: '/gen/genCode',
    method: 'post',
    responseType: 'arraybuffer',
    data
  })
}
