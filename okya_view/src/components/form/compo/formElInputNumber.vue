<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-input-number v-model="childValue" :disabled="properties.disabled" :precision="properties.precision"
                       :step="properties.step" :min="properties.min" :max="properties.max" @change="$emit('change', $event)"></el-input-number>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElInputNumber",
        data() {
            return {
                childValue:''
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
                default: '计数器'
            },
            prop: {
                type: String,
                default: ''
            },
            value: {
                type: String,
                default: ''
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
