package com.kankanews.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.core.GsonUtil;
import com.kankanews.search.core.JacksonUtil;
import com.kankanews.search.db.model.SearchResult;
import com.kankanews.search.db.model.Video;

public class QueryService {
	Logger logger = Logger.getLogger(QueryService.class);

	@Autowired
	private Properties globalConfig;
	private CloudSolrClient solrClient;

	public Map<String, Object> search(Map<String, String> searchTerm, int page,
			int rows, String[] sortfield, Boolean[] flag, Boolean isHighLight,
			String highlighttag) {
		// 检测输入是否合法
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
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
		try {
			// 初始化查询对象
			// query = new SolrQuery(field[0] + ":" + key[0]);
			// for (int i = 0; i < field.length; i++) {
			// query.addFilterQuery(field[i] + ":" + key[i]);
			// }
			StringBuffer queryStr = new StringBuffer();
			queryStr.append("docversion:" + indexVersion);
			// query = new SolrQuery("docversion:" + indexVersion + " AND " +
			// "all:习近平");
			for (Map.Entry<String, String> entry : searchTerm.entrySet()) {
				// query.addFilterQuery(entry.getKey() + ":" +
				// entry.getValue());
				queryStr.append(" AND " + entry.getKey() + ":"
						+ entry.getValue());
			}
			query = new SolrQuery(queryStr.toString());
			// 设置起始位置与返回结果数
			query.setStart((page - 1) * rows);
			query.setRows(rows);
			query.set("shards.tolerant", true);
			// 设置排序
			// for (int i = 0; i < sortfield.length; i++) {
			// if (flag[i]) {
			// query.addSort(sortfield[i], SolrQuery.ORDER.asc);
			// } else {
			// query.addSort(sortfield[i], SolrQuery.ORDER.desc);
			// }
			// }
			// 设置高亮
			if (isHighLight) {
				query.setHighlight(true); // 开启高亮组件
				query.addHighlightField("intro");// 高亮字段
				query.addHighlightField("title");// 高亮字段
				// if (highlighttag.trim().equals("")) {
				// query.setHighlightSimplePre("<em>");// 标记
				// query.setHighlightSimplePost("</em>");
				// } else {
				query.setHighlightSimplePre("<" + highlighttag + ">");// 标记
				query.setHighlightSimplePost("</" + highlighttag + ">");
				// }
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

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
	public Map<String, Object> searchGroup(Map<String, String> searchTerm,
			int page, int rows, boolean isHighLight, String highlighttag) {
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("num", "0");
		result.put("qtime", "0");
		result.put("queryresult", "");
		List<SearchResult> queryResult = new ArrayList<SearchResult>();
		// SolrQuery query = new SolrQuery();
		SolrQuery query = null;
		// param.addFilterQuery("title:" + QUERY_CONTENT);
		// query.setQuery("docversion:" + indexVersion);
		// for (Map.Entry<String, String> entry : searchTerm.entrySet()) {
		// query.addFilterQuery(entry.getKey() + ":" + entry.getValue());
		// }
		StringBuffer queryStr = new StringBuffer();
		queryStr.append("docversion:" + indexVersion);
		// query = new SolrQuery("docversion:" + indexVersion + " AND " +
		// "all:习近平");
		for (Map.Entry<String, String> entry : searchTerm.entrySet()) {
			// query.addFilterQuery(entry.getKey() + ":" +
			// entry.getValue());
			queryStr.append(" AND " + entry.getKey() + ":" + entry.getValue());
		}
		query = new SolrQuery(queryStr.toString());
		// for (int i = 0; i < field.length; i++) {
		// query.addFilterQuery(field[i] + ":" + word[i]);
		// }
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		query.set("shards.tolerant", true);
		query.setParam(GroupParams.GROUP, true);
		query.setParam(GroupParams.GROUP_FIELD, "titleGroup");
		query.setParam(GroupParams.GROUP_LIMIT, "1");
		query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
		query.setParam("hl", isHighLight);

		// 设置高亮
		if (isHighLight) {
			query.setHighlight(true); // 开启高亮组件
			query.addHighlightField("intro");// 高亮字段
			query.addHighlightField("title");// 高亮字段
			// query.setParam("hl.q", "keywords:" + word);
			// query.addHighlightField("keywords");// 高亮字段
			// if (highlighttag.trim().equals("")) {
			// query.setHighlightSimplePre("<em>");// 标记
			// query.setHighlightSimplePost("</em>");
			// } else {
			query.setHighlightSimplePre("<" + highlighttag + ">");// 标记
			query.setHighlightSimplePost("</" + highlighttag + ">");
			// }
			// query.setHighlightSnippets(1);// 结果分片数，默认为1
			// query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100
		}
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
