package com.kankanews.security;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class KanKanDes {
	private static Logger logger = Logger.getLogger(KanKanDes.class);
	public static Map<SelectionKey, String> selectionKeyMap = new HashMap<SelectionKey, String>();

	public static void main(String[] args) {
		logger.info("service start");
		try {
			ByteBuffer buf = ByteBuffer.allocate(1024);
			ServerSocketChannel serverSocketChannel = ServerSocketChannel
					.open();

			serverSocketChannel.socket().bind(new InetSocketAddress(2046));

			Selector selector = Selector.open();
			serverSocketChannel.configureBlocking(false);
			SelectionKey key = serverSocketChannel.register(selector,
					SelectionKey.OP_ACCEPT);
			while (true) {
				int readyChannels = selector.select();
				if (readyChannels == 0)
					continue;
				Set selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
				while (keyIterator.hasNext()) {
					SelectionKey keySelected = keyIterator.next();
					if (keySelected.isAcceptable()) {
						ServerSocketChannel channel = (ServerSocketChannel) keySelected
								.channel();
						SocketChannel socketChannel = channel.accept();
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_READ);// 注册读事件
						// a connection was accepted by a ServerSocketChannel.
					} else if (keySelected.isConnectable()) {
						// a connection was established with a remote server.
					} else if (keySelected.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) keySelected
								.channel();
						StringBuffer strs = new StringBuffer();
						while (true) {
							buf.clear();
							int n = socketChannel.read(buf);
							if (n > 0) {
								buf.flip();
								strs.append(getString(buf));
							} else if (n == 0) {
								keySelected.interestOps(SelectionKey.OP_WRITE);
								selectionKeyMap.put(keySelected,
										strs.toString());
								break;
							} else if (n == -1) {
								socketChannel.close();
								keySelected.cancel();
								break;
							}
						}
						// a channel is ready for reading
					} else if (keySelected.isWritable()) {
						SocketChannel socketChannel = (SocketChannel) keySelected
								.channel();
						String input = selectionKeyMap.get(keySelected);
//						logger.info(input);
						input = new String(Base64.decode(input), "UTF-8");
						// input = "decrypt|shanghaifab|cfbf49fef2b4b0c0";
						String[] inputArr = input.split("\\|");
						if (inputArr.length < 3) {
							socketChannel.write(ByteBuffer.wrap("args is error"
									.getBytes()));
						} else {
							String type = inputArr[0].toLowerCase();
							String keyEn = inputArr[1];
							String data = inputArr[2];
							if (data == "" || keyEn == "" || type == "") {
								socketChannel.write(ByteBuffer
										.wrap("args is error".getBytes()));
							} else if ("encrypt".equals(type)) {
								try {
									socketChannel
											.write(ByteBuffer.wrap(new String(
													DES.bytesToHexString(DES.encrypt(
															data.getBytes(),
															keyEn.getBytes())))
													.getBytes()));
								} catch (Exception e) {
									logger.error("", e);
									socketChannel.write(ByteBuffer
											.wrap("encrypt has error"
													.getBytes()));
								}
							} else if ("decrypt".equals(type)) {
								try {
									socketChannel.write(ByteBuffer
											.wrap(new String(DES.decrypt(
													DES.hexStringToBytes(data),
													keyEn.getBytes()))
													.getBytes()));
								} catch (Exception e) {
									logger.error("", e);
									socketChannel.write(ByteBuffer
											.wrap("decrypt has error"
													.getBytes()));
								}
							} else {
								socketChannel.write(ByteBuffer
										.wrap("type is no difined".getBytes()));
							}
						}
						keySelected.cancel();
						socketChannel.close();
					}
					keyIterator.remove();
				}
			}
		} catch (ClosedChannelException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("service end");
	}

	/**
	 * ByteBuffer 转换 String
	 * 
	 * @param buffer
	 * @return
	 */
	public static String getString(ByteBuffer buffer) {
		Charset charset = null;
		CharsetDecoder decoder = null;
		CharBuffer charBuffer = null;
		try {
			charset = Charset.forName("UTF-8");
			decoder = charset.newDecoder();
			// charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
			charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
			return charBuffer.toString();
		} catch (Exception ex) {
			logger.error("", ex);
			return "";
		}
	}

}
