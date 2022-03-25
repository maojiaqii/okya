import axios from "../axios";

export const radioAndCheckBoxDataSource = (data) => {
  return axios({
    url: '/form/getRadioCheckBoxData',
    method: 'post',
    data
  })
}

export const selectDataSource = (data) => {
  return axios({
    url: '/form/getSelectorData',
    method: 'post',
    data
  })
}

export const selectTreeDataSource = (data) => {
  return axios({
    url: '/form/getSelectTreeData',
    method: 'post',
    data
  })
}

export const getDatasourceField = (data) => {
  return axios({
    url: '/form/getDatasourceField',
    method: 'post',
    data
  })
}

export const saveFormTemplet = (data) => {
  return axios({
    url: '/form/saveFormTemplet',
    method: 'post',
    data
  })
}

export const findFormTemplet = (data) => {
  return axios({
    url: '/form/findFormTemplet',
    method: 'post',
    data
  })
}

export const getFormTemplet = (data) => {
  return axios({
    url: '/form/getFormTemplet',
    method: 'post',
    data
  })
}

export const getFormTempletForBusiness = (data) => {
  return axios({
    url: '/form/getFormTempletForBusiness',
    method: 'post',
    data
  })
}

export const getFormBusinessDataSource = (data) => {
  return axios({
    url: '/form/getFormBusinessDataSource',
    method: 'post',
    data
  })
}

export const saveFormDataSource = (data) => {
  return axios({
    url: '/form/saveFormDataSource',
    method: 'post',
    data
  })
}

export const findFormDataSource = (data) => {
  return axios({
    url: '/form/findFormDataSource',
    method: 'post',
    data
  })
}

export const getFormDataSource = (data) => {
  return axios({
    url: '/form/getFormDataSource',
    method: 'post',
    data
  })
}

export const deleteFormDb = (data) => {
  return axios({
    url: '/form/deleteFormDb',
    method: 'post',
    data
  })
}

export const saveFormBusiness = (data) => {
  return axios({
    url: '/form/saveFormBusiness',
    method: 'post',
    data
  })
}

export const updateFormBusiness = (data) => {
  return axios({
    url: '/form/updateFormBusiness',
    method: 'post',
    data
  })
}

export const findFormBusiness = (data) => {
  return axios({
    url: '/form/findFormBusiness',
    method: 'post',
    data
  })
}

export const getSelectFormBusiness = (data) => {
  return axios({
    url: '/form/getSelectFormBusiness',
    method: 'post',
    data
  })
}

export const deleteFormBusiness = (data) => {
  return axios({
    url: '/form/deleteFormBusiness',
    method: 'post',
    data
  })
}

export const initPage = (data) => {
  return axios({
    url: '/form/initPage',
    method: 'post',
    data
  })
}

export const saveDynamicFormData = (data) => {
  return axios({
    url: '/form/saveDynamicFormData',
    method: 'post',
    data
  })
}

export const getDynamicFormData = (data) => {
  return axios({
    url: '/form/getDynamicFormData',
    method: 'post',
    data
  })
}

export const deleteDynamicFormData = (data) => {
  return axios({
    url: '/form/deleteDynamicFormData',
    method: 'post',
    data
  })
}

