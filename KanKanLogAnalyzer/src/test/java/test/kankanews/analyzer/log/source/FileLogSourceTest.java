package test.kankanews.analyzer.log.source;

import java.io.File;

import org.junit.Test;

import com.kankanews.analyzer.log.source.FileLogSource;
import com.kankanews.analyzer.log.source.LogSource;

public class FileLogSourceTest {

	@Test
	public void mainTest() {
		LogSource logSource = new FileLogSource("C://Users/mu/Desktop/新建文件夹/log4j.properties");
		String line;
		while ((line = logSource.next()) != null) {
			System.out.println(line);
		}
		logSource.closs();
	}
	
	@Test
	public void test(){
//		String str = "|docversion:0 AND (title:(x6 x 6 )^10 OR all2:(x6 x 6 )) AND type:1 AND checked:1 AND nreinfo:0|";
//		int i = str.indexOf("title:") + 7;
//		int j = i;
//		while(str.charAt(j) != ')'){
//			j++;
//		}
//		System.out.println(str.subSequence(i, j));

//		String root = "C://Users/mu/Desktop/新建文件夹/logs";
//		File rootDirectory = new File(root);
//		for (String ss : rootDirectory.list()) {
//			System.out.println(ss);
//		}
		String log = "2016-02-05 16:24:47,638 - INFO - _word_:(好莱坞影帝道格拉斯自曝罹患舌癌)|";
		String[] ssss = log.split(" - ");
		String word = ssss[2];
		System.out.println(word.indexOf("_word_:"));
		System.out.println(word.subSequence(8, word.length() - 2));
		
	}

}
