package test.kankanews.search.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kankanews.search.utils.httpsqs.HttpsqsClient;
import com.kankanews.search.utils.httpsqs.HttpsqsException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/*.xml" })
public class HttpsqsClientTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private HttpsqsClient httpsqsClient;

	@Test
	public void testGetString() {
		String str = "";
		try {
			str = httpsqsClient.getString("gggggggg");
		} catch (HttpsqsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);
	}

	@Test
	public void testPostString() {
		httpsqsClient
				.putString("{\"id\":812452,\"classid\":5345,\"table\":\"kk_ecms_outlinks\",\"action\":\"update\"}");
	}
}
