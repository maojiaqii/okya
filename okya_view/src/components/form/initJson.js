let compoModels = [
  // 单行文本
  {
    "compoType": "formElInput",
    "icon": "icon iconfont icondanhangwenben",
    "name": "单行文本",
    "model": '',
    "span": 24,
    "key": "elInput" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "单行文本",
    "compoProp": {
      "titleWidth": "80px",
      "placeholder": "",
      "hidden": true,
      "disabled": false,
      "required": false,
      "showPassword": false
    },
    "binds": []
  },
// 多行文本
  {
    "compoType": "formElTextArea",
    "icon": "icon iconfont iconduohangwenben",
    "name": "多行文本",
    "model": '',
    "span": 24,
    "key": "textArea" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "多行文本",
    "compoProp": {
      "titleWidth": "80px",
      "placeholder": "",
      "hidden": true,
      "disabled": false,
      "required": false
    },
    "binds": []
  },
// 单选框组
  {
    "compoType": "formElRadio",
    "icon": "icon iconfont icondanxuankuangzu",
    "name": "单选框组",
    "model": '',
    "span": 24,
    "key": "elRadio" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "单选框组",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "remoteOption": "",
      "value": "VAL",
      "label": "LAB"
    },
    "binds": []
  },
// 多选框组
  {
    "compoType": "formElCheckBox",
    "icon": "icon iconfont iconduoxuankuang1",
    "name": "多选框组",
    "model": [],
    "span": 24,
    "key": "elCheckbox" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "多选框组",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "remoteOption": "",
      "value": "VAL",
      "label": "LAB"
    },
    "binds": []
  },
// 计数器
  {
    "compoType": "formElInputNumber",
    "icon": "icon iconfont iconjishuqi",
    "name": "计数器",
    "model": 0,
    "span": 24,
    "key": "inputNumber" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "计数器",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "min": 0,
      "max": 9,
      "step": 1,
      "precision": 0
    },
    "binds": []
  },
// 开关
  {
    "compoType": "formElSwitch",
    "icon": "icon iconfont iconkaiguanguan",
    "name": "开关",
    "model": "0",
    "span": 24,
    "key": "switch" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "开关",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "activeText": '是',
      "activeValue": '0',
      "inActiveText": '否',
      "inActiveValue": '1'
    },
    "binds": []
  },
// 日期选择器
  {
    "compoType": "formElDatePicker",
    "icon": "icon iconfont iconriqi-copy",
    "name": "日期选择器",
    "model": "",
    "span": 24,
    "key": "datePicker" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "日期选择器",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "type": "date",
      "format": "yyyy-MM-dd"
    },
    "binds": []
  },
// 时间选择器
  {
    "compoType": "formElTimePicker",
    "icon": "icon iconfont iconshijian1",
    "name": "时间选择器",
    "model": "",
    "span": 24,
    "key": "timePicker" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "时间选择器",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "format": "HH:mm:ss"
    },
    "binds": []
  },
// 下拉选择
  {
    "compoType": "formElSelecter",
    "icon": "icon iconfont iconxialakuang1",
    "name": "下拉选择",
    "model": "",
    "span": 24,
    "key": "select" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "下拉选择",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "multiple": false,
      "remoteOption": "",
      "value": "VAL",
      "label": "LAB"
    },
    "binds": []
  },
// 下拉树
  {
    "compoType": "formElSelectTree",
    "icon": "icon iconfont iconxialashu",
    "name": "下拉树",
    "model": "",
    "span": 24,
    "key": "popupTree" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "下拉树",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "single": true,
      "remoteOption": ""
    },
    "binds": []
  },
// 评分
  {
    "compoType": "formElRate",
    "icon": "icon iconfont iconpingfen",
    "name": "评分",
    "model": "0",
    "span": 24,
    "key": "rate" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "评分",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "max": 5
    },
    "binds": []
  },
// 文件上传
  {
    "compoType": "formElUpload",
    "icon": "icon iconfont iconwenjianshangchuan",
    "name": "文件上传",
    "model": [],
    "span": 24,
    "key": "fileUpload" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "文件上传",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "many": false,
      "fileCount": 1,
      "tip": ""
    },
    "binds": []
  },
// 文字
  {
    "compoType": "formWord",
    "icon": "icon iconfont iconwenzi1",
    "name": "文字",
    "model": "文字标题",
    "span": 24,
    "key": "word" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "文字标题",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "style": "text-align:center;font-weight:bold;padding:30px"
    }
  },
// 分割线
  {
    "compoType": "formElDivider",
    "icon": "icon iconfont iconfengexian",
    "name": "分割线",
    "model": "分割线",
    "span": 24,
    "key": "divider" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "分割线",
    "compoProp": {
      "align": "left",
      "hidden": true
    }
  },
// 子表单
  {
    "compoType": "formElEditTable",
    "icon": "icon iconfont iconbiaodanmoshi",
    "name": "子表单",
    "model": [],
    "span": 24,
    "key": "editTable" + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
    "compoTitle": "子表单",
    "compoProp": {
      "titleWidth": "80px",
      "hidden": true,
      "disabled": false,
      "required": false,
      "permsDelete": "",
      "permsEdit": "",
      "permsAddOne": "",
      "permsCancelDelete": "",
      "maxHeight": 400,
      "editTableColumns": []
    },
    "binds": []
  }
]

export {
  compoModels
}

