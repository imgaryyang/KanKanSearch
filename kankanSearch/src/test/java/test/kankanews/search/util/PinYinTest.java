package test.kankanews.search.util;

import java.util.Map;

import org.apache.solr.client.solrj.util.ClientUtils;
import org.junit.Test;

import com.kankanews.search.utils.GsonUtil;
import com.kankanews.search.utils.JacksonUtil;
import com.kankanews.search.utils.PinYinUtil;

public class PinYinTest {

	@Test
	public void testGetString() {
		String pinyin = PinYinUtil.converterToFirstSpell("中国共匪");
		System.out.println(pinyin);
	}

	@Test
	public void testHash() {
		// String pinyin = PinYinUtil.converterToFirstSpell("中国共匪");
		// System.out.println(pinyin);
		String word = "我草泥马2333";
		System.out.println(word.hashCode());
		System.out.println(Math.abs(word.hashCode()) % 4);
		String word2 = "我勒个去";
		System.out.println(word2.hashCode());
		System.out.println(Math.abs(word2.hashCode()) % 4);
	}

	@Test
	public void test() {
		String ss = "{\"num\":\"2\",\"queryresult\":[{\"id\":\"7386774\",\"author\":\"\",\"authorid\":\"0\",\"classid\":\"8132\",\"checked\":\"1\",\"imagegroup\":\"\",\"intro\":\"<p>连日来，上海新闻舆论战线深入学习领会习近平总书记新闻舆论工作座谈会重要讲话精神，各单位纷</p><p>纷表示要将总书记提出的“48字”新闻舆论工作的职责和使命，作为新时期全面指导新闻舆论工作的总方</p><p>针，坚持阵地意识、引领意识和创新意识，为开创党的新闻舆论工作新局面作出新贡献。</p>\",\"keywords\":\"\",\"newstime\":\"1456066750\",\"onclick\":\"0\",\"sourceid\":\"\",\"title\":\"上海新闻舆论战线深入学习习近平重要讲话\",\"titlepic\":\"\",\"titleurl\":\"http://www.kankanews.com/a/2016-02-21/0037386774.shtml\",\"type\":\"2\",\"taskid\":\"0\",\"videourl\":\"\"},{\"id\":\"7386773\",\"author\":\"\",\"authorid\":\"0\",\"classid\":\"8132\",\"checked\":\"0\",\"imagegroup\":\"\",\"intro\":\"<p>连日来，上海新闻舆论战线深入学习领会习近平总书记新闻舆论工作座谈会重要讲话精神，各单位纷</p><p>纷表示要将总书记提出的“48字”新闻舆论工作的职责和使命，作为新时期全面指导新闻舆论工作的总方</p><p>针，坚持阵地意识、引领意识和创新意识，为开创党的新闻舆论工作新局面作出新贡献。</p>\",\"keywords\":\"\",\"newstime\":\"1456066328\",\"onclick\":\"5\",\"sourceid\":\"\",\"title\":\"上海新闻舆论战线深入学习习近平重要讲话\",\"titlepic\":\"\",\"titleurl\":\"http://www.kankanews.com/a/2016-02-21/0037386773.shtml\",\"type\":\"2\",\"taskid\":\"0\",\"videourl\":\"\"}],\"qtime\":\"758\"}";
		// JacksonUtil.toObject(ss, Map.class);
		Map map = GsonUtil.toMapObj(ss);
		System.out.println(map);
	}
}
