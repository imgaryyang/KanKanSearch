package com.kankanews.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles a server-side channel.
 */
public class KanKanAESNHandler extends ChannelInboundHandlerAdapter { // (1)

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		String input = (String) msg;
		String[] inputArr = input.split("\\|");
		if (inputArr.length < 3) {
			ctx.writeAndFlush("args is error");
		} else {
			String type = inputArr[0].toLowerCase();
			String keyEn = inputArr[1];
			String data = inputArr[2];
			String iv = inputArr[3];
			if (data == "" || keyEn == "" || type == "" || iv == "") {
				ctx.writeAndFlush("args is error");
			} else if ("encrypt".equals(type)) {
				try {
					System.out.println(keyEn + " " + data + " " + iv);
					String ss = new String(Base64.encode(AES.encrypt(data,
							keyEn, iv)));
					ctx.writeAndFlush(ss);
				} catch (Exception e) {
					ctx.writeAndFlush("encrypt has error");
				}
			} else if ("decrypt".equals(type)) {
				try {
					String str = new String(AES.decrypt(Base64.decode(data),
							keyEn, iv), "utf-8");
					ctx.writeAndFlush(str);
				} catch (Exception e) {
					ctx.writeAndFlush("decrypt has error");
				}
			} else {
				ctx.writeAndFlush("type is no difined");
			}
		}
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		cause.printStackTrace();
		ctx.close();
	}
}