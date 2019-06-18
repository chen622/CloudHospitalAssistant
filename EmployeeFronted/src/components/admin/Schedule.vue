<template>
    <div>
        <a-card>
            <a-calendar @change="selectDate">
                <ul class="events" slot="dateCellRender" slot-scope="value">
                    <li v-for="item in getListData(value)" :key="item.id">
                        <a-alert type="success">
                            <span slot="message">({{(item.registrationType.name)[0]}})<span style="font-weight: bold">{{item.user.realName}}</span>-{{item.constantVariable.name}}-{{item.limitRegistrationAmount}}个</span>
                        </a-alert>
                    </li>
                </ul>
            </a-calendar>
        </a-card>

    </div>
</template>

<script>

    import moment from 'moment'

    export default {
        name: "Schedule",
        props:
            ['department'],
        data: () => ({
            loading: false,
            currentMoment: moment(),
            results: [],
            lastResults: [],
            nextResults: [],
        }),
        methods: {
            selectDate (date) {
                let that = this
                this.loading = true
                that.$store.commit('setLoading', true)
                this.currentMoment = date
                this.$api.get("/job_schedule/getScheduleAndLastAndNextWithMonth/" + this.department[2] + "/" + date.utc().valueOf(), null,
                    res => {
                        if (res.code === '100') {
                            for (let i = 0; i <= 31; i++) {
                                that.results[i] = []
                                that.lastResults[i] = []
                                that.nextResults[i] = []
                            }
                            res.data.current.forEach(result => {
                                date = new Date(result.date)
                                that.results[date.getDate()].push(result)
                            })
                            res.data.last.forEach(result => {
                                date = new Date(result.date)
                                that.lastResults[date.getDate()].push(result)
                            })
                            res.data.next.forEach(result => {
                                date = new Date(result.date)
                                that.nextResults[date.getDate()].push(result)
                            })
                        } else {
                            that.$message.error(res.msg)
                        }
                        that.$store.commit('setLoading', false)
                    }, () => {
                    }
                )
            },
            getListData (value) {
                if (value.month() === this.currentMoment.month()) {
                    return this.results[value.date()];
                } else if (value.month() === this.currentMoment.month() + 1) {
                    return this.nextResults[value.date()];
                } else if (value.month() === this.currentMoment.month() - 1) {
                    return this.lastResults[value.date()];
                }
            }
        }
    }
</script>

<style>
    .events {
        list-style: none;
        margin: 0;
        padding: 0;
    }

    .ant-alert {
        padding: 4px;
        text-align: center;
    }

    .ant-alert-message {
        overflow: hidden;
        white-space: nowrap;
        width: 100%;
        text-overflow: ellipsis;
        font-size: 12px;
    }
</style>