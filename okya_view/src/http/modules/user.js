import axios from "../axios";

export const findPage = data => {
  return axios({
    url: '/user/findPage',
    method: 'post',
    data
  })
}

export const save = data => {
  return axios({
    url: '/user/save',
    method: 'post',
    data
  })
}

export const update = data => {
  return axios({
    url: '/user/update',
    method: 'post',
    data
  })
}

export const delet = data => {
  return axios({
    url: '/user/delete',
    method: 'post',
    data
  })
}
