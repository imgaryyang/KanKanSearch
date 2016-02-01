package com.kankanews.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;

import com.kankanews.search.config.GlobalConfig;
import com.kankanews.search.db.model.SearchResult;
import com.kankanews.search.utils.JacksonUtil;

public class QueryService {
	Logger logger = Logger.getLogger(QueryService.class);

	private CloudSolrClient solrClient;

	private static final String _SCORE_FUN_ = "product(sum(div(newstime,ms(NOW)),map(sub(ms(NOW),product(newstime,1000)),0,604800000,3,0),map(sub(ms(NOW),product(newstime,1000)),604800000,1209600000,2,0),map(sub(ms(NOW),product(newstime,1000)),1209600000,2592000000,1.8,0),map(sub(ms(NOW),product(newstime,1000)),2592000000,5184000000,1.6,0),map(sub(ms(NOW),product(newstime,1000)),5184000000,7776000000,1.4,0),map(sub(ms(NOW),product(newstime,1000)),7776000000,10368000000,1.2,0),map(sub(ms(NOW),product(newstime,1000)),10368000000,12960000000,1,0),map(sub(ms(NOW),product(newstime,1000)),12960000000,15552000000,0.8,0),map(sub(ms(NOW),product(newstime,1000)),15552000000,23328000000,0.4,0),map(sub(ms(NOW),product(newstime,1000)),23328000000,31536000000,0.2,0)),100)";

	public Map<String, Object> search(String queryStr, int page, int rows,
			String[] sortfield, Boolean[] flag, Boolean isHighLight,
			String hightLightQuery, String highlighttag) {
		// 检测输入是否合法
		String indexVersion = GlobalConfig._INDEX_VERSION_;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("num", "0");
		result.put("qtime", "0");
		result.put("queryresult", "");
		List<SearchResult> queryResult = new ArrayList<SearchResult>();
		if (null == sortfield || null == flag
				|| sortfield.length != flag.length) {
			return null;
		}
		SolrQuery query = null;
		StringBuffer queryStrBuf = new StringBuffer();
		try {
			queryStrBuf.append("docversion:" + indexVersion);
			queryStrBuf.append(queryStr);
			query = new SolrQuery();
			query.setStart((page - 1) * rows);
			query.setRows(rows);
			query.set("shards.tolerant", true);
			// if (isSort) {
			query.setQuery(queryStrBuf.toString());
			query.setParam("sort", "score desc,newstime desc");
			// query.setSort("score", ORDER.desc);
			// query.setSort("newstime", ORDER.desc);
			// } else {
			// query.set("q.alt", queryStrBuf.toString());
			// query.set("defType", "dismax");
			// query.set("bf", _SCORE_FUN_);
			// }hl.q
			if (isHighLight) {
				query.setParam("hl.q", "(intro:" + hightLightQuery
						+ " AND title:" + hightLightQuery + ")");
				query.setHighlight(true); // 开启高亮组件
				query.addHighlightField("intro");// 高亮字段
				query.addHighlightField("title");// 高亮字段
				query.setHighlightSimplePre("<" + highlighttag + ">");// 标记
				query.setHighlightSimplePost("</" + highlighttag + ">");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		logger.info("|" + queryStrBuf.toString() + "|");

		QueryResponse rsp = null;
		try {
			System.out.println(query.getQuery());
			rsp = solrClient.query(query);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return result;
		}
		Map<String, Map<String, List<String>>> map = rsp.getHighlighting();
		for (SolrDocument solrDocument : rsp.getResults()) {
			SearchResult video = new SearchResult(solrDocument);
			if (isHighLight) {
				Map<String, List<String>> highFields = map.get(solrDocument
						.get("docId"));
				if (highFields != null && highFields.get("title") != null
						&& !highFields.get("title").isEmpty()) {
					video.setTitle(map.get(solrDocument.get("docId"))
							.get("title").get(0).toString());
				}
				if (highFields != null && highFields.get("intro") != null
						&& !highFields.get("intro").isEmpty()) {
					video.setIntro(map.get(solrDocument.get("docId"))
							.get("intro").get(0).toString());
				}
			}
			queryResult.add(video);
		}
		result.put("num", rsp.getResults().getNumFound() + "");
		result.put("qtime", rsp.getQTime() + "");
		result.put("queryresult", queryResult);
		// 返回查询结果
		return result;
	}

	/**
	 * @Author fjsh
	 * @Title SearchGroup
	 * @Description 按group进行查找
	 * @param QUERY_CONTENT
	 *            查询内容
	 * @param QUERY_ROWS
	 *            查找的数量,默认是10
	 * @param GROUP
	 *            true or false 是否按group查询
	 * @param GROUP_FIELD
	 *            查询field
	 * @param GROUP_LIMIT
	 *            The number of results (documents) to return for each group.
	 *            Defaults to 1
	 * @Return void
	 * @Throws
	 * @Date 2015-1-7 输出结果的时候，由于定义的数据索引没有做很好是调整，显示的结果并不理想，不过此方法可以作为参考
	 */
	public Map<String, Object> searchGroup(String queryStr, int page, int rows,
			boolean isHighLight, String hightLightQuery, String highlighttag) {
		String indexVersion = GlobalConfig._INDEX_VERSION_;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("num", "0");
		result.put("qtime", "0");
		result.put("queryresult", "");
		List<SearchResult> queryResult = new ArrayList<SearchResult>();
		SolrQuery query = null;
		StringBuffer queryStrBuf = new StringBuffer();
		queryStrBuf.append("docversion:" + indexVersion);
		queryStrBuf.append(queryStr);
		query = new SolrQuery();
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		query.set("shards.tolerant", true);
		query.setParam(GroupParams.GROUP, true);
		query.setParam(GroupParams.GROUP_FIELD, "titleGroup");
		query.setParam(GroupParams.GROUP_LIMIT, "1");
		query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
		query.setParam("hl", isHighLight);

		// if (isSort) {
		query.setQuery(queryStrBuf.toString());
		// query.setSort("score", ORDER.desc);
		// query.setSort("newstime", ORDER.desc);
		query.setParam("sort", "score desc,newstime desc");
		// } else {
		// query.set("q.alt", queryStrBuf.toString());
		// query.set("defType", "dismax");
		// query.set("bf", _SCORE_FUN_);
		// }

		// 设置高亮
		if (isHighLight) {
			query.setParam("hl.q", "(intro:" + hightLightQuery + " AND title:"
					+ hightLightQuery + ")");
			query.setHighlight(true); // 开启高亮组件
			query.addHighlightField("intro");// 高亮字段
			query.addHighlightField("title");// 高亮字段
			query.setHighlightSimplePre("<" + highlighttag + ">");// 标记
			query.setHighlightSimplePost("</" + highlighttag + ">");
		}
		logger.info("|" + queryStrBuf.toString() + "|");
		QueryResponse response = null;
		try {
			response = solrClient.query(query);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return result;
		}
		int foundNum = 0;
		GroupResponse groupResponse = response.getGroupResponse();
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		if (groupResponse != null) {
			for (GroupCommand groupCommand : groupResponse.getValues()) {
				// foundNum += groupCommand.getMatches();
				foundNum += groupCommand.getNGroups();
				for (Group group : groupCommand.getValues()) {
					SolrDocumentList list = group.getResult();
					SearchResult video = new SearchResult(list.get(0));
					if (isHighLight) {
						for (SolrDocument solrDocument : list) {
							Map<String, List<String>> highFields = map
									.get(solrDocument.get("docId"));
							if (highFields != null
									&& highFields.get("title") != null
									&& !highFields.get("title").isEmpty()) {
								video.setTitle(map
										.get(solrDocument.get("docId"))
										.get("title").get(0).toString());
							}
							if (highFields != null
									&& highFields.get("intro") != null
									&& !highFields.get("intro").isEmpty()) {
								video.setIntro(map
										.get(solrDocument.get("docId"))
										.get("intro").get(0).toString());
							}
						}
					}
					queryResult.add(video);
				}
			}
		}
		result.put("num", foundNum + "");
		result.put("qtime", response.getQTime() + "");
		result.put("queryresult", JacksonUtil.toString(queryResult));// GsonUtil.toString(queryResult)
		return result;
	}

	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

}
