<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item label="消息标题">
          <el-input v-model='filters["NOTICE_TITLE,%%"]' clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" v-on:click="search" plain>查询</el-button>
        </el-form-item>
        <el-form-item>
          <CusButton perms="notice:send:new" type="primary" @click="handleAdd"/>
        </el-form-item>
        <el-form-item>
          <CusButton perms="notice:send:target" type="primary" @click="handleAddTarget"/>
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <CusTable ref="custable" permsEdit="notice:send:edit" edit-tip="查看"
              permsExport="notice:send:export"
              :showOperation=true :showCheckBox=false
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns" datasource="BS_NOTICES"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
      <template v-slot:NOTICE_LEVEL="slotProps">
        <span v-if="slotProps.colData == '0'" style="color: green">
          <i class="icon iconfont iconic_userlevel_">一般</i>
        </span>
        <span v-else-if="slotProps.colData == '1'" style="color: orange">
          <i class="icon iconfont iconic_userlevel_1">中等</i>
        </span>
        <span v-else="slotProps.colData == '2'" style="color: red">
          <i class="icon iconfont iconic_userlevel_2">特急</i>
        </span>
      </template>
    </CusTable>
    <!--新增编辑界面-->
    <el-dialog :title="operation?'新增':'查看'" width="45%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <div style="height: 60vh; margin-right: 10px">
        <el-scrollbar style="height: 100%;">
          <el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="fm">
            <el-row>
              <el-col :span="12">
                <el-form-item label="消息编号" prop="NOTICE_ID">
                  <el-input v-model="dataForm.NOTICE_ID" disabled auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="紧急程度" prop="NOTICE_LEVEL">
                  <el-select v-model="dataForm.NOTICE_LEVEL" :disabled="!operation" clearable placeholder="请选择紧急程度">
                    <el-option value="0" label="一般">
                      <span style="float: left;color: green">
                        <i class="icon iconfont iconic_userlevel_">     一般</i>
                      </span>
                    </el-option>
                    <el-option value="1" label="中等">
                      <span style="float: left;color: orange">
                        <i class="icon iconfont iconic_userlevel_1">     中等</i>
                      </span>
                    </el-option>
                    <el-option value="2" label="特急">
                      <span style="float: left;color: red">
                        <i class="icon iconfont iconic_userlevel_2">     特急</i>
                      </span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="消息标题" prop="NOTICE_TITLE">
              <el-input v-model="dataForm.NOTICE_TITLE" :disabled="!operation" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="消息内容" prop="NOTICE_CONTENT">
              <el-input type="textarea" :rows="8" v-model="dataForm.NOTICE_CONTENT" :disabled="!operation"
                        auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="消息附件">
              <el-upload
                action=""
                class="upload-demo"
                :multiple="true"
                drag
                :limit="3"
                :auto-upload="false"
                :file-list="noticeFile"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :on-exceed="handleExceed"
                :before-upload="beforeUpload"
                :before-remove="beforeRemove"
                :on-change="handleChange" :disabled="!operation">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              </el-upload>
            </el-form-item>
          </el-form>
          <el-transfer filterable :titles="['所有用户', '已选联系人']" :disabled="!operation" v-model="selectUser"
                       :data="allUser"></el-transfer>
        </el-scrollbar>
      </div>
      <div slot="footer" class="dialog-footer" v-if="operation">
        <el-button @click.native="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>

    <!--新增编辑界面-->
    <el-dialog title="常用联系人" width="45%" :visible.sync="targetVisible" :close-on-click-modal="false" append-to-body>
      <div style="height: 60vh; margin-right: 10px">
        <el-transfer filterable :titles="['所有用户', '常用联系人']" v-model="frequentContacts" :data="allUser"></el-transfer>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="targetVisible = false">取消</el-button>
        <el-button type="primary" @click.native="targetSubmit" :loading="targetLoading">完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

    export default {
        data() {
            return {
                filters: {
                    'NOTICE_TITLE,%%': ''
                },
                columns: [
                    {prop: "NOTICE_ID", label: "消息编号", width: 150, sortable: true, visible: true},
                    {prop: "NOTICE_TITLE", label: "消息标题", width: 300, sortable: true, visible: true},
                    {prop: "NOTICE_CONTENT", label: "消息内容", width: 400, sortable: false, visible: true},
                    {prop: "NOTICE_LEVEL", label: "紧急程度", width: 110, sortable: true, visible: true, filter: true},
                    {prop: "CREATE_DATE", label: "创建时间", width: 150, sortable: true, visible: true}
                ],
                pageResult: {},
                operation: false, // true:新增, false:编辑
                editDialogVisible: false, // 新增编辑界面是否显示
                editLoading: false,
                targetVisible: false, // 新增编辑界面是否显示
                targetLoading: false,
                reload: true,
                dataFormRules: {
                    NOTICE_ID: [
                        {required: true, message: '请输入报表Id', trigger: 'blur'}
                    ],
                    NOTICE_TITLE: [
                        {required: true, message: '请输入消息标题', trigger: 'blur'}
                    ],
                    NOTICE_CONTENT: [
                        {required: true, message: '请输入消息内容', trigger: 'blur'}
                    ],
                    NOTICE_LEVEL: [
                        {required: true, message: '请选择紧急程度', trigger: 'blur'}
                    ]
                },
                // 新增编辑界面数据
                dataForm: {
                    NOTICE_ID: '',
                    NOTICE_TITLE: '',
                    NOTICE_CONTENT: '',
                    NOTICE_FILE: '',
                    NOTICE_LEVEL: ''
                },
                noticeFile: [],
                selectUser: [],
                allUser: [],
                frequentContacts: []
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
                        if (this.selectUser.length == 0) {
                            this.$message({message: '请选择需要发送的对象！', type: 'warning'})
                            return;
                        }
                        this.$confirm('确认发送吗？', '提示', {}).then(() => {
                            this.editLoading = true
                            let params = Object.assign({}, this.dataForm)
                            params.userCode = JSON.parse(sessionStorage.getItem('user')).userCode;
                            params.userName = JSON.parse(sessionStorage.getItem('user')).userName;
                            params.selectUser = JSON.stringify(this.selectUser)
                            console.log(params)
                            this.$api.notice.save({
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
                                }
                            })
                        })
                    }
                })
            },
            // 批量删除
            handleDelete: function (data) {
                this.$api.common.delet({data: data.params, }).then(data.callback)
            },
            // 显示新增界面
            handleAdd: function () {
                this.editDialogVisible = true
                this.operation = true
                this.dataForm = {
                    NOTICE_ID: 'NTC' + JSON.parse(sessionStorage.getItem('user')).userCode + parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                    NOTICE_TITLE: '',
                    NOTICE_CONTENT: '',
                    NOTICE_FILE: '',
                    NOTICE_LEVEL: ''
                };
                this.noticeFile = [];
                this.initContactors()
                this.$refs.fm.clearValidate()
            },
            // 显示编辑界面
            handleEdit: function (params) {
                this.noticeFile = [];
                this.dataForm = Object.assign({}, params.row)
                if (this.dataForm.NOTICE_FILE && this.dataForm.NOTICE_FILE.lastIndexOf("/") > 0) {
                    let allF = this.dataForm.NOTICE_FILE.split(",");
                    for(let oFile in allF){
                        if(allF[oFile].length > 0){
                            this.noticeFile.push({
                                name: allF[oFile].substring(allF[oFile].lastIndexOf("/") + 1),
                                url: location.protocol + '//' + location.host + allF[oFile]
                            })
                        }
                    }
                } else {
                    this.noticeFile = []
                }
                this.operation = false
                this.$api.notice.getAllUsers({}).then((res) => {
                    this.allUser = JSON.parse(res.message)
                    this.allUser.forEach(i => {
                        i.disabled = true
                    })
                })
                this.$api.notice.getNoticeUsers({'noticeId': this.dataForm.NOTICE_ID}).then((res) => {
                    if (res.success) {
                        this.selectUser = JSON.parse(res.message)
                    }
                })
                this.editDialogVisible = true
                this.$refs.fm.clearValidate()
            },
            handleChange(file, fileList) {
                if(fileList.length > 0){
                    let finalFile = []
                    for(let afile in fileList){
                        let reader = new FileReader()
                        reader.readAsDataURL(fileList[afile].raw)
                        reader.onload = () => {
                            finalFile.push(JSON.stringify({name: fileList[afile].name, data: reader.result + ""}))
                            this.dataForm.NOTICE_FILE = JSON.stringify(finalFile)
                        }
                    }
                }
            },
            beforeUpload(file) {
                const isLtM = file.size / 1024 / 1024 < 10;
                if (!isLtM) {
                    file = {}
                    this.$message.error('上传的文件大小不能超过 10MB!');
                }
                return isLtM;
            },
            handleRemove(file, fileList) {
                this.dataForm.NOTICE_FILE = ''
            },
            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },
            beforeRemove(file, fileList) {
                return this.$confirm(`确定移除 ${file.name}?`);
            },
            handlePreview(file) {
                if (file.url) {
                    window.location.href = file.url
                }
            },
            initContactors() {
                this.$api.notice.getAllUsers({}).then((res) => {
                    this.allUser = JSON.parse(res.message)
                    this.allUser.forEach(i => {
                        if (i.key == JSON.parse(sessionStorage.getItem('user')).userCode) {
                            i.disabled = true
                        } else {
                            i.disabled = false
                        }
                    })
                })
                this.$api.notice.getSelectUsers({
                    "userCode": JSON.parse(sessionStorage.getItem('user')).userCode
                }).then((res) => {
                    this.frequentContacts = JSON.parse(res.message);
                    if (!this.frequentContacts.includes(JSON.parse(sessionStorage.getItem('user')).userCode)) {
                        this.frequentContacts.push(JSON.parse(sessionStorage.getItem('user')).userCode)
                    }
                })
            },
            handleAddTarget() {
                this.targetVisible = true
                this.initContactors()
            },
            targetSubmit() {
                this.targetLoading = true
                this.$api.notice.saveFrequentContacts({
                    'userCodes': JSON.stringify(this.frequentContacts),
                    'userCode': JSON.parse(sessionStorage.getItem('user')).userCode
                }).then((res) => {
                    if (res.success) {
                        this.targetLoading = false;
                        this.targetVisible = false;
                        this.$message({message: res.message, type: 'success'});
                    } else {
                        this.targetLoading = false
                        this.$message({message: res.message, type: 'error'})
                    }
                })
            }
        },
        mounted() {
        },
        watch: {
            frequentContacts: {
                handler(val) {
                    this.selectUser = JSON.parse(JSON.stringify(val));
                }, deep: true
            }
        }
    }
</script>
