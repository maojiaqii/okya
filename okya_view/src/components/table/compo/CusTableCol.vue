<template>
  <el-table-column v-if="(column.visible == 'true' || column.visible == true) && column.children && column.children.length > 0"
                   :show-overflow-tooltip="true"
                   :prop="column.prop"
                   :label="column.label" :fixed="column.fixed" :align="column.align ? column.align : 'center'">
    <CusTableCol v-for="(columnC, index) in column.children" :column="columnC" :key="index" :table-data="tableData"
                 :selectIndex="selectIndex"/>
  </el-table-column>
  <el-table-column
    v-else-if="(column.visible == 'true' || column.visible == true) && (!column.children || column.children.length == 0)"
    :show-overflow-tooltip="true"
    :prop="column.prop" :label="column.label" :width="column.width" :min-width="column.minWidth"
    :sortable="(column.sortable == 'true' || column.sortable == true) ? 'custom' : false"
    :fixed="column.fixed"
    :key="column.prop" :type="column.type" :align="column.align ? column.align : 'center'"
    :filter-method="column.filter == 'true' || column.filter == true ? filterHandler : null"
    :render-header="(h,obj) => renderLastHeader(h,obj,column)">
    <template slot-scope="scope">
      <template v-if="column.editAttr == 'el-input' && scope.$index == selectIndex">
        <el-input size="mini" v-model="scope.row[column.prop]" auto-complete="off" clearable
                  style="width: 100%;"></el-input>
      </template>
      <template v-else-if="column.editAttr == 'el-input-number' && scope.$index == selectIndex">
        <el-input-number size="mini" v-model="scope.row[column.prop]" :precision="column.precision"
                         :step="column.step" :min="column.min" :max="column.max" style="width: 100%;"></el-input-number>
      </template>
      <template v-else-if="column.editAttr == 'el-radio' && scope.$index == selectIndex">
        <tableElRadio v-model="scope.row[column.prop]" :valueFlag="column.valueFlag"
                      :labelFlag="column.labelFlag" :datasource="column.datasource" style="width: 100%;">
        </tableElRadio>
      </template>
      <template v-else-if="column.editAttr == 'el-checkbox' && scope.$index == selectIndex">
        <tableElCheckBox v-model="scope.row[column.prop]" :valueFlag="column.valueFlag"
                         :labelFlag="column.labelFlag" :datasource="column.datasource" style="width: 100%;">
        </tableElCheckBox>
      </template>
      <template v-else-if="column.editAttr == 'el-select' && scope.$index == selectIndex">
        <tableElSelecter v-model="scope.row[column.prop]" :valueFlag="column.valueFlag"
                         :labelFlag="column.labelFlag" :datasource="column.datasource"
                         :staticOptions="column.staticOptions" style="width: 100%;">
        </tableElSelecter>
      </template>
      <template v-else-if="column.editAttr == 'el-tree' && scope.$index == selectIndex">
        <tableElSelectTree v-model="scope.row[column.prop]" :datasource="column.datasource" style="width: 100%;">
        </tableElSelectTree>
      </template>
      <template v-else-if="column.editAttr == 'el-rate' && scope.$index == selectIndex">
        <el-rate
          size="mini"
          v-model="scope.row[column.prop]"
          show-score
          allow-half
          :max="column.max"
          text-color="#ff9900"
          style="width: 100%;">
        </el-rate>
      </template>
      <template v-else-if="column.editAttr == 'el-switch' && scope.$index == selectIndex">
        <el-switch
          v-model="scope.row[column.prop]"
          size="mini"
          active-color="#13ce66"
          inactive-color="#ff4949"
          :active-text="column.activeText"
          :inactive-text="column.inActiveText"
          :active-value="column.activeValue"
          :inactive-value="column.inActiveValue"
          style="width: 100%;">
        </el-switch>
      </template>
      <template v-else-if="column.editAttr == 'el-date' && scope.$index == selectIndex">
        <el-date-picker
          v-model="scope.row[column.prop]"
          size="mini"
          :type="column.type"
          :format="column.dateformat"
          :value-format="column.dateformat"
          style="width: 100%;">
        </el-date-picker>
      </template>
      <template v-else-if="column.editAttr === 'el-time' && scope.$index == selectIndex">
        <tableElTimePicker
          v-model="scope.row[column.prop]"
          :format="column.timeformat"
          style="width: 100%;">
        </tableElTimePicker>
      </template>
      <template v-else>
        <slot :name="column.prop" v-bind:colData="scope.row[column.prop]">
          <span>{{scope.row[column.prop]}}</span>
        </slot>
      </template>
    </template>
  </el-table-column>
</template>

<script>
    import tableElRadio from "./tableElRadio";
    import tableElCheckBox from "./tableElCheckBox";
    import tableElSelecter from "./tableElSelecter";
    import tableElSelectTree from "./tableElSelectTree";
    import tableElTimePicker from "./tableElTimePicker";

    export default {
        components: {
            tableElRadio,
            tableElCheckBox,
            tableElSelecter,
            tableElSelectTree,
            tableElTimePicker
        },
        name: "CusTableCol",
        props: {
            column: {
                type: Object,
                required: true
            },
            tableData: Array,
            selectIndex: Number //可编辑表格时用到
        },
        methods: {
            filterDataHas: function (fData, fDatas) {
                let boo = false;
                fDatas.forEach(m => {
                    if (m.text == fData.text) {
                        boo = true
                    }
                })
                return boo;
            },
            renderLastHeader(h, {column, $index}, col) {
                column.sum = col.sum
                if (col.filter) {
                    let fDatas = [];
                    if (this.tableData) {
                        this.tableData.forEach(v => {
                            let fData = {};
                            fData.text = v[col.prop];
                            fData.value = v[col.prop];
                            if (fDatas.length > 0) {
                                if (!this.filterDataHas(fData, fDatas)) {
                                    fDatas.push(fData)
                                }
                            } else {
                                fDatas.push(fData)
                            }
                        })
                    }
                    column.filters = fDatas
                }
                if (col.content) {
                    return h('span', [
                        h('span', col.label),
                        h('el-tooltip', {
                            props: {
                                placement: 'top-start',
                                effect: 'dark',
                                content: col.content
                            }
                        }, [
                            h('i', {class: 'el-icon-info'}, '')
                        ])
                    ])
                } else {
                    return h('span', [
                        h('span', col.label)
                    ])
                }
            },
            filterHandler(value, row, column) {
                const property = column['property'];
                return row[property] === value;
            }
        }
    }
</script>

<style scoped>

</style>
