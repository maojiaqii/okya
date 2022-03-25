<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-input type="textarea" v-model="childValue" :disabled="properties.disabled" :placeholder="properties.placeholder"
                auto-complete="off" clearable @input="$emit('input', $event)"></el-input>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElTextArea",
        data() {
            return {
                childValue: this.value
            }
        },
        model: {
            prop: 'value',
            event: 'input'
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
                default: '多行文本'
            },
            prop: {
                type: String,
                default: ''
            },
            value: {
                type: String,
                default: ''
            },
            placeholder: {
                type: String,
                default: '请输入内容'
            },
            properties: {
                type: Object,
                default: {}
            }
        },
        watch: {
            //监听value变化（slect控件不是必要，暂时不明）
            value(newValue, oldValue) {
                this.childValue = newValue;
            }
        },
        mounted(){
        }
    }
</script>

<style scoped>

</style>
