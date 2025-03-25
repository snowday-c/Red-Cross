const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  pwa: {
    iconPaths: {
        favicon32: '/public/favicon.ico',
        favicon16: '/public/favicon.ico',
        appleTouchIcon: 'f/public/avicon.ico',
        maskIcon: '/public/favicon.ico',
        msTileImage: '/public/favicon.ico'
    }
  },

  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8090', // 后端地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' // 去掉发送的请求路径中的 /api
        }
      }
    }
  }
};
