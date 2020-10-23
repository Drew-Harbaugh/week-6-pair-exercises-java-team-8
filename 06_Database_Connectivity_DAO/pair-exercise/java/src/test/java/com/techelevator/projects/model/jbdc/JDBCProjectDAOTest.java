package com.techelevator.projects.model.jbdc;

import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.SQLException;

public class JDBCProjectDAOTest {

    private static SingleConnectionDataSource dataSource;
    private JDBCProjectDAO sut;

    /* Before any tests are run, this method initializes the datasource for testing.
     *  Only happens one time, before all the tests in this class */
    @BeforeClass
    public static void setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1"); //Don't do this, this is just for the example
        /* The following line disables autocommit for connections
         * returned by this DataSource. This allows us to rollback
         * any changes after each test */
        dataSource.setAutoCommit(false);
    }

    /* After all tests have finished running, this method will close the DataSource
     *  Only happens one time, after all the tests in this class */
    @AfterClass
    public static void closeDataSource() throws SQLException {
        dataSource.destroy();
    }

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update("TRUNCATE department CASCADE"); //remove existing data

        String sqlQuery = "INSERT INTO project (name, from_date, to_date) " +
                "VALUES ('Active Project', null, date '2020-12-02'), ('Inactive Project', null, date '2010-12-02');";
        jdbcTemplate.update(sqlQuery);

        sut = new JDBCProjectDAO(dataSource);
    }

    /* After each test, we rollback any changes that were made to the database so that
     * everything is clean for the next test */
    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @Test
    public void get_all_active_projects_returns_correct_projects() {
        //Arrange


        //Act

        //Assert
    }

}
