<template>
  <PopupTreeInput
    :data="data1"
    v-model="childValue"
    @change="$emit('change', $event)"
    v-if="refresh">
  </PopupTreeInput>
</template>

<script>

    export default {
        name: "searchElSelectTree",
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
            value: {
                type: String,
                default: ''
            },
            dataSource: {
                type: String,
                default: ''
            }
        },
        methods: {
            initData: function () {
                this.$api.form.selectTreeDataSource({
                    'searchProperties': JSON.stringify({'': ''}),
                    'datasource': this.dataSource
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
        },
        mounted() {
            this.initData()
        }
    }
</script>

<style scoped>

</style>
