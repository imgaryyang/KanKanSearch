package com.kankanews.fmi.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Demo {
	public static void main(String[] args) throws IOException, ParseException {
		IndexBuild.init();
		List<String> tempList = new ArrayList<String>();
		Analyzer analyzer = new StandardAnalyzer();
		Date data1 = new Date();

		prefixQuery(tempList);

		// 读取索引并查询
		DirectoryReader ireader = DirectoryReader.open(IndexBuild._directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		// 解析一个简单的查询
		Filter filters = new DuplicateFilter("dz",
				KeepMode.KM_USE_LAST_OCCURRENCE,
				ProcessingMode.PM_FULL_VALIDATION);
		QueryParser parser = new QueryParser("dz", analyzer);
		parser.setDefaultOperator(QueryParser.Operator.AND);
		Query query = parser.parse("肯");
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		// 迭代输出结果
		System.out.println(hits.length);
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			if (!tempList.contains(hitDoc.get("dz").trim()))
				tempList.add(hitDoc.get("dz").trim());
		}
		ireader.close();

		for (String ss : tempList) {
			System.out.println(ss);
		}

		IndexBuild._directory.close();
		Date data2 = new Date();
		System.out.println(data2.getTime() - data1.getTime());
	}

	public static void prefixQuery(List<String> resultList) {
		try {
			DirectoryReader ireader = DirectoryReader
					.open(IndexBuild._directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			Filter filters = new DuplicateFilter("dzna",
					KeepMode.KM_USE_FIRST_OCCURRENCE,
					ProcessingMode.PM_FULL_VALIDATION);
			Term pre1 = new Term("dzna", "肯");
			Sort sort = new Sort((new SortField("dz", SortField.Type.STRING,
					true)));
			PrefixQuery preQuery = null;
			preQuery = new PrefixQuery(pre1);
			ScoreDoc[] hits = isearcher.search(preQuery, filters, 1000).scoreDocs;
			System.out.println(hits.length);
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				if (!resultList.contains(hitDoc.get("dzna").trim()))
					resultList.add(hitDoc.get("dzna").trim());
			}
			ireader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
