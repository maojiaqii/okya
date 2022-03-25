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
        <el-form-item label="主表名称">
          <el-input v-model="filters.tableName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-on:click="searchForm" plain>查询</el-button>
        </el-form-item>
        <el-form-item>
          <CusButton perms="sys:formDataSource:new" type="primary" @click="handleAdd"/>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <CusTable ref="custable" permsEdit="sys:formDataSource:edit" permsDelete="sys:formDataSource:delete"
              :showOperation=true :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    </CusTable>

    <el-dialog :title="operation?'新增':'编辑'" width="98%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <FormDBDesign v-if="editDialogVisible" :editForm="editForm" :isNew="operation"></FormDBDesign>
    </el-dialog>
  </div>
</template>

<script>
    import FormDBDesign from "../../form/FormDBDesign.vue";

    export default {
        components: {
            FormDBDesign
        },
        data() {
            return {
                formsSelector: [],
                filters: {
                    formId: '',
                    tableName: ''
                },
                columns: [
                    {prop: "FORM_ID", label: "表单编号", minWidth: 40, sortable: true, visible: true},
                    {prop: "TABLE_NAME", label: "主表名称", minWidth: 80, visible: true, sortable: true},
                    {prop: "HAS_SET_YEAR", label: "是否关联业务年度", minWidth: 80, visible: true, sortable: true}
                ],
                pageResult: {},

                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                editLoading: false,
                editForm: {}
            }
        },
        methods: {
            initFormsSelector: function () {
                this.$api.form.getFormTemplet({}).then((res) => {
                    this.formsSelector = JSON.parse(res.message)
                })
            },
            // 获取数据
            searchForm: function () {
                this.$refs.custable.pageNum = 1
                this.$refs.custable.sortProp = ''
                this.$refs.custable.sortOrder = ''
                this.$api.form.findFormDataSource({
                    'formId': this.filters.formId,
                    'tableName': this.filters.tableName,
                    'pageNum': 1,
                    'pageSize': this.$refs.custable.pageSize,
                    'sortProp': '',
                    'sortOrder': ''
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
            },
            // 获取分页数据
            findPage: function (param) {
                this.$api.form.findFormDataSource({
                    'formId': this.filters.formId,
                    'tableName': this.filters.tableName,
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
                this.$confirm('如删除的表单已产生业务数据，删除将丢失所有业务数据，无法撤回，确定修改吗？', '危险操作', {}).then(() => {
                    this.$api.form.deleteFormDb(data.params).then(data.callback)
                }).catch((e) => {
                })
            },
            // 显示新增界面
            handleAdd: function () {
                this.editForm = {};
                this.editDialogVisible = true;
                this.operation = true;
            },
            // 显示编辑界面
            handleEdit: function (params) {
                this.editForm = this.formsSelector.find((item) => item.FORM_ID === params.row.FORM_ID);
                this.editDialogVisible = true;
                this.operation = false;
            }
        },
        mounted() {
            this.initFormsSelector();
        },
        watch: {
            editDialogVisible: {
                handler(val) {
                    if (!val) {
                        this.findPage();
                    }
                }
            }
        }
    }
</script>
