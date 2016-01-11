package com.kankanews.search.similarity;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;

public class IgnoreLengthNormSimilarity extends DefaultSimilarity {
	private static final long serialVersionUID = 1L;

	@Override
	public float lengthNorm(FieldInvertState state) {
		return 1f;
	}
}
