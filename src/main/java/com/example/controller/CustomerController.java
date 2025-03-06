package com.example.controller;

import com.example.dao.CustomerDAO;
import com.example.model.Customer;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {
    private final CustomerDAO customerDAO = new CustomerDAO();

    // 🔹 Get all customers (Admin Only)
    @GET
    @AdminRequired
    public Response getAllCustomers() {
        List<Customer> customers = customerDAO.getAllCustomers();
        return Response.ok(customers).build();
    }

    // 🔹 Get customer by ID
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        Customer customer = customerDAO.getCustomerById(id);
        if (customer != null) {
            return Response.ok(customer).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer not found\"}")
                    .build();
        }
    }

    // 🔹 Add new customer
    @POST
    public Response addCustomer(Customer customer) {
        if (customerDAO.addCustomer(customer)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Customer added successfully\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to add customer\"}")
                .build();
    }

    // 🔹 Update customer details
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer customer) {
        customer.setId(id);
        if (customerDAO.updateCustomer(customer)) {
            return Response.ok("{\"message\": \"Customer updated successfully\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to update customer\"}")
                .build();
    }

    // 🔹 Delete customer (Admin Only)
    @DELETE
    @Path("/{id}")
    @AdminRequired
    public Response deleteCustomer(@PathParam("id") int id) {
        if (customerDAO.deleteCustomer(id)) {
            return Response.ok("{\"message\": \"Customer deleted successfully\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to delete customer\"}")
                .build();
    }
}
