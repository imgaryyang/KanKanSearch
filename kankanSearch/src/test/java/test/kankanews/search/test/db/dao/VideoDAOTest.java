package test.kankanews.search.test.db.dao;

import java.util.List;

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
	public void testGetAllVideo() {
		List videos = videoDAO.getListByHQL("from Video", null);
		System.out.println(videos.size());
		// System.out.println(userDao.findOneByName("张三s"));
		// System.out.println(userDao.addUser(user));
	}
}
