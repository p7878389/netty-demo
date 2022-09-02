## netty client 启动流程

- NettyClientStartListener spring容器启动后执行netty 启动逻辑
- NettyClient 线程组、断线重连监听（NettyConnectionListener）处理器初始化，jvm关闭钩子
- MessageEncoder、MessageDecoder 自定义编码、解码器

## netty相关处理器

- AuthorizationRequestHandler（鉴权）
  > 鉴权处理，链接建立后立即向服务端发起认证（执行channelActive方法） 指令类型：AUTHENTICATION

- HeartBeatRequestHandler（心跳检查）
  > 在指定时间内为收到或者未发送相关指令信息触发 **userEventTriggered** 方法，发送心跳包到服务端

- ReconnectHandler（断线重连）
  > 执行 **channelInactive** 方法重连服务端，当重连次数大于指定阈值时系统自动退出（关闭JVM进程）

- BusinessHandler（业务处理）
  > 处理后续业务逻辑 拉取任务、推送分析结果、jira分析等等

- GlobalExceptionHandler（全局异常处理器）
  > netty 调用链中出现的异常拦截处理器

## 基于spring scheduler的动态定时任务（未测试）
  > 定时任务位于 com.shareworks.codeanalysis.client.config.threadpool 包下
- ThreadPoolTaskSchedulerConfig（线程池配置）
- DynamicScheduleTask（动态定时任务新增、更新、删除）
- ShareworksCustomizeTask（任务类型，根据不同得类型执行不同业务逻辑）
