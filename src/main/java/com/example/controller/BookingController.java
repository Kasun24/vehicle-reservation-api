package com.example.controller;

import com.example.dao.BookingDAO;
import com.example.model.Booking;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

    // 🔹 Get bookings for a specific customer (User)
    @GET
    @Path("/{customerId}")
    public Response getCustomerBookings(@PathParam("customerId") int customerId) {
        try {
            List<Booking> bookings = bookingDAO.getCustomerBookings(customerId);
            return Response.ok(bookings).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
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

}
