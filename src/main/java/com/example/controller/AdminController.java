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
        try {
            boolean updated = userDAO.updateUserRole(username, role);
            if (updated) {
                return Response.ok("{\"message\": \"User role updated successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"User not found or role update failed\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Delete User (Admin Only)
    @DELETE
    @Path("/delete")
    @AdminRequired
    public Response deleteUser(@QueryParam("username") String username) {
        try {
            boolean deleted = userDAO.deleteUser(username);
            if (deleted) {
                return Response.ok("{\"message\": \"User deleted successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"User not found or could not be deleted\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
