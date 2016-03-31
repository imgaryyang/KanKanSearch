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
	public void testAddAll() {
		String version = "0";
		// indexService.addWhole(version);
	}

	@Test
	public void testDeleteAll() {
		String version = "0";
		indexService.deleteWhole(version);
	}

	@Test
	public void testAddOne() {
		// String[] ids = new String[] { "4259717", "4260368", "4260438",
		// "4260519", "4260551", "4260695", "4260708", "4260749",
		// "930184", "4260784", "4259567", "4260203", "4260429",
		// "4260671", "4260416", "4260496", "4260685", "4260713",
		// "930201", "930185", "4257585", "4259187", "4259189", "4259227",
		// "4259229", "4260184", "4260285", "4260408", "4260651",
		// "4260711", "4260726", "4260674", "4260716", "928916", "930195",
		// "930240", "931566", "930186", "4260681", "4259516", "4260081",
		// "4260714", "4260719", "4260661", "4260756", "4260391",
		// "4260453", "4260474", "4260526", "4260727", "930202", "454929",
		// "4260632", "4259555", "4259557", "4259823", "4259349",
		// "4260245", "4260316", "4260575", "4260633", "4260640",
		// "4260664", "4260715" };
		// System.out.println("总更新 : " + ids.length);
		// for (String string : ids) {
		// IncrementNew news = new IncrementNew();
		// news.setId(string);
		// news.setTable("kk_ecms_kankanvideos");
		// indexService.deleteOne(news);
		// indexService.addOne(news);
		// }
		IncrementNew news = new IncrementNew();
		news.setId("4259717");
		news.setTable("kk_ecms_kankanvideos");
		indexService.deleteOne(news);
		// indexService.addOne(news);
	}
}
