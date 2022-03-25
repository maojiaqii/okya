<template>
  <div>
    <!-- 按钮区域 -->
    <div style="padding-left: 20px; padding-top: 20px;">
      <el-button type="primary" icon="el-icon-search" v-on:click="searchBt" plain size="small">查询</el-button>
      <el-button type="primary" icon="el-icon-download" @click="exportExcel()" plain size="small">导出</el-button>
      <el-button type="primary" icon="el-icon-printer" plain size="small" v-print="doPrint">打印</el-button>
    </div>

    <!-- 查询条件区域 -->
    <div style="padding-left: 20px; padding-top: 20px;">
      <el-form :inline="true" :model="filters" size="small">
        <el-form-item v-for="(element, index) in searches" :label="element.SEARCH_NAME" :key="index">
          <component :is="element.SEARCH_COMPO" v-model="filters[element.SEARCH_FIELD + ',' + element.SEARCH_CONDITION]"
                     :dataSource="element.DATA_SOURCE"></component>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据展示表格区域 -->
    <CusTable id="print" :ref="table.REPORT_ID" :max-height="table.maxHeight" :show-pagination="table.showPagination"
              :show-row-num="table.showRowNum" :show-sum="table.showSum" :size="table.size"
              :data="pageResult.content" :totalCount="pageResult.count" :columns="columns"
              :datasource="table.dataSource"
              @findPage="findPage">
    </CusTable>
  </div>
</template>

<script>
    import FileSaver from 'file-saver'
    import XLSX from 'xlsx'

    import searchElInput from "../../form/compo/searchElInput";
    import searchElDatePicker from "../../form/compo/searchElDatePicker";
    import searchElTimePicker from "../../form/compo/searchElTimePicker";
    import searchElSelecter from "../../form/compo/searchElSelecter";
    import searchElSelectTree from "../../form/compo/searchElSelectTree";

    export default {
        name: 'DnRe',
        components: {
            searchElInput,
            searchElSelectTree,
            searchElDatePicker,
            searchElTimePicker,
            searchElSelecter
        },
        data() {
            return {
                reportId: '',
                searches: [],
                filters: {},
                table: {},
                columns: [],
                pageResult: {},
                needYear: 'Y',
                doPrint: {id: "print", popTitle: ""}
            }
        },
        mounted() {
            this.reportId = this.$route.query.reportId;
            this.initPage();
        },
        methods: {
            exportExcel() {
                //  判断要导出的节点中是否有fixed的表格，如果有，转换excel时先将该dom移除，然后append回去，
                let fix = document.querySelector('.el-table__fixed-right');
                let wb;
                var xlsxParam = {raw: true};//转换成excel时，使用原始的格式
                if (fix) {
                    wb = XLSX.utils.table_to_book(document.querySelector('#print').removeChild(fix), xlsxParam);
                    document.querySelector('#print').appendChild(fix);
                } else {
                    wb = XLSX.utils.table_to_book(document.querySelector('#print'), xlsxParam);
                }
                let wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'});
                try {
                    FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), 'Excel_' + new Date().toLocaleString() + '.xlsx');  //table是自己导出文件时的命名，随意
                } catch (e) {
                    console.log(e, wbout)
                }
                return wbout
            },
            // 初始化页面
            initPage() {
                this.$api.report.initPage({
                    'reportId': this.reportId
                }).then((res) => {
                    if (res.success) {
                        let page = JSON.parse(res.message);
                        this.needYear = page.NEED_YEAR;
                        this.searches = JSON.parse(page.searchField);
                        this.table.REPORT_ID = page.REPORT_ID;
                        this.doPrint.popTitle = page.REPORT_NAME;
                        this.table.showPagination = JSON.parse(page.SHOW_PAGINATION);
                        this.table.showRowNum = JSON.parse(page.SHOW_ROW_NUM);
                        this.table.showSum = JSON.parse(page.SHOW_SUM);
                        this.table.maxHeight = Number(page.MAX_HEIGHT);
                        this.table.size = page.size;
                        this.table.dataSource = page.DATA_SOURCE;
                        this.columns = JSON.parse(page.COLUMN_INFOS);
                        this.searches.forEach(v => {
                            this.filters[v.SEARCH_FIELD + ',' + v.SEARCH_CONDITION] = '';
                        });
                    } else {
                        this.$message({message: res.message, type: 'error'});
                    }
                })
            },
            searchBt() {
                this.$refs[this.table.REPORT_ID].pageNum = 1
                this.$refs[this.table.REPORT_ID].sortProp = ''
                this.$refs[this.table.REPORT_ID].sortOrder = ''
                this.$api.common.findPage({
                    'needYear': this.needYear,
                    'searchProperties': JSON.stringify(this.filters),
                    'tableName': this.$refs[this.table.REPORT_ID].datasource,
                    'pageNum': 1,
                    'pageSize': this.$refs[this.table.REPORT_ID].pageSize,
                    'sortProp': '',
                    'sortOrder': ''
                }).then((res) => {
                    this.pageResult = JSON.parse(res.message)
                })
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

            }
        },
        watch: {
            '$route'(to, from) {
                if (this.$store.state.app.viewOpenStyle !== 'tabs') { // 只有在面包屑模式下才重载已有组件
                    this.reportId = to.query.reportId;
                    this.initPage();
                }
            }
        }
    }
</script>
