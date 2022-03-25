import axios from "../axios";

export const findPage = data => {
  return axios({
    url: '/common/findPage',
    method: 'post',
    data
  })
}

export const save = data => {
  return axios({
    url: '/common/save',
    method: 'post',
    data
  })
}

export const delet = data => {
  return axios({
    url: '/common/deletCommon',
    method: 'post',
    data
  })
}

export const getSelectorData = data => {
  return axios({
    url: '/common/getSelectorData',
    method: 'post',
    data
  })
}

export const getTableCols = data => {
  return axios({
    url: '/common/getTableCols',
    method: 'post',
    data
  })
}
