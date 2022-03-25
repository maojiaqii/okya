<template>
  <div>
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="主表单设计"></el-step>
      <el-step title="子表单设计"></el-step>
      <el-step title="关联约束"></el-step>
      <el-step title="完成"></el-step>
    </el-steps>
    <transition name="el-zoom-in-center">
      <el-row key='formDs' :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
              v-show="active == 0">
        <el-col :span="1">
        </el-col>
        <el-col :span="6">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>控件区域</span>
            </div>
            <div style="height: 500px;">
              <el-scrollbar style="height: 100%;">
                <draggable :list="compoList" :options="{group:{name: flags,pull:'clone'}, sort: false}"
                           @start="start1"
                           @end="end1"
                           class="dragFromArea"
                           style="height: 100px">
                  <div class="list-complete-item" v-for="element in compoList" :key="element.compoType">
                    <div class="list-complete-item-handle2"><i :class="element.icon"></i> {{element.name}}</div>
                  </div>
                </draggable>
              </el-scrollbar>
            </div>
          </el-card>
        </el-col>
        <el-col :span="11">
          <el-card class="box-card">
            <div slot="header">
              <span>设计区域</span>
              <el-upload
                style="float: right; margin-left: 10px;"
                class="upload-demo"
                ref="upload"
                action="a"
                accept=".json"
                :before-upload="beforeUpload"
                :on-change="uploadJson"
                :auto-upload="false"
                :show-file-list="false"
                :limit="1"
                v-show="isNew">
                <el-link icon="el-icon-upload2">导入JSON</el-link>
              </el-upload>
              <el-link style="float: right;margin-left: 10px;" type="success" icon="el-icon-download"
                       @click="consoleJson">生成JSON
              </el-link>
              <el-link style="float: right;margin-left: 10px;" type="primary" icon="el-icon-view"
                       @click="preview">预览
              </el-link>
              <el-link style="float: right;margin-left: 10px;" type="danger" icon="el-icon-delete"
                       @click="clearComponent">清空
              </el-link>
              <el-link style="float: right;margin-left: 10px;" type="danger" icon="el-icon-delete"
                       @click="removeComponent">删除
              </el-link>
            </div>
            <div class="grid-content">
              <el-scrollbar style="height: 100%;">
                <div class="form-empty" v-if="selectList.list.length == 0">从左侧拖拽来添加字段</div>
                <el-form :disabled="true" :label-position="selectList.labelPosition" :size="selectList.size">
                  <el-row>
                    <!--视图区-->
                    <draggable :list="selectList.list" :options="{group:'article'}"
                               @start="start22"
                               @end="end22"
                               class="dragArea11"
                               style="height: 100px">
                      <component v-for="element in selectList.list" :key="element.id" class="list-complete-item1"
                                 :span="element.span"
                                 @click.native="compoSelect($event, element)"
                                 :is="element.compoType" :label="element.compoTitle" :prop="element.key"
                                 :model="element.model"
                                 :properties="element.compoProp">
                      </component>
                    </draggable>
                  </el-row>
                </el-form>
              </el-scrollbar>
            </div>
          </el-card>
        </el-col>
        <el-col :span="5">
          <el-card class="box-card">
            <!--选项卡-->
            <el-tabs value="fieldProps">
              <el-tab-pane key='1' label="字段属性" name="fieldProps" style="height: 500px;">
                <el-scrollbar style="height: 100%;">
                  <el-form label-width="80px" label-position="left" :model="fieldProps"
                           size="mini">
                    <el-form-item label="字段标识" prop="key">
                      <el-input v-model="fieldProps.key" auto-complete="off" placeholder="需确保字段标识唯一"
                                :disabled="!isNew"></el-input>
                    </el-form-item>
                    <el-form-item label="标题" prop="compoTitle">
                      <el-input v-model="fieldProps.compoTitle" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="控件区块" prop="compoWidth">
                      <el-input-number v-model="fieldProps.span" :min="1" :max="24"
                                       :disabled="fieldProps.compoType == 'formElEditTable'">
                      </el-input-number>
                      <b>/24</b>
                    </el-form-item>
                    <el-form-item label="标题宽度" prop="titleWidth">
                      <el-checkbox v-model="fieldProps.compoProp.titleWidth !== 'auto'" @change="tWC"
                                   :disabled="fieldProps.compoType == 'formElEditTable'">自定义
                      </el-checkbox>
                      <el-input v-model="fieldProps.compoProp.titleWidth"
                                :disabled="fieldProps.compoProp.titleWidth == 'auto' || fieldProps.compoType == 'formElEditTable'"/>
                    </el-form-item>
                    <el-form-item label="是否显示" prop="hidden">
                      <el-switch v-model="fieldProps.compoProp.hidden" active-color="#13ce66"
                                 inactive-color="#ff4949"
                                 active-text="是"
                                 inactive-text="否"/>
                    </el-form-item>
                    <el-form-item label="是否编辑" prop="disabled">
                      <el-switch v-model="fieldProps.compoProp.disabled" inactive-color="#13ce66"
                                 active-color="#ff4949"
                                 active-text="否"
                                 inactive-text="是"/>
                    </el-form-item>
                    <el-form-item label="是否必填" prop="required">
                      <el-switch v-model="fieldProps.compoProp.required" active-color="#13ce66"
                                 inactive-color="#ff4949"
                                 active-text="是"
                                 inactive-text="否"/>
                    </el-form-item>
                    <el-divider/>
                    <el-form-item label="占位内容" prop="placeholder"
                                  v-show="fieldProps.compoProp.placeholder !== undefined">
                      <el-input v-model="fieldProps.compoProp.placeholder" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="是否密码" prop="showPassword"
                                  v-show="fieldProps.compoProp.showPassword !== undefined">
                      <el-switch v-model="fieldProps.compoProp.showPassword" active-color="#13ce66"
                                 inactive-color="#ff4949"
                                 active-text="是"
                                 inactive-text="否"/>
                    </el-form-item>
                    <el-form-item label="数据源" prop="remoteOption"
                                  v-show="fieldProps.compoProp.remoteOption !== undefined">
                      <el-input v-model="fieldProps.compoProp.remoteOption" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="remoteOptionValue" v-show="fieldProps.compoProp.value !== undefined">
                      <el-input v-model="fieldProps.compoProp.value" auto-complete="off">
                        <template slot="prepend">值</template>
                      </el-input>
                    </el-form-item>
                    <el-form-item prop="remoteOptionLabel" v-show="fieldProps.compoProp.label !== undefined">
                      <el-input v-model="fieldProps.compoProp.label" auto-complete="off">
                        <template slot="prepend">标签</template>
                      </el-input>
                    </el-form-item>
                    <el-form-item label="最小值" prop="min" v-show="fieldProps.compoProp.min !== undefined">
                      <el-input-number v-model="fieldProps.compoProp.min"></el-input-number>
                    </el-form-item>
                    <el-form-item label="最大值" prop="max" v-show="fieldProps.compoProp.max !== undefined">
                      <el-input-number v-model="fieldProps.compoProp.max"></el-input-number>
                    </el-form-item>
                    <el-form-item label="步长" prop="step" v-show="fieldProps.compoProp.step !== undefined">
                      <el-input-number v-model="fieldProps.compoProp.step"></el-input-number>
                    </el-form-item>
                    <el-form-item label="精度" prop="precision" v-show="fieldProps.compoProp.precision !== undefined">
                      <el-input-number v-model="fieldProps.compoProp.precision" :min="0"></el-input-number>
                    </el-form-item>
                    <el-form-item label="打开描述" prop="activeText" v-show="fieldProps.compoProp.activeText !== undefined">
                      <el-input v-model="fieldProps.compoProp.activeText" auto-complete="off">
                      </el-input>
                    </el-form-item>
                    <el-form-item label="打开值" prop="activeValue"
                                  v-show="fieldProps.compoProp.activeValue !== undefined">
                      <el-input v-model="fieldProps.compoProp.activeValue" auto-complete="off"
                                @change="setSwitchDefault">
                      </el-input>
                    </el-form-item>
                    <el-form-item label="关闭描述" prop="inAactiveText"
                                  v-show="fieldProps.compoProp.inActiveText !== undefined">
                      <el-input v-model="fieldProps.compoProp.inActiveText" auto-complete="off">
                      </el-input>
                    </el-form-item>
                    <el-form-item label="关闭值" prop="inActiveValue"
                                  v-show="fieldProps.compoProp.inActiveValue !== undefined">
                      <el-input v-model="fieldProps.compoProp.inActiveValue" auto-complete="off">
                      </el-input>
                    </el-form-item>
                    <el-form-item label="显示类型" prop="type" v-show="fieldProps.compoProp.type !== undefined">
                      <el-select v-model="fieldProps.compoProp.type" filterable clearable>
                        <el-option label="year" value="year"/>
                        <el-option label="month" value="month"/>
                        <el-option label="date" value="date"/>
                        <el-option label="datetime" value="datetime"/>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="格式化" prop="format" v-show="fieldProps.compoProp.format !== undefined">
                      <el-input v-model="fieldProps.compoProp.format" auto-complete="off">
                      </el-input>
                    </el-form-item>
                    <el-form-item label="是否单选" prop="single" v-show="fieldProps.compoProp.single !== undefined">
                      <el-switch v-model="fieldProps.compoProp.single" @change="isSingle"
                                 active-color="#13ce66"
                                 inactive-color="#ff4949"
                                 active-text="是"
                                 inactive-text="否"/>
                    </el-form-item>
                    <el-form-item label="是否多选" prop="many" v-show="fieldProps.compoProp.many !== undefined">
                      <el-switch v-model="fieldProps.compoProp.many"
                                 active-color="#13ce66"
                                 inactive-color="#ff4949"
                                 active-text="是"
                                 inactive-text="否"
                                 disabled/>
                    </el-form-item>
                    <el-form-item label="文件上限" prop="fileCount" v-show="fieldProps.compoProp.fileCount !== undefined">
                      <el-input-number v-model="fieldProps.compoProp.fileCount" :min="1" disabled></el-input-number>
                    </el-form-item>
                    <el-form-item label="说明文字" prop="tip" v-show="fieldProps.compoProp.tip !== undefined">
                      <el-input v-model="fieldProps.compoProp.tip" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="文字样式" prop="style" v-show="fieldProps.compoProp.style !== undefined">
                      <el-input v-model="fieldProps.compoProp.style" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="显示位置" prop="align" v-show="fieldProps.compoProp.align !== undefined">
                      <el-select v-model="fieldProps.compoProp.align" clearable>
                        <el-option label="left" value="left"/>
                        <el-option label="center" value="center"/>
                        <el-option label="right " value="right "/>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="最大高度" prop="maxHeight" v-show="fieldProps.compoProp.maxHeight !== undefined">
                      <el-input-number v-model="fieldProps.compoProp.maxHeight"></el-input-number>
                    </el-form-item>
                    <el-form-item label="新增标识" prop="permsAddOne"
                                  v-show="fieldProps.compoProp.permsAddOne !== undefined">
                      <el-input v-model="fieldProps.compoProp.permsAddOne" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="删除标识" prop="permsDelete"
                                  v-show="fieldProps.compoProp.permsDelete !== undefined">
                      <el-input v-model="fieldProps.compoProp.permsDelete" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="编辑标识" prop="permsEdit" v-show="fieldProps.compoProp.permsEdit !== undefined">
                      <el-input v-model="fieldProps.compoProp.permsEdit" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="撤销标识" prop="permsCancelDelete"
                                  v-show="fieldProps.compoProp.permsCancelDelete !== undefined">
                      <el-input v-model="fieldProps.compoProp.permsCancelDelete" auto-complete="off"></el-input>
                    </el-form-item>
                  </el-form>
                </el-scrollbar>
              </el-tab-pane>
              <el-tab-pane key='2' label="表单属性" name="formProps" style="height: 500px;">
                <el-form label-width="80px" label-position="left" :model="selectList"
                         size="mini">
                  <el-form-item label="表单编号" prop="formId">
                    <el-input v-model="selectList.formId" auto-complete="off" placeholder="需确保表单编号唯一"
                              :disabled="!isNew"></el-input>
                  </el-form-item>
                  <el-form-item label="表单名称" prop="formName">
                    <el-input v-model="selectList.formName" auto-complete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="对齐方式" prop="labelPosition">
                    <el-select v-model="selectList.labelPosition">
                      <el-option label="left" value="left"/>
                      <el-option label="right" value="right"/>
                      <el-option label="top" value="top"/>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="部件尺寸" prop="size">
                    <el-select v-model="selectList.size">
                      <el-option label="medium" value="medium"/>
                      <el-option label="small" value="small"/>
                      <el-option label="mini" value="mini"/>
                    </el-select>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
        <el-col :span="1">
          <el-button @click="next" size="mini" icon="el-icon-arrow-right" circle plain/>
        </el-col>
      </el-row>
    </transition>

    <transition name="el-zoom-in-center">
      <el-row key='fieldRe' :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
              v-show="active == 1">
        <el-col :span="1">
          <el-button @click="back" size="mini" icon="el-icon-arrow-left" circle plain/>
        </el-col>
        <el-col :span="4">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>主表单字段</span>
            </div>
            <div style="height: 500px;">
              <el-scrollbar style="height: 100%;">
                <div class="list-complete-item0" v-for="(element, index) in selectList.list"
                     v-if="element.compoType == 'formElEditTable'"
                     :key="index" @click="editableSelect($event, element)">
                  <i :class="element.icon"></i> {{element.compoTitle}}
                </div>
              </el-scrollbar>
            </div>
          </el-card>
        </el-col>
        <el-col :span="18">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>子表单字段</span>
            </div>
            <div style="height: 500px;">
              <el-scrollbar style="height: 100%;">
                <!--表格内容栏-->
                <CusEditTable ref="childFormTable" permsEdit="sys:FormDb:edit" :lineNumber="false"
                              permsDelete="sys:FormDb:delete" permsAddOne="sys:FormDb:addOne"
                              permsCancelDelete="sys:FormDb:cancelDelete"
                              :showOperation=true :addModel="childFormTableAddModel" v-if="childFormTableReloadFlag"
                              v-model="childFormTableResult" :columns="childFormTableResultColumns">
                </CusEditTable>
              </el-scrollbar>
            </div>
          </el-card>
        </el-col>
        <el-col :span="1">
          <el-button @click="next" size="mini" icon="el-icon-arrow-right" circle plain/>
        </el-col>
      </el-row>
    </transition>

    <transition name="el-zoom-in-center">
      <el-row key='fieldRe' :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
              v-show="active == 2">
        <el-col :span="1">
          <el-button @click="back" size="mini" icon="el-icon-arrow-left" circle plain/>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>表单字段</span>
            </div>
            <div style="height: 500px;">
              <el-scrollbar style="height: 100%;">
                <el-badge class="list-complete-item" v-for="element in selectList.list"
                          v-if="element.compoType !== 'formElDivider' && element.compoType !== 'formWord' && element.compoType !== 'formElEditTable'"
                          is-dot
                          :key="element.compoType" @click.native="fieldSelect($event, element)"
                          :hidden="!element.binds.length">
                  <i :class="element.icon"></i> {{element.compoTitle}}
                </el-badge>
              </el-scrollbar>
            </div>
          </el-card>
        </el-col>
        <el-col :span="16">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>约束关系</span>
            </div>
            <div style="height: 500px;">
              <el-scrollbar style="height: 100%;">
                <!--表格内容栏-->
                <CusEditTable ref="bindTable" permsEdit="sys:FormDb:edit"
                              permsDelete="sys:FormDb:delete" permsAddOne="sys:FormDb:addOne"
                              permsCancelDelete="sys:FormDb:cancelDelete"
                              :showOperation=true :addModel="addModel" v-if="reloadFlag"
                              v-model="bindTableResult" :columns="bindTableResultColumns">
                </CusEditTable>
              </el-scrollbar>
            </div>
          </el-card>
        </el-col>
        <el-col :span="1">
          <el-button @click="next" size="mini" icon="el-icon-arrow-right" circle plain/>
        </el-col>
      </el-row>
    </transition>

    <transition name="el-zoom-in-center">
      <el-row key='fieldRe1' :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
              v-show="active == 3">
        <el-col :span="1">
        </el-col>
        <el-col :span="22">
          <div style="height: 500px;">
            <div class="comfirm-word">确认完成表单设计，并提交保存吗？
              <br>
              <div style="margin: 50px">
                <el-button @click="back" type="primary" icon="el-icon-back" plain size="medium">返回上一步</el-button>
                <CusButton @click="saveForm" type="success" :loading="editLoading" perms="sys:FormDb:new"
                           size="medium"/>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="1">
        </el-col>
      </el-row>
    </transition>

    <!--预览界面-->
    <el-dialog title="表单预览" width="50%" :visible.sync="previewDialogVisible" :close-on-click-modal="false" append-to-body>
      <div class="el-dialog-div">
        <el-scrollbar style="height: 100%;">
          <el-form :label-position="formProps.labelPosition" :size="formProps.size" :disabled="true">
            <el-row :gutter="20">
              <component v-for="element in selectList.list" :key="element.id" :span="element.span"
                         v-if="previewDialogVisible"
                         :is="element.compoType" :label="element.compoTitle" :prop="element.key" :model="element.model"
                         :properties="element.compoProp">
              </component>
            </el-row>
          </el-form>
        </el-scrollbar>
      </div>
    </el-dialog>
  </div>
</template>

<script src="./FormDesign.js"></script>
<style lang="css">
  @import url('./FormDesign.css');
</style>
