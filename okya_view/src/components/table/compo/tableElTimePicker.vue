<template>
  <el-time-picker
    v-model="childValue"
    :value-format="format"
    :disabled="disabled"
    size="mini"
    @change="$emit('change', $event)">
  </el-time-picker>
</template>

<script>
    export default {
        name: "tableElTimePicker",
        data() {
            return {
                childValue: new Date()
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            format: {
                type: String,
                default: ''
            },
            value: {
                type: String,
                default: ''
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        mounted() {
            let h = parseInt(this.value.split(':')[0])
            let m = parseInt(this.value.split(':')[1])
            let s = parseInt(this.value.split(':')[2])
            this.childValue = new Date(2020, 1, 1, h, m, s)
        }
    }
</script>

<style scoped>

</style>
