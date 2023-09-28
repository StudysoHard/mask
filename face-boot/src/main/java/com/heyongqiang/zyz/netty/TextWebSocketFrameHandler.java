package com.heyongqiang.zyz.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

// 处理WebSocket协议的Handler
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup channelGroup;

    public TextWebSocketFrameHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    // 用户事件监听，每次客户端连接时候自动触发
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String content = "Client " + ctx.channel().remoteAddress().toString().substring(1) + " joined";
        System.out.println(content);
        // 如果是握手完成事件，则从Pipeline中删除HttpRequestHandler，并将当前channel添加到ChannelGroup中
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 从Pipeline中删除HttpRequestHandler
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 通知所有已连接的WebSocket客户端，新的客户端已经连接上了
            TextWebSocketFrame msg = new TextWebSocketFrame(content);
            channelGroup.writeAndFlush(msg);
            // 将WebSocket Channel添加到ChannelGroup中，以便可以它接收所有消息
            channelGroup.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    // 每次客户端发送消息时执行
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        System.out.println("读取到的消息：" + msg.retain());
        // 将读取到的消息写到ChannelGroup中所有已经连接的客户端
        channelGroup.writeAndFlush(msg.retain());
    }
}