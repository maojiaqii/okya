import axios from "../axios";

export const getCoTree = (data) => {
  return axios({
    url: '/company/getCoTree',
    method: 'post',
    data
  })
}
