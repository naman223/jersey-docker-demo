package com.jersey.docker.resource;

import com.jersey.docker.model.Employee;
import com.jersey.docker.repository.EmployeeRepository;
import com.jersey.docker.repository.EmployeeRepositoryImpl;
import com.jersey.docker.repository.exception.EmployeeAlreadyExists;
import com.jersey.docker.repository.exception.EmployeeNotFound;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/")
public class EmployeeResource {

    private EmployeeRepository employeeRepository = EmployeeRepositoryImpl.getInstance();

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getAllEmployees() {
        try {
            List<Employee> employeeList =  employeeRepository.getAllEmployees();
            GenericEntity<List<Employee>> list = new GenericEntity<List<Employee>>(employeeList) {
            };
            return Response.ok(list).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getEmployee(@PathParam("id") int id) {
        Employee employee;
        try {
            employee = employeeRepository.getEmployee(id);
            return Response.ok(employee).build();
        } catch (EmployeeNotFound e) {
            return e.toResponse();
        } catch (Exception e) {
            return internalError();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response updateEmployee(Employee employee, @PathParam("id") int id) {
        employeeRepository.updateEmployee(employee, id);
        return Response.ok(employee).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response deleteEmployee(@PathParam("id") int id) {
        employeeRepository.deleteEmployee(id);
        return Response.status(Response.Status.OK.getStatusCode()).build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response addEmployee(Employee employee) {
        try {
            employeeRepository.addEmployee(new Employee(employee.getId(), employee.getFirstName()));
            return Response.status(Response.Status.CREATED.getStatusCode()).entity(employee).build();
        } catch (EmployeeAlreadyExists e) {
            return e.toResponse();
        } catch (Exception e) {
            return internalError();
        }
    }

    private Response internalError() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
    }
}
