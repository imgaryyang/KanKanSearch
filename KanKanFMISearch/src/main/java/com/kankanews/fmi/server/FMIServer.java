package com.kankanews.fmi.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.kankanews.fmi.search.IndexBuild;
import com.kankanews.fmi.thrift.interfaze.IFMISearchService;

public class FMIServer {
	/**
	 * 启动 Thrift 服务器
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IndexBuild.init();
			// 设置服务端口为 7911
			TServerSocket serverTransport = new TServerSocket(4096);
			// 设置协议工厂为 TBinaryProtocol.Factory
			Factory proFactory = new TBinaryProtocol.Factory();
			// 关联处理器与 Hello 服务的实现
			TProcessor processor = new IFMISearchService.Processor(
					new FMISearchServiceImpl());
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(
					serverTransport).processor(processor));
			System.out.println("Start server on port 4096...");
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}
