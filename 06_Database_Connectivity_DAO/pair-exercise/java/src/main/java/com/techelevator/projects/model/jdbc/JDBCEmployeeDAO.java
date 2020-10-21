package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> result = new ArrayList<>();
		String query = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date FROM employee;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);

		while (rowSet.next()) {
			result.add(mapRowToEmployee(rowSet));
		}
		return result;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> result = new ArrayList<>();
		String query = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date" +
				" FROM employee" +
				" WHERE first_name ILIKE ? OR last_name ILIKE ?;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, "%" + firstNameSearch + "%", "%" + lastNameSearch + "%");

		while (rowSet.next()) {
			result.add(mapRowToEmployee(rowSet));
		}
		return result;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		List<Employee> result = new ArrayList<>();
		String query = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date" +
				" FROM employee" +
				" WHERE department_id = ?;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, id);

		while (rowSet.next()) {
			result.add(mapRowToEmployee(rowSet));
		}
		return result;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List<Employee> result = new ArrayList<>();
		String query = "SELECT *\n" + //Ask David about this tomorrow, getting an error when we type out all of the fields
				"FROM employee\n" +
				"LEFT JOIN project_employee ON employee.employee_id = project_employee.employee_id\n" +
				"WHERE project_id IS NULL;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);

		while (rowSet.next()) {
			result.add(mapRowToEmployee(rowSet));
		}
		return result;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List<Employee> result = new ArrayList<>();
		String query = "SELECT *\n" + //Ask David about this tomorrow, getting an error when we type out all of the fields
				"FROM employee\n" +
				"LEFT JOIN project_employee ON employee.employee_id = project_employee.employee_id\n" +
				"WHERE project_id = ?;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, projectId);

		while (rowSet.next()) {
			result.add(mapRowToEmployee(rowSet));
		}
		return result;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		
	}

	private Employee mapRowToEmployee(SqlRowSet rowSet) {
		Employee employee = new Employee();

		employee.setFirstName(rowSet.getString("first_name"));
		employee.setLastName(rowSet.getString("last_name"));
		employee.setDepartmentId(rowSet.getLong("department_id"));
		employee.setId(rowSet.getLong("employee_id"));
		employee.setBirthDay(rowSet.getDate("birth_date").toLocalDate());
		employee.setGender(rowSet.getString("gender").charAt(0));
		employee.setHireDate(rowSet.getDate("hire_date").toLocalDate());

		return employee;
	}

}
