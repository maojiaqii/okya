<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item label="表单名称">
          <el-select v-model="filters.formId" filterable clearable>
            <el-option
              v-for="item in formsSelector"
              :key="item.FORM_ID"
              :label="item.FORM_NAME"
              :value="item.FORM_ID"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-on:click="searchForm" plain>查询</el-button>
        </el-form-item>
        <el-form-item>
          <CusButton perms="sys:formBusiness:new" type="primary" @click="handleAdd"/>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <CusTable ref="formBusinessCustable" permsEdit="sys:formBusiness:edit" permsDelete="sys:formBusiness:delete"
              permsExport="sys:formBusiness:export"
              :showOperation=true
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    </CusTable>

    <el-dialog :title="operation?'新增':'编辑'" width="80%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <div style="height: 60vh;">
        <el-scrollbar style="height: 100%;">
          <el-form :model="dataForm" :rules="dataFormRules" ref="fm" label-position="right"
                   size="small">
            <el-form-item label="选择表单" prop="formId" label-width="100px">
              <el-select v-model="dataForm.formId" filterable clearable :disabled="!operation"
                         @change="formSelectChange">
                <el-option
                  v-for="item in formsSelector"
                  :key="item.FORM_ID"
                  :label="item.FORM_NAME"
                  :value="item.FORM_ID"
                  :disabled="item.CAN_SELECT == 'true'"/>
              </el-select>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item label="表格ID" prop="tableId" label-width="100px">
                  <el-input v-model="dataForm.tableId" auto-complete="off" :disabled="!operation"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="数据源" prop="dataSource" label-width="100px">
                  <el-input v-model="dataForm.dataSource" auto-complete="off" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="最大高度" prop="dataSource" label-width="100px">
                  <el-input-number v-model="dataForm.maxHeight" :min="200"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="控件尺寸" prop="size" label-width="100px">
                  <el-select v-model="dataForm.size" clearable>
                    <el-option label="mini" value="mini"/>
                    <el-option label="small" value="small"/>
                    <el-option label="medium" value="medium"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="显示行号" prop="showRowNum" label-width="100px">
                  <el-switch v-model="dataForm.showRowNum"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="显示页脚" prop="showPagination" label-width="100px">
                  <el-switch v-model="dataForm.showPagination"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="显示合计" prop="showSum" label-width="100px">
                  <el-switch v-model="dataForm.showSum"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="显示操作列" prop="showOperation" label-width="100px">
                  <el-switch v-model="dataForm.showOperation"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="新增标志" prop="permsNew" label-width="100px">
                  <el-input v-model="dataForm.permsNew" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="编辑标志" prop="permsEdit" label-width="100px">
                  <el-input v-model="dataForm.permsEdit" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="删除标志" prop="permsDelete" label-width="100px">
                  <el-input v-model="dataForm.permsDelete" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="导出标志" prop="permsExport" label-width="100px">
                  <el-input v-model="dataForm.permsExport" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-divider content-position="left">表格列配置</el-divider>
            <el-form-item prop="columnInfos">
              <CusEditTable v-if="reload" permsEdit="sys:formBusiness:edit" :lineNumber="true"
                            permsDelete="sys:formBusiness:delete" permsAddOne="sys:formBusiness:addOne"
                            permsCancelDelete="sys:formBusiness:cancelDelete"
                            :showOperation=true :addModel="editTableAddModel"
                            v-model="dataForm.columnInfos" :columns="editTableColumns">
              </CusEditTable>
            </el-form-item>
            <el-divider content-position="left">查询条件配置</el-divider>
            <el-form-item prop="searchInfos">
              <CusEditTable v-if="reload" permsEdit="sys:formBusiness:edit1" :lineNumber="true"
                            permsDelete="sys:formBusiness:delete1" permsAddOne="sys:formBusiness:addOne1"
                            permsCancelDelete="sys:formBusiness:cancelDelete1"
                            :showOperation=true :addModel="editTableAddModel1"
                            v-model="dataForm.searchInfos" :columns="editTableColumns1">
              </CusEditTable>
            </el-form-item>
          </el-form>
        </el-scrollbar>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

    export default {
        components: {},
        data() {
            return {
                formsSelector: [],
                filters: {
                    formId: ''
                },
                columns: [
                    {prop: "FORM_ID", label: "表单编号", minWidth: 40, sortable: true, visible: true},
                    {prop: "FORM_NAME", label: "表单名称", minWidth: 80, visible: true, sortable: true}
                ],
                pageResult: {},

                dataFormRules: {
                    formId: [
                        {required: true, message: '请选择表单', trigger: 'blur'}
                    ],
                    tableId: [
                        {required: true, message: '请输入表单Id', trigger: 'blur'}
                    ],
                    dataSource: [
                        {required: true, message: '请输入表单数据源', trigger: 'blur'}
                    ],
                    size: [
                        {required: true, message: '请选择控件尺寸', trigger: 'blur'}
                    ]
                },
                // 新增编辑界面数据
                dataForm: {
                    formId: '',
                    tableId: '',
                    dataSource: '',
                    maxHeight: '',
                    size: '',
                    showRowNum: 'true',
                    showPagination: 'true',
                    showSum: 'false',
                    showOperation: 'true',
                    permsNew: '',
                    permsEdit: '',
                    permsDelete: '',
                    permsExport: '',
                    columnInfos: [],
                    searchInfos: []
                },

                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                editLoading: false,

                editTableColumns: [
                    {
                        prop: "prop",
                        label: "列标识",
                        width: 150,
                        visible: true,
                        editAttr: 'el-select',
                        valueFlag: 'VAL',
                        labelFlag: 'LAB',
                        staticOptions: []
                    },
                    {
                        prop: "label",
                        label: "列名称",
                        width: 150,
                        visible: true,
                        editAttr: 'el-input'
                    },
                    {
                        prop: "width",
                        label: "列宽度",
                        width: 150,
                        visible: true,
                        editAttr: 'el-input-number',
                        precision: 0,
                        step: 1,
                        min: 1,
                        max: 300
                    },
                    {
                        prop: "minWidth",
                        label: "列最小宽度",
                        width: 150,
                        visible: true,
                        editAttr: 'el-input-number',
                        precision: 0,
                        step: 1,
                        min: 1,
                        max: 300
                    },
                    {
                        prop: "sortable",
                        label: "是否可排序",
                        width: 150,
                        visible: true,
                        editAttr: 'el-switch',
                        activeText: "是",
                        inActiveText: "否",
                        activeValue: "true",
                        inActiveValue: "false"
                    },
                    {
                        prop: "visible",
                        label: "是否可见",
                        width: 150,
                        visible: true,
                        editAttr: 'el-switch',
                        activeText: "是",
                        inActiveText: "否",
                        activeValue: "true",
                        inActiveValue: "false"
                    },
                    {
                        prop: "filter",
                        label: "是否可过滤",
                        width: 150,
                        visible: true,
                        editAttr: 'el-switch',
                        activeText: "是",
                        inActiveText: "否",
                        activeValue: "true",
                        inActiveValue: "false"
                    },
                    {
                        prop: "align",
                        label: "对齐方式",
                        width: 150,
                        visible: true,
                        editAttr: 'el-select',
                        valueFlag: 'VAL',
                        labelFlag: 'LAB',
                        staticOptions: [{'VAL': 'left', 'LAB': '左对齐'},
                            {'VAL': 'center', 'LAB': '居中'},
                            {'VAL': 'right', 'LAB': '右对齐'}]
                    },
                    {
                        prop: "content",
                        label: "列头提示内容",
                        width: 150,
                        visible: true,
                        editAttr: 'el-input'
                    }
                ],
                editTableAddModel: {
                    "prop": "",
                    "label": "",
                    "width": 60,
                    "minWidth": 60,
                    "sortable": 'true',
                    "visible": 'true',
                    "filter": 'true',
                    "align": "center",
                    "content": ""
                },
                editTableColumns1: [
                    {
                        prop: "searchField",
                        label: "查询字段",
                        width: 150,
                        visible: true,
                        editAttr: 'el-select',
                        valueFlag: 'VAL',
                        labelFlag: 'LAB',
                        staticOptions: []
                    },
                    {
                        prop: "searchName",
                        label: "查询名称",
                        width: 150,
                        visible: true,
                        editAttr: 'el-input'
                    },
                    {
                        prop: "searchCompo",
                        label: "查询控件",
                        width: 150,
                        visible: true,
                        editAttr: 'el-select',
                        valueFlag: 'VAL',
                        labelFlag: 'LAB',
                        staticOptions: [{'VAL': 'searchElInput', 'LAB': '文本输入框'},
                            {'VAL': 'searchElDatePicker', 'LAB': '日期选择器'},
                            {'VAL': 'searchElTimePicker', 'LAB': '时间选择器'},
                            {'VAL': 'searchElSelecter', 'LAB': '下拉选择器'},
                            {'VAL': 'searchElSelectTree', 'LAB': '下拉树'}]
                    },
                    {
                        prop: "searchCondition",
                        label: "查询条件",
                        width: 150,
                        visible: true,
                        editAttr: 'el-select',
                        valueFlag: 'VAL',
                        labelFlag: 'LAB',
                        staticOptions: [{'VAL': 'g', 'LAB': '大于'},
                            {'VAL': 'g=', 'LAB': '大于等于'},
                            {'VAL': 'l', 'LAB': '小于'},
                            {'VAL': 'l=', 'LAB': '小于等于'},
                            {'VAL': '==', 'LAB': '等于'},
                            {'VAL': '%%', 'LAB': '类似'}]
                    },
                    {
                        prop: "dataSource",
                        label: "选择器数据源",
                        width: 250,
                        visible: true,
                        editAttr: 'el-input'
                    }
                ],
                editTableAddModel1: {
                    "searchField": "",
                    "searchName": "",
                    "searchCompo": "",
                    "searchCondition": "",
                    "dataSource": ""
                },
                reload: true
            }
        },
        methods: {
            initFormsSelector: function () {
                this.$api.form.getFormTempletForBusiness({}).then((res) => {
                    this.formsSelector = JSON.parse(res.message)
                })
            },
            formSelectChange: function (value) {
                this.reload = false;
                this.$api.form.getFormBusinessDataSource({"formId": value}).then((res) => {
                    this.dataForm.columnInfos = [];
                    this.dataForm.searchInfos = [];
                    if (res.success) {
                        let formBusinessDataSource = JSON.parse(res.message);
                        this.dataForm.dataSource = formBusinessDataSource.dataSource;
                        this.editTableColumns[0].staticOptions = JSON.parse(formBusinessDataSource.columnIds);
                        this.editTableColumns1[0].staticOptions = JSON.parse(formBusinessDataSource.columnIds);
                    } else {
                        this.editTableColumns[0].staticOptions = [];
                        this.editTableColumns1[0].staticOptions = [];
                        this.$message({message: res.message, type: 'error'})
                    }
                });
                this.$nextTick(() => {
                    this.reload = true;
                })
            },
            // 获取数据
            searchForm: function () {
                this.$refs.formBusinessCustable.pageNum = 1
                this.$refs.formBusinessCustable.sortProp = ''
                this.$refs.formBusinessCustable.sortOrder = ''
                this.$api.form.findFormBusiness({
                    'formId': this.filters.formId,
                    'pageNum': 1,
                    'pageSize': this.$refs.formBusinessCustable.pageSize,
                    'sortProp': '',
                    'sortOrder': ''
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
            },
            // 获取分页数据
            findPage: function (param) {
                this.$api.form.findFormBusiness({
                    'formId': this.filters.formId,
                    'pageNum': param.pageNum,
                    'pageSize': param.pageSize,
                    'sortProp': param.sortProp,
                    'sortOrder': param.sortOrder
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
            },
            // 批量删除
            handleDelete: function (data) {
                this.$api.form.deleteFormBusiness(data.params).then(data.callback)
            },
            // 显示新增界面
            handleAdd: function () {
                this.reload = false;
                this.dataForm = {
                    formId: '',
                    tableId: '',
                    dataSource: '',
                    maxHeight: '',
                    size: '',
                    showRowNum: 'true',
                    showPagination: 'true',
                    showSum: 'false',
                    showOperation: 'true',
                    permsNew: '',
                    permsEdit: '',
                    permsDelete: '',
                    permsExport: '',
                    columnInfos: [],
                    searchInfos: []
                }
                this.editTableColumns[0].staticOptions = [];
                this.editTableColumns1[0].staticOptions = [];
                this.editDialogVisible = true
                this.operation = true
                this.$nextTick(() => {
                    this.reload = true;
                })
            },
            // 显示编辑界面
            handleEdit: function (params) {
                let formId = params.row.FORM_ID;
                this.$api.form.getSelectFormBusiness({
                    'formId': formId
                }).then((res) => {
                    if (res.success) {
                        this.dataForm = JSON.parse(res.message);
                        this.dataForm.formId = formId;
                        this.dataForm.columnInfos = JSON.parse(this.dataForm.columnInfos);
                        this.dataForm.searchInfos = JSON.parse(this.dataForm.searchInfos);
                        this.editDialogVisible = true;
                        this.operation = false;
                    } else {
                        this.$message({message: res.message, type: 'error'})
                    }
                })
            },
            editSubmit: function () {
                this.$refs.fm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true
                            let params = Object.assign({}, this.dataForm)
                            params.columnInfos = JSON.stringify(params.columnInfos);
                            params.searchInfos = JSON.stringify(params.searchInfos);
                            if (this.operation) {
                                this.$api.form.saveFormBusiness(params).then((res) => {
                                    if (res.success) {
                                        this.$refs.formBusinessCustable.reloadData();
                                        this.$message({message: res.message, type: 'success'});
                                    } else {
                                        this.$message({message: res.message, type: 'error'})
                                    }
                                })
                            } else {
                                this.$api.form.updateFormBusiness(params).then((res) => {
                                    if (res.success) {
                                        this.$refs.formBusinessCustable.reloadData();
                                        this.$message({message: res.message, type: 'success'});
                                    } else {
                                        this.$message({message: res.message, type: 'error'})
                                    }
                                })
                            }
                            this.editLoading = false;
                            this.editDialogVisible = false;
                        })
                    }
                })
            }
        },
        mounted() {
            this.initFormsSelector()
        },
        watch: {
            editDialogVisible(v) {
                if (v) {
                    this.initFormsSelector();
                    if (this.dataForm.formId != '') {
                        this.$api.form.getFormBusinessDataSource({"formId": this.dataForm.formId}).then((res) => {
                            if (res.success) {
                                let formBusinessDataSource = JSON.parse(res.message);
                                this.editTableColumns[0].staticOptions = JSON.parse(formBusinessDataSource.columnIds);
                                this.editTableColumns1[0].staticOptions = JSON.parse(formBusinessDataSource.columnIds);
                            } else {
                                this.editTableColumns[0].staticOptions = [];
                                this.editTableColumns1[0].staticOptions = [];
                                this.$message({message: res.message, type: 'error'})
                            }
                        });
                    }
                }
            }
        }
    }
</script>
