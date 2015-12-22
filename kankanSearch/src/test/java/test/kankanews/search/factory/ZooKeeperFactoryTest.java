package test.kankanews.search.factory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/*.xml" })
public class ZooKeeperFactoryTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	public CuratorFramework zookeeperClient;

	@Test
	public void clientTest() {
//		zookeeperClient = zookeeperClient.usingNamespace(null);
		try {
//			zookeeperClient.delete().deletingChildrenIfNeeded()
//					.forPath("/aliases.json");
//			zookeeperClient.delete().deletingChildrenIfNeeded()
//					.forPath("/clusterstate.json"); 
			// zookeeperClient.delete().forPath("/schema.xml");
			// zookeeperClient.create().forPath("/myconf");
			// zookeeperClient.create().forPath("/myconf/schema.xml");
			// zookeeperClient.create().forPath("/myconf/solrconfig.xml");
			byte[] schema = FileUtils.readFileToByteArray(new File(
					"G://solr/conf/schema.xml"));
			zookeeperClient.setData().forPath("/solrConf/schema.xml", schema);
			//
			// byte[] solrconfig = FileUtils.readFileToByteArray(new File(
			// "G://solr/conf/solrconfig.xml"));
			// zookeeperClient.setData().forPath("/solrConf/solrconfig.xml",
			// solrconfig);

			// zookeeperClient.create().forPath("/solrConf/admin-extra.html");
			// byte[] admin_extra = FileUtils.readFileToByteArray(new File(
			// "G://solr/conf/admin-extra.html"));
			// zookeeperClient.setData().forPath("/solrConf/admin-extra.html",
			// admin_extra);
			//
			// zookeeperClient.create().forPath(
			// "/solrConf/admin-extra.menu-bottom.html");
			// byte[] admin_extra_menu_bottom = FileUtils
			// .readFileToByteArray(new File(
			// "G://solr/conf/admin-extra.menu-bottom.html"));
			// zookeeperClient.setData().forPath(
			// "/solrConf/admin-extra.menu-bottom.html",
			// admin_extra_menu_bottom);
			//
			// zookeeperClient.create().forPath(
			// "/solrConf/admin-extra.menu-top.html");
			// byte[] admin_extra_menu_top = FileUtils
			// .readFileToByteArray(new File(
			// "G://solr/conf/admin-extra.menu-top.html"));
			// zookeeperClient.setData()
			// .forPath("/solrConf/admin-extra.menu-top.html",
			// admin_extra_menu_top);
			// zookeeperClient.getData().forPath("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(zookeeperClient.getNamespace());

	}
}
