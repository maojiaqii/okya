<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-upload
        action=""
        :disabled="properties.disabled"
        class="upload-demo"
        :multiple="properties.many"
        :limit="properties.fileCount"
        drag
        :auto-upload="false"
        :file-list="childValue"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-exceed="handleExceed"
        :before-remove="beforeRemove"
        :on-change="handleChange">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em><br>{{properties.tip}}</div>
      </el-upload>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElUpload",
        data() {
            return {
                childValue: []
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            rule: {
                type: Array,
                default: () => []
            },
            span: {
                type: Number,
                default: 24
            },
            label: {
                type: String,
                default: '文件上传'
            },
            prop: {
                type: String,
                default: ''
            },
            value: {
                type: Array,
                default: () => []
            },
            properties: {
                type: Object,
                default: {}
            }
        },
        methods: {
            handleChange(file, fileList) {
                const isLtM = file.size / 1024 / 1024 < 10;
                if (!isLtM) {
                    file = {}
                    fileList = []
                    this.$message.error('上传的文件大小不能超过 10MB!');
                } else {
                    var reader = new FileReader()
                    reader.readAsDataURL(file.raw)
                    reader.onload = () => {
                        this.$emit('change', [{name: file.name, data: reader.result}])
                    }
                }
            },
            handleRemove(file, fileList) {
                this.$emit('change', [])
            },
            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择 `+this.properties.fileCount+` 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },
            beforeRemove(file, fileList) {
                return this.$confirm(`确定移除 ${file.name}?`);
            },
            handlePreview(file){
                if(file.url){
                    window.location.href = file.url
                }
            }
        },
        mounted() {
            this.childValue = this.value
        }
    }
</script>

<style scoped>

</style>
