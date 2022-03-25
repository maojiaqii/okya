<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-time-picker
        v-model="childValue"
        :value-format="properties.format"
        :disabled="properties.disabled"
        @change="$emit('change', $event)">
      </el-time-picker>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElTimePicker",
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
                default: '时间选择器'
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
        mounted() {
            if(this.value == ''){

            } else {
                this.childValue = new Date(this.value)
            }
            /*let h = parseInt(this.value.split(':')[0])
            let m = parseInt(this.value.split(':')[1])
            let s = parseInt(this.value.split(':')[2])
            this.childValue = new Date(2020, 1, 1, h, m, s)*/
        }
    }
</script>

<style scoped>

</style>
