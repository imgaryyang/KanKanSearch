package com.kankanews.search.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kankanews.search.service.QueryService;

@Controller
@RequestMapping("/query")
public class QueryAction {

	Logger logger = Logger.getLogger(QueryAction.class);

	@Autowired
	private QueryService queryService;

	@RequestMapping("/query")
	@ResponseBody
	public String query() {
		QueryResponse response = queryService.Search(
				new String[] { "title_iksmart" }, new String[] { "习近平" }, 0,
				100, new String[0], new Boolean[0], false);
		List<SolrDocument> list = response.getResults();
		for (SolrDocument solrDocument : list) {
			System.out.println(solrDocument.get("title"));
		}
		return "test";
	}

	@RequestMapping("/queryGroup")
	@ResponseBody
	public String queryGroup() {
		queryService.SearchGroup("习近平", 1, true, "title", "10");
		return "test";
	}
}
