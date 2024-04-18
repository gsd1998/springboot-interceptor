package net.javaguides.springboot.controller;


import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    //Retrieving all the employees
    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    //Creating a single employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @GetMapping("/list-employees")
    public List<Employee> listEmployee(){
        System.out.println("controller called");
        return List.of(new Employee(1,"fname1","lname1","name1@gmail.com")
                      ,new Employee(2,"fname2","lname2","name2@gmail.com"),
                       new Employee(3,"fname3","lname3","name3@gmail.com"));
    }

    //Get employee by Id
    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id doesn't exist" + id));
        return ResponseEntity.ok(employee);
    }

    //Update employee details by Id
    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long id,@RequestBody Employee employeeDetails){
        Employee updateEmployee = employeeRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Id doesn't exist" + id));

        updateEmployee.setFirstName(employeeDetails.getFirstName());
        updateEmployee.setLastName(employeeDetails.getLastName());
        updateEmployee.setEmailId(employeeDetails.getEmailId());

        employeeRepository.save(updateEmployee);

        return ResponseEntity.ok(updateEmployee);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id doesn't exist" + id));

        employeeRepository.delete(employee);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
