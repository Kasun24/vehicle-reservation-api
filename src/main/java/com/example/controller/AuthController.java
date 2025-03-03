package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.utils.JWTUtil;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class AuthController {
    private final UserDAO userDAO = new UserDAO();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User loginUser) {
        if (!userDAO.validateUser(loginUser.getUsername(), loginUser.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid username or password\"}")
                    .build();
        }

        // Generate JWT Token
        String token = JWTUtil.generateToken(loginUser.getUsername(), "USER");

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", "USER");

        return Response.ok(response).build();
    }

}
