package test.kankanews.search.factory;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/*.xml" })
public class SolrClientTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private CloudSolrClient solrClient;

	@Test
	public void testReloadConfig() {
		// File config = new
		// File("G://KanKanServer/solr-1/solr-conf/schema.xml");
		// try {
		// solrClient.uploadConfig(config.toPath(), "schema.xml");
		// solrClient.get
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// SolrCloud s;
		CollectionAdminRequest request = new CollectionAdminRequest();
		// request.set
	}
}
