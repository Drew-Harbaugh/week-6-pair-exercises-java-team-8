package com.techelevator.projects.model.jbdc;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCDepartmentDAOTest {

    private static SingleConnectionDataSource dataSource;
    private JDBCDepartmentDAO sut;

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

        sut = new JDBCDepartmentDAO(dataSource);
    }

    /* After each test, we rollback any changes that were made to the database so that
     * everything is clean for the next test */
    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @Test
    public void get_all_departments_returns_all_departments() {
        //Arrange
        for (int i = 0; i < 20; i++) {
            Department testDepartment = new Department();
            testDepartment.setName("Test Department " + i);
            sut.createDepartment(testDepartment);
        }

        //Act
        List<Department> departments = sut.getAllDepartments();

        //Assert
        Assert.assertEquals(20, departments.size());

        for (int i = 0; i < 20; i++) {
            Department testDepartment = departments.get(i);
            Assert.assertEquals("Test Department " + i, testDepartment.getName());
        }
    }

    @Test
    public void search_departments_returns_name() {
        //Arrange
        for (int i = 0; i < 20; i++) {
            Department testDepartment = new Department();
            testDepartment.setName("Test Department " + i);
            sut.createDepartment(testDepartment);
        }

        //Act
        List<Department> departments = sut.searchDepartmentsByName("Test");
        List<Department> onlyOneDepartment = sut.searchDepartmentsByName("Test Department 5");
        List<Department> notOnTheList = sut.searchDepartmentsByName("bad search");

        //Assert
        Assert.assertEquals(20, departments.size());
        Assert.assertEquals(1, onlyOneDepartment.size());
        Assert.assertEquals(0, notOnTheList.size());

        for (int i = 0; i < 20; i++) {
            Department testDepartment = departments.get(i);
            Assert.assertEquals("Test Department " + i, testDepartment.getName());
        }
    }

    @Test
    public void save_department_updates_department() {
        //Arrange
        Department testDepartment = new Department();
        testDepartment.setName("Test Department");
        sut.createDepartment(testDepartment);
        testDepartment.setName("Real Department");

        //Act
        sut.saveDepartment(testDepartment);

        //Assert
        Assert.assertEquals("Real Department", testDepartment.getName());
    }

    @Test
    public void create_department_puts_new_department_in_db() {
        //Arrange
        Department newDepartment = new Department();
        newDepartment.setName("New Department");

        //Act
        Department retrievedDepartment = sut.createDepartment(newDepartment);

        //Assert
        Assert.assertEquals(newDepartment.getName(), retrievedDepartment.getName());
    }

    @Test
    public void get_department_by_id_gets_correct_department() {
        //Arrange
        Department newDepartment = new Department();
        newDepartment.setName("New Department");
        sut.createDepartment(newDepartment);

        //Act
        Department retrievedDepartment = sut.getDepartmentById(newDepartment.getId());
        Department shouldBeNullDepartment = sut.getDepartmentById((long) 5);

        //Assert
        Assert.assertEquals(newDepartment.getName(), retrievedDepartment.getName());
        Assert.assertNull(shouldBeNullDepartment);
    }
}
