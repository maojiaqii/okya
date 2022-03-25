<template>
  <div class="container">
    <el-button type="primary" @click="previewTemp">预览</el-button>
    <el-button type="success" @click="handleZoom(1)">放大</el-button>
    <el-button type="warning" @click="handleZoom(-1)">缩小</el-button>
    <div class="chart-preview">
      <div style="clear: both;"></div>
      <div class="view-canvas">
        <div id="container" ref="canves"
             v-bind:style="{width: 100 * scale + '%',height: 100 * scale + '%'}"
        ></div>
      </div>
    </div>
  </div>
</template>
<script>
    import BpmnViewer from 'bpmn-js/lib/Viewer';

    export default {
        data() {
            return {
                containerEl: null,
                bpmnModeler: null,
                scale: 1,
                // 此变量为预览的xml文件数据，由于行数过多，附在了附件中，使用时，将附件整个复值到currentCanvasXml即可
                currentCanvasXml: `<bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" id="Definitions_1" targetNamespace="http://bpmn.io/schema/bpmn">
  <bpmn:process id="Process_1" isExecutable="false">
    <bpmn:startEvent id="StartEvent_1">
      <bpmn:outgoing>Flow_1fmanci</bpmn:outgoing>
      <bpmn:outgoing>Flow_1pme89u</bpmn:outgoing>
    </bpmn:startEvent>
    <bpmn:exclusiveGateway id="Gateway_1x59564" name="委委屈屈">
      <bpmn:incoming>Flow_14hr83g</bpmn:incoming>
      <bpmn:outgoing>Flow_0jbll3d</bpmn:outgoing>
      <bpmn:outgoing>Flow_07zvbc8</bpmn:outgoing>
    </bpmn:exclusiveGateway>
    <bpmn:task id="Activity_0ieiw9a" name="吾问无为谓">
      <bpmn:incoming>Flow_0jbll3d</bpmn:incoming>
      <bpmn:outgoing>Flow_1jfw2t9</bpmn:outgoing>
    </bpmn:task>
    <bpmn:task id="Activity_1uh7x98" name="wqewqeq">
      <bpmn:incoming>Flow_07zvbc8</bpmn:incoming>
      <bpmn:outgoing>Flow_0k1vekk</bpmn:outgoing>
    </bpmn:task>
    <bpmn:sequenceFlow id="Flow_0jbll3d" sourceRef="Gateway_1x59564" targetRef="Activity_0ieiw9a" />
    <bpmn:sequenceFlow id="Flow_07zvbc8" sourceRef="Gateway_1x59564" targetRef="Activity_1uh7x98" />
    <bpmn:task id="Activity_1fm0exq" name="A审批">
      <bpmn:incoming>Flow_1fmanci</bpmn:incoming>
      <bpmn:incoming>Flow_1pme89u</bpmn:incoming>
      <bpmn:outgoing>Flow_14hr83g</bpmn:outgoing>
    </bpmn:task>
    <bpmn:sequenceFlow id="Flow_1fmanci" sourceRef="StartEvent_1" targetRef="Activity_1fm0exq" />
    <bpmn:sequenceFlow id="Flow_14hr83g" sourceRef="Activity_1fm0exq" targetRef="Gateway_1x59564" />
    <bpmn:sequenceFlow id="Flow_1pme89u" sourceRef="StartEvent_1" targetRef="Activity_1fm0exq" />
    <bpmn:endEvent id="Event_1pbdj0z">
      <bpmn:incoming>Flow_1yujhii</bpmn:incoming>
    </bpmn:endEvent>
    <bpmn:exclusiveGateway id="Gateway_0f2bc9l" name="王企鹅无群二">
      <bpmn:incoming>Flow_1jfw2t9</bpmn:incoming>
      <bpmn:incoming>Flow_0k1vekk</bpmn:incoming>
      <bpmn:outgoing>Flow_1yujhii</bpmn:outgoing>
    </bpmn:exclusiveGateway>
    <bpmn:sequenceFlow id="Flow_1jfw2t9" sourceRef="Activity_0ieiw9a" targetRef="Gateway_0f2bc9l" />
    <bpmn:sequenceFlow id="Flow_0k1vekk" sourceRef="Activity_1uh7x98" targetRef="Gateway_0f2bc9l" />
    <bpmn:sequenceFlow id="Flow_1yujhii" sourceRef="Gateway_0f2bc9l" targetRef="Event_1pbdj0z" />
  </bpmn:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">
      <bpmndi:BPMNEdge id="Flow_0jbll3d_di" bpmnElement="Flow_0jbll3d">
        <di:waypoint x="500" y="175" />
        <di:waypoint x="500" y="100" />
        <di:waypoint x="630" y="100" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_07zvbc8_di" bpmnElement="Flow_07zvbc8">
        <di:waypoint x="500" y="225" />
        <di:waypoint x="500" y="300" />
        <di:waypoint x="630" y="300" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_1fmanci_di" bpmnElement="Flow_1fmanci">
        <di:waypoint x="209" y="200" />
        <di:waypoint x="280" y="200" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_14hr83g_di" bpmnElement="Flow_14hr83g">
        <di:waypoint x="380" y="200" />
        <di:waypoint x="475" y="200" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_1pme89u_di" bpmnElement="Flow_1pme89u">
        <di:waypoint x="209" y="200" />
        <di:waypoint x="280" y="200" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_1jfw2t9_di" bpmnElement="Flow_1jfw2t9">
        <di:waypoint x="730" y="100" />
        <di:waypoint x="840" y="100" />
        <di:waypoint x="840" y="175" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_0k1vekk_di" bpmnElement="Flow_0k1vekk">
        <di:waypoint x="730" y="300" />
        <di:waypoint x="840" y="300" />
        <di:waypoint x="840" y="225" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_1yujhii_di" bpmnElement="Flow_1yujhii">
        <di:waypoint x="865" y="200" />
        <di:waypoint x="952" y="200" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">
        <dc:Bounds x="173" y="182" width="36" height="36" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Gateway_1x59564_di" bpmnElement="Gateway_1x59564" isMarkerVisible="true">
        <dc:Bounds x="475" y="175" width="50" height="50" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="558" y="190" width="44" height="14" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Activity_0ieiw9a_di" bpmnElement="Activity_0ieiw9a">
        <dc:Bounds x="630" y="60" width="100" height="80" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Activity_1uh7x98_di" bpmnElement="Activity_1uh7x98">
        <dc:Bounds x="630" y="260" width="100" height="80" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Activity_1fm0exq_di" bpmnElement="Activity_1fm0exq">
        <dc:Bounds x="280" y="160" width="100" height="80" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Event_1pbdj0z_di" bpmnElement="Event_1pbdj0z">
        <dc:Bounds x="952" y="182" width="36" height="36" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Gateway_0f2bc9l_di" bpmnElement="Gateway_0f2bc9l" isMarkerVisible="true">
        <dc:Bounds x="815" y="175" width="50" height="50" />
        <bpmndi:BPMNLabel>
          <dc:Bounds x="727" y="190" width="67" height="14" />
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNShape>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn:definitions>
`
            };
        },
        methods: {
            handleZoom(flag) {
                if (flag < 0 && this.scale <= 1) {
                    return;
                }
                this.scale += flag;
                this.$nextTick(() => {
                    this.bpmnModeler.get('canvas').zoom('fit-viewport');
                });
            },
            previewTemp() {
                this.containerEl = this.$refs.canves;
                // 避免缓存，每次清一下
                this.bpmnModeler && this.bpmnModeler.destroy();
                this.scale = 1;
                this.bpmnModeler = new BpmnViewer({container: this.containerEl});
                const viewer = this.bpmnModeler;
                this.bpmnModeler.importXML(this.currentCanvasXml, (err) => {
                    if (err) {
                        console.error(err);
                    } else {
                        // 只实现预览，核心代码以下两句足够
                        const canvas = viewer.get('canvas');
                        canvas.zoom('fit-viewport');
                        // 以下代码为：为节点注册鼠标悬浮事件
                        const eventBus = this.bpmnModeler.get('eventBus');
                        const overlays = this.bpmnModeler.get('overlays');
                        eventBus.on('element.hover', (e) => {
                            console.log(e.element)

                        });
                        eventBus.on('element.out', () => {
                            overlays.clear();
                        });
                        //  注册悬浮事件结束
                    }
                });
            }
        }
    };
</script>
<style lang="scss">
  .container {
    .tipBox {
      width: 200px;
      background: #fff;
      border-radius: 4px;
      border: 1px solid #ebeef5;
      padding: 12px;
    }
  }
</style>
