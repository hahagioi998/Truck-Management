package io.egen.service;

import io.egen.entity.Employee;
import io.egen.exception.BadRequestException;
import io.egen.exception.ResourceNotFoundException;
import io.egen.repository.EmployeeRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class EmployeeServiceImplTest {

    @TestConfiguration
    static class EmployeeServiceImplTestConfiguration {

        @Bean
        public EmployeeService getService() {
            return new EmployeeServiceImpl();
        }
    }

    @Autowired
    private EmployeeService service;

    @MockBean
    private EmployeeRepository repository;

    private List<Employee> employees;

    @Before
    public void setup() {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Smith");
        emp.setEmail("jsmith@egen.io");

        employees = Collections.singletonList(emp);

        Mockito.when(repository.findAll())
               .thenReturn(employees);

        Mockito.when(repository.findById(emp.getId()))
               .thenReturn(Optional.of(emp));

        Mockito.when(repository.findByEmail("jsmith@egen.io"))
               .thenReturn(Optional.of(emp));
    }

    @After
    public void cleanup() {
    }

    @Test
    public void findAll() throws Exception {
        List<Employee> result = service.findAll();
        Assert.assertEquals("employee list should match", employees, result);
    }

    @Test
    public void findOne() throws Exception {
        Employee result = service.findOne(employees.get(0)
                                                   .getId());
        Assert.assertEquals("employee should match", employees.get(0), result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOneNotFound() throws Exception {
        Employee result = service.findOne("asdfasdf");
    }

    @Test
    public void create() throws Exception {
        Employee emp = new Employee();
        emp.setId(null);
        emp.setFirstName("Praveen");
        emp.setLastName("Salitra");
        emp.setEmail("psalitra@egen.io");

        Mockito.when(repository.save(emp))
               .then(invocationOnMock -> {
                   emp.setId("new-id");
                   return emp;
               });

        Employee newEmp = service.create(emp);

        //verify that the id is not null
        Assert.assertNotNull("new emp id should exist", newEmp.getId());

        //verify that the id is same as the mock id
        Assert.assertEquals("emp id should be same", "new-id", newEmp.getId());

        //verify that the repository.save() was called 1 times
        Mockito.verify(repository, Mockito.times(1))
               .save(emp);
    }

    @Test(expected = BadRequestException.class)
    public void createExistingEmail() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("Praveen");
        emp.setLastName("Salitra");
        emp.setEmail("jsmith@egen.io");
        Employee newEmp = service.create(emp);
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }
}