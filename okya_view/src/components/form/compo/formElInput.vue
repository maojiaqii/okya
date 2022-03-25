<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <!--:style="{width:properties.compoWidth+'%'}"-->
      <el-input v-model="childValue" :disabled="properties.disabled" :placeholder="properties.placeholder"
                auto-complete="off" clearable :show-password="properties.showPassword"
                @input="$emit('input', $event)"></el-input>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElInput",
        model: {
            prop: 'value',
            event: 'input'
        },
        data() {
            return {
                childValue:''
            }
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
                default: '单行文本'
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
        methods: {
        },
        watch: {
            //监听value变化（slect控件不是必要，暂时不明）
            value(newValue, oldValue) {
                this.childValue = newValue;
            }
        },
        mounted(){
            this.childValue = this.value
        }
    }
</script>

<style scoped>

</style>
