import axios from "../axios";

export const getNoticeUsers = (data) => {
  return axios({
    url: '/notice/getNoticeUsers',
    method: 'post',
    data
  })
}

export const save = (data) => {
  return axios({
    url: '/notice/save',
    method: 'post',
    data
  })
}

export const saveFrequentContacts = (data) => {
  return axios({
    url: '/notice/saveFrequentContacts',
    method: 'post',
    data
  })
}

export const getAllUsers = (data) => {
  return axios({
    url: '/notice/getAllUsers',
    method: 'post',
    data
  })
}

export const getSelectUsers = (data) => {
  return axios({
    url: '/notice/getSelectUsers',
    method: 'post',
    data
  })
}

export const updateNoticeTarget = (data) => {
  return axios({
    url: '/notice/updateNoticeTarget',
    method: 'post',
    data
  })
}
