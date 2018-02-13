package com.jersey.docker.repository;

import com.jersey.docker.model.Employee;
import com.jersey.docker.repository.exception.EmployeeAlreadyExists;
import com.jersey.docker.repository.exception.EmployeeNotFound;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private static EmployeeRepository employeeRepository;
    private List<Employee> employeeList;

    private EmployeeRepositoryImpl() {
        employeeList = new ArrayList<Employee>();
        employeeList.add(new Employee(1, "Naman"));
        employeeList.add(new Employee(2, "Ahaan"));
        employeeList.add(new Employee(3, "Aashi"));
    }

    public static EmployeeRepository getInstance() {
        if(employeeRepository==null)
            employeeRepository = new EmployeeRepositoryImpl();

        return employeeRepository;
    }


    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    public Employee getEmployee(int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        throw new EmployeeNotFound();
    }

    public void updateEmployee(Employee employee, int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                emp.setId(employee.getId());
                emp.setFirstName(employee.getFirstName());
                return;
            }
        }
        throw new EmployeeNotFound();
    }

    public void deleteEmployee(int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                employeeList.remove(emp);
                return;
            }
        }
        throw new EmployeeNotFound();
    }

    public void addEmployee(Employee employee) {
        for (Employee emp : employeeList) {
            if (emp.getId() == employee.getId()) {
                throw new EmployeeAlreadyExists();
            }
        }
        employeeList.add(employee);
    }
}
