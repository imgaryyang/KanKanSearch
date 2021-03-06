package test.kankanews.search.test.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.kankanews.search.test.db.base.JUnitDaoBase;

import com.kankanews.search.db.dao.VideoDAO;
import com.kankanews.search.db.model.Video;

public class VideoDAOTest extends JUnitDaoBase {

	@Autowired
	private VideoDAO videoDAO;

	@Test
	public void testUserDao() {
		Video video = videoDAO.get("200861");
		System.out.println(video.getTitle());
		// System.out.println(userDao.findOneByName("张三s"));
		// System.out.println(userDao.addUser(user));
	}

	@Test
	public void testGetAllNews() throws SQLException {
		ResultSet result = videoDAO.getAllNews();
		// ScrollableResults result =
		// videoDAO.getListByHQLScroll("select v.title from Video v",
		// null);
		int i = 0;
		while (result.next()) {
			System.out.println(++i + "  " + result.getString(1));
		}
		// System.out.println(userDao.findOneByName("张三s"));
		// System.out.println(userDao.addUser(user));
	}

	@Test
	public void testGetRangeNews() throws SQLException {
		long now = new Date().getTime() / 1000;
		ResultSet result = videoDAO.getRangeNews(now - 15768000L, now);
		// ScrollableResults result =
		// videoDAO.getListByHQLScroll("select v.title from Video v",
		// null);
		int i = 0;
		while (result.next()) {
			System.out.println(++i + "  " + result.getString(1));
		}
		// System.out.println(userDao.findOneByName("张三s"));
		// System.out.println(userDao.addUser(user));
	}
}
