<template>
    <div class="invoice-box">
        <table cellpadding="0" cellspacing="0">
            <tr class="top">
                <td colspan="5">
                    <table>
                        <tr>
                            <td class="title">
                                <img src="../assets/logo/logo-grey-white.png" style="width:100%; max-width:300px;">
                            </td>
                            <td/>
                            <td/>
                            <td/>
                            <td>
                                {{isRetreat?"冲红":""}}发票号 #: {{invoice.id}}<br>
                                创建时间: {{invoice.createdDate | formatDate}}<br>
                                打印时间: {{currentTime | formatDate}}
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr class="information">
                <td colspan="5">
                    <table>
                        <tr>
                            <td style="padding-left: 40px">
                                辽宁省沈阳市浑南区，<br>
                                营盘西街66号<br>
                                HIS 国际医院
                            </td>
                            <td/>
                            <td/>
                            <td/>
                            <td style="padding-right: 40px; padding-top: 20px">
                                病历号：{{patient.id}}&#12288;
                                姓名：{{patient.realName}}&#12288;
                                性别：{{patient.sex? "女": "男"}}<br>
                                结算方式：{{settlementType}}
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr style="max-width: 600px">
                <td colspan="5">
                    <table>
                        <tr class="heading">
                            <td>项目名称</td>
                            <td>缴费类型</td>
                            <td>项目数量</td>
                            <td>项目单价</td>
                            <td>备注</td>
                        </tr>

                        <tr class="details" v-for="i in item" :key="i.name">
                            <td>{{i.name}}</td>
                            <td>{{i.paymentType}}</td>
                            <td>{{i.quantity}}</td>
                            <td>{{i.unitPrice}}</td>
                            <td>{{i.note}}</td>
                        </tr>
                    </table>
                </td>
            </tr>


            <tr class="total">
                <td/>
                <td/>
                <td/>
                <td/>
                <td>合计:&#12288;&#12288;${{invoice.priceAmount}}</td>
            </tr>
        </table>
    </div>
</template>

<script>
    export default {
        name: "InvoiceTemplate",
        data: () => ({
            invoice: {
                id: 0,
                priceAmount: 0,
                createdDate: "2019-01-01 00:00:00"
            },
            currentTime: "2019-01-01 00:00:00",
            patient: {
                id: 0,
                realName: "ydy",
                sex: "男"
            },
            settlementType: "自费",
            isRetreat: false,
            item: [
                {
                    name: "x",
                    unitPrice: 0,
                    itemTotalAmount: 0,
                    note: "",
                    paymentType: "西药费",
                    quantity: 3,
                }
            ]
        }),
        filters: {
            formatDate: function (value) {
                let date = new Date(value);
                let y = date.getFullYear();
                let MM = date.getMonth() + 1;
                MM = MM < 10 ? ('0' + MM) : MM;
                let d = date.getDate();
                d = d < 10 ? ('0' + d) : d;
                let h = date.getHours();
                h = h < 10 ? ('0' + h) : h;
                let m = date.getMinutes();
                m = m < 10 ? ('0' + m) : m;
                let s = date.getSeconds();
                s = s < 10 ? ('0' + s) : s;
                return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
            }
        },
        methods: {
            getInvoice() {
                let that = this
                this.$api.get('/invoice/print/' + 12, null, function (res) {
                    if (res.code === '100') {
                        that.invoice = res.data.invoice
                        that.patient = res.data.patient
                        that.settlementType = res.data.settlementType
                        that.item = res.data.item
                        that.currentTime = res.data.currentTime;
                    } else {
                        that.$message.error(res.msg)
                    }
                }, () => {
                    that.$message.error("网络异常")
                })
            }
        },
        mounted() {
            this.getInvoice()
        }
    }
</script>

<style scoped>
    .invoice-box {
        max-width: 800px;
        margin: auto;
        padding: 30px;
        border: 1px solid #eee;
        box-shadow: 0 0 10px rgba(0, 0, 0, .15);
        font-size: 16px;
        line-height: 24px;
        font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
        color: #555;
        /*background-color: lightcoral;*/
    }

    .invoice-box table {
        width: 100%;
        line-height: inherit;
        text-align: left;
    }

    .invoice-box table td {
        padding: 5px;
        vertical-align: top;
    }

    .invoice-box table tr td:nth-child(n+2) {
        text-align: right;
    }

    .invoice-box table tr.top table td {
        padding-bottom: 20px;
        padding-right: 0px;
    }

    .invoice-box table tr.top table td.title {
        font-size: 45px;
        line-height: 45px;
        color: #333;
    }

    .invoice-box table tr.information table td {
        padding-bottom: 40px;
    }

    .invoice-box table tr.heading td {
        background: #eee;
        border-bottom: 1px solid #ddd;
        font-weight: bold;
    }

    .invoice-box table tr.heading td:nth-child(n) {
        text-align: left;
    }

    .invoice-box table tr.details td {
        padding-bottom: 20px;
    }

    .invoice-box table tr.details td:nth-child(n) {
        text-align: left;
    }

    .invoice-box table tr.item td {
        border-bottom: 1px solid #eee;
    }

    .invoice-box table tr.item.last td {
        border-bottom: none;
    }

    .invoice-box table tr.total td:nth-child(n) {
        border-top: 2px solid #eee;
        font-weight: bold;
        padding-right: 60px;
        text-align: right;
    }

    @media only screen and (max-width: 600px) {
        .invoice-box table tr.top table td {
            width: 100%;
            display: block;
            text-align: center;
        }

        .invoice-box table tr.information table td {
            width: 100%;
            display: block;
            text-align: center;
        }

        .invoice-box table tr.heading table td {
            width: 100%;
            display: block;
            text-align: center;
        }
    }

    /*!** RTL **!*/
    /*.rtl {*/
    /*    direction: rtl;*/
    /*    font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;*/
    /*}*/

    /*.rtl table {*/
    /*    text-align: right;*/
    /*}*/

    /*.rtl table tr td:nth-child(2) {*/
    /*    text-align: left;*/
    /*}*/

</style>