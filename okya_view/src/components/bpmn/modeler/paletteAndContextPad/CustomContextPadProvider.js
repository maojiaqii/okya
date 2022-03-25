import { assign } from "min-dash";

import store from '../../../../vuex'

export default function ContextPadProvider(
  config,
  injector,
  contextPad,
  modeling,
  elementFactory,
  connect,
  create,
  translate
) {
  this.modeling = modeling;
  this.elementFactory = elementFactory;
  this.connect = connect;
  this.create = create;
  this.translate = translate;
  config = config || {};

  if (config.autoPlace !== false) {
    this.autoPlace = injector.get("autoPlace", false);
  }

  contextPad.registerProvider(this);
}

ContextPadProvider.$inject = [
  "config.contextPad",
  "injector",
  "contextPad",
  "modeling",
  "elementFactory",
  "connect",
  "create",
  "translate"
];

ContextPadProvider.prototype.getContextPadEntries = function (element) {
  const {
    autoPlace,
    create,
    elementFactory,
    translate,
    modeling,
    connect
  } = this;

  function appendAction(type, className, title, options) {
    function appendStart(event, element) {
      var shape = elementFactory.createShape(assign({ type: type }, options));
      create.start(event, shape, {
        source: element
      });
    }

    var append = autoPlace
      ? function (event, element) {
        var shape = elementFactory.createShape(
          assign({ type: type }, options)
        );
        autoPlace.append(element, shape);
      }
      : appendStart;

    return {
      group: "model",
      className: className,
      title: title,
      action: {
        dragstart: appendStart,
        click: append
      }
    };
  }

  function removeElement(e) {
    modeling.removeElements([element]);
  }

  function clickElement(e) {
    store.commit("set_nodeVisible", true);
    //store.commit("set_nodeInfo", element.businessObject);
  }

  var actions = {};

  if (
    element.type === "bpmn:UserTask" ||
    element.type === "bpmn:SequenceFlow"
  ) {
    assign(actions, {
      edit: {
        group: "edit",
        className: "bpmn-icon-business-rule",
        title: translate("属性"),
        action: {
          click: clickElement
        }
      }
    });
  }

  assign(actions, {
    delete: {
      group: "edit",
      className: "bpmn-icon-trash",
      title: translate("Remove"),
      action: {
        click: removeElement
      }
    }
  });

  return actions;
};
