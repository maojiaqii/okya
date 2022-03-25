<template>
  <div>

    <!-- 按钮区域 -->
    <div style="padding-left: 20px; padding-top: 20px;">
      <el-button type="primary" icon="el-icon-search" v-on:click="searchBt" plain size="small">查询</el-button>
      <CusButton :perms="buttons.PERMS_NEW" type="primary" @click="addBt"/>
    </div>

    <!-- 查询条件区域 -->
    <div style="padding-left: 20px; padding-top: 20px;">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item v-for="(element, index) in searches" :label="element.searchName" :key="index">
          <component :is="element.searchCompo" v-model="filters[element.searchField + ',' + element.searchCondition]"
                     :dataSource="element.dataSource"></component>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据展示表格区域 -->
    <CusTable :ref="table.tableId" :permsEdit="buttons.PERMS_EDIT" :permsDelete="buttons.PERMS_DELETE"
              :permsExport="buttons.PERMS_EXPORT" :max-height="table.maxHeight"
              :showOperation="table.showOperation" :show-pagination="table.showPagination"
              :show-row-num="table.showRowNum" :show-sum="table.showSum" :size="table.size"
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              :datasource="table.dataSource"
              @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    </CusTable>

    <!--新增编辑界面-->
    <el-dialog :title="operation?'新增':'编辑'" width="50%" :visible.sync="editDialogVisible" :close-on-click-modal="false" append-to-body>
      <div ref="snapshot">
        <el-form ref="fm" :label-position="selectList.labelPosition" :size="selectList.size" :model="formProps"
                 :rules="formRules" v-if="editDialogVisible">
          <el-row :gutter="20">
            <component v-for="element in selectList.list" :key="element.key" :ref="element.key" :span="element.span"
                       :is="element.compoType" :label="element.compoTitle" :prop="element.key"
                       v-model="formProps[element.key]" :properties="element.compoProp" :addModel="element.addModel"
                       @change="vChange(element.key, formProps[element.key])"
                       @change.native="vChange(element.key, formProps[element.key])">
            </component>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-tooltip placement="top" content="交易快照">
          <el-button class="el-icon-camera" type="info" circle size="mini" @click="toImg"/>
        </el-tooltip>
        <el-button @click.native="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
    import html2canvas from "html2canvas"
    import printJS from 'print-js'

    import formElInput from "../../form/compo/formElInput";
    import formElTextArea from "../../form/compo/formElTextArea";
    import formElRadio from "../../form/compo/formElRadio";
    import formElCheckBox from "../../form/compo/formElCheckBox";
    import formElInputNumber from "../../form/compo/formElInputNumber";
    import formElSwitch from "../../form/compo/formElSwitch";
    import formElDatePicker from "../../form/compo/formElDatePicker";
    import formElTimePicker from "../../form/compo/formElTimePicker";
    import formElSelecter from "../../form/compo/formElSelecter";
    import formElSelectTree from "../../form/compo/formElSelectTree";
    import formElRate from "../../form/compo/formElRate";
    import formElUpload from "../../form/compo/formElUpload";
    import formWord from "../../form/compo/formWord";
    import formElDivider from "../../form/compo/formElDivider";
    import formElEditTable from "../../form/compo/formElEditTable";

    import searchElInput from "../../form/compo/searchElInput";
    import searchElDatePicker from "../../form/compo/searchElDatePicker";
    import searchElTimePicker from "../../form/compo/searchElTimePicker";
    import searchElSelecter from "../../form/compo/searchElSelecter";
    import searchElSelectTree from "../../form/compo/searchElSelectTree";

    export default {
        name: 'DndList',
        components: {
            formElInput,
            formElTextArea,
            formElRadio,
            formElCheckBox,
            formElInputNumber,
            formElSwitch,
            formElDatePicker,
            formElTimePicker,
            formElSelecter,
            formElSelectTree,
            formElRate,
            formElUpload,
            formWord,
            formElDivider,
            formElEditTable,
            searchElInput,
            searchElSelectTree,
            searchElDatePicker,
            searchElTimePicker,
            searchElSelecter
        },
        props: {
            cname: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                formId: '',
                buttons: {
                    PERMS_NEW: '',
                    PERMS_EDIT: '',
                    PERMS_DELETE: '',
                    PERMS_EXPORT: ''
                },
                searches: [],
                filters: {"CREATE_USER,==": JSON.parse(sessionStorage.getItem('user')).userCode},
                table: {},
                columns: [],
                pageResult: {},
                editLoading: false,
                selectList: {},
                formProps: {},
                formPropsBak: {},
                formRules: {},
                formFieldLabel: {},
                operation: false,
                editDialogVisible: false,
                fileCompo: [],
                arrayValueCompo: [],
                needYear: ''
            }
        },
        mounted() {
            this.formId = this.$route.query.formId;
            this.initPage();
        },
        methods: {

            // 初始化页面
            initPage() {
                this.$api.form.initPage({
                    'formId': this.formId
                }).then((res) => {
                    if (res.success) {
                        let page = JSON.parse(res.message);
                        this.buttons = JSON.parse(page.pageButtons);
                        this.searches = JSON.parse(page.searchField);
                        this.table = JSON.parse(page.asTables);
                        this.needYear = page.needYear
                        this.table.showOperation = JSON.parse(this.table.showOperation);
                        this.table.showPagination = JSON.parse(this.table.showPagination);
                        this.table.showRowNum = JSON.parse(this.table.showRowNum);
                        this.table.showSum = JSON.parse(this.table.showSum);
                        this.table.maxHeight = Number(this.table.maxHeight);
                        this.columns = JSON.parse(page.asTableColumnsList);
                        this.columns.forEach(value => value.filter = JSON.parse(value.filter))
                        this.searches.forEach(v => {
                            this.filters[v.searchField + ',' + v.searchCondition] = '';
                        });
                        this.selectList = JSON.parse(page.formTemplet)
                        this.selectList.list.forEach(v => {
                            if (v.compoType !== 'formWord' && v.compoType !== 'formElDivider' && v.compoType !== 'formElEditTable') {
                                if (v.compoType == 'formElUpload') {
                                    this.fileCompo.push(v.key);
                                }
                                if (v.compoType == 'formElSelectTree' || v.compoType == 'formElCheckBox') {
                                    this.arrayValueCompo.push(v.key);
                                }
                                this.formFieldLabel[v.key] = v.compoTitle
                                this.formProps[v.key] = v.model
                                this.formRules[v.key] = [{
                                    required: v.compoProp.required,
                                    validator: (rule, value, callback) => {
                                        value = this.formProps[v.key]
                                        if (v.compoProp.required && (value == null || value == '' || value == undefined || value == [])) {
                                            callback(new Error('[' + v.compoTitle + ']不可为空！'));
                                        } else if (v.binds.length !== 0) {
                                            v.binds.forEach(m => {
                                                switch (m.relation) {
                                                    case '<=':
                                                        if (value <= this.formProps[m.targetField]) {
                                                            callback();
                                                        } else {
                                                            callback(new Error('[' + v.compoTitle + '必须小于或者等于[' + this.formFieldLabel[m.targetField] + ']！'));
                                                        }
                                                        break;
                                                    case '<':
                                                        if (value < this.formProps[m.targetField]) {
                                                            callback();
                                                        } else {
                                                            callback(new Error('[' + v.compoTitle + ']必须小于[' + this.formFieldLabel[m.targetField] + ']！'));
                                                        }
                                                        break;
                                                    case '>=':
                                                        if (value >= this.formProps[m.targetField]) {
                                                            callback();
                                                        } else {
                                                            callback(new Error('[' + v.compoTitle + ']必须大于或者等于[' + this.formFieldLabel[m.targetField] + ']！'));
                                                        }
                                                        break;
                                                    case '>':
                                                        if (value > this.formProps[m.targetField]) {
                                                            callback();
                                                        } else {
                                                            callback(new Error('[' + v.compoTitle + ']必须大于[' + this.formFieldLabel[m.targetField] + ']！'));
                                                        }
                                                        break;
                                                    case '==':
                                                        if (value == this.formProps[m.targetField]) {
                                                            callback();
                                                        } else {
                                                            callback(new Error('[' + v.compoTitle + ']必须等于[' + this.formFieldLabel[m.targetField] + ']！'));
                                                        }
                                                        break;
                                                    case '!=':
                                                        if (value != this.formProps[m.targetField]) {
                                                            callback();
                                                        } else {
                                                            callback(new Error('[' + v.compoTitle + ']必须不等于[' + this.formFieldLabel[m.targetField] + ']！'));
                                                        }
                                                        break;
                                                    default:
                                                        callback();
                                                }
                                            })
                                        } else {
                                            callback();
                                        }
                                    }, trigger: ['change']
                                }]
                            } else if (v.compoType == 'formElEditTable') {
                                this.arrayValueCompo.push(v.key);
                                this.formRules[v.key] = [{required: false, message: '', trigger: 'blur'}]
                                this.formProps[v.key] = []
                                let addModel = {}
                                v.compoProp.editTableColumns.forEach(i => {
                                    if (i.editAttr == 'el-input-number') {
                                        addModel[i.prop] = 0;
                                    } else {
                                        addModel[i.prop] = '';
                                    }
                                })
                                v.addModel = addModel
                            } else {
                                this.formRules[v.key] = [{required: false, message: '', trigger: 'blur'}]
                            }
                        })
                        this.formPropsBak = JSON.parse(JSON.stringify(this.formProps));
                    } else {
                        this.$message({message: res.message, type: 'error'});
                    }
                })
            },
            searchBt() {
                this.$refs[this.table.tableId].pageNum = 1
                this.$refs[this.table.tableId].sortProp = ''
                this.$refs[this.table.tableId].sortOrder = ''
                this.$api.common.findPage({
                    'needYear': this.needYear,
                    'searchProperties': JSON.stringify(this.filters),
                    'tableName': this.$refs[this.table.tableId].datasource,
                    'pageNum': 1,
                    'pageSize': this.$refs[this.table.tableId].pageSize,
                    'sortProp': '',
                    'sortOrder': ''
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
            },
            addBt() {
                this.editDialogVisible = true
                this.operation = true
                this.formProps = JSON.parse(JSON.stringify(this.formPropsBak));
            },
            findPage(param) {
                this.$api.common.findPage({
                    'needYear': this.needYear,
                    'searchProperties': JSON.stringify(this.filters),
                    'tableName': param.datasource,
                    'pageNum': param.pageNum,
                    'pageSize': param.pageSize,
                    'sortProp': param.sortProp,
                    'sortOrder': param.sortOrder
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })

            },
            handleEdit(params) {
                this.$api.form.getDynamicFormData({
                    'guid': params.row.GUID,
                    'formId': this.formId
                }).then((res) => {
                    if (res.success) {
                        this.formProps = JSON.parse(res.message)
                        this.arrayValueCompo.forEach(v => {
                            this.formProps[v] = JSON.parse(this.formProps[v])
                        })
                        this.fileCompo.forEach(m => {
                            this.formProps[m] = [{
                                name: this.formProps[m].substring(this.formProps[m].lastIndexOf("/") + 1),
                                url: location.protocol + '//' + location.host + this.formProps[m]
                            }]
                        })
                        this.editDialogVisible = true
                        this.operation = false
                    } else {
                        this.$message({message: res.message, type: 'error'})
                    }
                })
            },
            // 批量删除
            handleDelete: function (data) {
                data.params.sysFormId = this.formId;
                data.params.fileCompo = JSON.stringify(this.fileCompo);
                this.$api.form.deleteDynamicFormData(data.params).then(data.callback);
            },
            // 新增、编辑
            editSubmit: function () {
                this.$refs.fm.clearValidate();
                let params = JSON.parse(JSON.stringify(this.formProps));
                this.arrayValueCompo.forEach(v => {
                    params[v] = JSON.stringify(params[v])
                })
                this.fileCompo.forEach(m => {
                    params[m] = JSON.stringify(params[m])
                })
                params.sysFormId = this.formId;
                params.fileCompo = JSON.stringify(this.fileCompo);
                params.CREATE_USER = JSON.parse(sessionStorage.getItem('user')).userCode;
                this.$refs.fm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true
                            this.$api.form.saveDynamicFormData(params).then((res) => {
                                if (res.success) {
                                    this.editLoading = false;
                                    this.editDialogVisible = false;
                                    this.$refs[this.table.tableId].reloadData();
                                    this.$message({message: res.message, type: 'success'});
                                } else {
                                    this.editLoading = false
                                    this.$message({message: res.message, type: 'error'})
                                }
                            })
                        })
                    }
                })
            },
            vChange: function (vm, val) {
                this.selectList.list.forEach(v => {
                    if (v.compoType === 'formElSelecter' || v.compoType === 'formElSelectTree') {
                        if (v.binds.length > 0 && v.binds[0].targetField === vm) {
                            if (typeof (vm) == 'string') {
                                this.$refs[v.key][0].initData(JSON.stringify([val]), v.binds[0].relation, v.binds[0].affectField)
                            } else {
                                this.$refs[v.key][0].initData(JSON.stringify(val), v.binds[0].relation, v.binds[0].affectField)
                            }
                        }
                    }
                })
            },
            toImg() { // 转图片打印
                html2canvas(this.$refs.snapshot, {
                    backgroundColor: null,
                    useCORS: true
                }).then((canvas) => {
                    const url = canvas.toDataURL()
                    printJS({
                        printable: url,
                        type: 'image',
                        documentTitle: '交易快照@MJQ'
                    })
                    console.log(url)
                })
            }
        },
        watch: {
            '$route'(to, from) {
                if (this.$store.state.app.viewOpenStyle !== 'tabs') { // 只有在面包屑模式下才重载已有组件
                    this.formId = to.query.formId;
                    this.initPage();
                }
            }
        }
    }
</script>
