<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-select v-model="childValue" filterable clearable :disabled="properties.disabled"
                 :multiple="properties.multiple" @change="$emit('change', $event)">
        <el-option
          v-for="item in options"
          :key="item[properties.value]"
          :label="item[properties.label]"
          :value="item[properties.value]"/>
      </el-select>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElSelecter",
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
                default: '下拉框'
            },
            prop: {
                type: String,
                default: ''
            },
            value: {},
            properties: {
                type: Object,
                default: {}
            }
        },
        methods: {
            initData: function (conditions, relation, affectField) {
                this.childValue = this.value
                this.$api.form.selectDataSource({
                    'datasource': this.properties.remoteOption,
                    'conditions': conditions,
                    'affectField': affectField,
                    'relation': relation
                }).then((res) => {
                    if (res.success) {
                        this.options = JSON.parse(res.message)
                    } else {
                        this.$message({message: res.message, type: 'error'})
                    }
                })
            }
        },
        mounted() {
            this.initData('', '', '')
        }
    }
</script>

<style scoped>

</style>
