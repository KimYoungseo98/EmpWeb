package kr.kosa.emp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
//Model
public class EmpDao {
	/*static {
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
	}*/

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
	 
	 public List<EmpVo> getAllEmps(){ 
		 List<EmpVo> empList=new ArrayList<>();
		 Connection con=null;
		 try {
			 con=dataSource.getConnection(); //Connection 객체 생성
			 String sql="select * from employees";
			 PreparedStatement stmt=con.prepareStatement(sql); //Statement 생성
			 ResultSet rs=stmt.executeQuery(); //쿼리 실행
			 while(rs.next()){
				 EmpVo emp=new EmpVo();
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
				 
				 empList.add(emp);
			 }
			 
		 }catch(Exception e) {
			 throw new RuntimeException(e);
		 }finally {
			 if(con!=null) try {con.close();} catch(Exception e) {}
		 }
		 return empList;
	 }
	 
	 public void intsertEmp(EmpVo emp) {
		 Connection con=null;
		 try {
			 con=dataSource.getConnection();
			 String sql="insert into employees(employee_id, first_name, last_name, "
					 +"email, phone_number, hire_date, job_id, salary, commission_pct, "
					 +"manager_id, department_id) "
					 +"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			 PreparedStatement stmt=con.prepareStatement(sql);
			 stmt.setInt(1, emp.getEmployeeId());
			 stmt.setString(2,  emp.getFirstName());
			 stmt.setString(3,  emp.getLastName());
			 stmt.setString(4, emp.getEmail());
			 stmt.setString(5,  emp.getPhoneNumber());
			 stmt.setDate(6,  emp.getHireDate());
			 stmt.setString(7, emp.getJobId());
			 stmt.setDouble(8, emp.getSalary());
			 stmt.setDouble(9, emp.getCommissionPct());
			 stmt.setInt(10, emp.getManagerId());
			 stmt.setInt(11, emp.getDepartmentId());
			 stmt.executeUpdate(); //변경된 행 반환
			 System.out.println("데이터가 입력되었습니다.");
			 
		 }catch(SQLException e){
			 throw new RuntimeException(e);
		 }finally {
			 if(con!=null) try {con.close();} catch(Exception e) { }
		 }
	 }
	 public List<Map<String,Object>> getJobIdList(){
		 	List<Map<String,Object>> jobIdList=new ArrayList<Map<String,Object>>();
		 	Connection con=null;
		 	try {
		 		con=dataSource.getConnection();
		 		String sql="select job_id, job_title from jobs";
		 		PreparedStatement stmt=con.prepareStatement(sql);
		 		ResultSet rs=stmt.executeQuery();
		 		while(rs.next()) {
		 			String jobId=rs.getString("job_id");
		 			String jobTitle=rs.getString("job_title");
		 			Map<String, Object> job=new HashMap<String, Object>();
		 			job.put("jobId", jobId);
		 			job.put("jobTitle", jobTitle);
		 			jobIdList.add(job);
		 		}
		 	}catch(SQLException e) {
		 		throw new RuntimeException(e);
		 	}finally {
		 		if(con!=null) try {con.close();} catch(Exception e) {}
		 	}
		 	return jobIdList;
		 }
	 
	 public List<Map<String,Object>> getEmpIdList(){
		 	List<Map<String,Object>> empIdList=new ArrayList<Map<String,Object>>();
		 	Connection con=null;
		 	try {
		 		con=dataSource.getConnection();
		 		String sql="select employee_id, first_name || ' ' || last_name as name from employees";
		 		PreparedStatement stmt=con.prepareStatement(sql);
		 		ResultSet rs=stmt.executeQuery();
		 		while(rs.next()) {
		 			String empId=rs.getString("employee_id");
		 			String name=rs.getString("name");
		 			Map<String, Object> emp=new HashMap<String, Object>();
		 			emp.put("empId", empId);
		 			emp.put("name", name);
		 			empIdList.add(emp);
		 		}
		 	}catch(SQLException e) {
		 		throw new RuntimeException(e);
		 	}finally {
		 		if(con!=null) try {con.close();} catch(Exception e) {}
		 	}
		 	return empIdList;
		 }
	 
	 public List<Map<String,Object>> getDeptIdList(){
		 	List<Map<String,Object>> deptIdList=new ArrayList<Map<String,Object>>();
		 	Connection con=null;
		 	try {
		 		con=dataSource.getConnection();
		 		String sql="select department_id, department_name from departments";
		 		PreparedStatement stmt=con.prepareStatement(sql);
		 		ResultSet rs=stmt.executeQuery();
		 		while(rs.next()) {
		 			String  deptId=rs.getString("department_id");
		 			String  deptName=rs.getString("department_name");
		 			Map<String, Object>  dept=new HashMap<String, Object>();
		 			 dept.put("deptId",  deptId);
		 			 dept.put("deptName",  deptName);
		 			 deptIdList.add(dept);
		 		}
		 	}catch(SQLException e) {
		 		throw new RuntimeException(e);
		 	}finally {
		 		if(con!=null) try {con.close();} catch(Exception e) {}
		 	}
		 	return deptIdList;
		 }
}
