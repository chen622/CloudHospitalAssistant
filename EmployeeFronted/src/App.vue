<template>
    <div id="app">
        <a-layout>
            <a-layout-header>
                <a-menu
                        v-model="current"
                        mode="horizontal"
                        :multiple="false"
                >
                    <a-menu-item key="index">
                        <a-icon type="home"/>
                        首页
                    </a-menu-item>
                    <a-menu-item key="app" disabled>
                        <a-icon type="appstore"/>
                        Navigation Two
                    </a-menu-item>
                    <a-sub-menu>
                        <span slot="title" class="submenu-title-wrapper"><a-icon type="setting"/>Navigation Three - Submenu</span>
                        <a-menu-item-group title="Item 1">
                            <a-menu-item key="setting:1">Option 1</a-menu-item>
                            <a-menu-item key="setting:2">Option 2</a-menu-item>
                        </a-menu-item-group>
                        <a-menu-item-group title="Item 2">
                            <a-menu-item key="setting:3">Option 3</a-menu-item>
                            <a-menu-item key="setting:4">Option 4</a-menu-item>
                        </a-menu-item-group>
                    </a-sub-menu>
                    <a-menu-item key="alipay">
                        <a href="https://ant.design" target="_blank" rel="noopener noreferrer">Navigation Four -
                            Link</a>
                    </a-menu-item>
                </a-menu>
            </a-layout-header>
            <a-layout-content>Content</a-layout-content>
            <a-layout-footer>Footer</a-layout-footer>
        </a-layout>
    </div>
</template>

<script>
    export default {
        data: () => ({
            current: ['mail'],
        }),

    }
</script>

<style>
    #app {
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
    }

    #app .ant-layout-header,
    #app .ant-layout-footer {
        background: #7dbcea;
        color: #fff;
    }

    #app .ant-layout-footer {
        line-height: 1.5;
    }
</style>
