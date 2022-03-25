<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-form :inline="true" :model="filters" size="small">
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
          <el-button type="primary" icon="el-icon-search" v-on:click="searchRole" plain>查询</el-button>
        </el-form-item>
        <el-form-item>
          <CusButton perms="base:role:new" type="primary" @click="handleAdd"/>
        </el-form-item>
        <el-form-item>
          <CusButton perms="base:role:permission" type="primary" @click="handlePermission"/>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <CusTable ref="custable" permsEdit="base:role:edit" permsDelete="base:role:delete" permsExport="base:role:export"
              :showOperation=true
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    </CusTable>
    <!--新增编辑界面-->
    <el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="fm">
        <el-form-item label="角色编号" prop="roleCode">
          <el-input v-model="dataForm.roleCode" :disabled="!operation" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="dataForm.roleName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="可用" prop="used">
          <el-switch
            v-model="dataForm.used"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="是"
            inactive-text="否"
            active-value="Y"
            inactive-value="N">
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>

    <!-- 部件权限弹出窗口 -->
    <el-drawer
      title="部件权限"
      :visible.sync="permissionDialogVisible"
      direction="rtl"
      size="30%"
      :before-close="handleClose"
      ref="drawer" append-to-body>
      <div style="height: calc(80vh); padding-left:5px;padding-right:5px;">
        <el-scrollbar style="height: 100%;">
          <el-input
            placeholder="输入关键字进行过滤"
            v-model="filterText">
          </el-input>
          <el-tree
            ref="permissionTree"
            class="filter-tree"
            :data="allPermissionData"
            node-key="id"
            :check-strictly="true"
            show-checkbox
            :expand-on-click-node=true
            :filter-node-method="filterNode"
            default-expand-all
            @check="checkNodeHandle">
          </el-tree>
        </el-scrollbar>
      </div>
      <div style="float:right;padding:10px;">
        <el-button @click="permissionCancel">取 消</el-button>
        <el-button type="primary" @click="$refs.drawer.closeDrawer()" :loading="loading">{{ loading ? '提交中 ...' : '确定'
          }}
        </el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>

    export default {
        data() {
            return {
                filterText: '',
                loading: false,
                filters: {
                    roleCode: ''
                },
                columns: [
                    {prop: "roleCode", label: "角色编号", minWidth: 40, sortable: true, visible: true},
                    {prop: "roleName", label: "角色名称", minWidth: 80, visible: true, sortable: true},
                    {prop: "used", label: "是否使用", minWidth: 30, sortable: true, visible: true}
                ],
                pageResult: {},

                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                permissionDialogVisible: false, // 部件权限界面是否显示
                editLoading: false,
                dataFormRules: {
                    roleName: [
                        {required: true, message: '请输入角色名称', trigger: ['blur']}
                    ],
                    roleCode: [
                        {required: true, message: '请输入角色编号', trigger: ['blur']}
                    ]
                },
                // 新增编辑界面数据
                dataForm: {
                    roleCode: '',
                    roleName: '',
                    used: 'Y'
                },
                roleData: [],
                allPermissionData: [],
                selectPermissionData: [],
                selectRow: []
            }
        },
        methods: {
            // 获取数据
            searchRole: function () {
                this.$refs.custable.pageNum = 1
                this.$refs.custable.sortProp = ''
                this.$refs.custable.sortOrder = ''
                this.$api.role.findPage({
                    'roleCode': this.filters.roleCode,
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
                this.$api.role.findPage({
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
                                this.$api.role.save(params).then((res) => {
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
                            } else {
                                this.$api.role.update(params).then((res) => {
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
                this.$api.role.delet(data.params).then(data.callback)
            },
            // 显示新增界面
            handleAdd: function () {
                this.editDialogVisible = true
                this.operation = true
                this.dataForm = {
                    roleCode: '',
                    roleName: '',
                    used: "Y"
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
            // 显示权限赋值界面
            handlePermission: function (params) {
                this.selectRow = this.$refs.custable.selections
                if (this.selectRow.length == 1) {
                    this.$api.menu.getComposByRole({'roleCode': this.selectRow[0].roleCode}).then((res) => {
                        this.$refs.permissionTree.setCheckedKeys(JSON.parse(res.message))
                    })
                    this.permissionDialogVisible = true
                } else {
                    this.$message({message: '请选择一个角色', type: 'warning'})
                }
            },
            // 获取角色列表
            getRoles: function () {
                this.$api.role.getRoles({}).then((res) => {
                    this.roleData = JSON.parse(res.message)
                })
            },
            getAllCompos: function () {
                this.$api.menu.getAllCompos({}).then((res) => {
                    this.allPermissionData = JSON.parse(res.message)
                })
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            handleClose: function () {
                if (this.loading) {
                    return;
                }
                this.$confirm('确定要提交表单吗？')
                    .then(_ => {
                        this.loading = true;
                        this.$api.role.setRolePermissions({
                            'permission': JSON.stringify(this.$refs.permissionTree.getCheckedKeys()),
                            'roleCode': this.selectRow[0].roleCode
                        }).then((res) => {
                            if (res.success) {
                                this.loading = false
                                this.permissionDialogVisible = false
                                this.$message({message: res.message, type: 'success'})
                            } else {
                                this.loading = false
                                this.$message({message: res.message, type: 'error'})
                            }
                        })
                    })
                    .catch(_ => {
                        this.permissionDialogVisible = false
                    });
            },
            permissionCancel: function () {
                this.loading = false;
                this.permissionDialogVisible = false;
            },
            checkNodeHandle: function (currentObj, treeStatus) {
                // 用于：父子节点严格互不关联时，父节点勾选变化时通知子节点同步变化，实现单向关联。
                let selected = treeStatus.checkedKeys.indexOf(currentObj.id) // -1未选中
                // 选中
                if (selected !== -1) {
                    // 子节点只要被选中父节点就被选中
                    this.selectedParent(currentObj)
                } else {
                    // 未选中 处理子节点全部未选中
                    if (currentObj.children.length !== 0) {
                        this.unSelectedChild(currentObj)
                    }
                }
            },
            // 统一处理子节点为未勾选状态
            unSelectedChild(treeList) {
                this.$refs.permissionTree.setChecked(treeList.id, false)
                for (let i = 0; i < treeList.children.length; i++) {
                    this.unSelectedChild(treeList.children[i], false)
                }
            },
            // 统一处理父节点为选中
            selectedParent(currentObj) {
                let currentNode = this.$refs.permissionTree.getNode(currentObj)
                if (currentNode.parent.key !== undefined) {
                    this.$refs.permissionTree.setChecked(currentNode.parent, true)
                    this.selectedParent(currentNode.parent)
                }
            }
        },
        mounted() {
            this.getRoles()
            this.getAllCompos()
        },
        watch: {
            filterText(val) {
                this.$refs.permissionTree.filter(val);
            }
        }
    }
</script>
