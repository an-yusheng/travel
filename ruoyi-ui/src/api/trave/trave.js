import request from '@/utils/request'

// 查询景点列表
export function list(query) {
  return request({
    url: '/travel/attractions/list',
    method: 'get',
    params: query
  })
}

// 保存
export function saveAttractions(data) {
  return request({
    url: '/travel/attractions/saveAttractions',
    method: 'post',
    data: data
  })
}


export function removeAttractions(data) {
  return request({
    url: '/travel/attractions/removeAttractions',
    method: 'post',
    data: data
  })
}

export function editAttractions(data) {
  return request({
    url: '/travel/attractions/editAttractions',
    method: 'post',
    data: data
  })
}

export function updatePreference(data) {
  return request({
    url: '/travel/attractions/updatePreference',
    method: 'post',
    data: data
  })
}


export function userPreference(query) {
  return request({
    url: '/travel/attractions/userPreference',
    method: 'get',
    params: query
  })
}

export function cpmputList(data) {
  return request({
    url: '/travel/attractions/cpmputList',
    method: 'post',
    data: data
  })
}

export function saveAttractionsUser(data) {
  return request({
    url: '/travel/attractionsUser/save',
    method: 'post',
    data: data
  })
}

export function removeAttractionsUser(data) {
  return request({
    url: '/travel/attractionsUser/remove',
    method: 'post',
    data: data
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

// 上传文件
export function upload(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data
  })
}
