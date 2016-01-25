package test.kankanews.search.util;

import org.junit.Test;

import test.kankanews.search.test.db.base.JUnitDaoBase;

import com.kankanews.search.utils.LexiconUtil;

public class LexiconUtilTest extends JUnitDaoBase {

	@Test
	public void testGetString() {
		LexiconUtil.insertDB();
		// System.out.println("河北省唐山市".length());
	}
}
