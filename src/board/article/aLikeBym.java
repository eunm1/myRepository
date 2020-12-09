package board.article;

public class aLikeBym {
	private int aid;
	private int mid;
	private String regDate;
	
	aLikeBym(){
		
	}
	
	aLikeBym(int aid, int mid, String regDate){
		this.aid = aid;
		this.mid = mid;
		this.regDate = regDate;
	}
	
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}
