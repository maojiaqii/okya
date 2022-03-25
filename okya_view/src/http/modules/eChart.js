import axios from "../axios";

export const getCommonData = (data) => {
  return axios({
    url: '/eChart/getCommonData',
    method: 'post',
    data
  })
}
