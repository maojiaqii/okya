<template>
  <div style="width:95%;padding-left: 18px;">
    <!--表格栏-->
    <el-table id="mjqTable" ref="mjqTable" border v-if="refresh" size="mini" style="width:100%;"
              header-cell-class-name="tableStyle"
              @header-contextmenu='headerContextMenu' @header-click='headerClick' @header-dragend="headerDragend">
      <el-table-column align="center" label="序号" width="50" v-if="showRowNum">
        <template slot-scope="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <CusTableColForReportDesign v-for="(column, key) in columns" :column="column" :key="key"/>
    </el-table>
    <!--右键弹出的菜单内容-->
    <!--动态计算菜单出现的位置-->
    <el-dropdown ref="messageDrop" :style="styleD">
      <el-dropdown-menu>
        <el-dropdown-item icon="icon iconfont iconmimabukejian" @click.native="deleteThisColumn">删除此列</el-dropdown-item>
        <el-dropdown-item icon="icon iconfont iconmimakejian" divided @click.native="addChildColumn">增加子列
        </el-dropdown-item>
        <el-dropdown-item icon="icon iconfont iconquxiaoguolv1" @click.native="addColumnBefore">向前插入一列
        </el-dropdown-item>
        <el-dropdown-item icon="icon iconfont iconquxiaoguolv1" @click.native="addColumnAfter">向后插入一列</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
    import CusTableColForReportDesign from '../compo/CusTableColForReportDesign'

    export default {
        name: "CusReport",
        components: {
            CusTableColForReportDesign
        },
        props: {
            columns: Array, // 表格列配置
            showRowNum: {
                type: String,
                default: "true"
            }
        },
        data() {
            return {
                styleD: '', // 显示右键菜单位置
                selectColumn: '', // 操作的列prop
                refresh: true
            }
        },
        methods: {
            headerContextMenu: function (column, event) {
                window.event.returnValue = false   //阻止浏览器自带的右键菜单弹出
                //给整个document绑定click监听事件， 左键单击任何位置执行foo方法
                document.addEventListener('mousedown', this.foo)
                //event对应的是鼠标事件，找到鼠标点击位置的坐标，给菜单定位
                this.styleD = "top:" + event.clientY + "px;" + "left:" + event.clientX + "px;position: fixed;"
                this.selectColumn = column.index;
                this.$refs.messageDrop.show();
            },
            loopColumnsToRemove(obj, j) {
                obj.forEach((item, index, arr) => {
                    if (item.cid == j) {
                        obj.splice(index, 1);
                    } else if (item.children) {
                        this.loopColumnsToRemove(item.children, j)
                    }
                })
            },
            loopColumnsToAdd(obj, j, position) {
                obj.some((item, index) => {
                    if (item.cid == j) {
                        if (position === "before") {
                            obj.splice(index, 0, {
                                cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                                prop: "",
                                label: "列名",
                                width: 80,
                                sortable: "true",
                                visible: "true",
                                filter: "false",
                                sum: "false",
                                align: "center",
                                content: ""
                            });
                        } else if (position === "after") {
                            obj.splice(index + 1, 0, {
                                cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                                prop: "",
                                label: "列名",
                                width: 80,
                                sortable: "true",
                                visible: "true",
                                filter: "false",
                                sum: "false",
                                align: "center",
                                content: ""
                            });
                        } else if (position === "child") {
                            if (!item.children) {
                                item.children = [];
                            }
                            item.children.push({
                                cid: parseInt(Math.random() * (1000000 - 1 + 1) + 1),
                                prop: "",
                                label: "列名",
                                width: 80,
                                sortable: "true",
                                visible: "true",
                                filter: "false",
                                sum: "false",
                                align: "center",
                                content: ""
                            });

                        }
                        return true
                    } else if (item.children) {
                        this.loopColumnsToAdd(item.children, j, position)
                    }
                })
            },
            deleteThisColumn: function () {
                this.refresh = false
                this.loopColumnsToRemove(this.columns, this.selectColumn)
                this.$nextTick(() => {
                    this.refresh = true
                })
            },
            addColumnBefore: function () {
                this.refresh = false
                this.loopColumnsToAdd(this.columns, this.selectColumn, "before")
                this.$nextTick(() => {
                    this.refresh = true
                })
            },
            addColumnAfter: function () {
                this.refresh = false
                this.loopColumnsToAdd(this.columns, this.selectColumn, "after")
                this.$nextTick(() => {
                    this.refresh = true
                })
            },
            addChildColumn: function () {
                this.refresh = false
                this.loopColumnsToAdd(this.columns, this.selectColumn, "child")
                this.$nextTick(() => {
                    this.refresh = true
                })
            },
            foo() {
                this.$refs.messageDrop.hide(); //关闭菜单栏
                document.removeEventListener('mousedown', this.foo)   //解绑click监听，很重要
            },
            headerClick: function (column, event) {
                this.$emit('headerClick', column.index)
            },
            headerDragend: function (newWidth, oldWidth, column, event) {
                this.$emit('headerDragend', {prop: column.index, wid: parseInt(newWidth)})
            }
        },
        mounted() {
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
