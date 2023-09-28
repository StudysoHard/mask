package com.heyongqiang.zyz.netty;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

// 处理Http协议的Handler，该Handler只会在第一次客户端连接时候有用。
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain()
                .getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果被请求的 URL 以/ws 结尾，那么我们将会把该协议升级为 WebSocket。
        if (wsUri.equalsIgnoreCase(request.getUri())) {
            // 将请求传递给下一个ChannelHandler，即WebSocketServerProtocolHandler处理
            // request.retain()会增加引用计数器，以防止资源被释放
            ctx.fireChannelRead(request.retain());
            return;
        }
        handleHttpRequest(ctx, request);
    }

    /**
     * 该方法读取首页html文件内容，然后将内容返回给客户端展示
     * @param ctx
     * @param request
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // HTTP1.1协议允许客户端先判定服务器是否愿意接受客户端发来的消息主体，以减少由于服务器拒绝请求所带来的额外资源开销
        if (HttpHeaders.is100ContinueExpected(request)) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
            ctx.writeAndFlush(response);
        }
        // 从resources目录读取index.html文件
        RandomAccessFile file = new RandomAccessFile(INDEX, "r");
        // 准备响应头信息
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(response);
        // 输出html文件内容
        ctx.write(new ChunkedNioFile(file.getChannel()));
        // 最后发送一个LastHttpContent来标记响应的结束
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        // 如果不是长链接，则在写操作完成后关闭Channel
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}