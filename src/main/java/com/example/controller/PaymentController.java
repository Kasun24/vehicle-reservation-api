package com.example.controller;

import com.example.dao.PaymentDAO;
import com.example.model.Payment;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {
    private final PaymentDAO paymentDAO = new PaymentDAO();

    // 🔹 Get all payments (Admin Only)
    @GET
    @AdminRequired
    public Response getAllPayments() {
        try {
            List<Payment> payments = paymentDAO.getAllPayments();
            return Response.ok(payments).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Get payment by Booking ID
    @GET
    @Path("/{bookingId}")
    public Response getPaymentByBookingId(@PathParam("bookingId") int bookingId) {
        try {
            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            if (payment != null) {
                return Response.ok(payment).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Payment not found\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Add a new payment (Admin Only)
    @POST
    @AdminRequired
    public Response addPayment(Payment payment) {
        try {
            if (paymentDAO.addPayment(payment)) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"Payment recorded successfully\"}")
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to record payment\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Update payment status (Mark as PAID) (Admin Only)
    @PUT
    @Path("/{bookingId}/status/{status}")
    @AdminRequired
    public Response updatePaymentStatus(@PathParam("bookingId") int bookingId, @PathParam("status") String status) {
        try {
            if (paymentDAO.updatePaymentStatus(bookingId, status)) {
                return Response.ok("{\"message\": \"Payment status updated successfully\"}").build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to update payment status\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
