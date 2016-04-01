package com.kankanews.security;

import java.io.UnsupportedEncodingException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles a server-side channel.
 */
public class KanKanDESNHandler extends ChannelInboundHandlerAdapter { // (1)

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		String input = (String) msg;
		System.out.println(input);
		try {
			input = new String(Base64.decode(input), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			ctx.writeAndFlush("args is error");
		}
		String[] inputArr = input.split("\\|");
		if (inputArr.length < 3) {
			ctx.writeAndFlush("args is error");
		} else {
			String type = inputArr[0].toLowerCase();
			String keyEn = inputArr[1];
			String data = inputArr[2];
			if (data == "" || keyEn == "" || type == "") {
				ctx.writeAndFlush("args is error");
			} else if ("encrypt".equals(type)) {
				try {
					ctx.writeAndFlush(new String(DES.bytesToHexString(DES
							.encrypt(data.getBytes(), keyEn.getBytes()))));
				} catch (Exception e) {
					ctx.writeAndFlush("encrypt has error");
				}
			} else if ("decrypt".equals(type)) {
				try {
					ctx.writeAndFlush(new String(DES.decrypt(
							DES.hexStringToBytes(data), keyEn.getBytes())));
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
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}