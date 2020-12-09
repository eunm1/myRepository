package board.article;

import java.sql.ResultSet;
import java.sql.SQLException;

import board.RowMapper;

public class aLikeByMRowMapper implements RowMapper<aLikeBym> {

	@Override
	public aLikeBym getRow(ResultSet rs) throws SQLException {
		
		int aid = rs.getInt("aid");
		int mid = rs.getInt("mid");
		String regDate = rs.getString("regDate");
		
		aLikeBym like = new aLikeBym();
		like.setAid(aid);
		like.setMid(mid);
		like.setRegDate(regDate);
		return like;
	}

}
