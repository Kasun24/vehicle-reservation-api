package com.example.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/help")
@Produces(MediaType.APPLICATION_JSON)
public class HelpController {

    @GET
    public Response getHelp() {
        Map<String, String> helpGuide = new HashMap<>();

        helpGuide.put("Authentication", "Use POST /api/auth/login with username and password to get a token.");
        helpGuide.put("User Management", "Admins can create users via POST /api/users and view users with GET /api/users.");
        helpGuide.put("Customer Management", "Admins can add customers via POST /api/customers and view customers with GET /api/customers.");
        helpGuide.put("Vehicle Management", "Use GET /api/vehicles to view available vehicles. Admins can add vehicles via POST /api/vehicles.");
        helpGuide.put("Booking System", "Customers can create a booking via POST /api/bookings and view details with GET /api/bookings/{id}.");
        helpGuide.put("Payment System", "Admins can manage payments via POST /api/payments and update status with PUT /api/payments/{bookingId}/status/{status}.");
        helpGuide.put("Driver Management", "Admins can add/update drivers via POST /api/drivers and view them with GET /api/drivers.");
        helpGuide.put("Logout", "Logout is handled client-side by removing the token from storage.");

        return Response.ok(helpGuide).build();
    }
}
