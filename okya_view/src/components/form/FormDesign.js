import {compoModels} from './initJson.js'
import draggable from 'vuedraggable'

import formElInput from "./compo/formElInput";
import formElTextArea from "./compo/formElTextArea";
import formElRadio from "./compo/formElRadio";
import formElCheckBox from "./compo/formElCheckBox";
import formElInputNumber from "./compo/formElInputNumber";
import formElSwitch from "./compo/formElSwitch";
import formElDatePicker from "./compo/formElDatePicker";
import formElTimePicker from "./compo/formElTimePicker";
import formElSelecter from "./compo/formElSelecter";
import formElSelectTree from "./compo/formElSelectTree";
import formElRate from "./compo/formElRate";
import formElUpload from "./compo/formElUpload";
import formWord from "./compo/formWord";
import formElDivider from "./compo/formElDivider";
import formElEditTable from "./compo/formElEditTable";

export default {
  name: 'FormDesign',
  components: {
    draggable,
    formElInput,
    formElTextArea,
    formElRadio,
    formElCheckBox,
    formElInputNumber,
    formElSwitch,
    formElDatePicker,
    formElTimePicker,
    formElSelecter,
    formElSelectTree,
    formElRate,
    formElUpload,
    formWord,
    formElDivider,
    formElEditTable
  },
  data() {
    return {
      flags: 'article',
      previewDialogVisible: false,
      compoList: compoModels,
      selectList: '',
      previewList: [],
      selectNd: '',
      selectElement: undefined,
      fieldProps: {
        key: '',
        compoTitle: '',
        compoProp: {
          compoWidth: '',
          titleWidth: 0,
          hidden: false,
          disabled: false,
          required: false
        }
      },
      formProps: {
        formId: '',
        formName: '',
        labelPosition: 'right',
        size: 'medium',
      },
      active: 0,

      reloadFlag: false,
      selectField: '',
      bindTableResultColumns: [
        {
          prop: "relation",
          label: "关系",
          minWidth: 40,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'VAL',
          labelFlag: 'LAB',
          staticOptions: []
        },
        {
          prop: "targetField",
          label: "目标字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'key',
          labelFlag: 'compoTitle',
          staticOptions: []
        },
        {
          prop: "affectField",
          label: "约束字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'VAL',
          labelFlag: 'LAB',
          staticOptions: []
        }
      ],
      bindTableResult: [],
      addModel: {
        "targetField": "",
        "affectField": "",
        "relation": ""
      },
      editLoading: false,

      selectChildFormTable: '',
      childFormTableResultColumns: [
        {
          prop: "prop",
          label: "字段标识",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        },
        {
          prop: "label",
          label: "字段名称",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        },
        {
          prop: "editAttr",
          label: "字段类型",
          minWidth: 60,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'VAL',
          labelFlag: 'LAB',
          staticOptions: [{'VAL': 'el-input', 'LAB': '文本输入框'},
            {'VAL': 'el-input-number', 'LAB': '数字输入框'},
            {'VAL': 'el-date', 'LAB': '日期选择器'},
            {'VAL': 'el-time', 'LAB': '时间选择器'},
            {'VAL': 'el-select', 'LAB': '下拉选择器'},
            {'VAL': 'el-tree', 'LAB': '下拉树'},
            {'VAL': 'el-switch', 'LAB': '开关'}]
        },
        {
          prop: "precision",
          label: "数字精度",
          minWidth: 60,
          visible: true,
          editAttr: 'el-input-number',
          precision: 0,
          step: 1,
          min: 0,
          max: 2
        },
        {
          prop: "datasource",
          label: "数据源",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        },
        {
          prop: "switch",
          label: "开关",
          minWidth: 40,
          visible: true,
          children: [{
            prop: "activeText",
            label: "打开描述",
            minWidth: 40,
            visible: true,
            editAttr: 'el-input',
            children:[]
          },
            {
              prop: "activeValue",
              label: "打开值",
              minWidth: 40,
              visible: true,
              editAttr: 'el-input',
              children:[]
            },
            {
              prop: "inactiveText",
              label: "关闭描述",
              minWidth: 40,
              visible: true,
              editAttr: 'el-input',
              children:[]
            },
            {
              prop: "inactiveValue",
              label: "关闭值",
              minWidth: 40,
              visible: true,
              editAttr: 'el-input',
              children:[]
            }]
        }
      ],
      childFormTableResult: [],
      childFormTableAddModel: {
        "prop": "",
        "label": "",
        "editAttr": "",
        "visible": true,
        "precision": 0,
        "datasource": "",
        "valueFlag": 'VAL',
        "labelFlag": 'LAB',
        "dateformat": "yyyy-MM-dd",
        "type": "date",
        "timeformat": "HH:mm:ss",
        "activeText": "",
        "activeValue": "",
        "inactiveText": "",
        "inactiveValue": ""
      },
      childFormTableReloadFlag: false
    }
  },
  props: {
    formTemplet: {
      type: String,
      default: '{"list": [], "formId": "", "formName": "", "labelPosition": "right", "size": "medium"}'
    },
    isNew: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    start1(event) {
    },
    end1(ev) {
      if (ev.to.className === 'dragArea11') {
        this.selectList.list = JSON.parse(JSON.stringify(this.selectList.list));
        this.selectList.list[ev.newIndex].key = this.selectList.list[ev.newIndex].compoType + parseInt(Math.random() * (1000000 - 1 + 1) + 1)
      }
    },
    start22(event) {
      this.flags = '222222'
    },
    end22(ev) {
      this.flags = 'article'
    },
    compoSelect: function (e, ele) {
      if (this.selectNd === e.currentTarget) {
        this.CopyCss(this.selectNd, '1px solid #bfcbd9', '', '');
        this.selectNd = '';
        this.selectElement = {
          key: '',
          compoTitle: '',
          compoProp: {
            compoWidth: '',
            titleWidth: 0,
            hidden: false,
            disabled: false,
            required: false
          }
        }
      } else {
        if (this.selectNd !== '') {
          this.CopyCss(this.selectNd, '1px solid #bfcbd9', '', '')
        }
        this.selectNd = e.currentTarget;
        this.selectElement = ele;
        this.CopyCss(this.selectNd, '1px dashed red', 'rgba(204,204,204,0.3)', '4px')
      }
      this.fieldProps = this.selectElement
    },
    editableSelect: function (e, ele) {
      this.childFormTableReloadFlag = false;
      this.childFormTableResult = [];
      // 选择字段后，改变样式
      if (this.selectChildFormTable === e.currentTarget) {
        this.CopyCss(this.selectChildFormTable, '1px solid #bfcbd9', '', '');
        this.selectChildFormTable = '';
      } else {
        if (this.selectChildFormTable !== '') {
          this.CopyCss(this.selectChildFormTable, '1px solid #bfcbd9', '', '')
        }
        this.selectChildFormTable = e.currentTarget;
        this.childFormTableResult = ele.compoProp.editTableColumns;
        this.CopyCss(this.selectChildFormTable, '1px dashed red', 'rgba(204,204,204,0.3)', '4px')
      }
      this.$nextTick(() => {
        this.childFormTableReloadFlag = true
      });
    },
    fieldSelect: function (e, ele) {
      this.reloadFlag = false;
      this.bindTableResult = [];
      // 选择字段后，改变样式
      if (this.selectField === e.currentTarget) {
        this.CopyCss(this.selectField, '1px solid #bfcbd9', '', '');
        this.selectField = '';
        this.bindTableResultColumns[0].staticOptions = [];
        this.bindTableResultColumns[2].staticOptions = [];
      } else {
        if (this.selectField !== '') {
          this.CopyCss(this.selectField, '1px solid #bfcbd9', '', '')
        }
        this.selectField = e.currentTarget;
        this.bindTableResult = ele.binds;
        this.CopyCss(this.selectField, '1px dashed red', 'rgba(204,204,204,0.3)', '4px')

        // 选择的字段为下拉选择时，【关联约束】仅显示级联关系；选择的字段为非下拉选择时，【关联约束】不显示级联关系
        if (ele.compoType === 'formElSelecter' || ele.compoType === 'formElSelectTree') {
          this.bindTableResultColumns[0].staticOptions = [{'VAL': 'baseOn', 'LAB': '基于（下拉选择专用）'},
            {'VAL': 'remove', 'LAB': '排除（下拉选择专用）'}]
          this.$api.form.getDatasourceField({
            'datasource': ele.compoProp.remoteOption
          }).then((res) => {
            if (res.success) {
              this.bindTableResultColumns[2].staticOptions = JSON.parse(res.message)
            } else {
              this.$message({message: res.message, type: 'error'})
              this.bindTableResultColumns[2].staticOptions = []
            }
          })
        } else {
          this.bindTableResultColumns[0].staticOptions = [{'VAL': '>', 'LAB': '大于'},
            {'VAL': '>=', 'LAB': '大于等于'},
            {'VAL': '<', 'LAB': '小于'},
            {'VAL': '<=', 'LAB': '小于等于'},
            {'VAL': '==', 'LAB': '等于'},
            {'VAL': '!=', 'LAB': '不等于'}]
          this.bindTableResultColumns[2].staticOptions = []
        }
      }
      this.$nextTick(() => {
        this.reloadFlag = true
      });
    },
    CopyCss: function (obj, border, background, borderradius) {
      obj.style.border = border;
      obj.style.background = background;
      obj.style.borderRadius = borderradius
    },
    removeComponent: function () {
      if (this.selectElement) {
        this.$confirm('确定要删除所选字段吗？')
          .then(_ => {
            for (let i = 0; i < this.selectList.list.length; i++) {
              if (this.selectList.list[i].key === this.selectElement.key) {
                this.selectList.list.splice(i, 1);
                break
              }
            }
            this.selectElement = undefined
          })
          .catch(_ => {
          });
      } else {
        this.$message({message: '未选择需要删除的字段', type: 'warning'})
      }
    }
    ,
    clearComponent: function () {
      this.$confirm('确定要清空表单吗？')
        .then(_ => {
          this.selectList = {"list": [], "formId": "", "formName": "", "labelPosition": "right", "size": "medium"};
          this.selectElement = undefined
        })
        .catch(_ => {
        });
    },
    tWC: function () {
      if (this.fieldProps.compoProp.titleWidth === 'auto') {
        this.fieldProps.compoProp.titleWidth = '80px'
      } else {
        this.fieldProps.compoProp.titleWidth = 'auto'
      }
    },
    isSingle: function (value) {
      if (value) {
        this.fieldProps.model = ''
      } else {
        this.fieldProps.model = []
      }
    },
    setSwitchDefault: function (value) {
      this.fieldProps.model = value
    },
    consoleJson: function () {
      console.log(this.selectList)
    },
    saveForm: function () {
      this.editLoading = true
      this.$api.form.saveFormTemplet({
        'formJson': JSON.stringify(this.selectList)
      }).then((res) => {
        if (res.success) {
          this.$message({message: res.message, type: 'success'})
        } else {
          this.$message({message: res.message, type: 'error'})
        }
      })
      this.editLoading = false
    },
    preview: function () {
      this.previewDialogVisible = true
    },
    beforeUpload: function (file) {
      const extension = file.name.substring(file.name.lastIndexOf('.') + 1) === 'json';
      if (!extension) {
        this.$message({
          message: '上传文件只能是json格式!',
          type: 'warning'
        });
      }
      return extension
    },
    uploadJson: function (file) {
      this.$refs.upload.clearFiles();
      if (this.selectList.list.length > 0) {
        this.$confirm('确认覆盖已设计的表单？', '提示', {}).then(() => {
          let reader = new FileReader();
          if (typeof FileReader === "undefined") {
            this.$message({
              type: "info",
              message: "您的浏览器不支持文件读取。"
            });
            return;
          }
          reader.readAsText(file.raw, "utf-8");
          var _this = this;
          reader.onload = function (e) {
            _this.selectList = JSON.parse(e.target.result);
          }
        })
      } else {
        let reader = new FileReader();
        if (typeof FileReader === "undefined") {
          this.$message({
            type: "info",
            message: "您的浏览器不支持文件读取。"
          });
          return;
        }
        reader.readAsText(file.raw, "utf-8");
        var _this = this;
        reader.onload = function (e) {
          _this.selectList = JSON.parse(e.target.result);
        }
      }
    },
    next() {
      this.active++
    },
    back() {
      this.active--
    }
  },
  mounted() {
    this.selectList = JSON.parse(this.formTemplet);
  },
  watch: {
    selectList: {
      handler(val) {
        this.bindTableResultColumns[1].staticOptions = val.list
      },
      deep: true
    },

  }
}
