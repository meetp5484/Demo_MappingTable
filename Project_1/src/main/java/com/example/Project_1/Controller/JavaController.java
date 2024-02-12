package com.example.Project_1.Controller;

import com.example.Project_1.Entity.Department;
import com.example.Project_1.Entity.Employee;
import com.example.Project_1.Repository.DepartmentRepository;
import com.example.Project_1.Repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class JavaController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<Map<String, Object>> getAllEmployeesWithDepartments() {
        List<Employee> employees = employeeRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Employee employee : employees) {
            Map<String, Object> employeeMap = new HashMap<>();
            employeeMap.put("empId", employee.getEmpId());
            employeeMap.put("empName", employee.getEmpName());
            List<String> departmentNames = employee.getDepartments().stream().map(Department::getDeptName).collect(Collectors.toList());
            employeeMap.put("departmentNames", departmentNames);
            response.add(employeeMap);
        }
        return response;
    }

    @PostMapping
    public Map<String, Object> createEmployee(  @RequestBody Map<String, Object> requestBody) {
        Employee employee = new Employee();
        employee.setEmpName((String) requestBody.get("empName"));
        List<Long> departmentIds = (List<Long>) requestBody.get("departmentIds");
        List<Department> departments = departmentRepository.findAllById(departmentIds);
        employee.setDepartments(departments);
        employee = employeeRepository.save(employee);
        return mapToEmployeeResponse(employee);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateEmployee(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setEmpName((String) requestBody.get("empName"));
            List<Long> departmentIds = (List<Long>) requestBody.get("departmentIds");
            List<Department> departments = departmentRepository.findAllById(departmentIds);
            employee.setDepartments(departments);
            employee = employeeRepository.save(employee);
            return mapToEmployeeResponse(employee);
        } else {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employeeRepository.delete(employee);
        } else {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
    }
    private Map<String, Object> mapToEmployeeResponse(  Employee employee) {
        Map<String, Object> response = new HashMap<>();
        response.put("empId", employee.getEmpId());
        response.put("empName", employee.getEmpName());
        List<String> departmentNames = employee.getDepartments().stream().map(Department::getDeptName).collect(Collectors.toList());
        response.put("departmentNames", departmentNames);
        return response;
    }
}
