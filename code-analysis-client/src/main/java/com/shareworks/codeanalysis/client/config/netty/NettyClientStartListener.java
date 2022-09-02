package com.shareworks.codeanalysis.client.config.netty;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:20
 */
@Component
@Slf4j
public class NettyClientStartListener implements CommandLineRunner {

    @Resource
    private NettyClient nettyClient;

    @Override
    public void run(String... args) throws Exception {
        try {
            nettyClient.nettyStart();
        } catch (Exception exception) {
            log.error("netty client failed to start",exception);
        }
    }
}
