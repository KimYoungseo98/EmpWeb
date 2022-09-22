package kr.kosa.web;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.kosa.emp.EmpDao;
import kr.kosa.emp.EmpVo;

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
		//System.out.println("EmpServlet 생성자 실행");
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException { // 서블릿 초기화
		//System.out.println("EmpServlet init() 메서드 실행");
		//String email = config.getInitParameter("email");
		//System.out.println("이메일 주소: " + email);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	EmpDao dao = new EmpDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri=request.getRequestURI();
//		System.out.println(uri);
//		System.out.println(uri.lastIndexOf('/'));
//		System.out.println(uri.substring(7));
		String cmd=uri.substring(uri.lastIndexOf('/'));
		
		String view="/index.jsp";
		if("/EmpList.do".equals(cmd)) {
			System.out.println("모든 사원의 정보를 조회합니다.");
			//DAO 메서드 호출, request에 정보 저장
			request.setAttribute("empList", dao.getAllEmps());
			//System.out.println(dao.getAllEmps().size());
			//view로 포워드(view 경로를 지정)
			view="/WEB-INF/views/emp/emplist.jsp";
		}else if("/EmpInsert.do".equals(cmd)) {
			System.out.println("입력 양식을 요청합니다.");
			request.setAttribute("jobIdList", dao.getJobIdList());
			request.setAttribute("mgrIdList", dao.getEmpIdList());
			request.setAttribute("deptIdList", dao.getDeptIdList());
			view="/WEB-INF/views/emp/empform.jsp";
		}
		RequestDispatcher disp=request.getRequestDispatcher(view);
		disp.forward(request, response);
	/*	System.out.println("doGet 메서드 실행");
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
		disp.forward(request, response);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri=request.getRequestURI();
	      
	      String cmd=uri.substring(uri.lastIndexOf('/'));
	      
	      if("/EmpInsert.do".equals(cmd)) {
	         //입력을 처리
	         String employeeId=request.getParameter("employeeId");
	         String firstName=request.getParameter("firstName");
	         String lastName=request.getParameter("lastName");
	         String email=request.getParameter("email");
	         String phoneNumber=request.getParameter("phoneNumber");
	         String jobId=request.getParameter("jobId");
	         String hireDate=request.getParameter("hireDate");
	         String salary=request.getParameter("salary");
	         String commissionPct=request.getParameter("commissionPct");
	         String managerId=request.getParameter("managerId");
	         String departmentId=request.getParameter("departmentId");
	         
	         EmpVo emp=new EmpVo();
	         
	         emp.setEmployeeId(Integer.parseInt(employeeId));
	         emp.setFirstName(firstName);
	         emp.setLastName(lastName);
	         emp.setEmail(email);
	         emp.setPhoneNumber(phoneNumber);
	         emp.setJobId(jobId);
//	         emp.setHireDate(Date.valueOf(hireDate));  1.8부터 사용 사능
	         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	         try {
	                  emp.setHireDate(new Date(format.parse(hireDate).getTime()));
	               } catch (ParseException e) {
	                  System.out.println("날짜 형식에 맞지 않습니다.");
	               }
	         emp.setSalary(Integer.parseInt(salary));
	         emp.setCommissionPct(Double.parseDouble(commissionPct));
	         emp.setManagerId(Integer.parseInt(managerId));
	         emp.setDepartmentId(Integer.parseInt(departmentId));
	         
	         dao.intsertEmp(emp);
	         response.sendRedirect("EmpList.do");
	         
	        // System.out.println(emp);
		}
	}

}
