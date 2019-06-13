module.exports = {
    devServer: {
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
};