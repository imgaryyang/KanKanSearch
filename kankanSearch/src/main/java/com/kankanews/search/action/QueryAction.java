package com.kankanews.search.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kankanews.search.core.AnalyseUtil;
import com.kankanews.search.service.QueryService;

@Controller
@RequestMapping("/search")
public class QueryAction {

	Logger logger = Logger.getLogger(QueryAction.class);

	@Autowired
	private QueryService queryService;

	/**
	 * 
	 * @param word
	 *            title + keywords + intro
	 * @param type
	 *            0 图文 1 视频 3 图集
	 * @param page
	 *            int 页数
	 * @param rows
	 *            int 行数
	 * @param highlight
	 *            true 高亮
	 * @param iswholeword
	 *            false 进行分词 true 分词
	 * @param isduplicate
	 *            false 去重 true 重复
	 * @return
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> query(
			@RequestParam(defaultValue = "") String word,
			@RequestParam(defaultValue = "") String wordsmart,
			@RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String newsid,
			@RequestParam(defaultValue = "") String type,
			@RequestParam(defaultValue = "") String checked,
			@RequestParam(defaultValue = "") String author,
			@RequestParam(defaultValue = "") String authorid,
			@RequestParam(defaultValue = "") String contentid,
			@RequestParam(defaultValue = "") String nreinfo,
			@RequestParam(defaultValue = "") String taskid,
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer rows,
			@RequestParam(defaultValue = "false") boolean highlight,
			@RequestParam(defaultValue = "false") boolean isduplicate,
			@RequestParam(defaultValue = "em") String highlighttag,
			@RequestParam(defaultValue = "") String analyse) {
		boolean isSort = false;
		StringBuffer buf = new StringBuffer();
		Map<String, String> searchTerm = new HashMap<String, String>();
		if (wordsmart != null && !wordsmart.trim().equals("")) {
			// searchTerm.put("all", word);
			// searchTerm.put("title", word2);
			// searchTerm.put("intro", word2);
			// searchTerm.put("keywords", word2);
			List<String> words = AnalyseUtil.analyse(wordsmart);
			StringBuffer wordBuf = new StringBuffer();
			for (String string : words) {
				wordBuf.append(string).append(" ");
			}
			if (words.size() == 1)
				isSort = true;
			String analysedWord = "(" + wordBuf.toString() + ")";
			buf.append(" AND (title_smart:").append(analysedWord + "^10");
			buf.append(" AND keywords_smart:").append(analysedWord + "^0.2");
			buf.append(" OR intro_smart:").append(analysedWord + "^1)");
		}
		if (word != null && !word.trim().equals("")) {
			// searchTerm.put("all2", word2);
			// searchTerm.put("title_smart", word + "^2");
			// searchTerm.put("intro_smart", word + "^1");
			// searchTerm.put("keywords_smart", word + "^0.5");
			List<String> words = AnalyseUtil.analyse(word);
			StringBuffer wordBuf = new StringBuffer();
			for (String string : words) {
				wordBuf.append(string).append(" ");
			}
			if (words.size() == 1)
				isSort = true;
			String analysedWord = "(" + wordBuf.toString() + ")";
			buf.append(" AND (title:").append(analysedWord + "^10");
			buf.append(" AND keywords:").append(analysedWord + "^0.2");
			buf.append(" OR intro:").append(analysedWord + "^1)");
		}
		if (newsid != null && !newsid.trim().equals(""))
			// searchTerm.put("id", newsid);
			buf.append(" AND id:").append(newsid);
		if (type != null && !type.trim().equals(""))
			// searchTerm.put("type", type);
			buf.append(" AND type:").append(type);
		if (checked != null && !checked.trim().equals(""))
			// searchTerm.put("checked", checked);
			buf.append(" AND checked:").append(checked);
		if (author != null && !author.trim().equals(""))
			// searchTerm.put("author", author);
			buf.append(" AND author:").append(author);
		if (authorid != null && !authorid.trim().equals(""))
			// searchTerm.put("authorid", authorid);
			buf.append(" AND authorid:").append(authorid);
		if (title != null && !title.trim().equals(""))
			// searchTerm.put("titleGroup", title);
			buf.append(" AND titleGroup:").append(title);
		if (contentid != null && !contentid.trim().equals(""))
			// searchTerm.put("contentid", contentid);
			buf.append(" AND contentid:").append(contentid);
		if (nreinfo != null && !nreinfo.trim().equals(""))
			// searchTerm.put("nreinfo", nreinfo);
			buf.append(" AND nreinfo:").append(nreinfo);
		if (taskid != null && !taskid.trim().equals(""))
			// searchTerm.put("taskid", taskid);
			buf.append(" AND taskid:").append(taskid);
		Map<String, Object> result;
		if (isduplicate) {
			result = queryService.search(buf.toString(), page, rows,
					new String[0], new Boolean[0], highlight, highlighttag,
					isSort);
		} else {
			result = queryService.searchGroup(buf.toString(), page, rows,
					highlight, highlighttag, isSort);
		}
		return result;
	}

	@RequestMapping("/queryGroup")
	@ResponseBody
	public String queryGroup() {
		return "test";
	}

	private String analyseWord(String word) {
		List<String> words = AnalyseUtil.analyse(word);
		StringBuffer wordBuf = new StringBuffer();
		for (String string : words) {
			wordBuf.append(string).append(" ");
		}
		return wordBuf.toString().trim();
	}
}
