package io.egen.controller;

import io.egen.entity.Employee;
import io.egen.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/employees")
@Api(description = "Employee related endpoints")
public class EmployeeController {

    //@Autowired
    EmployeeService service;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Find All Employee",
                  notes = "Returns a list of all employees avaialble in the database")
    @ApiResponses(value = {
                @ApiResponse(code = 200, message = "OK"),
                @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public List<Employee> findAll() {
        return service.findAll();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Find Employee by ID",
                  notes = "Return a single employee or throws 404")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public Employee findOne(
            @ApiParam(value = "id of the employee", required = true) @PathVariable("id") String empId) {
        return service.findOne(empId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Employee create(@RequestBody Employee emp) {
        return service.create(emp);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}",
                    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Employee update(@PathVariable("id") String empId, @RequestBody Employee emp) {
        return service.update(empId, emp);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable("id") String empId) {
        service.delete(empId);
    }
}