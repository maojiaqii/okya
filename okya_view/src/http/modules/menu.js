import axios from "../axios";

export const getMenu = data => {
  return axios({
    url: '/menu/getMenuButton',
    method: 'post',
    data
  })
}

export const getCompos = data => {
  return axios({
    url: '/menu/getCompos',
    method: 'post',
    data
  })
}

export const getParentCompos = data => {
  return axios({
    url: '/menu/getParentCompos',
    method: 'post',
    data
  })
}

export const save = data => {
  return axios({
    url: '/menu/save',
    method: 'post',
    data
  })
}

export const update = data => {
  return axios({
    url: '/menu/update',
    method: 'post',
    data
  })
}

export const delet = data => {
  return axios({
    url: '/menu/delete',
    method: 'post',
    data
  })
}

export const getAllCompos = data => {
  return axios({
    url: '/menu/getAllCompos',
    method: 'post',
    data
  })
}

export const getComposByRole = data => {
  return axios({
    url: '/menu/getComposByRole',
    method: 'post',
    data
  })
}
