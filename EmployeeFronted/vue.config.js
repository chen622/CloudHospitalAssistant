//vue.config.js
const CompressionPlugin = require("compression-webpack-plugin")


module.exports = {
    devServer: {//代理配置
        port: 8888,     // 端口
        proxy: {
            '/api': {
                target: 'https://sm.ms/api',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    },
    configureWebpack: () => {//打包配置
        if (process.env.NODE_ENV === 'production') {
            return {
                plugins: [
                    new CompressionPlugin({
                        test: /\.js$|\.html$|.\css/, //匹配文件名
                        threshold: 10240,//对超过10k的数据压缩
                        deleteOriginalAssets: false //不删除源文件
                    })
                ]
            }
        }

    },
};