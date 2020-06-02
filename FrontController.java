package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"*.do"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//properties파일에서 데이터읽어서 클래스로 변환후 다시 저장
	private Map<String, Action> actionMap;
	
	//servlet이 처음 실행할때 처리해주는 메서드
	@Override
	public void init() throws ServletException {
		
		actionMap=new HashMap<>();
		String path="/WEB-INF/actionConfig.properties";
		InputStream is=getServletContext().getResourceAsStream(path);
		if(is==null)
			System.out.println("파일연결실패!");
		else
			System.out.println("파일연결완료!");
		/////////////////////////////////////////////////////
		//Properties파일에서 정보를 읽어보자
		Properties prop=new Properties();
		try {
			//파일에서 데이터를 읽어서 Properties객체에 저장
			//<String,String>
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		////////////////////////////////////////////////
		//Properties에서 key만 읽어들여서..Set콜렉션으로 저장..
		Set<Object> keys=prop.keySet();
		for(Object obj : keys) {
			//하나의 key읽어오기
			String key= (String)obj;
			
			//key에 대응는 클래스 정보 읽어오기
			String className=prop.getProperty(key);
			//문자열로 클래스정보를 객체로 변환..
			try {
				Action action=(Action) Class.forName(className).newInstance();
				//멤버필드 Map에 저장
				actionMap.put(key, action);
				
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}//init() END
///////////////////////////////////////////////////////////////////////////
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//URI확인
		String uri=request.getRequestURI();
		System.out.println(uri);
		String[] uris=uri.split("/");
		String key=uris[uris.length - 1];//0~2 :3개
		//배열의 마지막 변수 인덱스ㅣ배열.length-1
		System.out.println(key);
		//Action ac=null;
		/*
		 * if(key.equals("page1.do")) { //로직처리_이동주소반환 ac=new Page1Action(); }else
		 * if(key.equals("page2.do")) { //로직처리_이동주소반환 ac=new Page2Action(); }else
		 * if(key.equals("page3.do")) { //로직처리_이동주소반환 ac=new Page3Action(); }
		 */
		
		//key에해당하는 클래스를 actionMap 에서 읽어들이자
		Action ac=actionMap.get(key);
		//실행
		String path=ac.process(request, response);
		
		//페이지 이동
		request.getRequestDispatcher(path).forward(request, response);
		
		
	}//doGet
////////////////////////////////////////////////////////////////////////////////
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
