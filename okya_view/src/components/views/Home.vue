<template>
  <div class="container">
    <div class="fit-content" :style="getFitContentStyle()" >
      <!-- 导航菜单栏 -->
      <MenuBar></MenuBar>
      <!-- 头部区域 -->
      <HeadBar></HeadBar>
      <!-- 主内容区域 -->
      <Main></Main>
    </div>
  </div>
</template>

<script>
    import HeadBar from "../bar/HeadBar"
    import MenuBar from "../bar/MenuBar"
    import Main from "./Main"
    import {Notification} from 'element-ui';
    import store from '../../vuex'
    import cfg from '../../../config'

    export default {
        data() {
            return {
                width:1536,
                height:731,
                currentRatio:1,
                wsUrl: "ws:101.132.122.164:11001/websocket/"
            };
        },
        methods:{
            updateScaleRatio(){
                var newWidth = document.documentElement.clientWidth;
                var newHeight = document.documentElement.clientHeight;
                console.log("页面尺寸：" + newWidth + "x" + newHeight)
                var ratio = newWidth / newHeight;
                console.log("页面尺寸比：" + ratio)
                var innerRatio = this.width / this.height;
                var scaleWidthRatio = newWidth/ this.width ;
                var scaleHeightRatio = newHeight / this.height ;
                console.log("innerRatio：" + innerRatio)
                console.log("scaleWidthRatio：" + scaleWidthRatio)
                console.log("scaleHeightRatio：" + scaleHeightRatio)
                if(ratio < innerRatio){
                    this.currentRatio = scaleWidthRatio;
                } else{
                    this.currentRatio = scaleHeightRatio;
                }
            },
            getFitContentStyle(){
                return {
                    width:this.width + 'px',
                    height:this.height + 'px',
                    transform:`scale(${this.currentRatio})`
                }
            }
        },
        components: {
            HeadBar,
            MenuBar,
            Main
        },
        mounted() {
            this.updateScaleRatio();
            /*
            * 监听页面刷新事件
            * 页面刷新前 需要保存当前打开的tabs的位置，刷新后按刷新前的顺序展示
            * 使用js的sessionStorage保存刷新页面前的数据
            * */
            window.addEventListener('beforeunload', e => {
                if (this.$store.state.app.viewOpenStyle == 'tabs') {
                    this.$store.commit('setRefresh', true);
                }
            });

            window.addEventListener('resize',()=>{
                this.updateScaleRatio();
            })

            //判断当前浏览器是否支持WebSocket
            if ('WebSocket' in window) {
                let websocket = new WebSocket("ws" + cfg.dev.proxyTable['/api'].target.replace("http", "") + "/websocket/" + JSON.parse(sessionStorage.getItem('user')).userCode);
                //let websocket = new WebSocket(this.wsUrl + JSON.parse(sessionStorage.getItem('user')).userCode);
                //连接发生错误回调方法
                websocket.onerror = function () {
                    console.log("WebSocket连接发生错误");
                };
                //连接成功建立回调方法
                websocket.onopen = function () {
                    console.log("WebSocket连接成功");
                }
                //接收消息回调方法
                websocket.onmessage = function (event) {
                    Notification({
                        title: '你有一条新的消息提醒',
                        dangerouslyUseHTMLString: true,
                        message: event.data,
                        position: 'bottom-right',
                        type: 'info',
                        duration: 0
                    });
                    store.commit('add_unCheckCount');
                }
                //连接关闭回调方法
                websocket.onclose = function () {
                    console.log("WebSocket连接关闭");
                }
                //监听窗口关闭事件
                window.onbeforeunload = function () {
                    closeWebSocket();
                }

                //关闭WebSocket连接
                function closeWebSocket() {
                    websocket.close();
                    this.$store.commit('set_websocket', null);
                }

                this.$store.commit('set_websocket', websocket);
            } else {
                Notification({
                    title: '警告',
                    message: '您使用的浏览器不支持websocket，将无法接收、发送消息！',
                    position: 'bottom-right',
                    type: 'warning',
                    duration: 0
                });
            }

        },
        created() {
            this.$router.push('/homePage');
        }
    };
</script>

<style scoped lang="scss">
  .container {
    top: 0px;
    left: 0px;
    right: 0px;
    flex:1;
    overflow:hidden;
    .fit-content{
      flex:1;
      transform-origin: left top;
      overflow: hidden;
      position: absolute;
    }
  }
</style>
