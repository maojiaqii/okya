import axios from "../axios";

export const findPage = (data) => {
  return axios({
    url: '/form/findPage',
    method: 'post',
    data
  })
}
