package com.kankanews.fmi.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.kankanews.fmi.thrift.interfaze.IFMISearchService;

public class FMIClient {
	public static void main(String[] args) {
		try {
			System.out.println("ddd");
			// 设置调用的服务地址为本地，端口为 7911
			TTransport transport = new TSocket("localhost", 4096);
			transport.open();
			System.out.println("dd2d");
			// 设置传输协议为 TBinaryProtocol
			TProtocol protocol = new TBinaryProtocol(transport);
			IFMISearchService.Client client = new IFMISearchService.Client(
					protocol);
			// 调用服务的 helloVoid 方法
			System.out.println(client.search("肯", "32", "10"));
			transport.close();
		} catch (TTransportException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}
