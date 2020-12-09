package board.article;


import java.sql.ResultSet;
import java.sql.SQLException;

import board.RowMapper;

public class ArticleRowMapper implements RowMapper<Article> {
	
	public Article getRow(ResultSet rs) throws SQLException {
	
		String title = rs.getString("title");
		int id = rs.getInt("id");
		String body = rs.getString("body");
		String nickname = rs.getString("nickname2");
		String regDate = rs.getString("regDate");
		int hit = rs.getInt("hit");
		int mid = rs.getInt("mid");
		int like = rs.getInt("like");

		Article article = new Article();
		article.setTitle(title);
		article.setBody(body);
		article.setNickname(nickname);
		article.setId(id);
		article.setRegDate(regDate);
		article.setHit(hit);
		article.setMid(mid);
		article.setLike(like);
		
		return article;
	}
}
