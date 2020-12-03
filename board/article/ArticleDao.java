package board.article;
import java.util.ArrayList;

import board.DBUtil2;

public class ArticleDao {

	private DBUtil2 db = new DBUtil2();
	
	public ArrayList<Article> getArticles() {
		String sql = "SELECT a.*, m.nickname nickname2 FROM article a INNER JOIN `member` m ON a.mid = m.id";
		return db.getRows(sql, new ArticleRowMapper());
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
		String sql = "SELECT a.*, m.nickname nickname2 FROM article a INNER JOIN `member` m ON a.mid = m.id where id = ?";
		return db.getRow(sql,new ArticleRowMapper(), aid);
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

}
