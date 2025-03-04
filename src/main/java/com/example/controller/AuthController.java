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
        UserDAO userDAO = new UserDAO();

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
    }


}
