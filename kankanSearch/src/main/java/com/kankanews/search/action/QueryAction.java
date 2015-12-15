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
import com.kankanews.search.db.model.Video;
import com.kankanews.search.service.QueryService;

@Controller
@RequestMapping("/search")
public class QueryAction {

	Logger logger = Logger.getLogger(QueryAction.class);

	@Autowired
	private QueryService queryService;

	@RequestMapping("/query")
	@ResponseBody
	public List<Video> query(String word, Integer page, Integer rows,
			boolean highlight) {
		QueryResponse response = queryService.Search(new String[] { "all",
				"docversion" }, new String[] { word, "1" }, 0, 100,
				new String[0], new Boolean[0], true);
		List<SolrDocument> list = response.getResults();
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		for (SolrDocument solrDocument : list) {
			System.out.println(solrDocument.get("title"));
		}
//		List<Video> videos = queryService.searchGroup(word, page, rows,
//				highlight);
//		return videos;
		return new ArrayList<Video>();
	}

	@RequestMapping("/queryGroup")
	@ResponseBody
	public String queryGroup() {
		return "test";
	}
}
