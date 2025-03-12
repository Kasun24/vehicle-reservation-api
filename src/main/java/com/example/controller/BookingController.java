package com.example.controller;

import com.example.dao.BookingDAO;
import com.example.model.Booking;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.utils.JWTUtil;

import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {
    private final BookingDAO bookingDAO = new BookingDAO();

    // 🔹 Create a new booking (User)
    @POST
    public Response createBooking(Booking booking) {
        try {
            if (bookingDAO.createBooking(booking)) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"Booking created successfully\"}")
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to create booking\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Update booking
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBooking(@PathParam("id") int id, Booking booking) {
        booking.setId(id);
        boolean updated = bookingDAO.updateBooking(booking);

        if (updated) {
            return Response.ok("{\"message\": \"Booking updated successfully\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to update booking\"}")
                    .build();
        }
    }

    // 🔹 Get all bookings (Admin Only)
    @GET
    @AdminRequired
    public Response getAllBookings() {
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            return Response.ok(bookings).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Get bookings for the logged-in user
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBookings(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Unauthorized access\"}")
                    .build();
        }

        // Extract token
        String token = authHeader.substring("Bearer ".length());

        try {
            // Get username from token
            String username = JWTUtil.extractUsername(token);

            if (username == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Invalid token\"}")
                        .build();
            }

            // Fetch bookings for the user
            List<Booking> bookings = bookingDAO.getBookingsByUsername(username);

            return Response.ok(bookings).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid token or unauthorized access\"}")
                    .build();
        }
    }

    // 🔹 Get booking by ID
    @GET
    @Path("/id/{id}")
    public Response getBookingById(@PathParam("id") int id) {
        try {
            Booking booking = bookingDAO.getBookingById(id);
            if (booking != null) {
                return Response.ok(booking).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Booking not found\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Delete Booking
    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") int id) {
        boolean deleted = bookingDAO.deleteBooking(id);
        if (deleted) {
            return Response.ok().entity("Booking deleted successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
        }
    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUserBooking(@HeaderParam("Authorization") String authHeader, Booking booking) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Unauthorized access\"}")
                    .build();
        }

        String token = authHeader.substring("Bearer ".length());
        String username = JWTUtil.extractUsername(token);

        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid token\"}")
                    .build();
        }

        // Set user ID based on the logged-in user
        booking.setUserId(bookingDAO.getUserIdByUsername(username));

        boolean created = bookingDAO.createUserBooking(booking);
        if (created) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Booking created successfully\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to create booking\"}")
                .build();
    }

    @PUT
    @Path("/{id}/cancel")
    @Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelUserBooking(@PathParam("id") int bookingId) {
        try {
            boolean updated = bookingDAO.cancelUserBooking(bookingId);

            if (updated) {
                return Response.ok("{\"message\": \"Booking cancelled successfully\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Booking not found or already cancelled\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
