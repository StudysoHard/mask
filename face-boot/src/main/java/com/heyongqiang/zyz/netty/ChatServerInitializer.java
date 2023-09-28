package com.heyongqiang.zyz.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Service;

public class ChatServerInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup channelGroup;

    public ChatServerInitializer(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        // 安装编解码器，以实现对HttpRequest、 HttpContent、LastHttp-Content与字节之间的编解码
        pipeline.addLast(new HttpServerCodec());
        // 专门处理写文件的Handler
        pipeline.addLast(new ChunkedWriteHandler());
        // Http聚合器，可以让pipeline中下一个Channel收到完整的HTTP信息
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        // 处理Http协议的ChannelHandler，只会在客户端第一次连接时候有用
        pipeline.addLast(new HttpRequestHandler("/ws"));
        // 升级Websocket后，使用该 ChannelHandler 处理Websocket请求
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 安装专门处理 Websocket TextWebSocketFrame 帧的处理器
        pipeline.addLast(new TextWebSocketFrameHandler(channelGroup));
    }
}