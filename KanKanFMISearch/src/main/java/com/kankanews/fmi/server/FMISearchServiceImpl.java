package com.kankanews.fmi.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.sandbox.queries.DuplicateFilter;
import org.apache.lucene.sandbox.queries.DuplicateFilter.KeepMode;
import org.apache.lucene.sandbox.queries.DuplicateFilter.ProcessingMode;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.thrift.TException;

import com.kankanews.fmi.model.Restaurant;
import com.kankanews.fmi.search.IndexBuild;
import com.kankanews.fmi.thrift.interfaze.IFMISearchService;
import com.kankanews.fmi.utils.GsonUtil;

public class FMISearchServiceImpl implements IFMISearchService.Iface {

	public String sayHello(String username) throws TException {
		// TODO Auto-generated method stub
		System.out.println(username);
		return "Hello World";
	}

	public String searchHints(String keyword) throws TException {
		List<String> tempList = new ArrayList<String>();

		prefixQueryHint(tempList, "dzna", keyword);
		if (tempList.size() < 5) {
			standQueryHint(tempList, "dz", keyword);
		}
		StringBuffer buf = new StringBuffer();
		for (String string : tempList) {
			buf.append(string);
			buf.append(",");
		}
		if (buf.toString().trim().equals(""))
			return "";
		return buf.toString().substring(0, buf.length() - 1);
	}

	public String search(String keyword, String page, String num)
			throws TException {
		int pages = Integer.parseInt(page);
		int nums = Integer.parseInt(num);
		List<Restaurant> tempList = new ArrayList<Restaurant>();
		if (pages > 100)
			return "";
		int start = (pages - 1) * nums;
		int resultNum = standQuery(tempList, keyword, start, start + nums - 1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num", resultNum + "");
		map.put("data", tempList);
		return GsonUtil.toString(map);
	}

	public void reIndex() throws TException {
		IndexBuild.init();
	}

	public int standQuery(List<Restaurant> resultList, String word, int start,
			int end) {
		int length = 0;
		try {
			Analyzer analyzer = new StandardAnalyzer();

			// 读取索引并查询
			DirectoryReader ireader = DirectoryReader
					.open(IndexBuild._directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			// 解析一个简单的查询
			QueryParser parser = new MultiFieldQueryParser(new String[] { "dz",
					"dwmc" }, analyzer);
			parser.setDefaultOperator(QueryParser.Operator.AND);
			Query query = parser.parse(word);
			ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
			// 迭代输出结果
			length = hits.length;
			if (hits.length < end)
				end = hits.length;
			for (int i = start; i < end; i++) {

				Document hitDoc = isearcher.doc(hits[i].doc);
				Restaurant res = new Restaurant(hitDoc.get("id"),
						hitDoc.get("xkzh"), hitDoc.get("dwmc"),
						hitDoc.get("jcrq"), hitDoc.get("jcjg"),
						hitDoc.get("dz"), hitDoc.get("dwdz"));
				resultList.add(res);
			}
			ireader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return length;
	}

	public void standQueryHint(List<String> resultList, String field,
			String word) {
		try {
			Analyzer analyzer = new StandardAnalyzer();

			// 读取索引并查询
			DirectoryReader ireader = DirectoryReader
					.open(IndexBuild._directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			// 解析一个简单的查询
			Filter filters = new DuplicateFilter(field,
					KeepMode.KM_USE_LAST_OCCURRENCE,
					ProcessingMode.PM_FULL_VALIDATION);
			QueryParser parser = new QueryParser(field, analyzer);
			parser.setDefaultOperator(QueryParser.Operator.AND);
			Query query = parser.parse(word);
			ScoreDoc[] hits = isearcher.search(query, filters, 10).scoreDocs;
			// 迭代输出结果
			System.out.println(hits.length);
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				if (resultList.size() < 5
						&& !resultList.contains(hitDoc.get("dz").trim()))
					resultList.add(hitDoc.get("dz").trim());
			}
			ireader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prefixQueryHint(List<String> resultList, String field,
			String word) {
		try {
			DirectoryReader ireader = DirectoryReader
					.open(IndexBuild._directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			Filter filters = new DuplicateFilter(field,
					KeepMode.KM_USE_FIRST_OCCURRENCE,
					ProcessingMode.PM_FULL_VALIDATION);
			Term pre1 = new Term(field, word);
			PrefixQuery preQuery = null;
			preQuery = new PrefixQuery(pre1);
			ScoreDoc[] hits = isearcher.search(preQuery, filters, 10).scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				if (resultList.size() < 5
						&& !resultList.contains(hitDoc.get(field).trim()))
					resultList.add(hitDoc.get(field).trim());
			}
			ireader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
