package test.kankanews.search.util;

import org.junit.Test;

import com.kankanews.search.utils.PinYinUtil;

public class PinYinTest {

	@Test
	public void testGetString() {
		String pinyin = PinYinUtil.converterToFirstSpell("中国共匪");
		System.out.println(pinyin);
	}
}
