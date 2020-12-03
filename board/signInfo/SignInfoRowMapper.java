package board.signInfo;
import java.sql.ResultSet;
import java.sql.SQLException;

import board.RowMapper;

public class SignInfoRowMapper implements RowMapper<SignInfo> {
	public SignInfo getRow(ResultSet rs) throws SQLException {
		
		String id = rs.getString("id");
		String pass = rs.getString("pass");
		String name = rs.getString("name");

		SignInfo signinfo = new SignInfo();
		signinfo.setId(id);
		signinfo.setPass(pass);
		signinfo.setName(name);
		
		return signinfo;
	}
}
