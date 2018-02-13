package com.jersey.docker.repository.exception;

import javax.ws.rs.core.Response;

public class EmployeeAlreadyExists extends RuntimeException {
    public Response toResponse() {
        return Response.status(Response.Status.CONFLICT.getStatusCode()).build();
    }
}
