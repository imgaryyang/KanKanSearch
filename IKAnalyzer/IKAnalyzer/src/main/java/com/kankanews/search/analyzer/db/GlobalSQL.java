package com.kankanews.search.analyzer.db;

public class GlobalSQL {
	public static final String _GET_LEXICON_MAX_ID_ = "SELECT max(id) from kk_lexicon";
	public static final String _GET_LEXICON_ALL_ = "SELECT * from kk_lexicon";
	public static final String _GET_LEXICON_INCREMENT_ = "SELECT word from kk_lexicon where id >= ? AND id <= ?";
}
