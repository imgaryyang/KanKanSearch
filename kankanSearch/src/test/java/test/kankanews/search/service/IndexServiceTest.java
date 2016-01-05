package test.kankanews.search.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.ScrollableResults;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.kankanews.search.test.db.base.JUnitDaoBase;

import com.kankanews.search.db.dao.VideoDAO;
import com.kankanews.search.db.model.IncrementNew;
import com.kankanews.search.db.model.Video;
import com.kankanews.search.service.IndexService;

public class IndexServiceTest extends JUnitDaoBase {

	@Autowired
	private IndexService indexService;

	@Test
	public void testAddOne() {
		IncrementNew news = new IncrementNew();
		news.setId("1096903");
		news.setTable("kk_ecms_kankanvideos");
		indexService.deleteOne(news);
		indexService.addOne(news);
	}
}
