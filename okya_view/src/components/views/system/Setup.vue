<template>
  <div>
    <!--工具栏-->
    <div class="toolbar" style="float:left; padding:18px;width:90%;">
      <el-form :model="settingForm" label-width="80px" :rules="settingFormRules" ref="fm">
        <el-form-item label="系统标题" prop="sysTitle">
          <el-input v-model="settingForm.sysTitle" maxlength="20"
                    show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="系统图标" prop="sysIcon">
          <el-upload
            action=""
            :show-file-list="false"
            :on-change="beforeAvatarUpload"
            accept="image/png,image/gif,image/jpg,image/jpeg">
            <el-avatar :size="50" :src="imageUrl"></el-avatar>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="float: right">
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">确定</el-button>
      </div>
    </div>
  </div>
</template>

<script>

    export default {
        data() {
            return {
                settingFormRules: {
                    sysTitle: [
                        {required: true, message: '请填写系统标题', trigger: 'blur'}
                    ]
                },
                // 设置界面数据
                settingForm: {
                    sysTitle: this.$store.state.app.appName,
                    sysIcon: ''
                },
                imageUrl: this.$store.state.app.appIcon,
                editLoading: false
            }
        },
        methods: {
            editSubmit: function () {
                this.$refs.fm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true
                            this.$api.sysSetup.save(this.settingForm).then(res => {
                                this.editLoading = false;
                                if (res.success) {
                                    this.$store.commit('setAppName', this.settingForm.sysTitle)
                                    if(res.message){
                                        this.$store.commit('setAppIcon', res.message)
                                    }
                                    this.$message({message: '设置成功', type: 'success'});
                                    this.$router.go(0);
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: res.message
                                    })
                                }
                            });
                        })
                    }
                })
            },
            beforeAvatarUpload(file, fileList) {
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isLt2M) {
                    this.$message.error('上传的系统图标大小不能超过 2MB!');
                } else {
                    var reader = new FileReader()
                    reader.readAsDataURL(file.raw)
                    reader.onload = () => {
                        this.settingForm.sysIcon = reader.result
                        this.imageUrl = URL.createObjectURL(file.raw);
                    }
                }
            }
        },
        mounted() {
        }
    }
</script>

<style scoped lang="scss">

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 50px;
    height: 50px;
    line-height: 50px;
    text-align: center;
  }

</style>
