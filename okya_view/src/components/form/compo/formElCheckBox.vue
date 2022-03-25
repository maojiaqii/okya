<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-checkbox-group v-model="childValue" :disabled="properties.disabled" @change="$emit('change', $event)">
        <el-checkbox v-for="item in options"
                     :key="item[properties.value]"
                     :label="item[properties.value]">{{item[properties.label]}}
        </el-checkbox>
      </el-checkbox-group>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElCheckBox",
        data() {
            return {
                childValue:[],
                options: []
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
                default: '多选框组'
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
            initData: function () {
                this.childValue = this.value
                this.$api.form.radioAndCheckBoxDataSource({'datasource': this.properties.remoteOption}).then((res) => {
                    if (res.success) {
                        this.options = JSON.parse(res.message)
                    } else {
                        this.$message({message: res.message, type: 'error'})
                    }
                })
            }
        },
        mounted() {
            this.initData()
        }
    }
</script>

<style scoped>

</style>
