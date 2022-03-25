<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-dropdown @command="handleCommand">
        <CusButton perms="sys:menu:add" type="primary">
          <template v-slot="slotProps">
            {{slotProps.label}}<i class="el-icon-arrow-down el-icon--right"></i>
          </template>
        </CusButton>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-for="item in compoTypeOptions"
                            :command="item.value"
                            :key="item.value">{{item.label}}
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!--表格内容栏-->
    <CusTable ref="custable" permsEdit="sys:menu:edit" permsDelete="sys:menu:delete" :showOperation=true
              permsExport="sys:menu:export" :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              :show-pagination=false
              rowKey="compoName" :show-row-num=false
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" v-if="reload">
      <template v-slot:icon="slotProps">
        <i :class="slotProps.colData"></i>
      </template>
      <template v-slot:compoType="slotProps">
        <el-tag v-if="slotProps.colData == 'content'" size="small">目录</el-tag>
        <el-tag v-else-if="slotProps.colData == 'menu'" size="small" type="success">菜单</el-tag>
        <el-tag v-else="slotProps.colData == 'button'" size="small" type="info">按钮</el-tag>
      </template>
    </CusTable>
    <!--新增编辑界面-->
    <el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="fm">
        <el-form-item label="部件编号" prop="compoId">
          <el-input v-model="dataForm.compoId" :disabled="!operation" auto-complete="off"
                    placeholder="推荐规则:目录英文名_菜单英文名_按钮英文名，保证不重复"/>
        </el-form-item>
        <el-form-item label="部件名称" prop="compoName">
          <el-input v-model="dataForm.compoName" auto-complete="off"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="部件类型" prop="compoType">
              <el-radio-group v-model="dataForm.compoType" size="small">
                <el-radio-button v-for="item in compoTypeOptions"
                                 :key="item.value"
                                 :label="item.value">{{item.label}}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="图标" prop="icon">
              <IconSelecter ref="icons" v-model="dataForm.icon"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="菜单URL" prop="url" v-if="dataForm.compoType == 'menu'">
          <el-input v-model="dataForm.url" auto-complete="off"
                    placeholder='必填！动态配置的业务，需要传参，格式：url${"param1":"value"}'/>
        </el-form-item>
        <el-form-item label="授权标识" prop="compo" v-if="dataForm.compoType == 'button'">
          <el-input v-model="dataForm.compo" auto-complete="off"
                    placeholder="必填！"/>
        </el-form-item>
        <el-form-item label="父级部件" prop="parentId">
          <PopupTreeInput
            :data="parentCompoOptions"
            v-model="dataForm.parentId">
          </PopupTreeInput>
        </el-form-item>
        <el-form-item label="部件级次" prop="orderNum">
          <el-input-number v-model="dataForm.orderNum" controls-position="right" :min="1"
                           :disabled="dataForm.compoType == 'button'"/>
        </el-form-item>
        <el-form-item label="部件信息">
          <el-input type="textarea" v-model="dataForm.describe"/>
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
                compoTypeOptions: [{
                    value: 'content',
                    label: '目录'
                }, {
                    value: 'menu',
                    label: '菜单'
                }, {
                    value: 'button',
                    label: '按钮'
                }],
                columns: [
                    {prop: "compoId", label: "部件id", minWidth: 10, visible: false},
                    {prop: "compoName", label: "部件名称", width: 200, visible: true, align: 'left'},
                    {prop: "compoType", label: "部件类型", width: 100, visible: true},
                    {prop: "icon", label: "图标", visible: true, width: 50},
                    {prop: "parentId", label: "父级部件", minWidth: 40, visible: false},
                    {prop: "parentName", label: "父级部件", width: 200, visible: true},
                    {prop: "describe", label: "部件信息", width: 200, visible: true},
                    {prop: "url", label: "菜单URL", width: 100, visible: true},
                    {prop: "compo", label: "授权标识", width: 100, visible: true, content: '按钮权限控制标志'},
                    {prop: "orderNum", label: "部件级次", width: 100, content: '在父部件内部的显示顺序', visible: true}
                ],
                pageResult: {},
                parentCompoOptions: [],
                reload: true,
                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                editLoading: false,
                dataFormRules: {
                    compoId: [
                        {required: true, message: '请输入部件编号', trigger: 'blur'}
                    ],
                    compoName: [
                        {required: true, message: '请输入部件名称', trigger: 'blur'}
                    ],
                    compoType: [
                        {required: true, message: '请选择部件类型', trigger: ['change', 'blur']}
                    ],
                    url: [
                        {required: true, message: '请输入菜单URL', trigger: ['change', 'blur']}
                    ],
                    compo: [
                        {required: true, message: '请输入授权标识', trigger: ['change', 'blur']}
                    ]
                },
                // 新增编辑界面数据
                dataForm: {
                    compoId: '',
                    compoName: '',
                    compoType: '',
                    icon: '',
                    parentId: '',
                    describe: '',
                    url: '',
                    compo: '',
                    orderNum: ''
                },
                tableLoading: false
            }
        },
        methods: {
            // 获取分页数据
            findPage: function (param) {
                this.tableLoading = true
                this.$api.menu.getCompos({
                    'compoName': '',
                    'pageNum': param.pageNum,
                    'pageSize': param.pageSize,
                    'sortProp': param.sortProp,
                    'sortOrder': param.sortOrder
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                    this.tableLoading = false
                })
            },
            initParentCompo: function () {
                this.$api.menu.getParentCompos({}).then((res) => {
                    this.parentCompoOptions = JSON.parse(res.message)
                })
            },
            // 显示新增界面
            handleCommand: function (command) {
                this.editDialogVisible = true
                this.operation = true
                this.dataForm = {
                    compoId: '',
                    compoName: '',
                    compoType: command,
                    icon: '',
                    parentId: '',
                    describe: '',
                    url: '',
                    compo: '',
                    orderNum: ''
                }
                this.initParentCompo();
                this.$refs.fm.clearValidate()
            },
            // 新增、编辑
            editSubmit: function () {
                this.$refs.fm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true
                            let params = Object.assign({}, this.dataForm)
                            if (this.operation) {
                                this.$api.menu.save(params).then((res) => {
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
                                this.$api.menu.update(params).then((res) => {
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
                this.$api.menu.delet(data.params).then(data.callback)
            },
            // 显示编辑界面
            handleEdit: function (params) {
                this.editDialogVisible = true
                this.operation = false
                this.initParentCompo();
                this.dataForm = Object.assign({}, params.row)
                this.$refs.fm.clearValidate()
            }
        }
    }
</script>
