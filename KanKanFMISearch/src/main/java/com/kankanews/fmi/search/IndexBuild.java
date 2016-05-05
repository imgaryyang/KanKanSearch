package com.kankanews.fmi.search;

import java.io.IOException;
import java.sql.ResultSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.kankanews.fmi.utils.DBHelper;

public class IndexBuild {
	public static Directory _directory;

	public static void init() {
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter;
		try {
			iwriter = new IndexWriter(directory, config);
			ResultSet result = DBHelper
					.executeQuery("select xkzh,dwmc,jcrq,jcjg,dz,dwdz from canyin");
			while (result.next()) {
				Document doc = new Document();
				doc.add(new Field("xkzh", setNullString(result
						.getString("xkzh")), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("dwmc", setNullString(result
						.getString("dwmc")), TextField.TYPE_STORED));
				doc.add(new Field("jcrq", setNullString(result
						.getString("jcrq")), TextField.TYPE_STORED));
				doc.add(new Field("jcjg", setNullString(result
						.getString("jcjg")), TextField.TYPE_STORED));
				doc.add(new Field("dz", setNullString(result.getString("dz")),
						TextField.TYPE_STORED));
				doc.add(new Field("dzna",
						setNullString(result.getString("dz")), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("dwdz", setNullString(result
						.getString("dwdz")), TextField.TYPE_STORED));
				iwriter.addDocument(doc);
			}
			iwriter.close();
			_directory = directory;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String setNullString(String src) {
		if (src == null)
			return "";
		return src;
	}
}
