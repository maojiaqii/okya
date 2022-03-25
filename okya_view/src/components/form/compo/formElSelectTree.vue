<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <PopupTreeInput
        :data="data1"
        v-model="childValue"
        :disabled="properties.disabled"
        :singleSelect="properties.single"
        @change="$emit('change', $event)"
        v-if="refresh">
      </PopupTreeInput>
    </el-form-item>
  </el-col>
</template>

<script>

    export default {
        name: "formElSelectTree",
        data() {
            return {
                data1: [],
                childValue: this.value,
                refresh: true
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
                default: '下拉树'
            },
            value: {},
            prop: {
                type: String,
                default: ''
            },
            properties: {
                type: Object,
                default: {}
            }
        },
        methods: {
            initData: function (conditions, relation, affectField) {
                if (this.properties.remoteOption !== '') {
                    this.$api.form.selectTreeDataSource({
                        'datasource': this.properties.remoteOption,
                        'conditions': conditions,
                        'affectField': affectField,
                        'relation': relation
                    }).then((res) => {
                        // 手动刷新组件
                        this.refresh = false
                        if (res.success) {
                            this.data1 = JSON.parse(res.message)
                        } else {
                            this.$message({message: res.message, type: 'error'})
                        }
                        this.$nextTick(() => {
                            // 手动刷新组件
                            this.refresh = true;
                        });
                    })
                }
            }
        },
        mounted() {
            this.initData('', '', '')
        }
    }
</script>

<style scoped>

</style>
