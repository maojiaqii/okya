export default {
  data() {
    return {
      active: 0,
      reloadFlag: true,
      selectField: '',
      selectChildForm: '',

      formsSelector: [],
      formSelect: {"GUID": "", "SET_YEAR": "", "FORM_NAME": "", "FORM_ID": "", "FORM": "{}"},
      formDataSourceModel: {
        "formId": '',
        "hasSetYear": "",
        "mainTableName": "",
        "childTableNames": {},
        "childTableColumns": {},
        "mainTableColumns": [],
        "viewSql": ""
      },
      formDataSourceModelRules: {
        formId: [
          {required: true, message: '请选择表单', trigger: 'blur'}
        ],
        mainTableName: [
          {required: true, message: '请输入主表名称', trigger: 'blur'}
        ],
        childTableNames: [
          {required: true, message: '请输入副表名称', trigger: 'blur'}
        ],
        viewSql: [
          {required: true, message: '请输入视图语句', trigger: 'blur'}
        ],
      },

      childTableInfoColumns: [
        {
          prop: "formField",
          label: "表单字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'prop',
          labelFlag: 'label',
          staticOptions: []
        },
        {
          prop: "tableField",
          label: "主表字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        },
        {
          prop: "tableFieldType",
          label: "字段类型",
          minWidth: 40,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'VAL',
          labelFlag: 'LAB',
          staticOptions: [{'VAL': 'varchar2', 'LAB': 'varchar2'}, {'VAL': 'clob', 'LAB': 'clob'}]
        },
        {
          prop: "isNull",
          label: "可以为空",
          minWidth: 40,
          visible: true,
          editAttr: 'el-switch',
          activeText: "是",
          inActiveText: "否",
          activeValue: "Y",
          inActiveValue: "N"
        },
        {
          prop: "isUnique",
          label: "唯一约束",
          minWidth: 40,
          visible: true,
          editAttr: 'el-switch',
          activeText: "是",
          inActiveText: "否",
          activeValue: "Y",
          inActiveValue: "N"
        },
        {
          prop: "joinTable",
          label: "从表名称",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        },
        {
          prop: "joinTableColumn",
          label: "从表字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        }
      ],
      mainTableInfoColumns: [
        {
          prop: "formField",
          label: "表单字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'key',
          labelFlag: 'compoTitle',
          staticOptions: []
        },
        {
          prop: "tableField",
          label: "主表字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        },
        {
          prop: "tableFieldType",
          label: "字段类型",
          minWidth: 40,
          visible: true,
          editAttr: 'el-select',
          valueFlag: 'VAL',
          labelFlag: 'LAB',
          staticOptions: [{'VAL': 'varchar2', 'LAB': 'varchar2'}, {'VAL': 'clob', 'LAB': 'clob'}]
        },
        {
          prop: "isNull",
          label: "可以为空",
          minWidth: 40,
          visible: true,
          editAttr: 'el-switch',
          activeText: "是",
          inActiveText: "否",
          activeValue: "Y",
          inActiveValue: "N"
        },
        {
          prop: "isUnique",
          label: "唯一约束",
          minWidth: 40,
          visible: true,
          editAttr: 'el-switch',
          activeText: "是",
          inActiveText: "否",
          activeValue: "Y",
          inActiveValue: "N"
        },
        {
          prop: "joinTable",
          label: "子表名称",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        },
        {
          prop: "joinTableColumn",
          label: "子表字段",
          minWidth: 40,
          visible: true,
          editAttr: 'el-input'
        }
      ],
      addModel: {
        "formField": "",
        "tableField": "",
        "tableFieldType": "",
        "isNull": "",
        "isUnique": "",
        "joinTable": "",
        "joinTableColumn": ""
      },
      editLoading: false,
      reloadF: false
    }
  },
  props: {
    isNew: {
      type: Boolean,
      default: true
    },
    editForm: {
      type: Object,
      default: {}
    }
  },
  methods: {
    initFormsSelector: function () {
      this.$api.form.getFormTemplet({}).then((res) => {
        this.formsSelector = JSON.parse(res.message)
      })
    },
    initFormDataSourceModel: function () {
      this.$api.form.getFormDataSource({
        "formId": this.editForm.FORM_ID
      }).then((res) => {
        if (res.success) {
          this.formDataSourceModel = JSON.parse(res.message)
        } else {
          this.$message({message: res.message, type: 'error'})
        }
      })
    },
    formSelectChange: function (value) {
      this.formDataSourceModel = {
        "formId": value,
        "hasSetYear": "",
        "mainTableName": "",
        "childTableNames": {},
        "childTableColumns": {},
        "mainTableColumns": [],
        "viewSql": ""
      };
      if (Object.keys(this.editForm).length !== 0) {
        this.formSelect = this.editForm;
      } else {
        let selectForm = this.formsSelector.find((item) => item.FORM_ID === value);
        this.formSelect = selectForm;
        if (this.isNew) {
          JSON.parse(selectForm.FORM).list.forEach(i => {
            if (i.compoType == 'formElEditTable') {
              this.formDataSourceModel.childTableColumns[i.key] = [];
            }
          })
        }
      }
      this.mainTableInfoColumns[0].staticOptions = JSON.parse(this.formSelect.FORM).list
    },
    childFormSelect: function (e, ele) {
      this.reloadFlag = false;
      // 选择字段后，改变样式
      if (this.selectField === e.currentTarget) {
        this.CopyCss(this.selectField, '1px solid #bfcbd9', '', '');
        this.selectField = '';
        this.selectChildForm = '';
        this.childTableInfoColumns[0].staticOptions = [];
      } else {
        if (this.selectField !== '') {
          this.CopyCss(this.selectField, '1px solid #bfcbd9', '', '');
        }
        this.selectField = e.currentTarget;
        this.selectChildForm = ele.key;
        this.childTableInfoColumns[0].staticOptions = ele.compoProp.editTableColumns;
        this.CopyCss(this.selectField, '1px dashed red', 'rgba(204,204,204,0.3)', '4px');
      }
      this.$nextTick(() => {
        this.reloadFlag = true;
      });
    },
    saveFormDb: function () {
      this.$refs.fm.validate((valid) => {
        if (valid) {
          if (!this.isNew) {
            this.$confirm('如修改的表单已产生业务数据，修改数据源将丢失所有业务数据，无法撤回，确定修改吗？', '危险操作', {}).then(() => {
              this.editLoading = true;
              let params = Object.assign({}, this.formDataSourceModel);
              params.childTableNames = JSON.stringify(params.childTableNames);
              params.childTableColumns = JSON.stringify(params.childTableColumns);
              params.mainTableColumns.push({
                "formField": "createUser",
                "tableField": "CREATE_USER",
                "tableFieldType": "varchar2",
                "isNull": "N",
                "isUnique": "N",
                "joinTable": "AS_USER",
                "joinTableColumn": "USER_CODE"
              });
              params.mainTableColumns = JSON.stringify(params.mainTableColumns);
              this.$api.form.saveFormDataSource(params).then((res) => {
                if (res.success) {
                  this.$message({message: res.message, type: 'success'});
                } else {
                  this.$message({message: res.message, type: 'error'})
                }
                this.editLoading = false
              })
            }).catch((e) => {
            })
          } else {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.editLoading = true;
              let params = Object.assign({}, this.formDataSourceModel);
              params.childTableNames = JSON.stringify(params.childTableNames);
              params.childTableColumns = JSON.stringify(params.childTableColumns);
              params.mainTableColumns = JSON.stringify(params.mainTableColumns);
              this.$api.form.saveFormDataSource(params).then((res) => {
                if (res.success) {
                  this.$message({message: res.message, type: 'success'});
                } else {
                  this.$message({message: res.message, type: 'error'})
                }
                this.editLoading = false
              })
            }).catch((e) => {
            })
          }
        }
      })
    },
    nextTab() {
      this.active++
      if (this.active == 2) {
        this.reloadF = true
      } else {
        this.reloadF = false
      }
    },
    backTab() {
      this.active--
      if (this.active == 2) {
        this.reloadF = true
      } else {
        this.reloadF = false
      }
    },
    CopyCss: function (obj, border, background, borderradius) {
      obj.style.border = border;
      obj.style.background = background;
      obj.style.borderRadius = borderradius
    },
  },
  mounted() {
    this.initFormsSelector();
    if (Object.keys(this.editForm).length !== 0) {
      this.formSelectChange()
      this.initFormDataSourceModel();
    }
  },
  watch: {}
}
