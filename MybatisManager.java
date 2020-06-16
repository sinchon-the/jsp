package mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

//싱글톤 패턴 SqlSessionFactory객체 만들기
public class MybatisManager {
	
	//외부접근불가
	private static SqlSessionFactory sqlSessionFactory;
	
	//getInstance메서드를 호출해서 SqlSessionFactory객체 생성 반환...
	public static SqlSessionFactory getInstance() {
		
		if(sqlSessionFactory==null)
			new MybatisManager();
		
		return sqlSessionFactory;
	}
	
	//외부에서 생성자로 객체 생성불가
	private MybatisManager() {
		String resource = "org/mybatis/example/mybatis-config.xml";
		InputStream inputStream=null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

}
