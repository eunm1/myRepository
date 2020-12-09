package board.article;
import java.sql.ResultSet;
import java.sql.SQLException;

import board.RowMapper;

public class ReplyRowMapper implements RowMapper<Reply> {
	public Reply getRow(ResultSet rs) throws SQLException {
		
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
		
		return reply;
	}
}
