<template>
  <PopupTreeInput
    :data="data1"
    v-model="childValue"
    :disabled="disabled"
    :singleSelect="single"
    @change="$emit('change', $event)"
    v-if="refresh"
    size="mini">
  </PopupTreeInput>
</template>

<script>

    export default {
        name: "tableElSelectTree",
        components: {
        },
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
            value: {},
            datasource: {
                type: String,
                default: ''
            },
            disabled: {
                type: Boolean,
                default: false
            },
            single: {
                type: Boolean,
                default: true
            }
        },
        methods: {
            initData: function () {
                if (this.datasource !== '') {
                    this.$api.form.selectTreeDataSource({'datasource': this.datasource}).then((res) => {
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
            this.initData()
        }
    }
</script>

<style scoped>

</style>
