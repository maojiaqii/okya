<template>
  <div>
    <!--表格内容栏-->
    <CusEditTable ref="custable" permsEdit="sys:edtTabel:edit"
                  permsDelete="sys:edtTabel:delete" permsAddOne="sys:edtTabel:addOne"
                  permsCancelDelete="sys:edtTabel:cancelDelete"
                  :showOperation=true :addModel="addModel" :maxHeight="400"
                  v-model="pageResult" :columns="columns">
    </CusEditTable>
    <el-button @click="cClick">asdadsad</el-button>
  </div>
</template>

<script>

    export default {
        data() {
            return {
                columns: [
                    {
                        prop: "userCode",
                        label: "用户编号",
                        minWidth: 40,
                        visible: true,
                        editAttr: 'el-input-number',
                        precision: 0,
                        step: 1,
                        min: 0,
                        max: 100
                    },
                    {
                        prop: "userName",
                        label: "用户名",
                        minWidth: 80,
                        visible: true,
                        editAttr: 'el-input'
                    },
                    {
                        prop: "date",
                        label: "日期",
                        minWidth: 80,
                        visible: true,
                        editAttr: 'el-date',
                        type: "date",
                        dateformat: "yyyy-MM-dd"
                    },
                    {
                        prop: "time",
                        label: "时间",
                        minWidth: 80,
                        visible: true,
                        editAttr: 'el-time',
                        timeformat: "HH:mm:ss"
                    },
                    {
                        prop: "rate",
                        label: "评分",
                        minWidth: 80,
                        visible: true,
                        editAttr: 'el-rate',
                        max: 5
                    },
                    {
                        prop: "roleCode",
                        label: "角色",
                        minWidth: 60,
                        visible: true,
                        editAttr: 'el-select',
                        datasource: 'vt_radio_datasource',
                        valueFlag: 'VAL',
                        labelFlag: 'LAB'
                    },
                    {
                        prop: "coCode",
                        label: "单位编号",
                        minWidth: 40,
                        visible: true,
                        editAttr: 'el-tree',
                        datasource: 'vt_tree_datasource',
                    },
                    {
                        prop: "used",
                        label: "是否使用",
                        minWidth: 30,
                        visible: true,
                        editAttr: 'el-switch',
                        activeText: "是",
                        inActiveText: "否",
                        activeValue: "Y",
                        inActiveValue: "N"
                    }
                ],
                pageResult: [{
                    "userCode": '50',
                    "userName": 'saadas',
                    "rate": 3.5,
                    "date": '2019-05-21',
                    "time": '09:11:45',
                    "roleCode": '001',
                    "coCode": '001001',
                    "used": 'Y'
                }, {
                    "userCode": '25',
                    "userName": 'fcsdfewefew',
                    "rate": 4.5,
                    "date": '2019-05-21',
                    "time": '09:11:45',
                    "roleCode": '001',
                    "coCode": '001001',
                    "used": 'N'
                }],
                addModel: {
                    "userCode": '',
                    "userName": '',
                    "rate": 0,
                    "date": '',
                    "time": '',
                    "roleCode": '',
                    "coCode": '',
                    "used": ''
                }
            }
        },
        methods: {
            cClick: function () {
                console.log('test页面')
                console.log(this.pageResult)
            }
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>
