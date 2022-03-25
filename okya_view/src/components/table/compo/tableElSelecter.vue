<template>
  <el-select v-model="childValue" filterable clearable :disabled="disabled" size="mini"
             @change="$emit('change', $event)">
    <el-option
      v-for="item in options"
      :key="item[valueFlag]"
      :label="item[labelFlag]"
      :value="item[valueFlag]"/>
  </el-select>
</template>

<script>
    export default {
        name: "tableElSelecter",
        data() {
            return {
                childValue: undefined,
                options: []
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            valueFlag: {
                type: String,
                default: ''
            },
            value: {
                type: String,
                default: ''
            },
            labelFlag: {
                type: String,
                default: ''
            },
            datasource: {
                type: String,
                default: ''
            },
            disabled: {
                type: Boolean,
                default: false
            },
            staticOptions: {
                type: Array,
                default: () => []
            }
        },
        methods: {
            initData: function () {
                this.childValue = this.value
                if(this.datasource !== undefined && this.datasource !== ''){
                    this.$api.form.selectDataSource({'datasource': this.datasource}).then((res) => {
                        if (res.success) {
                            this.options = JSON.parse(res.message)
                        } else {
                            this.$message({message: res.message, type: 'error'})
                        }
                    })
                } else if(this.staticOptions !== undefined && this.staticOptions !== []) {
                    this.options = this.staticOptions
                }
            }
        },
        mounted() {
            this.initData()
        },
        watch:{
            staticOptions:{
                handler(val){
                    this.initData()
                },
                deep: true
            }
        }
    }
</script>

<style scoped>

</style>
