package com.kankanews.search.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kankanews.search.core.GsonUtil;
import com.kankanews.search.db.model.SearchResult;
import com.kankanews.search.db.model.Video;
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
	 *            title + keywords
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
	public List<SearchResult> query(String word, String type, Integer page,
			Integer rows, boolean highlight, boolean iswholeword,
			boolean isduplicate) {
		// QueryResponse response = queryService.Search(new String[] { "all",
		// "docversion" }, new String[] { word, "0" }, 0, 100,
		// new String[0], new Boolean[0], false);
		// List<SolrDocument> list = response.getResults();
		// Map<String, Map<String, List<String>>> map =
		// response.getHighlighting();
		// System.out.println(map.get("643564").get("keywords").get(0));
		// for (SolrDocument solrDocument : list) {
		// if (map.get(solrDocument.get("id").toString()) != null &&
		// !map.get(solrDocument.get("id").toString()).get("keywords")
		// .isEmpty()) {
		// System.out.println(map.get(solrDocument.get("id"))
		// .get("keywords").get(0));
		// System.out.println(map.get(solrDocument.get("id")).get("title")
		// .get(0));
		// }
		// System.out.println(solrDocument.get("id"));
		// }

		List<SearchResult> results = queryService.searchGroup(word, page, rows,
				highlight);
		return results;
		// return new ArrayList<SearchResult>();
	}

	@RequestMapping("/queryGroup")
	@ResponseBody
	public String queryGroup() {
		return "test";
	}
}
