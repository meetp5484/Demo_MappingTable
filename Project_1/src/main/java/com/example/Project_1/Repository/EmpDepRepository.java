package com.example.Project_1.Repository;

import com.example.Project_1.Entity.EmployeeDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpDepRepository extends JpaRepository<EmployeeDepartment,Long> {
}
