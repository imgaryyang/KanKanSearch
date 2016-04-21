package com.kankanews.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

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
//		try {
//			input = new String(Base64.decode(input));
//		} catch (Exception e1) {
//			ctx.writeAndFlush("args is error");
//		}
//		input = "encrypt|shanghaifabu|[{\"hphm\":\"沪AVF019\",\"hpzl\":\"小型汽车号牌\",\"fdjh\":\"150025154\",\"ctype\":\"wfqk\"}]";
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
					String ss = DES.bytesToHexString(
							DES.encrypt(data.getBytes(), keyEn.getBytes()));
					ctx.writeAndFlush(ss);
					// ctx.writeAndFlush(DESOld.encrypt(data, keyEn));
				} catch (Exception e) {
					ctx.writeAndFlush("encrypt has error");
				}
			} else if ("decrypt".equals(type)) {
				try {
					String str = new String(DES.decrypt(
							DES.hexStringToBytes(data), keyEn.getBytes()), Charset
							.forName("UTF-8"));
					// String str = DESOld.decrypt(data, keyEn);
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
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}