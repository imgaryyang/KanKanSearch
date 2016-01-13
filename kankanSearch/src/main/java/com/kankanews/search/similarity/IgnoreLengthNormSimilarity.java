package com.kankanews.search.similarity;

import org.apache.log4j.Logger;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;

public class IgnoreLengthNormSimilarity extends DefaultSimilarity {
	private static final long serialVersionUID = 1L;

	@Override
	public float idf(long docFreq, long numDocs) {
		return 1f;
	}
}
