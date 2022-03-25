<template>
  <div id="popTreeDiv">
    <el-popover ref="popover" :placement="placement" :disabled="disabled" trigger="click" width="300">
      <el-input
        placeholder="输入关键字进行过滤"
        v-model="filterText">
      </el-input>
      <div id="treeDiv" style="float:left;width: 100%;height: 200px; overflow: auto;padding:5px;">
        <el-tree
          class="filter-tree"
          :data="data"
          :node-key="nodeKey"
          ref="popupTree"
          :accordion="accordion"
          :check-strictly="checkStrictly"
          show-checkbox
          :expand-on-click-node=true
          :default-expanded-keys="singleSelect ? [cValue] : cValue"
          @check-change="checkChange"
          :filter-node-method="filterNode">
        </el-tree>
      </div>
      <div style="float:right;padding:5px;">
        <el-link type="danger" icon="el-icon-delete" @click="clearSelect">清空</el-link>
      </div>
    </el-popover>
    <el-input v-model="showText" v-popover="disabled ? 'popover1' : 'popover'" readonly :placeholder="placeholder" :size="size" :disabled="disabled"
              style="cursor:pointer;">
    </el-input>
  </div>
</template>

<script>
    export default {
        name: 'PopupTreeInput',
        data() {
            return {
                cValue: this.value,
                filterText: '',
                showText: ''
            };
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            data: {
                type: Array,
                default: []
            },
            value: {},
            nodeKey: {
                type: String,
                default: 'id'
            },
            placeholder: {
                type: String,
                default: '点击选择内容'
            },
            accordion: {
                type: Boolean,
                default: true
            },
            placement: {
                type: String,
                default: 'right-start'
            },
            singleSelect: { // 是否单选，默认单选
                type: Boolean,
                default: true
            },
            checkStrictly: { // 是否严格的遵循父子不互相关联的做法
                type: Boolean,
                default: true
            },
            disabled: {
                type: Boolean,
                default: false
            },
            size: {
                type: String,
                default: 'medium'
            }
        },
        methods: {
            // 单选时，事件处理
            checkChange(data, checked, node) {
                if (this.singleSelect) {
                    if (checked === true) {
                        this.$refs.popupTree.setCheckedKeys([data[this.nodeKey]]);
                    }
                    this.$emit('change', this.$refs.popupTree.getCheckedKeys()[0])
                } else {
                    this.$emit('change', this.$refs.popupTree.getCheckedKeys())
                }
            },
            clearSelect: function () {
                this.$refs.popupTree.setCheckedKeys([]);
                if (this.singleSelect) {
                    this.$emit('change', '')
                } else {
                    this.$emit('change', [])
                }
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            }
        },
        watch: {
            filterText(val) {
                this.$refs.popupTree.filter(val);
            },
            value(newValue, oldValue) {
                if (this.singleSelect) {
                    this.$refs.popupTree.setCheckedKeys([newValue]);
                } else {
                    this.$refs.popupTree.setCheckedKeys(newValue);
                }
                let checkedData = this.$refs.popupTree.getCheckedNodes()
                this.showText = ''
                checkedData.forEach(v => {
                    this.showText += v.label + ','
                })
                this.showText = this.showText.substring(0, this.showText.length - 1)
            }
        },
        mounted() {
            if (this.cValue != null) {
                if (this.singleSelect) {
                    this.$refs.popupTree.setCheckedKeys([this.cValue]);
                } else {
                    this.$refs.popupTree.setCheckedKeys(this.cValue);
                }
                let checkedData = this.$refs.popupTree.getCheckedNodes()
                this.showText = ''
                checkedData.forEach(v => {
                    this.showText += v.label + ','
                })
                this.showText = this.showText.substring(0, this.showText.length - 1)
            }
        }
    }
</script>
