package com.jersey.docker.demo;

import com.jersey.docker.model.Employee;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class MyResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void health() {
        String responseMsg = target.path("health").request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }

    @Test
    public void getEmployee() {
        Response response = target.path("1").request().get(Response.class);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        Employee employee = response.readEntity(Employee.class);
        assertEquals(employee.getId(), Integer.parseInt("1"));
        assertEquals(employee.getFirstName(), "Naman");
    }

    @Test
    public void updateEmployee() {
        Employee emp = new Employee(2, "Ahaan Mehta");
        Response response = target.path("2").request().put(Entity.json(emp));
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        Employee employee = response.readEntity(Employee.class);
        assertEquals(employee.getId(), Integer.parseInt("2"));
        assertEquals(employee.getFirstName(), "Ahaan Mehta");
        assertNotSame(employee.getFirstName(), "Ahaan");
    }

    @Test
    public void getEmployee_whenEmployeeDoesNotExist() {
        Response response = target.path("10").request().get(Response.class);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getAllEmployees() {
        Response response = target.request().get(Response.class);
        List<Employee> employeeList = response.readEntity(new GenericType<List<Employee>>(){});
        System.out.println("\n\tSize of List:"+employeeList.size()+"\n");
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        assert (employeeList.size()>2);
    }

    @Test
    public void addEmployee() {
        Employee emp = new Employee(4, "Tanu");
        Response response = target.request().post(Entity.json(emp));
        assertEquals(response.getStatus(), Response.Status.CREATED.getStatusCode());
        getAllEmployees();
    }

    @Test
    public void addEmployee_whenAlreadyExist() {
        Employee emp = new Employee(1, "Tanu");
        Response response = target.request().post(Entity.json(emp));
        assertEquals(response.getStatus(), Response.Status.CONFLICT.getStatusCode());
    }
}
