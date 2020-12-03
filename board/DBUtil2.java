package board;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DBUtil2 {
	
	// ======================== DB ? ? ? λ³? ?Έ? =========================
	// ??Ό?΄λ²? ? λ³?
	String driver = "com.mysql.cj.jdbc.Driver";
	// dbms μ£Όμp
	String url = "jdbc:mysql://localhost:3306/t1?serverTimezone=UTC";
	// ?¬?©? κ³μ 
	String user = "sbsst";
	// ?¬?©? λΉλ?λ²νΈ
	String pass = "sbs123414";
	
	Connection conn = null;
	
	/*
	 * PreparedStatement ?Έ? λ©μ?. sqlκ³? ??? ??Όλ―Έν°λ₯? λ°μ ??Όλ―Έν° λ°μΈ?©? ???  ?΄μ€?
	 * */
	public PreparedStatement getPrepareStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement pstmt = null;
		conn = getConnection();
		pstmt = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			// instanceof? ?΄?€ ?Έ?€?΄?€? ???? ???Ό ? ?¬?©. 
			// A instanceof B -> Aκ°? B?????κΉ?? κ²°κ³Ό? true/false
			if (params[i] instanceof Integer) {
				pstmt.setInt(i + 1, (int) params[i]);
			} else {
				pstmt.setString(i + 1, (String) params[i]);
			}
		}

		return pstmt;
	}

	/* <T>? ? ?λ¦??΄?Όκ³? ?λ©? μ½λ? ???? ? ?΄??Όλ©? ?€λ₯? ???? ?¬?©?  ? ??Ό? λ³??μ²λΌ
	 ? ?΄?κ³? ????? μ»΄ν?Ό ? ?¬?©?? μͺ½μ? κ²°μ ?? κ²?.
	 ?΄?Ή λ©μ?? T? ArticleDao?? ?ΈμΆ? ?  ? ? ?΄μ§λ€.
	
	 * μ‘°νκ²°κ³Όλ₯? 1κ°? κ°?? Έ?€? λ©μ?. pkλ₯? μ‘°κ±΄?Όλ‘? μ‘°νλ₯? ?λ©? λ¬΄μ‘°κ±? 0κ°? or 1κ°κ? ??€λ―?λ‘?
	 * ?κ°κ? ??€?  κ²½μ° ?¬?©??¬ getRows?? ?λ²? ? μ‘°ν?? ?Ό? μ€μΌ ? ??€.
	 * */
	public <T> T getRow(String sql, RowMapper<T> mapper, Object... params) {
		
		T result = null;

		if (getRows(sql, mapper, params).size() != 0) {
			result = getRows(sql, mapper, params).get(0);
		}

		return result;
	}
	
	// resultSet? mapper? ?κ²? mapper?? λ°μΈ?©? κ°μ²΄λ₯? λ¦¬ν΄?΄μ£Όλ λ°©μ
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