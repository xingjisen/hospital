# yygh-site 网站

## 构建设置

```bash
# 安装依赖
$ npm 安装

# 在 localhost：3000 热重载服务
$ npm run dev

# 为生产和启动服务器构建
$ npm run build
$ npm run start

# 生成静态工程
$ npm run generate
```

有关工作原理的详细说明，请查看 [documentation]（https://nuxtjs.org）。

## 特殊目录

您可以创建以下额外目录，其中一些目录具有特殊行为。只需要 'pages';如果您不想使用它们的功能，可以将其删除。

### `assets`

assets 目录包含未编译的资源，例如 Stylus 或 Sass 文件、图像或字体。

有关此目录用法的更多信息，请参见 [文档]（https://nuxtjs.org/docs/2.x/directory-structure/assets）。

### `components`

components 目录包含您的 Vue.js 组件。组件构成了页面的不同部分，可以重复使用并导入到您的页面、布局甚至其他组件中。

有关此目录用法的更多信息，请参见 [文档]（https://nuxtjs.org/docs/2.x/directory-structure/components）。

### `layouts`

当您想更改 Nuxt 应用程序的外观时，布局非常有用，无论您是想包含侧边栏还是为移动设备和桌面使用不同的布局。

有关此目录用法的更多信息，请参见 [文档]（https://nuxtjs.org/docs/2.x/directory-structure/layouts）。

### `pages`

此目录包含您的应用程序视图和路由。Nuxt 将读取此目录中的所有 '*.vue' 文件并自动设置 Vue 路由器。

有关此目录用法的更多信息，请参见 [文档]（https://nuxtjs.org/docs/2.x/get-started/routing）。

### `plugins`

plugins 目录包含您希望在实例化根 Vue.js Application 之前运行的 JavaScript 插件。这是添加 Vue
插件和注入函数或常量的地方。每次你需要使用 'Vue.use（）' 时，你应该在 'plugins/' 中创建一个文件，并在 'nuxt.config.js'
中添加其插件路径。

有关此目录用法的更多信息，请参见 [文档]（https://nuxtjs.org/docs/2.x/directory-structure/plugins）。

### `static`

此目录包含您的静态文件。此目录中的每个文件都映射到 '/'。

示例：'/static/robots.txt' 被映射为 '/robots.txt'。

有关此目录用法的更多信息，请参见 [文档]（https://nuxtjs.org/docs/2.x/directory-structure/static）。

### `store`

此目录包含您的 Vuex 存储文件。在此目录中创建文件会自动激活 Vuex。

有关此目录用法的更多信息，请参见 [文档]（https://nuxtjs.org/docs/2.x/directory-structure/store）。
