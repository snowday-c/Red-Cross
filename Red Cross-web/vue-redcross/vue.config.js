const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8090', // 后端地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' // 去掉请求路径中的 /api
        }
      }
    }
  }
};