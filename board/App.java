package board;
import java.util.ArrayList;
import java.util.Scanner;

import board.article.Article;
import board.article.ArticleDao;
import board.article.Reply;
import board.member.Member;
import board.member.MemberDao;

//수정 확인하기
//삭제 진해아기
//read부분 출력 수정하기

public class App {
	private ArticleDao articleDao = new ArticleDao();
	private Scanner sc = new Scanner(System.in);
	private Member member = new Member();
	private MemberDao memberDao = new MemberDao();
	private String[] cmd;
	
	public void start() {
		while (true) {
			inputCommand();
			if(cmd[0].equals("article")) {
				if(cmd[1].equals("list")) {
					System.out.println("list");
					list();
				} else if(cmd[1].equals("update")) {
					updateArticle();
				} else if(cmd[1].equals("delete")) {
					deleteArticle();
				} else if(cmd[1].equals("add")) { //로그인 필요
					if(logincheck()) {
						addArticle();
					}
				} else if(cmd[1].equals("read")) { 
					if(logincheck()) {
						readArticle();
					}
				}else if(cmd[1].equals("search")) {
					
				}
			}else if(cmd[0].equals("member")) {
				if(cmd[1].equals("signup")) {
					signup();
				}else if(cmd[1].equals("signin")) {
					signin();
				}else if(cmd[1].equals("signout")) {
					signout();
				}else if(cmd[1].equals("myinfo")) {
					if(logincheck()) {
						myinfo();
					}
				}
			}else if(cmd[0].equals("help")) {
				help();
			}
			
		}
	}
	
	private void myinfo() {
		System.out.println("번호 : " + member.getId());
		System.out.println("아이디 : " + member.getLoginid());
		System.out.println("비번 : " + member.getLoginpass());
		System.out.println("이름 : " + member.getNickname());
		System.out.println("등록일 : " + member.getRegDate());
	}

	private boolean logincheck() {
		if(member.getLoginid() == null) {
			System.out.println("로그인 후 사용이 가능합니다.");
			return false;
		}
		return true;
	}

	private void help() {
		System.out.println("article [add: 게시물 추가 / list : 게시물 목록 조회 / read : 게시물 조회 / search : 검색]");
		System.out.println("member [signup : 회원가입 / signin : 로그인 / findpass : 비밀번호 찾기 / findid : 아이디 찾기 / logout : 로그아웃 / myinfo : 나의 정보 확인및 수정]");
	}

	private void signout() {
		if(member.getLoginid() != null) {
			member = new Member();
			System.out.println("로그아웃이 완료 되었습니다.");
		}
	}

	private void signin() {
		if(member.getLoginid() == null) { //signin 과 signup 중복
			System.out.print("id : ");
			String login_id = sc.nextLine();
			System.out.print("pass : ");
			String login_pass = sc.nextLine();
			
			
			if(memberDao.Checklogin(login_id, login_pass) == null) {
				System.out.println("없는 회원입니다.");
			}
			else{
				member = memberDao.Checklogin(login_id, login_pass);
//				System.out.println("아이디 : " + member.getId());
//				System.out.println("비번 : " + member.getLoginid());
//				System.out.println("비번 : " + member.getLoginpass());
//				System.out.println("이름 : " + member.getNickname());
				System.out.println("["+ member.getNickname() +"로그인]");
				System.out.println( member.getNickname() + "님 안녕하세요!");
				System.out.println("===============================");
			}
			
			
		}else {
			System.out.println("로그아웃 해 주세요");
		}
	}

	private void signup() {
		if(member.getLoginid() == null) {
			System.out.println("======회원가입을 진행합니다======");
			System.out.print("id를 입력해 주세요 : ");
			String login_id = sc.nextLine();
			System.out.print("pass를 입력해 주세요 : ");
			String login_pass = sc.nextLine();
			System.out.print("이름을 입력해 주세요 : ");
			String login_name = sc.nextLine();
			
			if(memberDao.insertSignInfo(login_id, login_pass, login_name)==1) {
				System.out.println("["+ login_name +"회원가입]");
				System.out.println("===============================");
			}
			
		}else {
			System.out.println("로그아웃 해 주세요");
		}
	}

	private void readArticle() {
		System.out.print("상세보기할 게시물 번호 : ");
		int aid = Integer.parseInt(sc.nextLine());
		
		articleDao.updateHit(aid);
		Article article = articleDao.getArticleById(aid);
		
		if(article == null) {
			System.out.println("없는 게시물입니다.");
		} else {
			printArticleAndReply(article, articleDao);
			logincheck();
			while(true) {
				System.out.print("상세보기 기능을 선택해주세요(1. 댓글 등록, 2. 좋아요, 3. 수정, 4. 삭제, 5. 목록으로) : ");
				int dcmd = Integer.parseInt(sc.nextLine());
				if(dcmd == 1) {
					readArticle_addreply(article);
				}else if(dcmd == 3){
					if(member.getId() == article.getMid()) {
						updateArticleToRead(aid);
						article = articleDao.getArticleById(aid);
						printArticle(article);
					}else {
						System.out.println("자신의 게시물만 수정 삭제 할 수 있습니다");
					}
				}else {
					break;
				}
			}
		}
	}
	
	private static void printArticle(Article article) {
		System.out.println("번호 : "+article.getId());
		System.out.println("제목 : "+article.getTitle());
		System.out.println("내용 : "+article.getBody());
		System.out.println("저자 : "+article.getNickname());
		System.out.println("저자 : "+article.getRegDate());
		System.out.println("저자 : "+article.getHit());
		System.out.println("================================");
	}

	private void updateArticleToRead(int aid) {
		String[] TitleandBody;
		TitleandBody = getTitleAndBody();
		articleDao.updateArticle(TitleandBody[0], TitleandBody[1], aid);
	}

	private void readArticle_addreply(Article article) {
		System.out.print("내용을 입력해주세요 :");
		String body = sc.nextLine();
		articleDao.insertReply(article.getId(), body, member.getNickname());
		printArticleAndReply(article, articleDao);
	}

	private void addArticle() {
		String[] TitleandBody;
		TitleandBody = getTitleAndBody();
		
		articleDao.insertArticle(TitleandBody[0], TitleandBody[1], member.getId());
	}

	private void deleteArticle() {
		System.out.print("삭제할 게시물 번호 : ");
		int aid = Integer.parseInt(sc.nextLine());
		articleDao.deleteArticle(aid);
	}

	private void updateArticle() {
		System.out.print("수정할 게시물 번호 : ");
		int aid = Integer.parseInt(sc.nextLine());
		
		String[] TitleandBody;
		TitleandBody = getTitleAndBody(); //return 두개의 값 받는 방법 2
		/*
		 * return 두개의 값 받는 방법 1 class생성하기
		 * return 두개의 값 받는 방법 3 pair클래스 사용하기
		 * https://qastack.kr/programming/2832472/how-to-return-2-values-from-a-java-method
		 * */
		articleDao.updateArticle(TitleandBody[0], TitleandBody[1], aid);
	}

	private String[] getTitleAndBody() {
		System.out.print("제목 : ");
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();
		
		return new String[] {title, body};
	}

	private void inputCommand() {
		String id = member.getLoginid();
		String name = member.getNickname();
		if(id == null) {
			id = "";
			name = "";
		}
		System.out.print("명령어를 입력해주세요["+id+"("+name+")] : ");
		String getcmd = sc.nextLine();
		this.cmd = getcmd.split(" ");
		
	}

	private void list() {
		ArrayList<Article> articles = articleDao.getArticles();
		articleListPrint(articles);
	}

	private void articleListPrint(ArrayList<Article> articles) {
		for(int i = 0; i < articles.size(); i++) {
			System.out.println("번호 : "+articles.get(i).getId());
			System.out.println("제목 : "+articles.get(i).getTitle());
			//System.out.println("내용 : "+articles.get(i).getBody());
			System.out.println("저자 : "+articles.get(i).getNickname());
			System.out.println("등록일 : "+articles.get(i).getRegDate());
			System.out.println("=================================");
		}
	}

	public static void printArticleAndReply(Article article, ArticleDao articleDao) {
		printArticle(article);
		ArrayList<Reply> reply =  articleDao.getReplyByArticleId(article.getId());
		
		for(int i = 0 ; i < reply.size() ; i++) {
			System.out.println("번호 : "+reply.get(i).getId());
			System.out.println("게시물 번호 : "+reply.get(i).getParentId());
			System.out.println("내용 : "+reply.get(i).getBody());
			System.out.println("작성자 : "+reply.get(i).getWriter());
			System.out.println("날짜 : "+reply.get(i).getRegDate());
			System.out.println("================================");
		}
	}
}
