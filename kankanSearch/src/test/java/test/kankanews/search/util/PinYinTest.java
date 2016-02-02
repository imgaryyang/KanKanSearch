package test.kankanews.search.util;

import org.junit.Test;

import com.kankanews.search.utils.PinYinUtil;

public class PinYinTest {

	@Test
	public void testGetString() {
		String pinyin = PinYinUtil.converterToFirstSpell("中国共匪");
		System.out.println(pinyin);
	}

	@Test
	public void testHash() {
//		String pinyin = PinYinUtil.converterToFirstSpell("中国共匪");
//		System.out.println(pinyin);
		String word = "我草泥马2333";
		System.out.println(word.hashCode());
		System.out.println(Math.abs(word.hashCode()) % 4);
		String word2 = "我勒个去";
		System.out.println(word2.hashCode());
		System.out.println(Math.abs(word2.hashCode()) % 4);
	}
}
