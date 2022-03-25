import axios from "../axios";

export const save = data => {
  return axios({
    url: '/sysSetup/save',
    method: 'post',
    data
  })
}

export const getSetup = data => {
  return axios({
    url: '/sysSetup/getSetup',
    method: 'post',
    data
  })
}
