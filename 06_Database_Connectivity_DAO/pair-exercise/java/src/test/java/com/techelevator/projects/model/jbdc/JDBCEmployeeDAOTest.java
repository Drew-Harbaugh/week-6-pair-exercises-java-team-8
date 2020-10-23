package com.techelevator.projects.model.jbdc;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCEmployeeDAOTest {

    private static final String[] FIRST_NAMES = new String[] {"One", "Two", "Three", "Four"};
    private static final String[] LAST_NAMES = new String[] {"1_last", "2_last", "3_last", "4_last"};
    private static final long[] DEPARTMENT_IDS = new long[] {(long) 1, (long) 2, (long) 3, (long) 3};
    private static final LocalDate[] BIRTH_DATES = new LocalDate[] {
            LocalDate.of(1981, 1, 1),
            LocalDate.of(1982, 2, 2),
            LocalDate.of(1983, 3, 3),
            LocalDate.of(1984, 4, 4)};
    private static final char[] GENDERS = new char[] {'M', 'F', 'M', 'F'};
    private static final LocalDate[] HIRE_DATES = new LocalDate[] {
            LocalDate.of(1991, 1, 1),
            LocalDate.of(1992, 2, 2),
            LocalDate.of(1993, 3, 3),
            LocalDate.of(1994, 4, 4)};

    private static SingleConnectionDataSource dataSource;
    private JDBCEmployeeDAO sut;

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

        jdbcTemplate.update("TRUNCATE employee CASCADE"); //remove existing data

        String sqlQuery = "INSERT INTO employee (first_name, last_name, department_id, birth_date, gender, hire_date)" +
                " VALUES (?, ?, ?, ?, ?, ?);";

        for (int i = 0; i < FIRST_NAMES.length; i++) {
            jdbcTemplate.update(sqlQuery, FIRST_NAMES[i], LAST_NAMES[i], DEPARTMENT_IDS[i], BIRTH_DATES[i], GENDERS[i], HIRE_DATES[i]);
        }

        sut = new JDBCEmployeeDAO(dataSource);
    }

    /* After each test, we rollback any changes that were made to the database so that
     * everything is clean for the next test */
    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @Test
    public void get_all_employees_returns_all_employees() {
        //Arrange
        List<Employee> testEmployees = createEmployees();

        //Act
        List<Employee> retrievedEmployees = sut.getAllEmployees();

        //Assert
        Assert.assertEquals(4, retrievedEmployees.size());

        for (int i = 0; i < FIRST_NAMES.length; i++) {
            assertEmployeesAreEqual(testEmployees.get(i), retrievedEmployees.get(i));
        }
    }

    @Test
    public void search_employees_by_name_returns_correct_employee() {
        //Arrange
        List<Employee> testEmployees = createEmployees();

        //Act
        List<Employee> retrieveOneEmployee = sut.searchEmployeesByName("One", "1_last");
        List<Employee> retrieveAllEmployees = sut.searchEmployeesByName("", "last");
        List<Employee> retrieveNoEmployees = sut.searchEmployeesByName("not", "here");


        //Assert
        Assert.assertEquals(1, retrieveOneEmployee.size());
        Assert.assertEquals(4, retrieveAllEmployees.size());
        Assert.assertEquals(0, retrieveNoEmployees.size());
        assertEmployeesAreEqual(testEmployees.get(0), retrieveOneEmployee.get(0));
    }

    @Test
    public void get_employees_by_department_id_returns_correct_employees() {
        //Arrange
        List<Employee> testEmployees = createEmployees();

        //Act
        List<Employee> retrieveOneEmployee = sut.getEmployeesByDepartmentId(1);
        List<Employee> retrieveTwoEmployees = sut.getEmployeesByDepartmentId(3);
        List<Employee> retrieveNoEmployees = sut.getEmployeesByDepartmentId(20);

        //Assert
        Assert.assertEquals(1, retrieveOneEmployee.size());
        Assert.assertEquals(2, retrieveTwoEmployees.size());
        Assert.assertEquals(0, retrieveNoEmployees.size());
        assertEmployeesAreEqual(testEmployees.get(0), retrieveOneEmployee.get(0));
    }

    @Test
    public void get_employees_without_projects_returns_correct_employees() {
//        //Arrange
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//
//        String sqlQuery = "INSERT INTO project_employee (project_id, employee_id)" +
//                " VALUES (?, ?);";
//
//        Long[] projectIdArray = new Long[] {null, (long) 1, (long) 2, (long) 6};
//        List<Employee> allEmployees = sut.getAllEmployees();
//
//        for (int i = 0; i < allEmployees.size(); i++) {
//            jdbcTemplate.update(sqlQuery, projectIdArray, allEmployees.get(i).getId());
//        }
//
//        //Act
//        List<Employee> retrieveEmployeesWithoutProjects = sut.getEmployeesWithoutProjects();
//
//        //Assert
    }

    @Test
    public void get_employee_by_project_id_returns_correct_employees() {

    }

    @Test
    public void change_employees_functions_correctly() {
//        //Arrange
//        List<Employee> allEmployees = sut.getAllEmployees();
//        Employee specialEmployee = allEmployees.get(0);
//
//        //Act
//        sut.changeEmployeeDepartment(specialEmployee.getId(), specialEmployee.getDepartmentId());
//
//        //Assert
//        Assert.assertEquals(3, specialEmployee.getDepartmentId());

    }

    private void assertEmployeesAreEqual(Employee expected, Employee actual) {
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getDepartmentId(), actual.getDepartmentId());
        Assert.assertEquals(expected.getBirthDay(), actual.getBirthDay());
        Assert.assertEquals(expected.getGender(), actual.getGender());
        Assert.assertEquals(expected.getHireDate(), actual.getHireDate());
    }

    private List<Employee> createEmployees() {
        List<Employee> result = new ArrayList<>();

        for (int i = 0; i < FIRST_NAMES.length; i++) {
            Employee testEmployee = new Employee();
            testEmployee.setFirstName(FIRST_NAMES[i]);
            testEmployee.setLastName(LAST_NAMES[i]);
            testEmployee.setDepartmentId(DEPARTMENT_IDS[i]);
            testEmployee.setBirthDay(BIRTH_DATES[i]);
            testEmployee.setGender(GENDERS[i]);
            testEmployee.setHireDate(HIRE_DATES[i]);

            result.add(testEmployee);
        }

        return result;
    }

}
