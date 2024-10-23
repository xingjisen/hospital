export default {
  // ip 端口
  server: {
    port: process.env.PORT,
    host: process.env.HOST
  },
  env: {
    //请求url
    BASE_URL: process.env.BASE_URL || "http://localhost:9000",
    //请求接口前缀
    BASE_API: process.env.BASE_API || "/api",
    //服务端渲染改为true
    CLIENT: process.env.CLIENT || "true",
    //端口
    PORT: process.env.PORT || "3000",
    //启动IP地址
    HOST: process.env.HOST || "0.0.0.0"
  },

  // 全局页眉：https://go.nuxtjs.dev/config-head
  head: {
    title: "yygh-site",
    htmlAttrs: {
      lang: "en"
    },
    meta: [
      { charset: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1" },
      { hid: "description", name: "description", content: "尚医通" },
      { name: "format-detection", content: "telephone=no" }

    ],
    link: [{ rel: "icon", type: "image/x-icon", href: "/favicon.ico" }]
  },

  // 全局 CSS：https://go.nuxtjs.dev/config-css
  css: [
    "element-ui/lib/theme-chalk/index.css"
  ],

  // 渲染前要运行的插件 page： https://go.nuxtjs.dev/config-plugins
  plugins: [
    // ssr==false?客户端渲染:服务端渲染，默认服务端渲染
    { src: "@/plugins/element-ui", ssr: false },
    { src: "@/plugins/main", ssr: false }
  ],

  // 自动导入零部件：https://go.nuxtjs.dev/config-components
  components: true,

  // 用于开发和构建的模块（推荐）：https://go.nuxtjs.dev/config-modules
  buildModules: [],

  //模块：https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/axios
    "@nuxtjs/axios"
  ],

  //Axios 模块配置：https://go.nuxtjs.dev/config-axios
  axios: {
    // 避免强制实施硬编码 localhost：3000 的解决方法：https://github.com/nuxt-community/axios-module/issues/308
    baseURL: process.env.BASE_API,
    proxy: true//开启代理
  },
  proxy: {
    [process.env.BASE_API]: {
      target: process.env.BASE_URL,
      changeOrigin: true,
      pathRewrite: { [`^${process.env.BASE_API}`]: "" }
    }
  },


  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    transpile: [/^element-ui/]
  }
};
