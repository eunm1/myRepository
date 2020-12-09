package board.article;
import java.util.ArrayList;

import board.DBUtil2;

public class ArticleDao {

	private DBUtil2 db = new DBUtil2();
	
	String basicSql = "SELECT a.*, m.nickname nickname2, COUNT(l.aid) `like` "
			+ "FROM article a "
			+ "LEFT JOIN `member` m ON a.mid = m.id "
			+ "LEFT JOIN AlikeByM l ON a.id = l.aid ";
	
	String groupby = "GROUP BY a.id ";
	
	public ArrayList<Article> getArticles() {
		return db.getRows(basicSql + groupby, new ArticleRowMapper());
	}
	
	public int updateArticle(String title, String body, int aid) {
		String sql = "update article set title = ?, body = ?, regDate = Now() where id = ?";
		return db.updateQuery(sql, title, body, aid);
	}
	
	public int deleteArticle(int aid) {
		String sql = "delete from article where id = ?";
		return db.updateQuery(sql, aid);
	}
	
	public int insertArticle(String title, String body, int mid) {
		String sql = "insert into article set title = ?, body = ?, mid = ?, regDate = NOW(), hit = 0";
		return db.updateQuery(sql, title, body, mid);
	}
	
	public Article getArticleById(int aid) {
		String sql = "where a.id = ? ";
		return db.getRow(basicSql + sql + groupby,new ArticleRowMapper(), aid);
	}
	
	public int insertReply(int aid, String body, String nickname) {
		String sql = "insert into reply set aid = ?, body = ?, writer = ?, regDate = NOW()";
		return db.updateQuery(sql, aid, body, nickname);
	}

	public ArrayList<Reply> getReplyByArticleId(int id) {
		String sql = "Select * from reply where aid = ?";
		return db.getRows(sql, new ReplyRowMapper() , id);
	}

	public int updateHit(int aid) {
		String sql = "update article set hit = hit + 1 where id = ?";
		return db.updateQuery(sql, aid);
	}

	public ArrayList<Article> getArticlesBySometing(int num, String keyword) {
		String sql = "where ";
		
		String where_query;
		keyword = "%"+keyword+"%";
		String groupby = "GROUP BY a.id ";
		
		if(num == 1) where_query = "a.title like ? ";
		else if(num == 2) where_query = "a.body like ? ";
		else if(num == 3) { 
			where_query = "a.title like ? or a.body like ? ";
			return db.getRows(basicSql+sql+where_query+groupby, new ArticleRowMapper(), keyword, keyword);
		}
		else 
			where_query = "m.nickname like ? ";
		
		// title like concat_ws(?,'%','%');
		
		return db.getRows(basicSql+sql+where_query+groupby, new ArticleRowMapper(), keyword);
	}

	public ArrayList<Article> getArticleSort(String sort_column, String sort_tool) {
		
		String sql = "order by " + sort_column + " " + sort_tool;
		return db.getRows(basicSql+groupby+sql, new ArticleRowMapper());
	}

	public aLikeBym isExistLike(int aid, int mid) {
		String sql = "select * from AlikeByM where aid = ? and mid = ?";
		
		return db.getRow(sql, new aLikeByMRowMapper(), aid, mid);
	}

	public void insertLike(int aid, int mid) {
		String sql = "insert into AlikeByM set aid = ?, mid =?, regDate = NOW()";
		db.updateQuery(sql, aid, mid);
	}

	public void deleteLike(int aid, int mid) {
		String sql = "delete from AlikeByM where aid = ? and mid = ?";
		db.updateQuery(sql, aid, mid);
	}

	public ArrayList<Article> getArticleByPage(String sort_Order_NOT, int currentPage, int blockCount) {
		String sql = "LIMIT ?, ?";
		return db.getRows(basicSql+groupby +sort_Order_NOT + sql, new ArticleRowMapper(), currentPage, blockCount);
	}

}
