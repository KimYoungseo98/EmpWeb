package kr.kosa.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.kosa.emp.EmpDao;

/**
 * Servlet implementation class EmpServlet
 */

public class EmpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmpServlet() {
		super();
		System.out.println("EmpServlet 생성자 실행");
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException { // 서블릿 초기화
		System.out.println("EmpServlet init() 메서드 실행");
		String email = config.getInitParameter("email");
		System.out.println("이메일 주소: " + email);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	EmpDao dao = new EmpDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("doGet 메서드 실행");
		String cmd = request.getParameter("cmd");
		String view = "/";
		if ("empcount".equals(cmd)) {
			System.out.println("사원의 수를 조회하는 요청입니다.");
			String deptStr = request.getParameter("deptid");
			if (deptStr == null) {
				int empcount = dao.getEmpCount();
				request.setAttribute("empcount", empcount);
			} else {
				int deptid = Integer.parseInt(deptStr);
				request.setAttribute("empcount", dao.getEmpCount(deptid));
			}
			view = "/emp/empcount.jsp";

		} else if ("getdept".equals(cmd)) {
			System.out.println("사원의 부서이름을 조회하는 요청입니다.");
			String empidStr = request.getParameter("empid");
			if (empidStr != null) {
				int empid = Integer.parseInt(empidStr);
				request.setAttribute("deptname", dao.getDepartmentNameByEmployeeId(empid));
			} else {
				request.setAttribute("deptname", "사원번호가 전달되어야 합니다.");
			}
			view = "/emp/getdept.jsp";

		} else if ("avgsal".equals(cmd)) {
			System.out.println("부서의 평균 급여를 조회하는 요청입니다.");
			String deptidStr = request.getParameter("deptid");
			if (deptidStr != null) {
				int deptid = Integer.parseInt(deptidStr);
				request.setAttribute("avgsal", dao.getAverageSalaryByDepartment(deptid));
			} else {
				request.setAttribute("avgsal", "부서번호가 전달되어야 합니다.");
			}
			view = "/emp/avgsal.jsp";

		} else if ("empsal".equals(cmd)) {
			System.out.println("사원의 급여를 조회하는 요청입니다.");
			int empid = Integer.parseInt(request.getParameter("empid"));
			request.setAttribute("salary", dao.getSalaryByEmployeeid(empid));
			view = "/emp/empsal.jsp";
		}else if("empdetail".equals(cmd)) {
			int empid=Integer.parseInt(request.getParameter("empid"));
			request.setAttribute("emp", dao.getEmpDetails(empid));
			view="/emp/empdetail.jsp";
		}

		RequestDispatcher disp = request.getRequestDispatcher(view);
		disp.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
