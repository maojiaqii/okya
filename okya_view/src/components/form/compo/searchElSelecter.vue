<template>
  <el-select v-model="childValue" filterable clearable @change="$emit('change', $event)">
    <el-option
      v-for="item in options"
      :key="item.VAL"
      :label="item.LAB"
      :value="item.VAL"/>
  </el-select>
</template>

<script>
    export default {
        name: "searchElSelecter",
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
                this.childValue = this.value
                this.$api.common.getSelectorData({
                    'needYear': 'Y',
                    'searchProperties': JSON.stringify({'': ''}),
                    'tableName': this.dataSource
                }).then((res) => {
                    this.options = JSON.parse(res.message)
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
