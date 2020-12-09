package board;
import java.util.ArrayList;
import java.util.Scanner;

import board.article.Article;
import board.article.ArticleDao;
import board.article.Reply;
import board.article.aLikeBym;
import board.member.Member;
import board.member.MemberDao;

public class App {
	private ArticleDao articleDao = new ArticleDao();
	private Scanner sc = new Scanner(System.in);
	private Member member = new Member();
	private MemberDao memberDao = new MemberDao();
	private String[] cmd;
	//int currentPage = 1;
	int blockCount = 3; //추후 blockCount 설정하는 명령 생성하기
	int totalPage = 0;
	//좋아요 정렬 기능 추가
	
	/*
	 입력값이 int인데 문자를 입력했을때 예외처리 필요
	 */
	
	public void start() {
		while (true) {
			inputCommand();
			if(cmd[0].equals("article")) {
				if(cmd[1].equals("list")) { //페이지 적용
					list();
				} else if(cmd[1].equals("update")) {
					updateArticle();
				} else if(cmd[1].equals("delete")) {
					deleteArticle();
				} else if(cmd[1].equals("add")) {
					if(logincheck()) {
						addArticle();
					}
				} else if(cmd[1].equals("read")) { 
					if(logincheck()) {
						readArticle();
					}
				}else if(cmd[1].equals("search")) { //paging적용하기
					searchArticle();
				}else if(cmd[1].equals("sort")) { //paging적용하기
					sortByLikeHit();
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
	
	private void pagingArticle(ArrayList<Article> article, String Sort_Order_NOT) {
		totalPageUpdate(article.size());
		int currentPage = 1;
		while(true) {
			
			printPageArticle(Sort_Order_NOT, currentPage);
			
			System.out.print("페이징 명령어를 입력해주세요 \n (prev : 이전, next : 다음, go : 선택, back : 뒤로가기) : ");
			String getcmd = sc.nextLine();
			
			if(getcmd.equals("prev")) { // 이전페이지
				if(currentPage == 1){
					System.out.println("첫번째 페이지 입니다.");
				}else {
					currentPage--;
				}
			}else if(getcmd.equals("next")) { // 다음 페이지
				if(currentPage == totalPage){
					System.out.println("첫번째 페이지 입니다.");
				}else {
					currentPage++;
				}
			}else if(getcmd.equals("go")) { // 페이지 선택
				System.out.print("페이지를 선택해주세요 : ");
				int selectPage = Integer.parseInt(sc.nextLine());
				if(selectPage < 0 || selectPage > totalPage) {
					System.out.println("해당 페이지는 존재하지 않습니다.");
				}else {
					currentPage = selectPage;
				}
			}else if(getcmd.equals("back")) { //이전 페이지 선택으로 가는 건지, 페이지를 나가는 건지
				break;
			}
		}
	}

	private void printPageArticle(String Sort_Order_NOT, int currentPage) {
		ArrayList<Article> articles = articleDao.getArticleByPage(Sort_Order_NOT,currentPage * blockCount - blockCount, blockCount);
		articleListPrint(articles);
		
		for(int i = 1 ; i <= totalPage ; i++) {
			if(i == currentPage) {
				System.out.print(" ["+i+"] ");
			}else {
				System.out.print(" "+ i +" ");
			}
		}
		System.out.println();
	}

	private void totalPageUpdate(int totalCount) {
		
		totalPage = (totalCount / blockCount == 0 ? totalCount / blockCount : (totalCount / blockCount) + 1);
		System.out.println(totalCount+" " + totalPage);
		/*
		 10 / 3 = 3.2, 4페이지
		 1 / 3 = 0, 1헤이지
		 2 / 3 = 0, 1페이지
		 3 / 3 = 1 , 1페이지
		 4 /3 = 1.5, 2페이지
		 */
	}

	
	/*paging 추가하기*/
	private void sortByLikeHit() {
		System.out.print("정렬 대상을 선택해 주세요 (like : 좋아요, hit : 조회수) : ");
		String Sort_column = sc.nextLine();
		if(Sort_column.equals("like") || Sort_column.equals("hit")) {
			if(Sort_column.equals("like")) Sort_column = "`like`";
			System.out.print("정렬 방법을 선택해 주세요 (asc : 오름차순, desc : 내림차순) : ");
			String Sort_tool = sc.nextLine();
			articleListPrint(articleDao.getArticleSort(Sort_column, Sort_tool));
		}
		
	}
	
	/*paging 추가하기*/
	private void searchArticle() {
		System.out.print("검색 항목을 선택해주세요 (1. 제목, 2. 내용, 3. 제목 + 내용, 4. 작성자) : ");
		int num = Integer.parseInt(sc.nextLine());
		
		if(num <= 0 || num > 4)return;
		else {
			System.out.print("검색 키워드를 입력해주세요 : ");
			String keyword = sc.nextLine();
			
			ArrayList<Article> articles = articleDao.getArticlesBySometing(num, keyword);
			articleListPrint(articles);
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
			if(memberDao.CheckloginID(login_id) != null) {
				System.out.println("동일한 ID가 존재합니다.");
			}
			else{
				if(memberDao.insertSignInfo(login_id, login_pass, login_name)==1) {
					System.out.println("["+ login_name +"회원가입]");
					System.out.println("===============================");
				}
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
			while(true) {
				System.out.print("상세보기 기능을 선택해주세요(1. 댓글 등록, 2. 좋아요, 3. 수정, 4. 삭제, 5. 목록으로) : ");
				int dcmd = Integer.parseInt(sc.nextLine());
				if(dcmd == 1) {
					readArticle_addreply(article);
				}else if(dcmd == 2) {
					aLikeBym like = articleDao.isExistLike(article.getId(), member.getId());
					if(like != null) {
						articleDao.deleteLike(article.getId(), member.getId());
						System.out.println("좋아요를 해제했습니다.");
					}else {
						articleDao.insertLike(article.getId(), member.getId());
						System.out.println("좋아요를 했습니다.");
					}
					printArticle(articleDao.getArticleById(article.getId()));
				}
				else if(dcmd == 3){
					if(member.getId() == article.getMid()) {
						updateArticleToRead(aid);
						article = articleDao.getArticleById(aid);
						printArticle(article);
					}else {
						System.out.println("자신의 게시물만 수정할 수 있습니다");
					}
				}else if(dcmd == 4){
					if(member.getId() == article.getMid()) {
						articleDao.deleteArticle(aid);
						list();
					}else {
						System.out.println("자신의 게시물만 삭제할 수 있습니다");
					}
				}
				else {
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
		System.out.println("조회수 : "+article.getHit());
		System.out.println("좋아요 : "+article.getLike());
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
			System.out.print("명령어를 입력해주세요 : ");
		}else {
			System.out.print("명령어를 입력해주세요["+id+"("+name+")] : ");
		}
		
		String getcmd = sc.nextLine();
		this.cmd = getcmd.split(" ");
		
	}

	private void list() {
		ArrayList<Article> articles = articleDao.getArticles();
		pagingArticle(articles, "");
	}

	private void articleListPrint(ArrayList<Article> articles) {
		for(int i = 0; i < articles.size(); i++) {
			printArticle(articles.get(i));
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
