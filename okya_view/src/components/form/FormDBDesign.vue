<template>
  <div>
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="选择表单"></el-step>
      <el-step title="子表单数据源"></el-step>
      <el-step title="主表单数据源"></el-step>
      <el-step title="视图设计"></el-step>
      <el-step title="完成"></el-step>
    </el-steps>
    <el-form ref="fm" :model="formDataSourceModel" :rules="formDataSourceModelRules" size="small">
      <transition name="el-zoom-in-center">
        <el-row :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
                v-show="active == 0">
          <el-col :span="1">
          </el-col>
          <el-col :span="22">
            <el-card class="box-card">
              <div style="padding-left:10px;width:90%;height: 500px;">
                <el-row>
                  <el-col :span="6">
                    <el-form-item label="选择表单" label-width="80px" prop="formId">
                      <el-select v-model="formDataSourceModel.formId" filterable clearable :disabled="!isNew"
                                 @change="formSelectChange">
                        <el-option
                          v-for="item in formsSelector"
                          :key="item.FORM_ID"
                          :label="item.FORM_NAME"
                          :value="item.FORM_ID"
                          :disabled="item.CAN_SELECT == 'true'"/>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="业务年度" label-width="80px" prop="hasSetYear">
                      <el-switch
                        v-model="formDataSourceModel.hasSetYear"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-text="是"
                        inactive-text="否"
                        active-value="Y"
                        inactive-value="N">
                      </el-switch>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
          <el-col :span="1">
            <el-button @click="nextTab" size="mini" icon="el-icon-arrow-right" circle plain/>
          </el-col>
        </el-row>
      </transition>

      <transition name="el-zoom-in-center">
        <el-row :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle" v-show="active == 1">
          <el-col :span="1">
            <el-button @click="backTab" size="mini" icon="el-icon-arrow-left" circle plain/>
          </el-col>
          <el-col :span="4">
            <el-card class="box-card">
              <div style="height: 500px;">
                <el-scrollbar style="height: 100%;">
                  <el-badge class="child-form-field" v-for="element in JSON.parse(formSelect.FORM).list"
                            v-if="element.compoType == 'formElEditTable'" is-dot
                            :key="element.key" @click.native="childFormSelect($event, element)"
                            :hidden="!element.binds.length">
                    <i :class="element.icon"></i> {{element.compoTitle}}
                  </el-badge>
                </el-scrollbar>
              </div>
            </el-card>
          </el-col>
          <el-col :span="18">
            <el-card class="box-card">
              <div style="height: 500px;">
                <el-scrollbar style="height: 100%;">
                  <el-row>
                    <el-col :span="8">
                      <el-form-item label="副表名称" label-width="80px" prop="childTableNames">
                        <el-input v-model="formDataSourceModel.childTableNames[selectChildForm]"></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-form-item prop="childTableColumns">
                      <!--表格内容栏-->
                      <CusEditTable permsEdit="sys:FormDb:edit" permsDelete="sys:FormDb:delete"
                                    permsAddOne="sys:FormDb:addOne" permsCancelDelete="sys:FormDb:cancelDelete"
                                    :showOperation=true :addModel="addModel" v-if="reloadFlag" :maxHeight="400"
                                    v-model="formDataSourceModel.childTableColumns[selectChildForm]"
                                    :columns="childTableInfoColumns">
                      </CusEditTable>
                    </el-form-item>
                  </el-row>
                  <div style="color:#F00">提示：子表单的物理表需要额外设置一个字段用于与主表的物理表关联使用</div>
                </el-scrollbar>
              </div>
            </el-card>
          </el-col>
          <el-col :span="1">
            <el-button @click="nextTab" size="mini" icon="el-icon-arrow-right" circle plain/>
          </el-col>
        </el-row>
      </transition>

      <transition name="el-zoom-in-center">
        <el-row key='fieldRe' :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
                v-show="active == 2">
          <el-col :span="1">
            <el-button @click="backTab" size="mini" icon="el-icon-arrow-left" circle plain/>
          </el-col>
          <el-col :span="22">
            <el-card class="box-card">
              <div style="height: 500px;">
                <el-scrollbar style="height: 100%;">
                  <el-row>
                    <el-col :span="4">
                      <el-form-item label="主表名称" label-width="80px" prop="mainTableName">
                        <el-input v-model="formDataSourceModel.mainTableName"></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <!--表格内容栏-->
                  <el-row>
                    <el-form-item prop="mainTableColumns">
                      <CusEditTable ref="MainTableInfo" permsEdit="sys:FormDb:edit" v-if="reloadF"
                                    permsDelete="sys:FormDb:delete" permsAddOne="sys:FormDb:addOne"
                                    permsCancelDelete="sys:FormDb:cancelDelete"
                                    :showOperation=true :addModel="addModel" :maxHeight="400"
                                    v-model="formDataSourceModel.mainTableColumns" :columns="mainTableInfoColumns">
                      </CusEditTable>
                    </el-form-item>
                  </el-row>
                  <div style="color:#F00">提示：表单字段选择子表单时，【子表名称】、【子表字段】必须填写上一步骤相应子表单设置的表名与关联字段</div>
                </el-scrollbar>
              </div>
            </el-card>
          </el-col>
          <el-col :span="1">
            <el-button @click="nextTab" size="mini" icon="el-icon-arrow-right" circle plain/>
          </el-col>
        </el-row>
      </transition>

      <transition name="el-zoom-in-center">
        <el-row :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
                v-show="active == 3">
          <el-col :span="1">
            <el-button @click="backTab" size="mini" icon="el-icon-arrow-left" circle plain/>
          </el-col>
          <el-col :span="22">
            <el-card class="box-card">
              <div style="height: 500px;">
                <el-scrollbar style="height: 100%;">
                  <el-form-item label="视图设计" prop="viewSql">
                    <el-input type="textarea" :rows="20"
                              placeholder="请输入符合数据库规范SQL语句，强烈建议视图加入主表GUID字段，别名必须为GUID，否则后台将强制加入T.GUID，可能造成视图创建失败等问题！"
                              v-model="formDataSourceModel.viewSql"></el-input>
                  </el-form-item>
                </el-scrollbar>
              </div>
            </el-card>
          </el-col>
          <el-col :span="1">
            <el-button @click="nextTab" size="mini" icon="el-icon-arrow-right" circle plain/>
          </el-col>
        </el-row>
      </transition>
    </el-form>

    <transition name="el-zoom-in-center">
      <el-row key='fieldRe1' :gutter="20" style="padding:25px;width:100%;" type="flex" align="middle"
              v-show="active == 4">
        <el-col :span="1">
        </el-col>
        <el-col :span="22">
          <div style="height: 500px;">
            <div class="comfirm-word">确认完成表单数据源的设计，并提交保存吗？
              <br>
              <div style="margin: 30px">
                <el-button @click="backTab" type="primary" icon="el-icon-back" plain size="medium">返回上一步</el-button>
                <CusButton @click="saveFormDb" type="success" :loading="editLoading" perms="sys:FormDb:new"
                           size="medium"/>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="1">
        </el-col>
      </el-row>
    </transition>
  </div>
</template>

<script src="./FormDBDesign.js"></script>
<style lang="css">
  @import url('./FormDBDesign.css');
</style>
