package board;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import board.article.ArticleRowMapper;


public class DBUtil2 {
	
	// ======================== DB 접속 정보 세팅 =========================
	// 드라이버 정보
	String driver = "com.mysql.cj.jdbc.Driver";
	// dbms 주소
	String url = "jdbc:mysql://localhost:3306/t1?serverTimezone=UTC";
	// 사용자 계정
	String user = "root";
	// 사용자 비밀번호
	String pass = "";
	
	Connection conn = null;
	
	/*
	 * PreparedStatement 세팅 메서드. sql과 필요한 파라미터를 받아 파라미터 바인딩을 대신 해줌
	 * */
	public PreparedStatement getPrepareStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement pstmt = null;
		conn = getConnection();
		pstmt = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			// instanceof는 어떤 인스턴스의 타입을 알아낼 때 사용. 
			// A instanceof B -> A가 B타입입니까? 결과는 true/false
			if (params[i] instanceof Integer) {
				pstmt.setInt(i + 1, (int) params[i]);
			} else {
				pstmt.setString(i + 1, (String) params[i]);
			}
		}

		return pstmt;
	}

	/* <T>는 제너릭이라고 하며 코드에 타입을 정해놓으면 다른 타입을 사용할 수 없으니 변수처럼
	 적어놓고 타입은 컴파일 때 사용하는 쪽에서 결정하는 것.
	 해당 메서드의 T는 ArticleDao에서 호출 할 때 정해진다.
	
	 * 조회결과를 1개 가져오는 메서드. pk를 조건으로 조회를 하면 무조건 0개 or 1개가 나오므로
	 * 한개가 확실할 경우 사용하여 getRows에서 한번 더 조회하는 일을 줄일 수 있다.
	 * */
	public <T> T getRow(String sql, RowMapper<T> mapper, Object... params) {
		
		T result = null;
		ArrayList<T> atricles = getRows(sql, mapper, params);
		if (atricles.size() != 0) {
			result = atricles.get(0);
		}

		return result;
	}
	
	// resultSet을 mapper에 넘겨 mapper에서 바인딩된 객체를 리턴해주는 방식
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