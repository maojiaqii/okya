<template>
  <div class="containers">
    <div class="canvas" ref="canvas"></div>
    <ul class="buttons">
      <li>
        <a href="javascript:" class="active" @click="handlerUndo" title="撤销操作">撤销</a>
      </li>
      <li>
        <a href="javascript:" class="active" @click="handlerRedo" title="恢复操作">恢复</a>
      </li>
      <li>
        <a href="javascript:" class="active" @click="handlerZoom(0.1)" title="放大">放大</a>
      </li>
      <li>
        <a href="javascript:" class="active" @click="handlerZoom(-0.1)" title="缩小">缩小</a>
      </li>
      <li>
        <a href="javascript:" class="active" @click="handlerZoom(0)" title="还原">还原</a>
      </li>
      <li>
        <a href="javascript:" @click="$refs.refFile.click()">加载本地BPMN文件</a>
        <input type="file" id="files" ref="refFile" style="display: none" @change="loadXML"/>
      </li>
      <li>
        <a href="javascript:" ref="saveXML" title="保存为bpmn">保存为BPMN文件</a>
      </li>
      <li>
        <a href="javascript:" ref="saveSvg" title="保存为svg">保存为SVG图片</a>
      </li>
    </ul>

    <!-- 节点属性弹出窗口 -->
    <el-drawer
      title="节点属性"
      :visible.sync="$store.state.app.nodeVisible"
      direction="rtl"
      size="30%"
      ref="drawer"
      :before-close="handleClose" append-to-body>
      <div style="height: calc(80vh); padding-left:5px;padding-right:5px;">
        <el-scrollbar style="height: 100%;">
          <el-form label-width="80px" label-position="left" :model="fieldProps"
                   size="mini">
            <el-form-item label="节点id" prop="id">
              <el-input v-model="fieldProps.id" auto-complete="off" placeholder="需确保字段标识唯一" disabled></el-input>
            </el-form-item>
            <el-form-item label="节点名称" prop="name">
              <el-input v-model="fieldProps.name" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="条件" prop="conditionExpressionBody" v-if="fieldProps.type == 'bpmn:SequenceFlow'">
              <el-input v-model="fieldProps.conditionExpressionBody" auto-complete="off"
                        placeholder="格式：${date>=7}"></el-input>
            </el-form-item>
            <el-form-item label="经办人" prop="assignee" v-if="fieldProps.type == 'bpmn:UserTask'">
              <el-input v-model="fieldProps.assignee" auto-complete="off"
                        placeholder="格式：${userCode}"></el-input>
            </el-form-item>
            <el-form-item label="是否独有" prop="exclusive" v-if="fieldProps.type == 'bpmn:UserTask'">
              <el-switch v-model="fieldProps.exclusive" active-color="#13ce66"
                         inactive-color="#ff4949"
                         active-text="是"
                         inactive-text="否"/>
            </el-form-item>
            <el-divider/>
            <el-form-item label="是否会签" prop="loopCharacteristics" v-if="fieldProps.type == 'bpmn:UserTask'">
              <el-switch v-model="fieldProps.loopCharacteristics" inactive-color="#ff4949"
                         active-color="#13ce66"
                         active-text="是"
                         inactive-text="否"/>
            </el-form-item>
            <el-form-item label="按序传递" prop="loopCharacteristicsIsSequential" v-if="fieldProps.loopCharacteristics">
              <el-switch v-model="fieldProps.loopCharacteristicsIsSequential" active-color="#ff4949"
                         inactive-color="#13ce66"
                         active-text="并行"
                         inactive-text="串行"/>
            </el-form-item>
            <el-form-item label="多签集合" prop="loopCharacteristicsCollection" v-if="fieldProps.loopCharacteristics">
              <el-input v-model="fieldProps.loopCharacteristicsCollection" auto-complete="off"
                        placeholder="格式：${userList}"></el-input>
            </el-form-item>
            <el-form-item label="参数" prop="loopCharacteristicsElementVariable" v-if="fieldProps.loopCharacteristics">
              <el-input v-model="fieldProps.loopCharacteristicsElementVariable" auto-complete="off"
                        placeholder="格式：userCode"></el-input>
            </el-form-item>
          </el-form>
        </el-scrollbar>
      </div>
      <div style="float:right;padding:10px;">
        <el-button @click="permissionCancel">取 消</el-button>
        <el-button type="primary" @click="$refs.drawer.closeDrawer()">确定</el-button>
      </div>
    </el-drawer>

  </div>
</template>

<script>
    import {CustomModeler} from "../bpmn/modeler";
    import {xmlStr} from "./xmlStr";

    export default {
        name: "BpmnDesign",
        mounted() {
            this.init();
            this.$store.commit("set_nodeVisible", false);
            this.$store.commit("set_nodeInfo", {});
        },
        data() {
            return {
                bpmnModeler: null,
                container: null,
                canvas: null,
                xmlStr: xmlStr,
                scale: 1,
                fieldProps: {},
                selectElement: {},
                loading: false
            };
        },
        methods: {
            init() {
                const canvas = this.$refs.canvas;
                this.bpmnModeler = new CustomModeler({
                    container: canvas,
                    additionalModules: [
                        {
                            // 禁用滚轮滚动
                            zoomScroll: ["value", ""],
                            // 禁止拖动线
                            // bendpoints: ["value", ""],
                            // 禁用左侧面板
                            // paletteProvider: ["value", ""],
                            // 禁止点击节点出现contextPad
                            //contextPadProvider: ["value", ""],
                            // 禁止双击节点出现label编辑框
                            //labelEditingProvider: ["value", ""]
                        }
                    ]
                });
                this.bpmnModeler.on('selection.changed', (e) => {
                    this.selectElement = e.newSelection[0]
                    console.log('sasasasa')
                    console.log(this.selectElement)
                })
                this.createNewDiagram();
            },
            async createNewDiagram() {
                try {
                    const result = await this.bpmnModeler.importXML(this.xmlStr);
                    const {warnings} = result;
                    //console.log(warnings);
                    this.success();
                } catch (err) {
                    console.log(err.message, err.warnings);
                }
            },
            success() {
                this.addBpmnListener();
            },
            async loadXML() {
                const that = this;
                const file = this.$refs.refFile.files[0];

                var reader = new FileReader();
                reader.readAsText(file);
                reader.onload = function () {
                    that.xmlStr = this.result;
                    that.createNewDiagram();
                };
            },
            async addBpmnListener() {
                const that = this;
                const downloadLink = this.$refs.saveXML;
                const downloadSvgLink = this.$refs.saveSvg;

                async function opscoffee() {
                    try {
                        const result = await that.saveSVG();
                        const {svg} = result;
                        that.setEncoded(downloadSvgLink, "ops-coffee.svg", svg);
                    } catch (err) {
                        console.log(err);
                    }
                    try {
                        const result = await that.saveXML();
                        const {xml} = result;
                        that.setEncoded(downloadLink, "ops-coffee.bpmn", xml);
                    } catch (err) {
                        console.log(err);
                    }
                }

                opscoffee();
                this.bpmnModeler.on("commandStack.changed", opscoffee);
            },
            async saveSVG(done) {
                try {
                    const result = await this.bpmnModeler.saveSVG(done);
                    return result;
                } catch (err) {
                    console.log(err);
                }
            },
            async saveXML(done) {
                try {
                    const result = await this.bpmnModeler.saveXML({format: true}, done);
                    return result;
                } catch (err) {
                    console.log(err);
                }
            },
            setEncoded(link, name, data) {
                const encodedData = encodeURIComponent(data);

                if (data) {
                    link.href = "data:application/bpmn20-xml;charset=UTF-8," + encodedData;
                    link.download = name;
                }
            },
            handlerRedo() {
                this.bpmnModeler.get("commandStack").redo();
            },
            handlerUndo() {
                this.bpmnModeler.get("commandStack").undo();
            },
            handlerZoom(radio) {
                const newScale = !radio ? 1.0 : this.scale + radio;
                this.bpmnModeler.get("canvas").zoom(newScale);
                this.scale = newScale;
            },
            permissionCancel: function () {
                this.$store.commit("set_nodeVisible", false);
            },
            handleClose: function () {
                this.$confirm('确定要将修改的属性应用到流程节点吗？')
                    .then(_ => {
                        this.selectElement.businessObject.name = this.fieldProps.name;
                        let modeling = this.bpmnModeler.get("modeling");
                        modeling.updateLabel(this.selectElement, this.fieldProps.name);
                        // 流程线独有
                        if (this.fieldProps.conditionExpressionBody !== undefined && this.fieldProps.conditionExpressionBody.length > 0) {
                            this.selectElement.businessObject.$attrs['xsi:type'] = 'bpmn:tFormalExpression';
                            modeling.updateProperties(this.selectElement, {'xsi:type': 'bpmn:tFormalExpression'});
                            //this.selectElement.businessObject.conditionExpression.$type = 'bpmn:FormalExpression';
                            this.selectElement.businessObject.conditionExpression.body = this.fieldProps.conditionExpressionBody;
                            modeling.updateProperties(this.selectElement, {
                                'conditionExpression': {
                                    '$type': 'bpmn:FormalExpression',
                                    'body': this.fieldProps.conditionExpressionBody
                                }
                            });
                        } else {
                            delete this.selectElement.businessObject.$attrs['xsi:type'];
                            delete this.selectElement.businessObject.conditionExpression;
                            modeling.updateProperties(this.selectElement, {
                                'conditionExpression': undefined
                            });
                            modeling.updateProperties(this.selectElement, {
                                'xsi:type': undefined
                            });
                        }
                        // usertask独有
                        if (this.fieldProps.type == 'bpmn:UserTask') {
                            this.selectElement.businessObject.$attrs['activiti:assignee'] = this.fieldProps.assignee;
                            modeling.updateProperties(this.selectElement, {
                                'activiti:assignee': this.fieldProps.assignee
                            });
                            this.selectElement.businessObject.$attrs['activiti:exclusive'] = this.fieldProps.exclusive;
                            modeling.updateProperties(this.selectElement, {
                                'activiti:exclusive': this.fieldProps.exclusive
                            });
                            if (this.fieldProps.loopCharacteristics) {
                                this.selectElement.businessObject.loopCharacteristics.isSequential = this.fieldProps.loopCharacteristicsIsSequential;
                                this.selectElement.businessObject.loopCharacteristics.$attrs['activiti:collection'] = this.fieldProps.loopCharacteristicsCollection;
                                this.selectElement.businessObject.loopCharacteristics.$attrs['activiti:elementVariable'] = this.fieldProps.loopCharacteristicsElementVariable;
                                modeling.updateProperties(this.selectElement, {
                                    'loopCharacteristics': {
                                        'isSequential': this.fieldProps.loopCharacteristicsIsSequential,
                                        '$attrs': {
                                            'activiti:collection': this.fieldProps.loopCharacteristicsCollection,
                                            'activiti:elementVariable': this.fieldProps.loopCharacteristicsElementVariable
                                        }
                                    }
                                });
                            } else {
                                delete this.selectElement.businessObject.loopCharacteristics;
                                modeling.updateProperties(this.selectElement, {
                                    'loopCharacteristics': undefined
                                });
                            }
                        }
                        this.$store.commit("set_nodeVisible", false);
                        this.$message({message: "更新节点属性成功！", type: 'success'})
                    })
                    .catch(e => {
                        console.log(e)
                        this.$store.commit("set_nodeVisible", false);
                        if (e.toString() !== "cancel") {
                            this.$message({message: "更新节点属性失败！", type: 'success'})
                        }
                    });
            }
        },
        watch: {
            "$store.state.app.nodeVisible"(value) {
                if (value) {
                    console.log(this.selectElement.businessObject)
                    let fd = {};
                    fd.id = this.selectElement.businessObject.id;
                    fd.name = this.selectElement.businessObject.name;
                    fd.type = this.selectElement.businessObject.$type;
                    // 流程线独有
                    if (this.selectElement.businessObject.conditionExpression !== undefined && this.selectElement.businessObject.conditionExpression.body !== undefined) {
                        fd.conditionExpressionType = this.selectElement.businessObject.$attrs['xsi:type'];
                        fd.conditionExpressionStype = this.selectElement.businessObject.conditionExpression.$type;
                        fd.conditionExpressionBody = this.selectElement.businessObject.conditionExpression.body;
                    }
                    // usertask独有
                    if (this.selectElement.businessObject.$type == 'bpmn:UserTask') {
                        fd.assignee = this.selectElement.businessObject.$attrs['activiti:assignee'];
                        fd.exclusive = this.selectElement.businessObject.$attrs['activiti:exclusive'] === 'true';
                        if (this.selectElement.businessObject.loopCharacteristics) {
                            fd.loopCharacteristics = true;
                            fd.loopCharacteristicsIsSequential = this.selectElement.businessObject.loopCharacteristics.isSequential;
                            fd.loopCharacteristicsCollection = this.selectElement.businessObject.loopCharacteristics.$attrs['activiti:collection'];
                            fd.loopCharacteristicsElementVariable = this.selectElement.businessObject.loopCharacteristics.$attrs['activiti:elementVariable'];
                        } else {
                            fd.loopCharacteristics = false;
                        }
                    }
                    this.fieldProps = JSON.parse(JSON.stringify(fd));
                } else {
                    this.fieldProps = {};
                }
            }
        },
        computed: {}
    };
</script>

<style scoped>
  .containers {
    margin-top: 5px;
    margin-left: 5px;
    width: 100%;
    height: 560px;
    background-image: linear-gradient(
      90deg,
      rgba(220, 220, 220, 0.5) 6%,
      transparent 0
    ),
    linear-gradient(rgba(192, 192, 192, 0.5) 6%, transparent 0);
    background-size: 12px 12px;
  }

  .bjs-powered-by {
    display: none;
  }

  .canvas {
    width: 100%;
    height: 100%;
  }

  .buttons {
    left: 20px;
    bottom: 20px;
    float: right;
  }

  .buttons li {
    display: inline-block;
    margin: 5px;
  }

  .buttons li a {
    color: #333;
    background: #fff;
    cursor: pointer;
    padding: 8px;
    border: 1px solid #ccc;
    text-decoration: none;
  }
</style>
