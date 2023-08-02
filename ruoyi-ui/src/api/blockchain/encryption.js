import request from '@/utils/request'

// 查询加密文件
export function list(query) {
  return request({
    url: '/encryption/file/list',
    method: 'get',
    params: query
  })
}

// 查询案件列表
export function caseList(query) {
  return request({
    url: '/encryption/file/caseList',
    method: 'get',
    params: query
  })
}


// 查询区块记录
export function blockList(query) {
  return request({
    url: '/encryption/file/blockList',
    method: 'get',
    params: query
  })
}

// 上传文件
export function upload(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data
  })
}

// 保存
export function saveEncryFile(data) {
  return request({
    url: '/encryption/file/save',
    method: 'post',
    data: data
  })
}

// 下载文件
export function downloadEncryFile(data) {
  return request({
    url: '/encryption/file/downloadEncryFile',
    method: 'post',
    data: data
  })
}

export function checkDownloadEncryFile(data) {
  return request({
    url: '/encryption/file/checkDownloadEncryFile',
    method: 'post',
    data: data
  })
}


export function transferFile(data) {
  return request({
    url: '/encryption/file/transferFile',
    method: 'post',
    data: data
  })
}

// 上链
export function upperChain(data) {
  return request({
    url: '/encryption/file/upperChain',
    method: 'post',
    data: data
  })
}

// 修改案件
export function updateCase(data) {
  return request({
    url: '/encryption/file/updateCase',
    method: 'post',
    data: data
  })
}


