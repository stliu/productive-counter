1. 启用spring boot management
2. 收集undertow metrics (io.undertow.servlet.api.MetricsCollector)
3. 换成使用spring (async) rest template 来做http client, 去掉jersey
4. 使用netty 来作为rest template的backend
5. 确定数据最终保存到哪里
6. 增加web 界面