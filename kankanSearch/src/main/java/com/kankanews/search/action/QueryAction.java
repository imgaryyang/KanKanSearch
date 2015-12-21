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

import com.kankanews.search.db.model.SearchResult;
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
			@RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String newsid,
			@RequestParam(defaultValue = "") String type,
			@RequestParam(defaultValue = "") String author,
			@RequestParam(defaultValue = "") String authorid,
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer rows,
			@RequestParam(defaultValue = "false") boolean highlight,
			@RequestParam(defaultValue = "true") boolean isduplicate) {
		Map<String, String> searchTerm = new HashMap<String, String>();
		if (word != null && !word.trim().equals(""))
			searchTerm.put("all", word);
		if (newsid != null && !newsid.trim().equals(""))
			searchTerm.put("id", newsid);
		if (type != null && !type.trim().equals(""))
			searchTerm.put("type", type);
		if (author != null && !author.trim().equals(""))
			searchTerm.put("author", author);
		if (authorid != null && !authorid.trim().equals(""))
			searchTerm.put("type", authorid);
		if (title != null && !title.trim().equals(""))
			searchTerm.put("titleGroup", title);
		Map<String, Object> result;
		if (isduplicate) {
			result = queryService.search(searchTerm, page, rows,
					new String[0], new Boolean[0], highlight);
		} else {
			result = queryService.searchGroup(searchTerm, page, rows,
					highlight);
		}
		return result;
	}

	@RequestMapping("/queryGroup")
	@ResponseBody
	public String queryGroup() {
		return "test";
	}
}
