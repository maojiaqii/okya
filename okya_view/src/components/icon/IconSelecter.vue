<template>
  <div id="iconSelectDiv">
    <el-popover ref="popover" placement="right-start" trigger="click" width="300" @show="reset()">
      <el-input v-model="name" @input.native="filterIcons" suffix-icon="el-icon-search"
                placeholder="请输入图标名称"/>
      <div style="height: 300px;overflow: auto;">
        <el-row>
          <el-col v-for="(item,index) in iconList" :key=index :span="6" style="margin-top: 5px;">
            <el-tooltip effect="dark" placement="top-start" :content="item" :open-delay="1000">
              <el-button :icon="item" @click="selectedIcon(item)"/>
            </el-tooltip>
          </el-col>
        </el-row>
      </div>
      <div style="float:right;padding:5px;">
        <el-link type="danger" icon="el-icon-delete" @click="clearSelect">清空</el-link>
      </div>
    </el-popover>
    <el-input v-model="value" v-popover:popover readonly placeholder="请选择图标" clearable
              style="cursor:pointer;">
      <template slot="prepend"><i :class="value"></i></template>
    </el-input>
  </div>
</template>
<script>
    import {icons} from '../../assets/icon/allIcons.js'

    export default {
        name: "IconSelector",
        data() {
            return {
                name: '',
                iconList: icons
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            value: {
                type: String,
                default: ''
            }
        },
        methods: {
            filterIcons() {
                if (this.name) {
                    this.iconList = icons.filter(item => item.includes(this.name))
                } else {
                    this.iconList = icons
                }
            },
            selectedIcon(name) {
                this.$emit('change', name)
                document.body.click()
            },
            clearSelect(){
                this.$emit('change', '')
                document.body.click()
            },
            reset() {
                this.name = ''
                this.iconList = icons
            }
        }
    }
</script>

<style>
</style>
