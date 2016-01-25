package com.kankanews.search.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.config.GlobalConfig;
import com.kankanews.search.db.dao.PhotoDAO;
import com.kankanews.search.db.dao.VideoDAO;
import com.kankanews.search.db.model.IncrementNew;
import com.kankanews.search.utils.DBHelper;
import com.kankanews.search.utils.GsonUtil;

public class IndexService {
	Logger logger = Logger.getLogger(IndexService.class);

	private VideoDAO videoDAO;
	private PhotoDAO photoDAO;
	private CloudSolrClient solrClient;

	private static int docIndexNum;
	private static boolean isIndexingWhole = false;

	public boolean addWhole() {
		logger.info("建立索引启动");
		isIndexingWhole = true;
		String indexVersion = GlobalConfig._INDEX_VERSION_;
		int curIndexVersion = Integer.parseInt(indexVersion);
		// int curIndexVersion = Integer.parseInt(indexVersion) + 1;
		Collection<SolrInputDocument> _docs = new ArrayList<SolrInputDocument>();
		ResultSet rs = videoDAO.getAllNews();
		if (rs == null) {
			isIndexingWhole = false;
			return false;
		}
		try {
			solrClient.connect();
			logger.info("建立连接");
			System.gc();
			docIndexNum = 0;
			while (rs.next()) {
				SolrInputDocument doc = resultSet2SolrDoc(rs, curIndexVersion
						+ "");
				docIndexNum++;
				if (doc != null)
					_docs.add(doc);
				if (_docs.size() >= 30000) {
					solrClient.add(_docs);
					Thread.sleep(1000);
					logger.info("提交");
					solrClient.commit();
					Thread.sleep(1000);
					_docs.clear();
				}
			}
			if (!_docs.isEmpty()) {
				solrClient.add(_docs);
				logger.info("提交");
				solrClient.commit();
				_docs.clear();
			}
			_docs = null;
		} catch (Exception e) {
			logger.error("", e);
			try {
				solrClient.deleteByQuery("docversion:" + curIndexVersion);
				solrClient.commit();
			} catch (Exception err) {
				logger.error("", err);
				return false;
			}
			return false;
		} finally {
			isIndexingWhole = false;
			DBHelper.closeConn(rs);
			System.gc();
			// try {
			// solrClient.close();
			// } catch (IOException e) {
			// logger.error("", e);
			// }
		}
		logger.info("建立索引结束");
		return true;
	}

	public boolean optimized() {
		solrClient.connect();
		try {
			solrClient.optimize();
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
			// try {
			// solrClient.close();
			// } catch (IOException e) {
			// logger.error("", e);
			// }
		}
		return true;
	}

	public boolean addOne(IncrementNew incrementNew) {
		String curIndexVersion = GlobalConfig._INDEX_VERSION_;
		String table = incrementNew.getTable();
		ResultSet rs = null;
		if (table.trim().equals("kk_ecms_kankanvideos")) {
			rs = videoDAO.getOne(incrementNew.getId());
		} else if (table.trim().equals("kk_ecms_photo")) {
			rs = photoDAO.getOne(incrementNew.getId());
		}
		solrClient.connect();
		try {
			if (rs != null) {
				while (rs.next()) {
					SolrInputDocument doc = resultSet2SolrDoc(rs,
							curIndexVersion);
					if (doc != null)
						solrClient.add(doc);
				}
				solrClient.commit();
			} else {
				logger.error(GsonUtil.toString(incrementNew) + "未查询到任何记录");
				return false;
			}
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
			DBHelper.closeConn(rs);
			// try {
			// solrClient.close();
			// } catch (IOException e) {
			// logger.error("", e);
			// }
		}
		logger.info("提交" + incrementNew.getId());
		return true;
	}

	public boolean deleteOne(IncrementNew incrementNew) {
		String indexVersion = GlobalConfig._INDEX_VERSION_;
		solrClient.connect();
		try {
			UpdateResponse response = solrClient.deleteByQuery("id:"
					+ incrementNew.getId() + " AND docversion:" + indexVersion);
			if ((Integer) response.getResponseHeader().get("status") == 0) {
				solrClient.commit();
				logger.info("删除索引结束" + incrementNew.getId());
				return true;
			} else
				return false;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
			// try {
			// solrClient.close();
			// } catch (IOException e) {
			// logger.error("", e);
			// }
		}
	}

	public boolean deleteWhole() {
		try {
			solrClient.connect();
			String indexVersion = GlobalConfig._INDEX_VERSION_;
			// int curIndexVersion = Integer.parseInt(indexVersion) + 1;
			// 删除所有的索引
			solrClient.deleteByQuery("docversion:" + indexVersion);
			// solrClient.deleteByQuery("*:*");
			solrClient.commit();
			logger.info("删除索引结束");
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
			// try {
			// solrClient.close();
			// } catch (IOException e) {
			// logger.error("", e);
			// }
		}
		return true;
	}

	public boolean reindex(long stTime, long edTime) {
		String curIndexVersion = GlobalConfig._INDEX_VERSION_;
		ResultSet rs = null;
		Collection<SolrInputDocument> _docs = new ArrayList<SolrInputDocument>();
		try {
			rs = videoDAO.getRangeNews(stTime, edTime);
			solrClient.connect();
			solrClient.deleteByQuery("newstime:[" + stTime + " TO " + edTime
					+ "]" + " AND docversion:" + curIndexVersion);
			if (rs != null) {
				while (rs.next()) {
					SolrInputDocument doc = resultSet2SolrDoc(rs,
							curIndexVersion);
					if (doc != null) {
						_docs.add(doc);
						if (_docs.size() >= 5000) {
							solrClient.add(_docs);
							Thread.sleep(1000);
							logger.info("提交");
							solrClient.commit();
							Thread.sleep(1000);
							_docs.clear();
						}
					}
				}
				if (!_docs.isEmpty()) {
					solrClient.add(_docs);
					logger.info("提交");
					solrClient.commit();
					_docs.clear();
				}
				_docs = null;
				System.gc();
			} else {
				logger.error("重建索引未查询到任何记录");
				return false;
			}
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
			DBHelper.closeConn(rs);
		}
		return true;
	}

	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

	public VideoDAO getVideoDAO() {
		return videoDAO;
	}

	public void setVideoDAO(VideoDAO videoDAO) {
		this.videoDAO = videoDAO;
	}

	public int getDocIndexNum() {
		return docIndexNum;
	}

	public void setDocIndexNum(int docIndexNum) {
		this.docIndexNum = docIndexNum;
	}

	public static boolean isIndexingWhole() {
		return isIndexingWhole;
	}

	public static void setIndexingWhole(boolean isIndexingWhole) {
		IndexService.isIndexingWhole = isIndexingWhole;
	}

	public PhotoDAO getPhotoDAO() {
		return photoDAO;
	}

	public void setPhotoDAO(PhotoDAO photoDAO) {
		this.photoDAO = photoDAO;
	}

	private SolrInputDocument resultSet2SolrDoc(ResultSet rs,
			String curIndexVersion) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", rs.getString("id"));
			doc.addField("classid", rs.getString("classid"));
			doc.addField("type", rs.getString("type"));
			doc.addField("checked", rs.getString("checked"));
			doc.addField("title", rs.getString("title"));
			doc.addField("title_smart", rs.getString("title"));
			doc.addField("titleGroup", rs.getString("title"));
			doc.addField("onclick", rs.getString("onclick"));
			doc.addField("titlepic", rs.getString("titlepic"));
			doc.addField("newstime", rs.getString("newstime"));
			doc.addField("keywords", rs.getString("keywords"));
			doc.addField("keywords_smart", rs.getString("keywords"));
			doc.addField("videourl", rs.getString("videourl"));
			doc.addField("titleurl", rs.getString("titleurl"));
			doc.addField("authorid", rs.getString("authorid"));
			doc.addField("author", rs.getString("author"));
			doc.addField("intro", rs.getString("intro"));
			doc.addField("intro_smart", rs.getString("intro"));
			doc.addField("taskid", rs.getString("taskid"));
			doc.addField("sourceid", rs.getString("sourceid"));
			doc.addField("imagegroup", rs.getString("imagegroup"));
			doc.addField("nreinfo", rs.getString("nreinfo"));
			doc.addField("contentid", rs.getString("contentid"));
			doc.addField("docversion", curIndexVersion);
			doc.addField("docTable", rs.getString("docTable"));
			return doc;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

}
