<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item label="表单名称">
          <el-input v-model="filters.formName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-on:click="searchForm" plain>查询</el-button>
        </el-form-item>
        <el-form-item>
          <CusButton perms="sys:form:new" type="primary" @click="handleAdd"/>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <CusTable ref="custable" permsEdit="sys:form:edit" permsDelete="sys:form:delete" permsExport="sys:form:export"
              :showOperation=true
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    </CusTable>

    <el-dialog :title="operation?'新增':'编辑'" width="98%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <FormDesign v-if="editDialogVisible" :formTemplet="formTemplet" :isNew="operation"></FormDesign>
    </el-dialog>
  </div>
</template>

<script>
    import FormDesign from "../../form/FormDesign.vue";

    export default {
        components: {
            FormDesign
        },
        data() {
            return {
                active: 0,
                filters: {
                    formName: ''
                },
                columns: [
                    {prop: "formId", label: "表单编号", minWidth: 40, sortable: true, visible: true},
                    {prop: "formName", label: "表单名称", minWidth: 80, visible: true, sortable: true}
                ],
                pageResult: {},

                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                editLoading: false,
                formTemplet: '{"list": [], "formId": "", "formName": "", "labelPosition": "right", "size": "medium"}'
            }
        },
        methods: {
            // 获取数据
            searchForm: function () {
                this.$refs.custable.pageNum = 1
                this.$refs.custable.sortProp = ''
                this.$refs.custable.sortOrder = ''
                this.$api.form.findFormTemplet({
                    'formName': this.filters.formName,
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
                this.$api.form.findFormTemplet({
                    'formName': this.filters.formName,
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
                this.$api.form.deleteFormTempLet(data.params).then(data.callback)
            },
            // 显示新增界面
            handleAdd: function () {
                this.editDialogVisible = true
                this.operation = true
            },
            // 显示编辑界面
            handleEdit: function (params) {
                this.formTemplet = params.row.form
                this.editDialogVisible = true
                this.operation = false
            }
        },
        mounted() {
        },
        watch: {
            editDialogVisible: {
                handler(val) {
                    if(!val){
                        this.findPage();
                        this.formTemplet = '{"list": [], "formId": "", "formName": "", "labelPosition": "right", "size": "medium"}';
                    }
                }
            }
        }
    }
</script>
