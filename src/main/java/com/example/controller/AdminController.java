package com.example.controller;

import com.example.dao.UserDAO;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminController {
    private final UserDAO userDAO = new UserDAO();

    // 🔹 Update User Role (Admin Only)
    @PUT
    @Path("/update-role")
    @AdminRequired
    public Response updateUserRole(@QueryParam("username") String username, @QueryParam("role") String role) {
        if (userDAO.updateUserRole(username, role)) {
            return Response.ok("{\"message\": \"User role updated successfully\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to update user role\"}")
                .build();
    }

    // 🔹 Delete User (Admin Only)
    @DELETE
    @Path("/delete")
    @AdminRequired
    public Response deleteUser(@QueryParam("username") String username) {
        if (userDAO.deleteUser(username)) {
            return Response.ok("{\"message\": \"User deleted successfully\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to delete user\"}")
                .build();
    }
}
