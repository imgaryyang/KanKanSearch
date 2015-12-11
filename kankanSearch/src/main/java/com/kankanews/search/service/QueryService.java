package com.kankanews.search.service;

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
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;

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
				query.addHighlightField("title_ikusersmart");// 高亮字段
				query.setHighlightSimplePre("<font color=\"red\">");// 标记
				query.setHighlightSimplePost("</font>");
				query.setHighlightSnippets(1);// 结果分片数，默认为1
				query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100

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
	public void SearchGroup(String QUERY_CONTENT, int QUERY_ROWS,
			Boolean GROUP, String GROUP_FIELD, String GROUP_LIMIT) {
		SolrQuery param = new SolrQuery();
		// param.addFilterQuery("title:" + QUERY_CONTENT);
		param.setQuery("title_iksmart:" + QUERY_CONTENT);
		param.setRows(QUERY_ROWS);
		param.setParam(GroupParams.GROUP, GROUP);
		param.setParam(GroupParams.GROUP_FIELD, GROUP_FIELD);
		param.setParam(GroupParams.GROUP_LIMIT, GROUP_LIMIT);
		QueryResponse response = null;
		try {
			response = solrClient.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Integer> info = new HashMap<String, Integer>();
		GroupResponse groupResponse = response.getGroupResponse();
		if (groupResponse != null) {
			List<GroupCommand> groupList = groupResponse.getValues();
			System.out.println(groupList.size());
			for (GroupCommand groupCommand : groupList) {
				List<Group> groups = groupCommand.getValues();
				System.out.println(groups.size());
				for (Group group : groups) {
					SolrDocumentList list = group.getResult();
					info.put(group.getGroupValue(), (int) group.getResult()
							.getNumFound());
					System.out.println(group.getGroupValue() + "---"
							+ group.getResult().getNumFound());
				}
			}
		}
	}

	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

}
