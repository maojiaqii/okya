import axios from "../axios";

export const getRoles = (data) => {
  return axios({
    url: '/role/getRoles',
    method: 'post',
    data
  })
}

export const findPage = (data) => {
  return axios({
    url: '/role/findPage',
    method: 'post',
    data
  })
}

export const save = (data) => {
  return axios({
    url: '/role/save',
    method: 'post',
    data
  })
}

export const update = (data) => {
  return axios({
    url: '/role/update',
    method: 'post',
    data
  })
}

export const delet = (data) => {
  return axios({
    url: '/role/delete',
    method: 'post',
    data
  })
}

export const setRolePermissions = (data) => {
  return axios({
    url: '/role/setRolePermissions',
    method: 'post',
    data
  })
}
