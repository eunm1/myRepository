package board;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import board.article.Article;
import board.article.Reply;
import board.signInfo.SignInfo;


//?†ÑÎ∞òÏ†Å?úºÎ°? ÏΩîÎìúÍ∞? Î∞òÎ≥µ?ù¥ ?êò?ñ¥?Ñú DBUtill2?óê Î¶¨Ìå©?Ü†Îß? ?ïòÍ≤†Îã§.

public class DBUtil {
	// ?ìú?ùº?ù¥Î≤? ?†ïÎ≥?
	String driver = "com.mysql.cj.jdbc.Driver";
	// dbms Ï£ºÏÜåp
	String url = "jdbc:mysql://localhost:3306/t1?serverTimezone=UTC";

	// ?Ç¨?ö©?ûê Í≥ÑÏ†ï
	String user = "sbsst";
	// ?Ç¨?ö©?ûê ÎπÑÎ?Î≤àÌò∏
	String pass = "sbs123414";

	Connection conn = null;
	
	public PreparedStatement getPrepareStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement pstmt = null;
		conn = getConnection();
		pstmt = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			if (params[i] instanceof Integer) {
				pstmt.setInt(i + 1, (int) params[i]);
			} else {
				pstmt.setString(i + 1, (String) params[i]);
			}
		}
		
		return pstmt;
	}
	
	public ArrayList<Article> getRows(String sql, Object...params) {
		if(params.length != 0 && params[0] instanceof Object[]) {
			params = (Object[])params[0];
		}
		
		ArrayList<Article> articles = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = getPrepareStatement(sql, params);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String title = rs.getString("title");
				int id = rs.getInt("id");
				String body = rs.getString("body");
				String nickname = rs.getString("nickname");
				int hit = rs.getInt("hit");

				Article article = new Article();
				article.setTitle(title);
				article.setBody(body);
				article.setNickname(nickname);
				article.setId(id);
				article.setHit(hit);

				articles.add(article);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pstmt, conn);
		}

		return articles;
	}

	// Í∞íÏùò ?ú†Î¨? Ï≤¥ÌÅ¨
	public Article getRow(String sql, Object...params) {
		Article article = null;
		
		if(getRows(sql, params).size() != 0) {			
			article = getRows(sql, params).get(0);
		}
		
		return article;
	}
	
	
	public int updateQuery(String sql, Object... params) {
		if(params.length != 0 && params[0] instanceof Object[]) {
			params = (Object[])params[0];
		}
		
		int rst = 0;
		PreparedStatement pstmt = null;
		
		try {
			System.out.println(sql);
			pstmt = getPrepareStatement(sql, params);
			rst = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt, conn);
		}
		return rst;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;

	}
	
	public ArrayList<Reply> getReplyRows(String sql, Object...params) {
		if(params.length != 0 && params[0] instanceof Object[]) {
			params = (Object[])params[0];
		}
		
		ArrayList<Reply> Reply = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = getPrepareStatement(sql, params);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int aid = rs.getInt("aid");
				int id = rs.getInt("id");
				String body = rs.getString("body");
				String writer = rs.getString("writer");
				String regDate = rs.getString("regDate");

				Reply reply = new Reply();
				reply.setBody(body);
				reply.setId(id);
				reply.setParentId(aid);
				reply.setRegDate(regDate);
				reply.setWriter(writer);
				Reply.add(reply);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pstmt, conn);
		}

		return Reply;
	}
	
	public ArrayList<SignInfo> getRowsBylogin(String sql, Object...params) {
		if(params.length != 0 && params[0] instanceof Object[]) {
			params = (Object[])params[0];
		}
		
		ArrayList<SignInfo> sign = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = getPrepareStatement(sql, params);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String pass = rs.getString("pass");
				String name = rs.getString("name");

				SignInfo signinfo = new SignInfo();
				signinfo.setId(id);
				signinfo.setPass(pass);
				signinfo.setName(name);
				
				sign.add(signinfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pstmt, conn);
		}

		return sign;
	}

	public void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
			
		try {
			if (rs != null) {
				rs.close();
			}
			close(pstmt, conn);			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(PreparedStatement pstmt, Connection conn) {

		try {			
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}
