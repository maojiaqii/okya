<template>
      <el-date-picker
        v-model="childValue"
        type="date"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
        @change="$emit('change', $event)">
      </el-date-picker>
</template>

<script>
    export default {
        name: "searchElDatePicker",
        data() {
            return {
                childValue: ''
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
        methods: {
        },
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
