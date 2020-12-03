package board;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DBUtil2 {
	
	// ======================== DB ?��?�� ?���? ?��?�� =========================
	// ?��?��?���? ?���?
	String driver = "com.mysql.cj.jdbc.Driver";
	// dbms 주소p
	String url = "jdbc:mysql://localhost:3306/t1?serverTimezone=UTC";
	// ?��?��?�� 계정
	String user = "sbsst";
	// ?��?��?�� 비�?번호
	String pass = "sbs123414";
	
	Connection conn = null;
	
	/*
	 * PreparedStatement ?��?�� 메서?��. sql�? ?��?��?�� ?��?��미터�? 받아 ?��?��미터 바인?��?�� ???�� ?���?
	 * */
	public PreparedStatement getPrepareStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement pstmt = null;
		conn = getConnection();
		pstmt = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			// instanceof?�� ?��?�� ?��?��?��?��?�� ???��?�� ?��?��?�� ?�� ?��?��. 
			// A instanceof B -> A�? B???��?��?���?? 결과?�� true/false
			if (params[i] instanceof Integer) {
				pstmt.setInt(i + 1, (int) params[i]);
			} else {
				pstmt.setString(i + 1, (String) params[i]);
			}
		}

		return pstmt;
	}

	/* <T>?�� ?��?���??��?���? ?���? 코드?�� ???��?�� ?��?��?��?���? ?���? ???��?�� ?��?��?�� ?�� ?��?��?�� �??��처럼
	 ?��?��?���? ???��?? 컴파?�� ?�� ?��?��?��?�� 쪽에?�� 결정?��?�� �?.
	 ?��?�� 메서?��?�� T?�� ArticleDao?��?�� ?���? ?�� ?�� ?��?��진다.
	
	 * 조회결과�? 1�? �??��?��?�� 메서?��. pk�? 조건?���? 조회�? ?���? 무조�? 0�? or 1개�? ?��?���?�?
	 * ?��개�? ?��?��?�� 경우 ?��?��?��?�� getRows?��?�� ?���? ?�� 조회?��?�� ?��?�� 줄일 ?�� ?��?��.
	 * */
	public <T> T getRow(String sql, RowMapper<T> mapper, Object... params) {
		
		T result = null;

		if (getRows(sql, mapper, params).size() != 0) {
			result = getRows(sql, mapper, params).get(0);
		}

		return result;
	}
	
	// resultSet?�� mapper?�� ?���? mapper?��?�� 바인?��?�� 객체�? 리턴?��주는 방식
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