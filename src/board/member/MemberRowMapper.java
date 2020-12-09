package board.member;
import java.sql.ResultSet;
import java.sql.SQLException;

import board.RowMapper;

public class MemberRowMapper implements RowMapper<Member> {

	@Override
	public Member getRow(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String loginid = rs.getString("loginId");
		String loginpass = rs.getString("loginpass");
		String nickname = rs.getString("nickname");
		String regDate = rs.getString("regDate");

		Member member = new Member();
		member.setId(id);
		member.setLoginid(loginid);
		member.setLoginpass(loginpass);
		member.setNickname(nickname);
		member.setRegDate(regDate);
		
		return member;
	}

}
