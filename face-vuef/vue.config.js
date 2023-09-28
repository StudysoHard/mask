const { defineConfig } = require('@vue/cli-service')

// const path = require('path');//引入path模块
// function resolve(dir) {
//   return path.join(__dirname, dir)//path.join(__dirname)设置绝对路径
// }

module.exports = defineConfig({
    // 选项
    //  基本路径
    publicPath: "./",
    //  构建时的输出目录
    outputDir: "dist",
    //  放置静态资源的目录
    assetsDir: "static",
    //  html 的输出路径
    indexPath: "index.html",
    //文件名哈希
    filenameHashing: true,
    lintOnSave: true,
    //  是否使用带有浏览器内编译器的完整构建版本
    runtimeCompiler: false,
    //  babel-loader 默认会跳过 node_modules 依赖。
    transpileDependencies: [ /* string or regex */ ],
    //  是否为生产环境构建生成 source map？
    productionSourceMap: true,
    //  设置生成的 HTML 中 <link rel="stylesheet"> 和 <script> 标签的 crossorigin 属性。
    crossorigin: "",
    //  在生成的 HTML 中的 <link rel="stylesheet"> 和 <script> 标签上启用 Subresource Integrity (SRI)。
    integrity: false,
    //  调整内部的 webpack 配置
    configureWebpack: () => {}, //(Object | Function)
    chainWebpack: () => {},
    // 配置 webpack-dev-server 行为。
    devServer: {
        open: process.platform === 'darwin',
        host: '0.0.0.0',
        port: 8080,
        https: false,
        // hotOnly: false,
        // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/cli-service.md#配置代理
        proxy: {
            '/api': {
                target: 'http://localhost:5002',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    "^/api": '/'
                }
            },
            '/work': {
                target: 'http://www.heyongqiang.work:8889',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    "^/work": '/'
                }
            }
        }
    }

})