<template>
  <div style="width: 98%;margin-top: 10px;">
    <el-row :gutter="20" style="margin-left: 0px;">
      <el-col :span="16">
        <el-row>
          <el-col style="margin-bottom: calc(1vh);" :span="24">
            <el-card style="height: 370px;" :body-style="{padding: '0px'}" shadow="hover">
              <el-row>
                <el-col :span="12">
                  <div ref="chart2" style="width:100%;height:calc(50vh)"/>
                </el-col>
                <el-col :span="12">
                  <div ref="chart3" style="width:100%;height:calc(50vh)"/>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
          <el-col :span="24">
            <el-card style="height: 220px;" :body-style="{padding: '0px'}" shadow="hover">
              <div ref="chart1" style="width:100%;height:calc(29vh)"/>
            </el-card>
          </el-col>
        </el-row>

      </el-col>
      <el-col :span="8">
        <el-card style="height: 605px;" :body-style="{padding: '0px'}" shadow="hover">
          <div slot="header" class="clearfix">
            <span>公告</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    // 引入 ECharts 主模块
    import echarts from 'echarts/lib/echarts'
    // 引入柱状图
    import bar from 'echarts/lib/chart/bar'
    // 引入饼图
    import pie from 'echarts/lib/chart/pie'
    // 引入提示框和标题组件
    import tooltip from 'echarts/lib/component/tooltip'
    import title from 'echarts/lib/component/title'

    export default {
        data() {
            return {}
        },
        methods: {
            getEchartData1() {
                const chart1 = this.$refs.chart1
                if (chart1) {
                    let dt = undefined;
                    this.$api.eChart.getCommonData({
                        'tableName': 'VT_BILL_DAILY'
                    }).then((res) => {
                        dt = JSON.parse(res.message)
                        if (dt) {
                            let x = []
                            let value = []
                            const myChart = echarts.init(chart1, 'light');
                            dt.forEach(v => {
                                x.push(v['name'])
                                value.push(v['value'])
                            })
                            myChart.setOption({
                                title: {
                                    text: '单位当日开票量'
                                },
                                tooltip: {},
                                xAxis: {
                                    data: x
                                },
                                yAxis: {},
                                series: [{
                                    name: '开票量',
                                    type: 'bar',
                                    data: value
                                }]
                            })
                        }
                        window.addEventListener("resize", function () {
                            myChart.resize()
                        })
                        this.$on('hook:destroyed', () => {
                            window.removeEventListener("resize", function () {
                                myChart.resize();
                            });
                        })
                    })
                }
                const chart2 = this.$refs.chart2
                if (chart2) {
                    this.$api.eChart.getCommonData({
                        'tableName': 'VT_BILL_AMT_DAILY'
                    }).then((res) => {
                        const myChart = echarts.init(chart2, 'light')
                        myChart.setOption({
                            title: {
                                text: '单位当日开票金额'
                            },
                            tooltip: {},
                            series: [{
                                type: 'pie',
                                data: JSON.parse(res.message)
                            }]
                        })
                        window.addEventListener("resize", function () {
                            myChart.resize()
                        })
                        this.$on('hook:destroyed', () => {
                            window.removeEventListener("resize", function () {
                                myChart.resize();
                            });
                        })
                    })
                }
                const chart3 = this.$refs.chart3
                if (chart3) {
                    this.$api.eChart.getCommonData({
                        'tableName': 'VT_INCOME_AMT_DAILY'
                    }).then((res) => {
                        const myChart = echarts.init(chart3, 'light')
                        myChart.setOption({
                            title: {
                                text: '单位当日收入'
                            },
                            tooltip: {},
                            series: [{
                                type: 'pie',
                                data: JSON.parse(res.message)
                            }]
                        })
                        window.addEventListener("resize", function () {
                            myChart.resize()
                        })
                        this.$on('hook:destroyed', () => {
                            window.removeEventListener("resize", function () {
                                myChart.resize();
                            });
                        })
                    })
                }
            }

        },
        mounted() {
            //this.getEchartData1();
        }
    }
</script>
