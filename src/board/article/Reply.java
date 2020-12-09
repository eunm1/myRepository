package board.article;

public class Reply {

	int id;
	int parentId;
	String body;
	String writer;
	String regDate;
	
	public Reply() {
		
	}
	
	public Reply(int id, int parentId, String body, String writer, String regDate) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.body = body;
		this.writer = writer;
		this.regDate = regDate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}
