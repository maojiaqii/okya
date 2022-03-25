<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item label="报表名称">
          <el-input v-model='filters["REPORT_NAME,%%"]' clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-on:click="search" plain>查询</el-button>
        </el-form-item>
        <el-form-item>
          <CusButton perms="report:design:new" type="primary" @click="handleAdd"/>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <CusTable ref="custable" permsEdit="report:design:edit" permsDelete="report:design:delete"
              permsExport="report:design:export"
              :showOperation=true
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns" datasource="AS_REPORT"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    </CusTable>
    <!--新增编辑界面-->
    <el-dialog :title="operation?'新增':'编辑'" width="80%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <div style="height: 60vh; margin-right: 10px">
        <el-scrollbar style="height: 100%;">
          <el-form :model="dataForm" ref="fm" :rules="dataFormRules" label-position="right" size="small">
            <el-row>
              <el-col :span="12">
                <el-form-item label="报表ID" prop="REPORT_ID" label-width="100px">
                  <el-input v-model="dataForm.REPORT_ID" auto-complete="off" :disabled="!operation"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报表名称" prop="REPORT_NAME" label-width="100px">
                  <el-input v-model="dataForm.REPORT_NAME" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="数据源" prop="DATA_SOURCE" label-width="100px">
                  <el-input v-model="dataForm.DATA_SOURCE" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最大高度" prop="MAX_HEIGHT" label-width="100px">
                  <el-input-number v-model="dataForm.MAX_HEIGHT" :min="200"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="控件尺寸" prop="size" label-width="100px">
                  <el-select v-model="dataForm.size" clearable>
                    <el-option label="mini" value="mini"/>
                    <el-option label="small" value="small"/>
                    <el-option label="medium" value="medium"/>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="显示行号" prop="SHOW_ROW_NUM" label-width="100px">
                  <el-switch v-model="dataForm.SHOW_ROW_NUM"
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
                <el-form-item label="显示页脚" prop="SHOW_PAGINATION" label-width="100px">
                  <el-switch v-model="dataForm.SHOW_PAGINATION"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="显示合计" prop="SHOW_SUM" label-width="100px">
                  <el-switch v-model="dataForm.SHOW_SUM"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="关联年度" prop="NEED_YEAR" label-width="100px">
              <el-switch v-model="dataForm.NEED_YEAR"
                         active-color="#13ce66"
                         inactive-color="#ff4949"
                         active-text="是"
                         inactive-text="否"
                         active-value="Y"
                         inactive-value="N"/>
            </el-form-item>
            <el-divider content-position="left">查询条件配置</el-divider>
            <el-form-item prop="searchInfos">
              <CusEditTable v-if="reload" permsEdit="report:design:edit1" :lineNumber="true"
                            permsDelete="report:design:delete1" permsAddOne="report:design:addOne1"
                            permsCancelDelete="report:design:cancelDelete1"
                            :showOperation=true :addModel="editTableAddModel1"
                            v-model="dataForm.searchInfos" :columns="editTableColumns1">
              </CusEditTable>
            </el-form-item>
          </el-form>
          <el-divider content-position="left">报表列配置</el-divider>
          <CusReport ref="designTable" v-if="reload" :columns="dataForm.COLUMN_INFOS"
                     :show-row-num="dataForm.SHOW_ROW_NUM"
                     @headerClick="headerClick" @headerDragend="headerDragend"></CusReport>
          <el-form :model="colPr" ref="fm1" :rules="dataFormRules1" label-position="right" size="small">
            <el-row>
              <el-col :span="6">
                <el-form-item label="列名" prop="label" label-width="80px">
                  <el-input v-model="colPr.label" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="数据项" prop="prop" label-width="80px">
                  <el-select v-model="colPr.prop" filterable allow-create clearable>
                    <el-option
                      v-for="item in datasourceCols"
                      :key="item.VAL"
                      :label="item.LAB"
                      :value="item.VAL"/>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="列宽" prop="width" label-width="80px">
                  <el-input-number v-model="colPr.width" :min="40"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="对齐方式" prop="align" label-width="80px">
                  <el-select v-model="colPr.align" clearable>
                    <el-option label="左对齐" value="left"/>
                    <el-option label="居中" value="center"/>
                    <el-option label="右对齐" value="right"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="4">
                <el-form-item label="可排序" prop="sortable" label-width="80px">
                  <el-switch v-model="colPr.sortable"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="可见" prop="visible" label-width="80px">
                  <el-switch v-model="colPr.visible"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="可筛选" prop="filter" label-width="80px">
                  <el-switch v-model="colPr.filter"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="合计" prop="sum" label-width="80px">
                  <el-switch v-model="colPr.sum"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             active-text="是"
                             inactive-text="否"
                             active-value="true"
                             inactive-value="false"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="备注" prop="content" label-width="80px">
                  <el-input v-model="colPr.content" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
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
        data() {
            return {
                filters: {
                    'REPORT_NAME,%%': ''
                },
                columns: [
                    {prop: "REPORT_NAME", label: "报表名称", minWidth: 40, sortable: true, visible: true},
                    {prop: "DATA_SOURCE", label: "数据源", minWidth: 40, sortable: true, visible: true},
                    {prop: "SHOW_ROW_NUM", label: "显示行号", minWidth: 40, sortable: false, visible: true},
                    {prop: "SHOW_PAGINATION", label: "是否分页", minWidth: 40, sortable: true, visible: true},
                    {prop: "SHOW_SUM", label: "显示合计行", minWidth: 40, sortable: true, visible: true}
                ],
                pageResult: {},
                datasourceCols: [],
                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                editLoading: false,
                editTableColumns1: [
                    {
                        prop: "SEARCH_FIELD",
                        label: "查询字段",
                        width: 150,
                        visible: true,
                        editAttr: 'el-select',
                        valueFlag: 'VAL',
                        labelFlag: 'LAB',
                        staticOptions: []
                    },
                    {
                        prop: "SEARCH_NAME",
                        label: "查询名称",
                        width: 150,
                        visible: true,
                        editAttr: 'el-input'
                    },
                    {
                        prop: "SEARCH_COMPO",
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
                        prop: "SEARCH_CONDITION",
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
                        prop: "DATA_SOURCE",
                        label: "选择器数据源",
                        width: 250,
                        visible: true,
                        editAttr: 'el-input'
                    }
                ],
                editTableAddModel1: {
                    "SEARCH_FIELD": "",
                    "SEARCH_NAME": "",
                    "SEARCH_COMPO": "",
                    "SEARCH_CONDITION": "",
                    "DATA_SOURCE": ""
                },
                reload: true,
                dataFormRules: {
                    REPORT_ID: [
                        {required: true, message: '请输入报表Id', trigger: 'blur'}
                    ],
                    REPORT_NAME: [
                        {required: true, message: '请输入报表名称', trigger: 'blur'}
                    ],
                    DATA_SOURCE: [
                        {required: true, message: '请输入报表数据源', trigger: ['blur']}
                    ],
                    size: [
                        {required: true, message: '请选择报表尺寸', trigger: ['blur']}
                    ]
                },
                dataFormRules1: {
                    prop: [
                        {required: true, message: '请选择列的数据项', trigger: 'blur'}
                    ],
                    label: [
                        {required: true, message: '请输入列名', trigger: 'blur'}
                    ],
                    align: [
                        {required: true, message: '请选择列的对齐方式', trigger: 'blur'}
                    ]
                },
                // 新增编辑界面数据
                dataForm: {
                    REPORT_ID: '',
                    REPORT_NAME: '',
                    DATA_SOURCE: '',
                    MAX_HEIGHT: '',
                    size: 'small',
                    SHOW_ROW_NUM: 'true',
                    SHOW_PAGINATION: 'true',
                    SHOW_SUM: 'false',
                    NEED_YEAR: 'Y',
                    COLUMN_INFOS: [{
                        cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                        prop: "",
                        label: "列名",
                        width: 80,
                        sortable: "true",
                        visible: "true",
                        filter: "false",
                        sum: "false",
                        align: "center",
                        content: ""
                    }],
                    searchInfos:
                        []
                },
                treeProp: {
                    id: 'COLUMN_ID',
                    label: 'COLUMN_NAME'
                },
                colPr: {
                    cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                    prop: "",
                    label: "",
                    width: 80,
                    sortable: "true",
                    visible: "true",
                    filter: "false",
                    sum: "false",
                    align: "center",
                    content: ""
                }
            }
        },
        methods: {
            // 获取数据
            search: function () {
                this.$refs.custable.pageNum = 1
                this.$refs.custable.sortProp = ''
                this.$refs.custable.sortOrder = ''
                this.$api.common.findPage({
                    'needYear': 'N',
                    'searchProperties': JSON.stringify(this.filters),
                    'tableName': this.$refs.custable.datasource,
                    'pageNum': 1,
                    'pageSize': this.$refs.custable.pageSize,
                    'sortProp': '',
                    'sortOrder': ''
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
            },
            // 表格查询回调实现
            findPage: function (param) {
                this.$api.common.findPage({
                    'needYear': 'N',
                    'searchProperties': JSON.stringify(this.filters),
                    'tableName': param.datasource,
                    'pageNum': param.pageNum,
                    'pageSize': param.pageSize,
                    'sortProp': param.sortProp,
                    'sortOrder': param.sortOrder
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
            },
            // 新增、编辑
            editSubmit: function () {
                this.$refs.fm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true
                            let params = Object.assign({}, this.dataForm)
                            if (this.operation) {
                                this.$api.report.save({
                                    'datas': JSON.stringify(params)
                                }).then((res) => {
                                    if (res.success) {
                                        this.editLoading = false;
                                        this.editDialogVisible = false;
                                        this.$refs.custable.reloadData();
                                        this.$message({message: res.message, type: 'success'});
                                        this.$refs['fm'].resetFields();
                                    } else {
                                        this.editLoading = false
                                        this.$message({message: res.message, type: 'error'})
                                        this.$refs['fm'].resetFields()
                                    }
                                })
                            } else {
                                this.$api.report.update({'datas': JSON.stringify(params)}).then((res) => {
                                    if (res.success) {
                                        this.editLoading = false
                                        this.editDialogVisible = false
                                        this.$refs.custable.reloadData();
                                        this.$message({message: res.message, type: 'success'})
                                        this.$refs['fm'].resetFields()
                                    } else {
                                        this.editLoading = false
                                        this.$message({message: res.message, type: 'error'})
                                        this.$refs['fm'].resetFields()
                                    }
                                })
                            }
                        })
                    }
                })
            },
            // 批量删除
            handleDelete: function (data) {
                this.$api.report.delet(data.params).then(data.callback)
            },
            // 显示新增界面
            handleAdd: function () {
                this.reload = false
                this.editDialogVisible = true
                this.operation = true
                this.dataForm = {
                    REPORT_ID: '',
                    REPORT_NAME: '',
                    DATA_SOURCE: '',
                    MAX_HEIGHT: '',
                    size: 'small',
                    SHOW_ROW_NUM: 'true',
                    SHOW_PAGINATION: 'true',
                    SHOW_SUM: 'false',
                    NEED_YEAR: 'Y',
                    COLUMN_INFOS: [{
                        cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                        prop: "",
                        label: "列名",
                        width: 80,
                        sortable: "true",
                        visible: "true",
                        filter: "false",
                        sum: "false",
                        align: "center",
                        content: ""
                    }],
                    searchInfos: []
                },
                    this.colPr = {
                        cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                        prop: "",
                        label: "",
                        width: 80,
                        sortable: "true",
                        visible: "true",
                        filter: "false",
                        sum: "false",
                        align: "center",
                        content: ""
                    }
                this.$nextTick(() => {
                    this.reload = true
                })
                this.$refs.fm.clearValidate()
            },
            // 显示编辑界面
            handleEdit: function (params) {
                this.dataForm = Object.assign({}, params.row)
                this.dataForm.COLUMN_INFOS = JSON.parse(this.dataForm.COLUMN_INFOS)
                this.$api.report.getSearchProp({
                    'reportId': this.dataForm.REPORT_ID
                }).then((res) => {
                    if (res.success) {
                        this.reload = false
                        this.dataForm.searchInfos = JSON.parse(res.message)
                        this.colPr = {
                            cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                            prop: "",
                            label: "",
                            width: 80,
                            sortable: "true",
                            visible: "true",
                            filter: "false",
                            sum: "false",
                            align: "center",
                            content: ""
                        }
                        this.editDialogVisible = true
                        this.operation = false
                        this.$nextTick(() => {
                            this.reload = true
                        })
                    } else {
                        this.$message({message: res.message, type: 'error'})
                    }
                    this.$refs.fm.clearValidate()
                })
            },
            headerClick: function (columnProp) {
                this.findColumns(this.dataForm.COLUMN_INFOS, columnProp)
            },
            headerDragend: function (parms) {
                this.setWidth(this.dataForm.COLUMN_INFOS, parms.cid, parms.wid)
            },
            findColumns(obj, j) {
                obj.forEach((item, index, arr) => {
                    if (item.cid == j) {
                        this.colPr = item;
                    } else if (item.children) {
                        this.findColumns(item.children, j)
                    }
                })
            },
            setWidth(obj, prop, wid) {
                obj.forEach((item, index, arr) => {
                    if (item.cid == prop) {
                        item.width = wid;
                    } else if (item.children) {
                        this.setWidth(item.children, prop, wid)
                    }
                })
            }
        },
        computed: {
            DATA_SOURCE() {
                return this.dataForm.DATA_SOURCE;
            }
        },
        watch: {
            DATA_SOURCE(parms) {
                this.$api.common.getTableCols({
                    'tableName': parms
                }).then((res) => {
                    if (res.success) {
                        this.datasourceCols = JSON.parse(res.message);
                        this.editTableColumns1[0].staticOptions = JSON.parse(res.message);
                    } else {
                        this.$message({message: res.message, type: 'error'})
                    }
                })
            }
        }
    }
</script>
