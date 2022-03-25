import axios from "../axios";

export const login = data => {
  return axios({
    url: '/userLogin',
    method: 'post',
    data
  })
}

export const logout = data => {
  return axios({
    url: '/userLogout',
    method: 'post',
    data
  })
}

export const editPass = data => {
  return axios({
    url: '/editPass',
    method: 'post',
    data
  })
}

export const receiveAllNotices = data => {
  return axios({
    url: '/receiveAllNotices',
    method: 'post',
    data
  })
}
