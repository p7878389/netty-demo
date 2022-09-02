## netty client 启动流程

- NettyStartListener spring容器启动后执行netty 启动逻辑
- NettyServer 线程组、处理器初始化，jvm关闭钩子
- ShareworksMessageEncoder、ShareworksMessageDecoder 自定义编码、解码器

## netty相关处理器

- AuthorizationRequestHandler（鉴权）
  > 鉴权处理，链接建立后立即向服务端发起认证（执行channelActive方法） 指令类型：AUTHENTICATION

- HeartBeatRequestHandler（心跳检查）
  > 在指定时间内为收到或者未发送相关指令信息触发 **userEventTriggered** 方法，发送心跳包到服务端

- BusinessHandler（业务处理）
  > 处理后续业务逻辑 拉取任务、推送分析结果、jira分析等等

- ServerGlobalExceptionHandler（全局异常处理器）
  > netty 调用链中出现的异常拦截处理器
