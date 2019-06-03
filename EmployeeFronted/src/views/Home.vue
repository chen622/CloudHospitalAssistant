<template>
    <div>
        <a-row type="flex" justify="center" style="margin-bottom: 50px">
            <a-col span="24">
                <a-carousel autoplay arrows>
                    <div
                            slot="prevArrow"
                            class="custom-slick-arrow"
                            style="left: 10px;zIndex: 1"
                    >
                        <a-icon type="left-circle"/>
                    </div>
                    <div
                            slot="nextArrow"
                            class="custom-slick-arrow"
                            style="right: 10px"
                    >
                        <a-icon type="right-circle"/>
                    </div>
                    <div><img src="../assets/banner01.jpg" class="carousel-img"></div>
                    <div><img src="../assets/banner02.jpg" class="carousel-img"></div>
                    <div><img src="../assets/banner03.jpg" class="carousel-img"></div>
                    <div><img src="../assets/banner04.jpg" class="carousel-img"></div>
                    <div><img src="../assets/banner05.jpg" class="carousel-img"></div>
                </a-carousel>
            </a-col>
            <a-col span="18">
                <a-card style="width:100%;margin-top: 50px;"
                        title="科室导航"
                        :tabList="departmentKind"
                        :activeTabKey="departmentKey"
                        @tabChange="key => tabChange(key)"
                        :headStyle="{fontSize: '30px'}">
                    <a-card-grid v-for="dep in departments[departmentKey]" :key="dep"
                                 style="width:25%;text-align:center">
                        {{dep}}
                    </a-card-grid>
                </a-card>
            </a-col>
        </a-row>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data: () => ({
            departmentKind: [
                {
                    key: '0',
                    tab: '临床科室',
                },
                {
                    key: '1',
                    tab: '医技科室',
                }
            ],
            departmentKey: '0',
            departments: [
                ["儿科", "眼科", "皮肤科"],
                ["超声科", "药剂科"]
            ]
        }),
        methods: {
            tabChange: function (key) {
                this.departmentKey = key
            }
        }
    }
</script>

<style scoped>
    .ant-carousel >>> .slick-slide {
        text-align: center;
        width: 100%;
        /*height: 160px;*/
        /*line-height: 160px;*/
        overflow: hidden;
        z-index: 99;
    }

    .ant-carousel >>> .custom-slick-arrow {
        width: 25px;
        height: 25px;
        font-size: 25px;
        color: #888;
        background: none;
        opacity: 0.5;
    }

    .ant-carousel >>> .custom-slick-arrow:before {
        display: none;
    }

    .ant-carousel >>> .custom-slick-arrow:hover {
        color: #888;
        background: none;
        opacity: 1;
    }

    .carousel-img {
        width: 100%;
        margin: 0;
    }
</style>