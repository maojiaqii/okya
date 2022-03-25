<template>
  <div style="width:95%;padding-left: 18px;">
    <!--表格栏-->
    <el-table id="mjqTable" ref="mjqTable" border :data="data" stripe highlight-current-row
              :default-expand-all="expandAll" v-if="refresh"
              @selection-change="selectionChange"
              @sort-change="sortChange" :show-summary="showSum" :summary-method="getSummaries"
              v-loading="loading" :max-height="maxHeight" :size="size" style="width:100%;"
              element-loading-text="拼命加载中"
              element-loading-background="rgba(0, 0, 0, 0.2)" header-cell-class-name="tableStyle"
              @header-contextmenu='headerContextMenu' :row-key="rowKey" :tree-props="{children: 'children'}">
      <div slot="empty" style="height: calc(60vh);line-height: calc(60vh);">
        <el-image
          style="width: 200px; height: 200px"
          :src="require('../../../assets/empty.svg')"
          fit="cover">
        </el-image>
      </div>
      <el-table-column align="center" type="selection" width="50"
                       v-if="showCheckBox"></el-table-column>
      <el-table-column align="center" label="序号" width="50" v-if="showRowNum">
        <template slot-scope="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <CusTableCol v-for="(column, key) in columns" :column="column" :key="key" :tableData="data">
        <template v-slot:[column.prop]="slotProps">
          <template>
            <slot :name="column.prop" v-bind:colData="slotProps.colData"/>
          </template>
        </template>
      </CusTableCol>
      <el-table-column label="操作" min-width="100" align="center" fixed="right" v-if="showOperation">
        <template slot-scope="scope">
          <CusCircleButton :btnName="editTip" :perms="permsEdit" :size="size" type="primary"
                           @click="handleEdit(scope.$index, scope.row)"/>
          <CusCircleButton :btnName="deleteTip" :perms="permsDelete" :size="size" type="danger"
                           @click="handleDelete(scope.$index, scope.row)"/>
        </template>
      </el-table-column>
    </el-table>
    <!--分页栏-->
    <div class="toolbar" style="padding:10px;">
      <CusButton :btnName="'批量' + deleteTip" :perms="permsDelete" :size="size" type="danger"
                 @click="handleBatchDelete()"
                 :disabled="this.selections.length===0" style="float:left;"/>
      <CusButton btnName="导出Excel" :perms="permsExport" :size="size" type="primary"
                 @click="exportExcel()" style="float:left;"/>
      <el-pagination v-if="showPagination" layout="prev, pager, next, total, sizes, jumper"
                     @current-change="refreshPageNo"
                     @size-change="refreshPageSize"
                     :page-sizes="[20, 50, 100, 200]"
                     :current-page="pageNum" :page-size="pageSize" :total="totalCount"
                     style="float:right;">
      </el-pagination>
    </div>
    <!--右键弹出的菜单内容-->
    <!--动态计算菜单出现的位置-->
    <el-dropdown ref="messageDrop" :style="styleD">
      <el-dropdown-menu>
        <el-dropdown-item icon="icon iconfont iconmimabukejian" @click.native="hideThisColumn">隐藏此列</el-dropdown-item>
        <el-dropdown-item icon="icon iconfont iconmimakejian" divided @click.native="showAllColumn">显示全部列
        </el-dropdown-item>
        <el-dropdown-item icon="icon iconfont iconquxiaoguolv1" @click.native="clearFilter">清除所有过滤</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
    import FileSaver from 'file-saver'
    import XLSX from 'xlsx'
    import CusTableCol from '../compo/CusTableCol'

    export default {
        name: "CusTable",
        components: {
            CusTableCol
        },
        props: {
            columns: Array, // 表格列配置
            data: Array, // 表格分页数据
            totalCount: Number, // 总条数
            permsEdit: String,  // 编辑权限标识
            permsDelete: String,  // 删除权限标识
            permsExport: String,  // 导出excel标识
            editTip: {
                type: String,
                default: "编辑"
            }, // 编辑按钮提示
            deleteTip: {
                type: String,
                default: "删除"
            }, // 编辑按钮提示
            showSum: { // 显示合计行
                type: Boolean,
                default: false
            },
            size: { // 尺寸样式
                type: String,
                default: 'mini'
            },
            align: {  // 文本对齐方式
                type: String,
                default: 'center'
            },
            maxHeight: {  // 表格最大高度
                type: Number,
                default: 450
            },
            showOperation: {  // 是否显示操作组件
                type: Boolean,
                default: false
            },
            showCheckBox: {  // 是否显示操作组件
                type: Boolean,
                default: true
            },
            showPagination: {  // 是否显示操作组件
                type: Boolean,
                default: true
            },
            rowKey: {
                type: String,
                default: ''
            },
            showRowNum: {
                type: Boolean,
                default: true
            },
            expandAll: {
                type: Boolean,
                default: false
            },
            datasource: {
                type: String,
                default: ''
            },
            loading: true,  // 加载标识
        },
        data() {
            return {
                // 分页信息
                pageNum: 1,
                pageSize: 20,
                selections: [],  // 列表选中行
                sortProp: '',
                sortOrder: '',
                styleD: '', // 显示右键菜单位置
                hideColumn: [], // 手动隐藏列
                selectColumn: '', // 操作的列prop
                refresh: true
            }
        },
        methods: {
            clearFilter: function () {
                this.$refs.mjqTable.clearFilter();
            },
            // 分页查询
            findPage: function () {
                this.$emit('findPage', {
                    datasource: this.datasource,
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    sortProp: this.sortProp,
                    sortOrder: this.sortOrder
                })
            },
            headerContextMenu: function (column, event) {
                window.event.returnValue = false   //阻止浏览器自带的右键菜单弹出
                //给整个document绑定click监听事件， 左键单击任何位置执行foo方法
                document.addEventListener('mousedown', this.foo)
                //event对应的是鼠标事件，找到鼠标点击位置的坐标，给菜单定位
                this.styleD = "top:" + event.clientY + "px;" + "left:" + event.clientX + "px;position: fixed;"
                this.selectColumn = column.property;
                this.$refs.messageDrop.hide();
                this.$refs.messageDrop.show();
            },
            loopColumns(obj, j, bool) {
                obj.filter(i => {
                    if (i.prop == j) {
                        i.visible = bool
                    } else if (i.children) {
                        this.loopColumns(i.children, j, bool)
                    }
                })
            },
            hideThisColumn: function () {
                this.refresh = false
                this.hideColumn.push(this.selectColumn);
                this.loopColumns(this.columns, this.selectColumn, false)
                this.$nextTick(() => {
                    this.refresh = true
                })
            },
            showAllColumn: function () {
                this.refresh = false
                this.hideColumn.filter(j => {
                    this.loopColumns(this.columns, j, true)
                })
                this.hideColumn = [];
                this.$nextTick(() => {
                    this.refresh = true
                })
            },
            foo() {
                this.$refs.messageDrop.hide(); //关闭菜单栏
                document.removeEventListener('mousedown', this.foo)   //解绑click监听，很重要
            },
            // 选择切换
            selectionChange: function (selections) {
                this.selections = selections
            },
            // 排序
            sortChange(column) {
                this.sortProp = column.prop
                this.sortOrder = column.order
                this.$emit('findPage', {
                    datasource: this.datasource,
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    sortProp: this.sortProp,
                    sortOrder: this.sortOrder
                })
            },
            // 页数刷新
            refreshPageNo: function (pageNum) {
                this.pageNum = pageNum
                this.findPage()
            },
            // 行数刷新
            refreshPageSize: function (pageSize) {
                this.pageSize = pageSize
                this.pageNum = 1
                this.findPage()
            },
            // 编辑
            handleEdit: function (index, row) {
                this.$emit('handleEdit', {index: index, row: row})
            },
            // 删除
            handleDelete: function (index, row) {
                this.delete(row)
            },
            // 批量删除
            handleBatchDelete: function () {
                this.delete(this.selections)
            },
            // 删除操作
            delete: function (datas) {
                this.$confirm('确认' + this.deleteTip + '选中记录吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    let param = []
                    if (datas.length > 0) {
                        for (let i = 0; i < datas.length; i++) {
                            param.push(datas[i])
                        }
                    } else {
                        param.push(datas)
                    }
                    let callback = res => {
                        if (res.success) {
                            this.$message({message: res.message, type: 'success'})
                            this.findPage()
                        } else {
                            this.$message({message: res.message, type: 'error'})
                        }
                    }
                    this.$emit('handleDelete', {params: {"datas": JSON.stringify(param)}, callback: callback})
                }).catch(() => {
                })
            },
            getSummaries(param) {
                const {columns, data} = param;
                const sums = [];
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '合计';
                        return;
                    }
                    if (column.sum) {
                        const values = data.map(item => Number(item[column.property]));
                        if (!values.every(value => isNaN(value))) {
                            sums[index] = values.reduce((prev, curr) => {
                                const value = Number(curr);
                                if (!isNaN(value)) {
                                    return prev + curr;
                                } else {
                                    return prev;
                                }
                            }, 0);
                        }
                    }
                });
                return sums;
            },
            //导出数据
            exportExcel() {
                //  判断要导出的节点中是否有fixed的表格，如果有，转换excel时先将该dom移除，然后append回去，
                let fix = document.querySelector('.el-table__fixed-right');
                let wb;
                var xlsxParam = {raw: true};//转换成excel时，使用原始的格式
                if (fix) {
                    wb = XLSX.utils.table_to_book(document.querySelector('#mjqTable').removeChild(fix), xlsxParam);
                    document.querySelector('#mjqTable').appendChild(fix);
                } else {
                    wb = XLSX.utils.table_to_book(document.querySelector('#mjqTable'), xlsxParam);
                }
                let wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'});
                try {
                    FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), 'Excel_' + new Date().toLocaleString() + '.xlsx');  //table是自己导出文件时的命名，随意
                } catch (e) {
                    console.log(e, wbout)
                }
                return wbout
            },
            reloadData() {
                this.sortProp = ''
                this.sortOrder = ''
                this.pageNum = 1
                this.$emit('findPage', {
                    datasource: this.datasource,
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    sortProp: this.sortProp,
                    sortOrder: this.sortOrder
                })
            }
        },
        mounted() {
            if (!this.showPagination) { // 是否分页，不分页则全不显示，此处写死，后去再优化
                this.pageSize = 10000
            }
            this.findPage();
        }
    }
</script>

<style lang="scss">
  .tableStyle {
    background-color: rgba(25, 137, 250, 0.05) !important;
    color: rgba(0, 0, 0, 0.47);
    font-weight: 700;
  }
</style>
