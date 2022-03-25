<template>
  <div class="container" :class="$store.state.app.collapse?'position-collapse-left':'position-left'"
       :style="'background :'+ $store.state.app.viewColor + ';'">
    <!-- 导航菜单隐藏显示切换 -->
    <span class="collapse-switcher" @click.prevent="collapse" :style="'line-height: 60px;background :'+ $store.state.app.viewColor + ';'">
      <i :class="$store.state.app.collapse?'icon iconfont iconmingxi' : 'icon iconfont iconsuolvetuxianshi'"></i>
    </span>
    <span class="tool-bar">
      <!-- 用户信息 -->
      <el-dropdown class="user-info-dropdown" trigger="hover">
        <span class="el-dropdown-link">
          <el-badge :value="$store.state.app.unCheckCount" class="item" style="margin-top: 15px; margin-right: 5px">
            <el-avatar style="margin-top: -5px; margin-right: -5px" :src="this.userAvatar"/>
          </el-badge>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item icon="el-icon-user-solid" disabled>{{username}}</el-dropdown-item>
          <el-dropdown-item icon="el-icon-message-solid" @click.native="notices">消息中心<el-badge
            :value="$store.state.app.unCheckCount" class="item"/></el-dropdown-item>
          <el-dropdown-item icon="el-icon-s-tools" @click.native="setting">设置</el-dropdown-item>
          <el-dropdown-item icon="icon iconfont icon029xiugaimima" @click.native="editPass">修改密码</el-dropdown-item>
          <el-dropdown-item icon="icon iconfont icontuichu2" divided @click.native="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </span>

    <!--新增编辑界面-->
    <el-dialog title="系统设置" width="40%" :visible.sync="settingDialogVisible" :close-on-click-modal="false" append-to-body>
      <el-form :model="settingForm" label-width="80px" :rules="settingFormRules" ref="fm_set">
        <el-form-item label="页面风格" prop="viewOpenStyle">
          <el-radio-group v-model="settingForm.viewOpenStyle">
            <el-radio label="tabs">页签模式</el-radio>
            <el-radio label="breadcrumb">面包屑模式</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="主题颜色" prop="viewColor">
          <el-color-picker v-model="settingForm.viewColor" show-alpha
                           :predefine="['rgba(84, 92, 100, 1)']"></el-color-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="settingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">确定</el-button>
      </div>
    </el-dialog>

    <!--新增编辑界面-->
    <el-dialog title="密码修改" width="40%" :visible.sync="settingDialogVisible2" :close-on-click-modal="false" append-to-body>
      <el-form :model="passForm" label-width="80px" :rules="settingFormRules2" ref="fm_password">
        <el-form-item label="用户编号" prop="userCode">
          <el-input v-model="passForm.userCode" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="passForm.userName" disabled></el-input>
        </el-form-item>
        <el-form-item label="原密码" prop="oldPass">
          <el-input type="password" v-model="passForm.oldPass" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass1">
          <el-input type="password" v-model="passForm.pass1" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="pass2">
          <el-input type="password" v-model="passForm.pass2" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="settingDialogVisible2 = false">取消</el-button>
        <el-button type="primary" @click.native="sureEditPass" :loading="editLoading">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-if="noticeDialogVisible" title="消息列表" width="70%" :visible.sync="noticeDialogVisible" :close-on-click-modal="false" append-to-body>
      <CusTable ref="messageTable" permsEdit="notice:receive:view" permsDelete="notice:receive:ignore"
                permsExport="notice:send:export" :showOperation=true
                :data="pageResult.content" :totalCount="pageResult.count" :columns="noticeColumns"
                datasource="VT_NOTICES" editTip="查看" delete-tip="忽略"
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
        <template v-slot:RECEIVE_STATUS="slotProps">
          <span v-if="slotProps.colData == '0' || slotProps.colData == '1'" style="color: red">
            <i class="icon iconfont iconic_userlevel_">未读</i>
          </span>
          <span v-else-if="slotProps.colData == '2'" style="color: green">
            <i class="icon iconfont iconic_userlevel_">已读</i>
          </span>
        </template>
      </CusTable>
    </el-dialog>

    <el-dialog title="查看" width="50%" :visible.sync="noticeDetailDialogVisible" :close-on-click-modal="false" append-to-body>
      <div ref="snapshot">
        <el-form :model="noticeForm" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="消息编号">
                <el-input v-model="noticeForm.NOTICE_ID" disabled auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="紧急程度">
                <el-select v-model="noticeForm.NOTICE_LEVEL" disabled clearable placeholder="请选择紧急程度">
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
          <el-form-item label="消息标题">
            <el-input v-model="noticeForm.NOTICE_TITLE" disabled auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="消息内容">
            <el-input type="textarea" :rows="8" v-model="noticeForm.NOTICE_CONTENT" disabled
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="消息附件">
            <el-upload
              action=""
              class="upload-demo"
              :multiple="true"
              :auto-upload="false"
              :file-list="noticeFile"
              :on-preview="handlePreview" disabled>
            </el-upload>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="发送人">
                <el-input v-model="noticeForm.CREATE_USER_NAME" disabled auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发送时间">
                <el-input v-model="noticeForm.CREATE_DATE" disabled auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-tooltip placement="top" content="打印">
          <el-button class="el-icon-camera" type="info" circle size="mini" @click="toImg"/>
        </el-tooltip>
      </div>
    </el-dialog>

  </div>
</template>

<script>
    import md5 from 'js-md5';
    import html2canvas from "html2canvas"
    import printJS from 'print-js'

    export default {
        data() {
            var validatePass3 = (rule, value, callback) => {
                if (value != this.passForm.oldPass) {
                    callback(new Error('密码错误，请重新输入！'));
                } else {
                    callback();
                }
            };
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.passForm.pass1 != '') {
                        this.$refs.cc.validateField('pass2');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.passForm.pass1) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                settingDialogVisible2: false,
                username: JSON.parse(sessionStorage.getItem('user')).userName,
                userAvatar: "",
                activeIndex: '1',
                settingDialogVisible: false,
                noticeDialogVisible: false,
                noticeDetailDialogVisible: false,
                editLoading: false,
                settingFormRules: {
                    viewOpenStyle: [
                        {required: true, message: '请选择页面风格', trigger: 'blur'}
                    ]
                },
                settingFormRules2: {
                    userCode: [
                        {disabled: false, trigger: 'blur'}

                    ],
                    userName: [
                        {disabled: false, trigger: 'blur'}

                    ],
                    oldPass: [
                        {required: true, validator: validatePass3, trigger: 'blur'}

                    ],
                    pass1: [
                        {required: true, validator: validatePass, trigger: 'blur'}
                    ],
                    pass2: [
                        {required: true, validator: validatePass2, trigger: 'blur'}
                    ],

                },
                // 设置界面数据
                settingForm: {
                    viewOpenStyle: '',
                    viewColor: ''
                },
                noticeForm: {
                    NOTICE_ID: '',
                    NOTICE_TITLE: '',
                    NOTICE_CONTENT: '',
                    NOTICE_FILE: '',
                    NOTICE_LEVEL: '',
                    CREATE_USER_NAME: '',
                    CREATE_DATE: ''
                },
                passForm: JSON.parse(sessionStorage.getItem('user')),
                noticeColumns: [
                    {prop: "RECEIVE_STATUS", label: "消息状态", width: 110, sortable: true, visible: true, filter: true},
                    {prop: "NOTICE_LEVEL", label: "紧急程度", width: 110, sortable: true, visible: true, filter: true},
                    {prop: "NOTICE_ID", label: "消息编号", width: 150, sortable: true, visible: true},
                    {prop: "NOTICE_TITLE", label: "消息标题", width: 300, sortable: true, visible: true},
                    {prop: "NOTICE_CONTENT", label: "消息内容", width: 300, sortable: false, visible: true},
                    {prop: "CREATE_USER_NAME", label: "发送人", width: 90, sortable: true, visible: true},
                    {prop: "CREATE_DATE", label: "发送时间", width: 150, sortable: true, visible: true},
                    {prop: "CHECK_DATE", label: "首次查看时间", width: 150, sortable: true, visible: true}
                ],
                pageResult: {},
                noticeFile:[]
            };
        },
        methods: {
            toImg() { // 转图片打印
                html2canvas(this.$refs.snapshot, {
                    backgroundColor: null,
                    useCORS: true
                }).then((canvas) => {
                    const url = canvas.toDataURL()
                    printJS({
                        printable: url,
                        type: 'image',
                        documentTitle: '消息@MJQ'
                    })
                })
            },
            // 表格查询回调实现
            findPage: function (params) {
                this.$api.common.findPage({
                    'needYear': 'N',
                    'searchProperties': JSON.stringify({'USER_CODE,==': JSON.parse(sessionStorage.getItem('user')).userCode}),
                    'tableName': params.datasource,
                    'pageNum': params.pageNum,
                    'pageSize': params.pageSize,
                    'sortProp': params.sortProp,
                    'sortOrder': params.sortOrder
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
            },
            handleEdit: function (params) {
                this.noticeFile = [];
                this.noticeForm = Object.assign({}, params.row)
                if (this.noticeForm.NOTICE_FILE && this.noticeForm.NOTICE_FILE.lastIndexOf("/") > 0) {
                    let allF = this.noticeForm.NOTICE_FILE.split(",");
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
                this.noticeDetailDialogVisible = true
                this.$api.notice.updateNoticeTarget({
                    "noticeId": JSON.stringify([this.noticeForm.NOTICE_ID]),
                    "userCode": this.noticeForm.USER_CODE,
                    "receiveStatus": '2'
                });
                this.$nextTick(() => {
                    params.row.RECEIVE_STATUS = "2";
                })
                this.$store.commit('reduce_unCheckCount', 1);
            },
            handlePreview(file) {
                if (file.url) {
                    window.location.href = file.url
                }
            },
            handleDelete: function (data) {
                let userCode = '';
                let noticeId = []
                JSON.parse(data.params.datas).forEach(v => {
                    userCode = v.USER_CODE
                    noticeId.push(v.NOTICE_ID)
                })
                this.$api.notice.updateNoticeTarget({
                    "noticeId": JSON.stringify(noticeId),
                    "userCode": userCode,
                    "receiveStatus": '3'
                }).then(data.callback)
                this.$store.commit('reduce_unCheckCount', data.params.datas.length);
            },
            sureEditPass: function () {
                this.$confirm('确认提交吗？', '提示', {}).then(() => {
                    this.$api.login.editPass({
                        "oldPass": md5(this.passForm.oldPass),
                        "pass": md5(this.passForm.pass1),
                        "id": this.passForm.userCode
                    }).then(res => {
                        if (res.success) {
                            this.$store.commit('clearStates'); // 清空全局属性
                            sessionStorage.clear(); // 清空session
                            this.$router.options.routes[0].children = []; // 清空动态路由
                            this.$router.replace('/');
                            this.$message.success('修改成功，重新登录')
                        } else {
                            this.$message.success('修改失败')
                        }
                    })
                })

            },
            //折叠导航栏
            collapse: function () {
                this.$store.commit('onCollapse');
            },
            //退出登录
            logout: function () {
                this.$confirm("确认退出吗?", "提示", {
                    type: "warning"
                })
                    .then(() => {
                        this.$api.login.logout({}).then(res => {
                            if (res.success) {
                                this.$store.commit('clearStates'); // 清空全局属性
                                sessionStorage.clear(); // 清空session
                                this.$router.options.routes[0].children = []; // 清空动态路由
                                this.$router.replace('/');
                                this.$message.success('退出成功')
                            }
                        });
                    }).catch(() => {
                });
            },
            setting: function () {
                this.settingDialogVisible = true;
                this.settingForm = {
                    viewOpenStyle: this.$store.state.app.viewOpenStyle,
                    viewColor: this.$store.state.app.viewColor
                }
            },
            notices: function () {
                this.noticeDialogVisible = true;
            },
            editPass: function () {
                this.settingDialogVisible2 = true;
            },
            editSubmit: function () {
                this.$refs.fm_set.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true
                            this.$store.commit('setViewOpenStyle', this.settingForm.viewOpenStyle)
                            this.$store.commit('setViewColor', this.settingForm.viewColor)
                            this.editLoading = false;
                            this.settingDialogVisible = false;
                            this.$refs['fm_set'].resetFields();
                            this.$message({message: '设置成功', type: 'success'});
                            setTimeout(() => {
                                this.$router.go(0)
                            }, 1000);
                        })
                    }
                })
            },
            receiveAllNotices(userCode) {
                this.$api.login.receiveAllNotices({'userCode': userCode}).then(res => {
                    this.$store.commit('set_unCheckCount', parseInt(res.message))
                });
            }
        },
        mounted() {
            let user = sessionStorage.getItem("user");
            if (user) {
                this.userName = JSON.parse(sessionStorage.getItem('user')).userName;
                this.userAvatar = require("@/assets/user.png");
                this.receiveAllNotices(JSON.parse(sessionStorage.getItem('user')).userCode);
            }
        }
    };
</script>

<style scoped lang="scss">
  .container {
    position: absolute;
    right: 0px;
    height: 65px;

    .collapse-switcher {
      height: 65px;
      padding-left: 10px;
      width: 28px;
      float: left;
      cursor: pointer;
      border-color: rgba(111, 123, 131, 0.8);
      border-left-width: 1px;
      border-left-style: solid;
      border-right-width: 1px;
      border-right-style: solid;
      color: white;
      background: #504e6180;
    }

    .tool-bar {
      float: right;
      text-align: right;
      padding-right: 30px;

      .user-info-dropdown {
        font-size: 15px;
        cursor: pointer;
        color: #fff;
        float: right;

        img {
          width: 20px;
          height: 15px;
          border-radius: 10px;
          margin-top: 10px;
        }
      }
    }

    .position-left {
      left: 210px
    }

    .position-collapse-left {
      left: 68px;
    }
  };
</style>
