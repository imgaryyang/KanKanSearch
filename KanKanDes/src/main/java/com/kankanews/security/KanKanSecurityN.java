package com.kankanews.security;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class KanKanSecurityN {

	private int portDes;
	private int portAes;

	public KanKanSecurityN(int portDes, int portAes) {
		this.portDes = portDes;
		this.portAes = portAes;
	}

	public void runDes() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(20); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup(20);
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ByteBuf delimiter = Unpooled
											.copiedBuffer("$=$".getBytes());
									ch.pipeline().addLast(
											new DelimiterBasedFrameDecoder(
													409600, delimiter),
											new StringDecoder(Charset
													.forName("GBK")),
											new StringEncoder(Charset
													.forName("UTF-8")),
											new KanKanDESNHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, false); // (6)
			ChannelFuture f = b.bind(portDes).sync(); // (7)
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void runAes() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(10); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup(10);
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ByteBuf delimiter = Unpooled
											.copiedBuffer("$=$".getBytes());
									ch.pipeline().addLast(
											new DelimiterBasedFrameDecoder(
													409600, delimiter),
											new StringDecoder(Charset
													.forName("UTF-8")),
											new StringEncoder(Charset
													.forName("UTF-8")),
											new KanKanAESNHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, false); // (6)
			ChannelFuture f = b.bind(portAes).sync(); // (7)
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int portDes = 2046;
		int portAes = 2048;
		final KanKanSecurityN desN = new KanKanSecurityN(portDes, portAes);
		new Thread(new Runnable() {
			public void run() {
				try {
					desN.runAes();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				try {
					desN.runDes();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		while (true) {

		}
	}
}
