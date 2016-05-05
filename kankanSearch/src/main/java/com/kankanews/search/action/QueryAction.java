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

import com.kankanews.search.service.AppQueryService;
import com.kankanews.search.service.QueryService;
import com.kankanews.search.utils.AnalyseUtil;

@Controller
@RequestMapping("/search")
public class QueryAction {

	Logger logger = Logger.getLogger(QueryAction.class);

	@Autowired
	private QueryService queryService;

	@Autowired
	private AppQueryService appQueryService;

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
			@RequestParam(defaultValue = "*") String sttime,
			@RequestParam(defaultValue = "*") String edtime,
			@RequestParam(defaultValue = "") String notnullfield,
			@RequestParam(defaultValue = "") String classid,
			@RequestParam(defaultValue = "") String areaname,
			@RequestParam(defaultValue = "") String labels,
			@RequestParam(defaultValue = "") String doctable,
			@RequestParam(defaultValue = "") String _item) {
		StringBuffer buf = new StringBuffer();
		Map<String, String> searchTerm = new HashMap<String, String>();
		String analysedWord = null;
		// if (wordsmart != null && !wordsmart.trim().equals("")) {
		// // searchTerm.put("all", word);
		// // searchTerm.put("title", word2);
		// // searchTerm.put("intro", word2);
		// // searchTerm.put("keywords", word2);
		// List<String> words = AnalyseUtil.analyse(wordsmart);
		// StringBuffer wordBuf = new StringBuffer();
		// for (String string : words) {
		// wordBuf.append(string).append(" ");
		// }
		// analysedWord = "(" + wordBuf.toString() + ")";
		// buf.append(" AND (title_smart:").append(analysedWord + "^1");
		// buf.append(" AND keywords_smart:").append(analysedWord + "");
		// buf.append(" OR intro_smart:").append(analysedWord + ")");
		// }
		// if (wordsmart != null && !wordsmart.trim().equals("")) {
		// List<String> words = AnalyseUtil.analyse(wordsmart, true);
		// StringBuffer wordBuf = new StringBuffer();
		// for (String string : words) {
		// wordBuf.append(string).append(" ");
		// }
		// if (wordBuf.toString().trim().equals("")) {
		// Map<String, Object> result = new HashMap<String, Object>();
		// result.put("num", "0");
		// result.put("qtime", "0");
		// result.put("queryresult", "");
		// return result;
		// }
		// analysedWord = "(" + wordBuf.toString() + ")";
		// buf.append(" AND (title:").append(analysedWord + "^10");
		// buf.append(" OR all_smart:").append(analysedWord + ")");
		// }
		if (word != null && !word.trim().equals("")) {
			logger.info("_search:|" + _item + "|" + word + "|");
			List<String> words = AnalyseUtil.analyse(word, false);
			StringBuffer wordBuf = new StringBuffer();
			for (String string : words) {
				wordBuf.append(string).append(" ");
			}
			if (wordBuf.toString().trim().equals("")) {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("num", "0");
				result.put("qtime", "0");
				result.put("queryresult", "");
				return result;
			}
			analysedWord = "(" + wordBuf.toString() + ")";
			buf.append(" AND (title:").append(analysedWord + "^10");
			buf.append(" OR all:").append(analysedWord + ")");
		}
		if (newsid != null && !newsid.trim().equals(""))
			buf.append(" AND id:").append(newsid);
		if (type != null && !type.trim().equals(""))
			buf.append(" AND type:").append(type);
		if (checked != null && !checked.trim().equals(""))
			buf.append(" AND checked:").append(checked);
		if (classid != null && !classid.trim().equals(""))
			buf.append(" AND classid:").append(classid);
		if (author != null && !author.trim().equals(""))
			buf.append(" AND author:").append(author);
		if (authorid != null && !authorid.trim().equals(""))
			buf.append(" AND authorid:").append(authorid);
		if (title != null && !title.trim().equals("")) {
			buf.append(" AND titleGroup:").append(escapeQueryChars(title));
		}
		if (contentid != null && !contentid.trim().equals(""))
			buf.append(" AND contentid:").append(contentid);
		if (nreinfo != null && !nreinfo.trim().equals(""))
			buf.append(" AND nreinfo:").append(nreinfo);
		if (areaname != null && !areaname.trim().equals(""))
			buf.append(" AND areaname:").append(areaname);
		if (labels != null && !labels.trim().equals(""))
			buf.append(" AND labels:").append(labels);
		if (taskid != null && !taskid.trim().equals(""))
			buf.append(" AND taskid:").append(taskid);
		if (doctable != null && !doctable.trim().equals(""))
			buf.append(" AND docTable:").append(doctable);
		if (!sttime.trim().equals("*") || !edtime.trim().equals("*")) {
			buf.append(" AND newstime:[ ").append(sttime).append(" TO ")
					.append(edtime).append("]");
		}
		if (notnullfield != null && !notnullfield.trim().equals("")) {
			String[] fields = notnullfield.split("\\|");
			for (String string : fields) {
				buf.append(" AND -" + string + ":\"\"");
			}

		}
		Map<String, Object> result;
		if (isduplicate) {
			result = queryService.search(buf.toString(), page, rows,
					new String[0], new Boolean[0], highlight, analysedWord,
					highlighttag);
		} else {
			result = queryService.searchGroup(buf.toString(), page, rows,
					highlight, analysedWord, highlighttag);
		}
		return result;
	}

	@RequestMapping("/queryApp")
	@ResponseBody
	public Map<String, Object> queryApp(
			@RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String classid,
			@RequestParam(defaultValue = "") String type,
			@RequestParam(defaultValue = "") String checked,
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer rows,
			@RequestParam(defaultValue = "false") boolean highlight,
			@RequestParam(defaultValue = "false") boolean isduplicate,
			@RequestParam(defaultValue = "em") String highlighttag,
			@RequestParam(defaultValue = "*") String sttime,
			@RequestParam(defaultValue = "*") String edtime,
			@RequestParam(defaultValue = "") String _item) {
		StringBuffer buf = new StringBuffer();
		String analysedWord = null;
		if (title != null && !title.trim().equals("")) {
			List<String> words = AnalyseUtil.analyse(title, false);
			StringBuffer wordBuf = new StringBuffer();
			for (String string : words) {
				wordBuf.append(string).append(" ");
			}
			if (wordBuf.toString().trim().equals("")) {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("num", "0");
				result.put("qtime", "0");
				result.put("queryresult", "");
				return result;
			}
			analysedWord = "(" + wordBuf.toString() + ")";
			buf.append(" AND title:").append(analysedWord);
		}
		if (type != null && !type.trim().equals(""))
			buf.append(" AND type:").append(type);
		if (checked != null && !checked.trim().equals(""))
			buf.append(" AND checked:").append(checked);
		if (classid != null && !classid.trim().equals(""))
			buf.append(" AND classid:").append(classid);
		if (!sttime.trim().equals("*") || !edtime.trim().equals("*")) {
			buf.append(" AND newstime:[ ").append(sttime).append(" TO ")
					.append(edtime).append("]");
		}
		Map<String, Object> result;
		if (isduplicate) {
			result = appQueryService.search(buf.toString(), page, rows,
					highlight, analysedWord, highlighttag);
		} else {
			result = appQueryService.searchGroup(buf.toString(), page, rows,
					highlight, analysedWord, highlighttag);
		}
		return result;
	}

	// private String analyseWord(String word) {
	// List<String> words = AnalyseUtil.analyse(word);
	// StringBuffer wordBuf = new StringBuffer();
	// for (String string : words) {
	// wordBuf.append(string).append(" ");
	// }
	// return wordBuf.toString().trim();
	// }

	public static String escapeQueryChars(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '('
					|| c == ')' || c == ':' || c == '^' || c == '[' || c == ']'
					|| c == '\"' || c == '{' || c == '}' || c == '~'
					|| c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
					|| Character.isWhitespace(c)) {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
