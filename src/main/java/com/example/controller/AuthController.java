package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.utils.JWTUtil;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {
    private final UserDAO userDAO = new UserDAO();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        try {
            // Validate user credentials
            if (!userDAO.validateUser(user.getUsername(), user.getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Invalid username or password\"}")
                        .build();
            }

            // Get role from the database
            String role = userDAO.getUserRole(user.getUsername());

            // Generate JWT token
            String token = JWTUtil.generateToken(user.getUsername(), role);

            return Response.ok("{\"token\": \"" + token + "\", \"role\": \"" + role + "\"}").build();

        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Authentication error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        try {
            // Check if username already exists
            if (userDAO.getUserByUsername(user.getUsername()) != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"Username already taken\"}")
                        .build();
            }

            // Set default role to "USER" if none provided
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }

            if (userDAO.registerUser(user)) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"User registered successfully\"}")
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to register user\"}")
                    .build();

        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Registration error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
