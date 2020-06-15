package controller;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;

import action.Action;

@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Map<String , Action> classMap=new Hashtable<>();
	
	//FrontController 가 처음 시작할때 딱 한번만 처리되는 메서드
	@Override
	public void init() throws ServletException  {
		// 파일을 연결해서 파일정보 읽어오자
		String file="/action/ClassInfo.properties";
		Properties prop=null;
		try {
			prop = Resources.getResourceAsProperties(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//InputStream is=this.getClass().getResourceAsStream(file);
		/*
		 * Properties prop=new Properties(); try {
		 * prop.load(this.getClass().getResourceAsStream(file)); } catch (IOException
		 * e1) { e1.printStackTrace(); }
		 */
		//Resources 객체를 이용하면 간단하게 properties정보를 읽을수 있다.
		//key만 set으로 저장
		Set<Object> keys=prop.keySet();
		//문자열로된 class정보를 객체로 만드는 과정
		
		for(Object obj:keys) {
			String key=(String)obj;
			String className=(String) prop.get(key);
			try {
				//문자열로된 클래스정보를 Action로객체생성
				Action ac=(Action) Class.forName(className).getDeclaredConstructor().newInstance();
				//생성된 key와 Action을 Map에저장
				classMap.put(key, ac);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	//get메서드로 요청이 되었을때 처리해주는 메서드
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri=request.getRequestURI();
		
		//로직처리하고, 페이지이동처리
		//Action action=null;//
		//action 객체생성....
		
		/*
		 * if(uri.contains("insert")) {//문자열에서 특정글자가 포함되어있는지..판단하는 메서드 action=new
		 * InsertAction(); }else if(uri.contains("update")) { action=new UpdateAction();
		 * }else if(uri.contains("select")) { action=new SelectAction(); }else
		 * if(uri.contains("delete")) { action=new DeleteAction(); }
		 */
		//action=properties파일에있는 내용중 선택한 클래스객체
		
		
		//Map의 정보에서 key에해당하는 객체 읽어서 Action 객체완성
		Action action=classMap.get("insert");
		
		// 로직처리
		String path=null;
		if(action!=null) {
			path=action.process(request, response);
		}
		
		//페이지이동: process메서드의 리턴정보가 path정보이다..
		//페이지이동시 request객체에 데이터도 같이 갖고 갈때 RequestDispatcher를 이용해서 페이지이동
		System.out.println(path);
		//if(path != null) {
		//	RequestDispatcher rd=request.getRequestDispatcher(path);
		//	rd.forward(request, response);
		//}
	}
	
	//post메서드로 요청이 되었을때 처리해주는 메서드
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
