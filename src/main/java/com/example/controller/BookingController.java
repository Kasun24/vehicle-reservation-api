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
        if (bookingDAO.createBooking(booking)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Booking created successfully\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to create booking\"}")
                .build();
    }

    // 🔹 Get all bookings (Admin Only)
    @GET
    @AdminRequired
    public Response getAllBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings();
        return Response.ok(bookings).build();
    }

    // 🔹 Get bookings for a specific customer (User)
    @GET
    @Path("/{customerId}")
    public Response getCustomerBookings(@PathParam("customerId") int customerId) {
        List<Booking> bookings = bookingDAO.getCustomerBookings(customerId);
        return Response.ok(bookings).build();
    }

    // 🔹 Cancel booking (Admin Only)
    @DELETE
    @Path("/{id}")
    @AdminRequired
    public Response cancelBooking(@PathParam("id") int id) {
        if (bookingDAO.cancelBooking(id)) {
            return Response.ok("{\"message\": \"Booking cancelled successfully\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to cancel booking\"}")
                .build();
    }
    // 🔹 Get booking by ID
    @GET
    @Path("/id/{id}")
    public Response getBookingById(@PathParam("id") int id) {
        Booking booking = bookingDAO.getBookingById(id);
        if (booking != null) {
            return Response.ok(booking).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Booking not found\"}")
                    .build();
        }
    }

}
