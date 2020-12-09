package board.member;

import board.DBUtil2;

public class MemberDao {
	private DBUtil2 db = new DBUtil2();

	public int insertSignInfo(String login_id, String login_pass, String login_name) {
//		String sql = "insert into login set id = ?, pass = ?, `name` = ?";
//		return db.updateQuery(sql, login_id, login_pass, login_name);
		String sql = "insert into `member` set loginid = ?, loginpass = ?, nickname = ?, regDate = NOW()";
		return db.updateQuery(sql, login_id, login_pass, login_name);
	}

	public Member Checklogin(String login_id, String login_pass) {
//		public SignInfo Checklogin(String login_id, String login_pass) {
//		String sql = "select * from login where id = ? and pass = ?";
//		return db.getRow(sql, new SignInfoRowMapper(), login_id, login_pass);
		String sql = "select * from `member` where loginid = ? and loginpass = ?";
		return db.getRow(sql, new MemberRowMapper(), login_id, login_pass);
	}

	public Object CheckloginID(String login_id) {
		String sql = "select * from `member` where loginid = ?";
		return db.getRow(sql, new MemberRowMapper(), login_id);
	}
}
