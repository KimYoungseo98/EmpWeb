package kr.kosa.emp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

public class EmpDao {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 로드
			System.out.println("드라이버 클래스가 로드되었습니다.");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 클래스 로드 실패");
			e.printStackTrace();
		}
	}

	private String url; // = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id; // = "hr";
	private String pw; // = "hr";

	public EmpDao(String url, String id, String pw) {
		this.url = url;
		this.id = id;
		this.pw = pw;
	}

	public EmpDao(ServletContext application) {
		this.url = application.getInitParameter("OracleURL");
		this.id = application.getInitParameter("OracleId");
		this.pw = application.getInitParameter("OraclePwd");
	}

	DataSource dataSource;

	public EmpDao() {
		try {
			Context initCtx = new InitialContext();
			dataSource = (DataSource) initCtx.lookup("java:comp/env/dbcp_myoracle");
			// Context ctx = (Context)initCtx.lookup("java:com/env");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public int getEmpCount() {
		int count = 0;
		// Connection 생성
		Connection con = null;
		try {

			con = dataSource.getConnection();
			System.out.println(con);
			String sql = "select count(*) from employees";
			PreparedStatement stmt = con.prepareStatement(sql); // Statement 생성
			ResultSet rs = stmt.executeQuery(); // SQL 쿼리 전송
			if (rs.next()) { // 결과집합소비
				count = rs.getInt(1);
			}
			System.out.println("사원의 수: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // Connection 닫기
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}

		return count;
	}

	public int getEmpCount(int deptno) {
		int count = 0;
		// Connection 생성
		Connection con = null;
		try {
			// con = DriverManager.getConnection(url, id, pw); // 커넥션 생성
			con = dataSource.getConnection();
			System.out.println(con);
			String sql = "select count(*) from employees where department_id=?";
			PreparedStatement stmt = con.prepareStatement(sql); // Statement 생성
			stmt.setInt(1, deptno);
			ResultSet rs = stmt.executeQuery(); // SQL 쿼리 전송
			if (rs.next()) { // 결과집합소비
				count = rs.getInt(1);
			}
			System.out.println("사원의 수: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // Connection 닫기
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}

		return count;
	}

	/*
	 * public double getAverageSalaryByDepartment(int deptno) { int count = 0; //
	 * Connection 생성 Connection con = null; try { // con =
	 * DriverManager.getConnection(url, id, pw); // 커넥션 생성 con =
	 * dataSource.getConnection(); System.out.println(con); String sql =
	 * "select avg(salary) from employees where department_id=?"; PreparedStatement
	 * stmt = con.prepareStatement(sql); // Statement 생성 stmt.setInt(1, deptno);
	 * ResultSet rs = stmt.executeQuery(); // SQL 쿼리 전송 if(rs.next()) { // 결과집합소비
	 * count = rs.getInt(1); } System.out.println("사원의 수: " + count);
	 * }catch(Exception e) { e.printStackTrace(); }finally { // Connection 닫기
	 * if(con!=null) try { con.close(); } catch(Exception e) {} }
	 * 
	 * return count; }
	 */

	public double getAverageSalaryByDepartment(int deptno) {
		Connection con = null;
		double result = 0;
		try {
			con = dataSource.getConnection();
			String sql = "select round(avg(salary), 2) from employees where department_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, deptno);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getDouble(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
		return result;
	}

	public int getSalaryByEmployeeid(int empid) {
		Connection con = null;
		int salary = 0; // 사원의 급여를 저장할 변수
		try {
			con = dataSource.getConnection();
			String sql = "select salary from employees where employee_id=?";
			PreparedStatement stmt = con.prepareStatement(sql); // statement 객체 생성
			stmt.setInt(1, empid); // 첫번째 거부터 시작 empid를
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				salary = rs.getInt(1); // salary에 값을 저장
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				} // 거의 습관성으로 넣어줘라
		}
		return salary;
	}

	public String getDepartmentNameByEmployeeId(int empid) {
		Connection con = null;
		String departmentName = null;
		try {
			con = dataSource.getConnection();
			String sql = "select department_name from employees "
					+ "join departments on employees.department_id=departments.department_id " + "where employee_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, empid);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				departmentName = rs.getString("department_name");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
		return departmentName;
	}

	 public EmpVo getEmpDetails(int empid) {
	      EmpVo emp = new EmpVo();
	      Connection con = null;
	      try {
	         con = dataSource.getConnection();
	         String sql = "select employee_id, first_name, last_name, email, phone_number,"
	               + " job_id, hire_date, salary, commission_pct, manager_id, department_id "
	               + " from employees where employee_id=?";
	         PreparedStatement stmt = con.prepareStatement(sql);
	         stmt.setInt(1, empid);
	         ResultSet rs = stmt.executeQuery();
	         if(rs.next()) { //결과를 vo에 매핑시킴
	            emp.setEmployeeId(rs.getInt("employee_id"));
	            emp.setFirstName(rs.getString("first_name"));
	            emp.setLastName(rs.getString("last_name"));
	            emp.setEmail(rs.getString("email"));
	            emp.setPhoneNumber(rs.getString("phone_number"));
	            emp.setJobId(rs.getString("job_id"));
	            emp.setHireDate(rs.getDate("hire_date"));
	            emp.setSalary(rs.getDouble("salary"));
	            emp.setCommissionPct(rs.getDouble("commission_pct"));
	            emp.setManagerId(rs.getInt("manager_id"));
	            emp.setDepartmentId(rs.getInt("department_id"));
	         }
	      }catch(Exception e) {
	         throw new RuntimeException(e);
	      }finally {
	         if(con!=null) try {con.close();} catch(Exception e) {}
	      }
	      return emp;
	   }
}
