package com.kankanews.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.kankanews.search.db.model.Video;

public class QueryService {
	Logger logger = Logger.getLogger(QueryService.class);

	private CloudSolrClient solrClient;

	public QueryResponse Search(String[] field, String[] key, int start,
			int count, String[] sortfield, Boolean[] flag, Boolean hightlight) {
		// 检测输入是否合法
		if (null == field || null == key || field.length != key.length) {
			return null;
		}
		if (null == sortfield || null == flag
				|| sortfield.length != flag.length) {
			return null;
		}
		SolrQuery query = null;
		try {
			// 初始化查询对象
			query = new SolrQuery(field[0] + ":" + key[0]);
			for (int i = 0; i < field.length; i++) {
				query.addFilterQuery(field[i] + ":" + key[i]);
			}
			// 设置起始位置与返回结果数
			query.setStart(start);
			query.setRows(count);
			query.set("shards.tolerant", true);
			// 设置排序
			for (int i = 0; i < sortfield.length; i++) {
				if (flag[i]) {
					query.addSort(sortfield[i], SolrQuery.ORDER.asc);
				} else {
					query.addSort(sortfield[i], SolrQuery.ORDER.desc);
				}
			}
			// 设置高亮
			if (null != hightlight) {
				query.setHighlight(true); // 开启高亮组件
				query.addHighlightField("keywords");// 高亮字段
				query.addHighlightField("title");// 高亮字段
				query.setHighlightSimplePre("<font color=\"red\">");// 标记
				query.setHighlightSimplePost("</font>");
				// query.setHighlightSnippets(1);// 结果分片数，默认为1
				// query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		QueryResponse rsp = null;
		try {
			rsp = solrClient.query(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 返回查询结果
		return rsp;
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
	public List<Video> searchGroup(String word, int page, int rows,
			boolean isHighLight) {
		List<Video> videos = new ArrayList<Video>();
		SolrQuery query = new SolrQuery();
		// param.addFilterQuery("title:" + QUERY_CONTENT);
		query.setQuery("all:" + word);
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		query.setParam(GroupParams.GROUP, true);
		query.setParam(GroupParams.GROUP_FIELD, "title");
		query.setParam(GroupParams.GROUP_LIMIT, "1");
		query.setParam("hl", isHighLight);

		// 设置高亮
		if (isHighLight) {
			query.setHighlight(true); // 开启高亮组件
			query.setParam("hl.fl", "keywords");
			query.setParam("hl.q", "keywords:" + word);
			// query.addHighlightField("keywords");// 高亮字段
			query.setHighlightSimplePre("<font color=\"red\">");// 标记
			query.setHighlightSimplePost("</font>");
			// query.setHighlightSnippets(1);// 结果分片数，默认为1
			// query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100
		}
		QueryResponse response = null;
		try {
			response = solrClient.query(query);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		GroupResponse groupResponse = response.getGroupResponse();
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		if (groupResponse != null) {
			for (GroupCommand groupCommand : groupResponse.getValues()) {
				for (Group group : groupCommand.getValues()) {
					SolrDocumentList list = group.getResult();
					Video video = new Video(list.get(0));
					if (isHighLight) {
						for (SolrDocument doc : list) {
							video.setTitle(map.get(doc.getFieldValue("id"))
									.toString());
							System.out.println(map.get(doc.getFieldValue("id")
									.toString()));
						}
					}
					videos.add(video);
				}
			}
		}
		return videos;
	}

	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

}
