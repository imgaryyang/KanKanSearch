package com.kankanews.search.similarity;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;

public class IgnoreLengthNormAndTfSimilarity extends DefaultSimilarity {
	private static final long serialVersionUID = 1L;

	@Override
	// term freq 表示 term 在一个document的出现次数,这里设置为1.0f表示不考滤这个因素影响
	public float tf(float freq) {
		return 1.0f;
	}

	@Override
	public float lengthNorm(FieldInvertState state) {
		return 1f;
	}
}
