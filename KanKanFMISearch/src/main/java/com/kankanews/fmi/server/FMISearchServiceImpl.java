package com.kankanews.fmi.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
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
		System.out.println(keyword);
		List<String> tempList = new ArrayList<String>();
		Date data1 = new Date();

		prefixQueryHint(tempList, "dzna", keyword);
		if (tempList.size() < 5) {
			standQueryHint(tempList, "dz", keyword);
		}
		Date data2 = new Date();
		System.out.println(data2.getTime() - data1.getTime());
		System.out.println(tempList.size());
		StringBuffer buf = new StringBuffer();
		for (String string : tempList) {
			buf.append(string);
			buf.append(",");
		}
		return buf.toString().substring(0, buf.length() - 1);
	}

	public String search(String keyword, int page, int num) throws TException {
		List<Restaurant> tempList = new ArrayList<Restaurant>();
		if (page > 100)
			return "";
		int start = (page - 1) * num;
		standQuery(tempList, keyword, start, start + num - 1);

		return GsonUtil.toString(tempList);
	}

	public void standQuery(List<Restaurant> resultList, String word, int start,
			int end) {
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
			ScoreDoc[] hits = isearcher.search(query, null, end).scoreDocs;
			// 迭代输出结果
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
