package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.Map;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private final UserDAO userDAO = new UserDAO();

    // 🔹 Get User Profile (Authenticated User)
    @GET
    @Path("/profile")
    public Response getProfile(@Context SecurityContext securityContext) {
        try {
            String loggedInUsername = securityContext.getUserPrincipal().getName();
            User user = userDAO.getUserByUsername(loggedInUsername);
            if (user != null) {
                return Response.ok(user).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"User not found\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Update User Profile (Authenticated User)
    @PUT
    @Path("/profile")
    public Response updateProfile(User user, @Context SecurityContext securityContext) {
        try {
            String loggedInUsername = securityContext.getUserPrincipal().getName();
            boolean updated = userDAO.updateUserProfile(loggedInUsername, user);

            if (updated) {
                return Response.ok("{\"message\": \"Profile updated successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Failed to update profile\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Create New User (Admin Only)
    @POST
    @AdminRequired
    public Response createUser(User user) {
        try {
            if (userDAO.getUserByUsername(user.getUsername()) != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"Username already exists\"}")
                        .build();
            }

            boolean success = userDAO.registerUser(user);
            if (success) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"User created successfully\"}")
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Failed to create user\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Update User Role (Admin Only)
    @PUT
    @Path("/update-role")
    @Consumes(MediaType.APPLICATION_JSON) // ✅ Accepts JSON data
    @Produces(MediaType.APPLICATION_JSON)
    @AdminRequired
    public Response updateUserRole(Map<String, String> requestData) {
        String username = requestData.get("username");
        String newRole = requestData.get("role");

        if (username == null || newRole == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Username and role are required.\"}")
                    .build();
        }

        boolean updated = userDAO.updateUserRole(username, newRole);
        if (updated) {
            return Response.ok("{\"message\": \"User role updated successfully.\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"User not found or role update failed.\"}")
                    .build();
        }
    }

    // 🔹 Update User Details (Admin Only)
    @PUT
    @Path("/{username}")
    @AdminRequired
    public Response updateUserDetails(@PathParam("username") String username, User user) {
        try {
            boolean updated = userDAO.updateUserProfile(username, user);

            if (updated) {
                return Response.ok("{\"message\": \"User details updated successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Failed to update user details\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Get All Users (Admin Only)
    @GET
    @AdminRequired
    public Response getAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            return Response.ok(users).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Delete User (Admin Only)
    @DELETE
    @Path("/{username}")
    @AdminRequired
    public Response deleteUser(@PathParam("username") String username) {
        try {
            boolean deleted = userDAO.deleteUser(username);
            if (deleted) {
                return Response.ok("{\"message\": \"User deleted successfully\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"User not found\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
