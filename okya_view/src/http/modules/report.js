import axios from "../axios";

export const save = (data) => {
  return axios({
    url: '/report/save',
    method: 'post',
    data
  })
}

export const update = (data) => {
  return axios({
    url: '/report/update',
    method: 'post',
    data
  })
}

export const delet = (data) => {
  return axios({
    url: '/report/delet',
    method: 'post',
    data
  })
}

export const initPage = (data) => {
  return axios({
    url: '/report/initPage',
    method: 'post',
    data
  })
}

export const getSearchProp = (data) => {
  return axios({
    url: '/report/getSearchProp',
    method: 'post',
    data
  })
}
