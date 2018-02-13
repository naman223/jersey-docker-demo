package com.jersey.docker.repository.exception;

import javax.ws.rs.core.Response;

public class EmployeeNotFound extends RuntimeException {
    public Response toResponse() {
        return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
    }
}
