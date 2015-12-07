package test.kankanews.search.test.db.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.kankanews.search.test.db.base.JUnitDaoBase;

import com.kankanews.search.db.dao.VideosDAO;
import com.kankanews.search.db.model.Video;

public class VideosDAOTest extends JUnitDaoBase {

	@Autowired
	private VideosDAO mVideosDAO;

	@Test
	public void testUserDao() {
		Video video = mVideosDAO.get("200861");
		System.out.println(video.getTitle());
		// System.out.println(userDao.findOneByName("张三s"));
		// System.out.println(userDao.addUser(user));
	}
}
