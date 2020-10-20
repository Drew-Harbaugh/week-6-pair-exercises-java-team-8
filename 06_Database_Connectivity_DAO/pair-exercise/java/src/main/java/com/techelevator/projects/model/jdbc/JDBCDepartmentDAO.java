package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> result = new ArrayList<>();
		String query = "SELECT department_id, name FROM department;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);

		while (rowSet.next()) {
			result.add(mapRowToDepartment(rowSet));
		}
		return result;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> result = new ArrayList<>();
		String query = "SELECT department_id, name FROM department WHERE name LIKE ?;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, "%" + nameSearch + "%");

		while (rowSet.next()) {
			result.add(mapRowToDepartment(rowSet));
		}
		return result;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		return null;
	}

	@Override
	public Department getDepartmentById(Long id) {
		Department result = new Department();
		String query = "SELECT department_id, name FROM department WHERE department_id = ?;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, id);

		if (rowSet.next()) { //Check if our query returned a row
			result = mapRowToDepartment(rowSet);
		} else {
			System.out.println("No department found with that ID");
			result = null;
		}

		return result;
	}

	private Department mapRowToDepartment(SqlRowSet rowSet) {
		Department department = new Department();
		department.setId(rowSet.getLong("department_id"));
		department.setName(rowSet.getString("name"));
		return department;
	}

}
