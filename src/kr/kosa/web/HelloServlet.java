package kr.kosa.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.kosa.emp.EmpDao;

/**
 * Servlet implementation class HelloServlet
 */

public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    EmpDao dao=new EmpDao();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("서블릿 실행");
		
		int count=dao.getEmpCount();
		
		request.setAttribute("cnt", count);//뷰(jsp파일)에서 출력할 데이터를 request에 저장
		
		RequestDispatcher disp=request.getRequestDispatcher("/hello.jsp");
		disp.forward(request, response); //뷰로 포워드
//		response.setCharacterEncoding("ms949");
//		response.getWriter()
//		.append("Served at: ")
//		.append(request.getContextPath())
//		.append("<h1>반갑습니다.</h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
