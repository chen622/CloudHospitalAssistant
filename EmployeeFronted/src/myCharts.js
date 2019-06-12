/**
 * 各种画echarts图表的方法都封装在这里
 * 注意：这里echarts没有采用按需引入的方式，只是为了方便学习
 */

import echarts from 'echarts'

const install = function(Vue) {
    Object.defineProperties(Vue.prototype, {
        $chart: {
            get() {
                return {
                    //画一条简单的线
                    line1: function (id) {
                        this.chart = echarts.init(document.getElementById(id));
                        this.chart.clear();

                        const optionData = {
                            xAxis: {
                                type: 'category',
                                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [{
                                data: [820, 932, 901, 934, 1290, 1330, 1320],
                                type: 'line',
                                smooth: true
                            }]
                        };

                        this.chart.setOption(optionData);
                    },bar1:function(id,title,subtext,name,data){
                        console.log('**************')
                        this.chart = echarts.init(document.getElementById(id));
                        console.log(this.chart)
                        this.chart.clear();
                        const option = {
                            title: {
                                x: 'center',
                                text: title,
                                subtext: subtext,
                            },
                            tooltip: {
                                trigger: 'item'
                            },
                            toolbox: {
                                show: true,
                                feature: {
                                    dataView: {show: true, readOnly: false},
                                    restore: {show: true},
                                    saveAsImage: {show: true}
                                }
                            },
                            calculable: true,
                            grid: {
                                borderWidth: 0,
                                y: 80,
                                y2: 60
                            },
                            xAxis: [
                                {
                                    type: 'feeTypeName',
                                    show: false,
                                    data: name
                                }
                            ],
                            yAxis: [
                                {
                                    type: 'value',
                                    show: false
                                }
                            ],
                            series: [
                                {
                                    name: name,
                                    type: 'bar',
                                    itemStyle: {
                                        normal: {
                                            color: function(params) {
                                                // build a color map as your need.
                                                var colorList = [
                                                  '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                                                   '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                                                   '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                                                ];
                                                return colorList[params.dataIndex]
                                            },
                                            label: {
                                                show: true,
                                                position: 'top',
                                                formatter: '{b}\n{c}'
                                            }
                                        }
                                    },
                                    data: data,
                                    markPoint: {
                                        tooltip: {
                                            trigger: 'item',
                                            backgroundColor: 'rgba(0,0,0,0)',
                                            formatter: function(params){
                                                return '<img src="' 
                                                        + params.data.symbol.replace('image://', '')
                                                        + '"/>';
                                            }
                                        },
                                        data: [
                                        ]
                                    }
                                }
                            ]
                        };
                        this.chart.setOption(option);
                        console.log(this.chart)                      
                    }
                }
            }
        }
    })
}

export default {
    install
}
