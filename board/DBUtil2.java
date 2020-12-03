package board;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DBUtil2 {
	
	// ======================== DB ? ‘?† ? •ë³? ?„¸?Œ… =========================
	// ?“œ?¼?´ë²? ? •ë³?
	String driver = "com.mysql.cj.jdbc.Driver";
	// dbms ì£¼ì†Œp
	String url = "jdbc:mysql://localhost:3306/t1?serverTimezone=UTC";
	// ?‚¬?š©? ê³„ì •
	String user = "sbsst";
	// ?‚¬?š©? ë¹„ë?ë²ˆí˜¸
	String pass = "sbs123414";
	
	Connection conn = null;
	
	/*
	 * PreparedStatement ?„¸?Œ… ë©”ì„œ?“œ. sqlê³? ?•„?š”?•œ ?ŒŒ?¼ë¯¸í„°ë¥? ë°›ì•„ ?ŒŒ?¼ë¯¸í„° ë°”ì¸?”©?„ ???‹  ?•´ì¤?
	 * */
	public PreparedStatement getPrepareStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement pstmt = null;
		conn = getConnection();
		pstmt = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			// instanceof?Š” ?–´?–¤ ?¸?Š¤?„´?Š¤?˜ ???…?„ ?•Œ?•„?‚¼ ?•Œ ?‚¬?š©. 
			// A instanceof B -> Aê°? B???…?…?‹ˆê¹?? ê²°ê³¼?Š” true/false
			if (params[i] instanceof Integer) {
				pstmt.setInt(i + 1, (int) params[i]);
			} else {
				pstmt.setString(i + 1, (String) params[i]);
			}
		}

		return pstmt;
	}

	/* <T>?Š” ? œ?„ˆë¦??´?¼ê³? ?•˜ë©? ì½”ë“œ?— ???…?„ ? •?•´?†“?œ¼ë©? ?‹¤ë¥? ???…?„ ?‚¬?š©?•  ?ˆ˜ ?—†?œ¼?‹ˆ ë³??ˆ˜ì²˜ëŸ¼
	 ? ?–´?†“ê³? ???…?? ì»´íŒŒ?¼ ?•Œ ?‚¬?š©?•˜?Š” ìª½ì—?„œ ê²°ì •?•˜?Š” ê²?.
	 ?•´?‹¹ ë©”ì„œ?“œ?˜ T?Š” ArticleDao?—?„œ ?˜¸ì¶? ?•  ?•Œ ? •?•´ì§„ë‹¤.
	
	 * ì¡°íšŒê²°ê³¼ë¥? 1ê°? ê°?? ¸?˜¤?Š” ë©”ì„œ?“œ. pkë¥? ì¡°ê±´?œ¼ë¡? ì¡°íšŒë¥? ?•˜ë©? ë¬´ì¡°ê±? 0ê°? or 1ê°œê? ?‚˜?˜¤ë¯?ë¡?
	 * ?•œê°œê? ?™•?‹¤?•  ê²½ìš° ?‚¬?š©?•˜?—¬ getRows?—?„œ ?•œë²? ?” ì¡°íšŒ?•˜?Š” ?¼?„ ì¤„ì¼ ?ˆ˜ ?ˆ?‹¤.
	 * */
	public <T> T getRow(String sql, RowMapper<T> mapper, Object... params) {
		
		T result = null;

		if (getRows(sql, mapper, params).size() != 0) {
			result = getRows(sql, mapper, params).get(0);
		}

		return result;
	}
	
	// resultSet?„ mapper?— ?„˜ê²? mapper?—?„œ ë°”ì¸?”©?œ ê°ì²´ë¥? ë¦¬í„´?•´ì£¼ëŠ” ë°©ì‹
	public <T> ArrayList<T> getRows(String sql, RowMapper<T> mapper, Object... params) {
		
		if (params.length != 0 && params[0] instanceof Object[]) {
			params = (Object[]) params[0];
		}
		
		ArrayList<T> rows = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = getPrepareStatement(sql, params);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				T obj = mapper.getRow(rs);
				rows.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pstmt, conn);
		}

		return rows;
	}

//		public Article getRow(String sql, Object... params) {
//			return getRows(sql, params).get(0);
//		}
	//
	public int updateQuery(String sql, Object... params) {
		if (params.length != 0 && params[0] instanceof Object[]) {
			params = (Object[]) params[0];
		}

		int rst = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = getPrepareStatement(sql, params);
			rst = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt, conn);
		}
		System.out.println(rst);
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

	public void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {

		try {
			if (rs != null) {
				rs.close();
			}
			close(pstmt, conn);
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}