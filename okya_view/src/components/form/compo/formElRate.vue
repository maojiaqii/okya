<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-rate
        v-model="childValue"
        :disabled="properties.disabled"
        show-score
        allow-half
        :max="properties.max"
        text-color="#ff9900"
        @change="$emit('change', $event)">
      </el-rate>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElRate",
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
                default: '评分'
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
        mounted(){
            this.childValue = this.value
        }
    }
</script>

<style scoped>

</style>
