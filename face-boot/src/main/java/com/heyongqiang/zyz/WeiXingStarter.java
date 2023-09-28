package com.heyongqiang.zyz;

import com.heyongqiang.zyz.netty.ChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@MapperScan("com.heyongqiang.zyz.mapper")
public class WeiXingStarter {

    public static void main(String[] args) {
        SpringApplication.run(WeiXingStarter.class,args);
        start();
    }
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void start() {
        ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitializer(channelGroup));
            ChannelFuture future = bootstrap.bind(new InetSocketAddress(7777)).syncUninterruptibly();
            System.out.println("Starting ChatServer on port 7777 ...");
            future.channel().closeFuture().syncUninterruptibly();
        } finally {
            channelGroup.close();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
