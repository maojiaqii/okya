<template>
  <el-input v-model="childValue" auto-complete="off" clearable
            @input="$emit('input', $event)"></el-input>
</template>

<script>
    export default {
        name: "searchElInput",
        model: {
            prop: 'value',
            event: 'input'
        },
        data() {
            return {
                childValue: ''
            }
        },
        props: {
            value: {
                type: String,
                default: ''
            }
        },
        methods: {},
        watch: {
            //监听value变化（slect控件不是必要，暂时不明）
            value(newValue, oldValue) {
                this.childValue = newValue;
            }
        },
        mounted() {
            this.childValue = this.value
        }
    }
</script>

<style scoped>

</style>
