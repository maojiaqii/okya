<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item label="用户名">
          <el-input v-model="filters.userName" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="filters.roleCode" filterable clearable>
            <el-option
              v-for="item in roleData"
              :key="item.roleCode"
              :label="item.roleName"
              :value="item.roleCode"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-on:click="searchUser" plain>查询</el-button>
        </el-form-item>
        <el-form-item>
          <CusButton perms="base:user:new" type="primary" @click="handleAdd"/>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <CusTable ref="custable" permsEdit="base:user:edit" permsDelete="base:user:delete"
              permsExport="base:user:export"
              :showOperation=true
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    </CusTable>
    <!--新增编辑界面-->
    <el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="fm">
        <el-form-item label="用户编号" prop="userCode">
          <el-input v-model="dataForm.userCode" :disabled="!operation" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="dataForm.userName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleCode">
          <el-select v-model="dataForm.roleCode" filterable clearable placeholder="请选择角色">
            <el-option
              v-for="item in roleData"
              :key="item.roleCode"
              :label="item.roleName"
              :value="item.roleCode"/>
          </el-select>
        </el-form-item>
        <el-form-item label="单位" prop="coCode">
          <PopupTreeInput
            :data="coData"
            v-model="dataForm.coCode">
          </PopupTreeInput>
        </el-form-item>
        <el-form-item label="可用" prop="used">
          <el-switch
            v-model="dataForm.used"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="是"
            inactive-text="否"
            active-value="是"
            inactive-value="否">
          </el-switch>
        </el-form-item>
      </el-form>
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
                    userName: '',
                    roleCode: ''
                },
                columns: [
                    {
                        prop: "user", label: "用户", width: 160, visible: true, children: [
                            {
                                prop: "userCode",
                                label: "编号",
                                width: 100,
                                visible: true,
                                sortable: true,
                                content: "用户编号",
                                children: []
                            }, {
                                prop: "userName",
                                label: "名称",
                                width: 100,
                                visible: true,
                                sortable: true,
                                content: "用户名称",
                                children: []
                            }]
                    },
                    {
                        prop: "roleName",
                        label: "角色名称",
                        minWidth: 60,
                        sortable: true,
                        visible: true,
                        filter: true,
                        children: []
                    },
                    {
                        prop: "co", label: "单位", width: 160, visible: true, children: [
                            {
                                prop: "coCode",
                                label: "编号",
                                width: 100,
                                visible: true,
                                filter: true,
                                children: []
                            }, {
                                prop: "coName",
                                label: "名称",
                                width: 100,
                                visible: true,
                                sortable: true,
                                children: []
                            }]
                    },
                    {
                        prop: "used", label: "是否使用", minWidth: 30, sortable: true, visible: true,
                        children: []
                    }
                ],
                pageResult: {},

                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                editLoading: false,
                dataFormRules: {
                    userName: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    userCode: [
                        {required: true, message: '请输入用户编号', trigger: 'blur'}
                    ],
                    roleCode: [
                        {required: true, message: '请选择角色', trigger: ['blur']}
                    ],
                    coCode: [
                        {required: true, message: '请选择单位', trigger: ['blur']}
                    ]
                },
                // 新增编辑界面数据
                dataForm: {
                    userCode: '',
                    userName: '',
                    roleCode: '',
                    coCode: '',
                    used: '是'
                },
                roleData: [],
                coData: []
            }
        },
        methods: {
            // 获取数据
            searchUser: function () {
                this.$refs.custable.pageNum = 1
                this.$refs.custable.sortProp = ''
                this.$refs.custable.sortOrder = ''
                this.$api.user.findPage({
                    'userName': this.filters.userName,
                    'roleCode': this.filters.roleCode,
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
                this.$api.user.findPage({
                    'userName': this.filters.userName,
                    'roleCode': this.filters.roleCode,
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
                                this.$api.user.save(params).then((res) => {
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
                                this.$api.user.update(params).then((res) => {
                                    if (res.success) {
                                        this.editLoading = false
                                        this.editDialogVisible = false
                                        this.$refs.custable.reloadData();
                                        this.$message({message: res.message, type: 'success'})
                                        this.$refs['fm'].resetFields()
                                    } else {
                                        this.editLoading = false
                                        this.$message({message: res.message, type: 'error'})
                                        this.$refs.fm.resetFields()
                                    }
                                })
                            }
                        })
                    }
                })
            },
            // 批量删除
            handleDelete: function (data) {
                this.$api.user.delet(data.params).then(data.callback)
            },
            // 显示新增界面
            handleAdd: function () {
                this.editDialogVisible = true
                this.operation = true
                this.dataForm = {
                    userCode: '',
                    userName: '',
                    roleCode: '',
                    coCode: '',
                    used: "是"
                }
                this.$refs.fm.clearValidate()
            },
            // 显示编辑界面
            handleEdit: function (params) {
                this.editDialogVisible = true
                this.operation = false
                this.dataForm = Object.assign({}, params.row)
                this.$refs.fm.clearValidate()
            },
            // 获取角色列表
            getRoles: function () {
                this.$api.role.getRoles({}).then((res) => {
                    this.roleData = JSON.parse(res.message)
                })
            },
            // 获取单位列表
            getCos: function () {
                this.$api.company.getCoTree({}).then((res) => {
                    this.coData = JSON.parse(res.message)
                })
            }
        },
        mounted() {
            this.getRoles()
            this.getCos()
        }
    }
</script>

<style scoped>

</style>
