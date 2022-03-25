<template>
  <el-time-picker
    v-model="childValue"
    @change="$emit('change', $event)">
  </el-time-picker>
</template>

<script>
    export default {
        name: "searchElTimePicker",
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
            value: {
                type: String,
                default: ''
            }
        },
        methods: {},
        mounted() {
            if(this.value == ''){

            } else {
                this.childValue = new Date(this.value)
            }
        }
    }
</script>

<style scoped>

</style>
