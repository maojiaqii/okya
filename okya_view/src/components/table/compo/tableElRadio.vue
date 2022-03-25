<template>
  <el-radio-group size="mini" v-model="childValue" @change="$emit('change', $event)" :disabled="disabled">
    <el-radio v-for="item in options"
              :key="item[valueFlag]"
              :label="item[valueFlag]">{{item[labelFlag]}}
    </el-radio>
  </el-radio-group>
</template>

<script>
    export default {
        name: "tableElRadio",
        data() {
            return {
                childValue: [],
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
            }
        },
        methods: {
            initData: function () {
                this.childValue = this.value
                this.$api.form.radioAndCheckBoxDataSource({'datasource': this.datasource}).then((res) => {
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
