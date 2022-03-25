<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-radio-group v-model="childValue" :disabled="properties.disabled" @change="$emit('change', $event)">
        <el-radio v-for="item in options"
                  :key="item[properties.value]"
                  :label="item[properties.value]">{{item[properties.label]}}
        </el-radio>
      </el-radio-group>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElRadio",
        data() {
            return {
                childValue:'',
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
                default: '单选框组'
            },
            prop: {
                type: String,
                default: ''
            },
            value: {
                type: String,
                default: ''
            },
            properties: {
                type: Object,
                default: {}
            }
        },
        methods: {
            initData: function () {
                this.$api.form.radioAndCheckBoxDataSource({'datasource': this.properties.remoteOption}).then((res) => {
                    if (res.success) {
                        this.options = JSON.parse(res.message)
                        if(this.value == ''){
                            this.childValue = this.options[0][this.properties.value]
                        } else {
                            this.childValue = this.value
                        }
                    } else {
                        this.$message({message: res.message, type: 'error'})
                    }
                })
            }
        },
        mounted() {
            this.initData()
        },
        watch:{
            properties:{
                handler(val){
                    this.$api.form.radioAndCheckBoxDataSource({'datasource': val.remoteOption}).then((res) => {
                        if (res.success) {
                            this.options = JSON.parse(res.message)
                        } else {
                            this.$message({message: res.message, type: 'error'})
                        }
                    })
                },
                deep:true
            }
        }
    }
</script>

<style scoped>

</style>
