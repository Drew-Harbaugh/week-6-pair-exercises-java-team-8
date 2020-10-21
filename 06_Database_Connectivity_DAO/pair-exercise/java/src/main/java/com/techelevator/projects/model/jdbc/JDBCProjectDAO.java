package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.techelevator.projects.model.Employee;
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
			result.add(mapRowToProejct(rowSet));
		}
		return result;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		
	}

	private Project mapRowToProejct(SqlRowSet rowSet) {
		Project project = new Project();
		Date startDate = rowSet.getDate("from_date");
		Date endDate = rowSet.getDate("to_date");

		project.setId(rowSet.getLong("project_id"));
		project.setName(rowSet.getString("name"));
		if (startDate != null) {
			project.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		if (endDate != null) {
			project.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		return project;
	}

}
