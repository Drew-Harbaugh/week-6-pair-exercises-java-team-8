package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		List<Project> result = new ArrayList<>();
		String query = "SELECT project_id, name, from_date, to_date\n" +
				"FROM project\n" +
				"WHERE NOW() BETWEEN from_date AND to_date OR to_date IS NULL;";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);

		while (rowSet.next()) {
			result.add(mapRowToProject(rowSet));
		}
		return result;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		jdbcTemplate.update("DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?", projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String query = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?);";
		jdbcTemplate.update(query, projectId, employeeId);
	}

	private Project mapRowToProject(SqlRowSet rowSet) {
		Project project = new Project();
		Date startDate = rowSet.getDate("from_date");
		Date endDate = rowSet.getDate("to_date");

		project.setId(rowSet.getLong("project_id"));
		project.setName(rowSet.getString("name"));
		if (startDate != null) {
			project.setStartDate(startDate.toLocalDate());
		}
		if (endDate != null) {
			project.setEndDate(endDate.toLocalDate());
		}
		return project;
	}

}
