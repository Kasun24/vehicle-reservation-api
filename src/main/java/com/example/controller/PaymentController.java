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
                    .entity("{\"error\": \"Server error while fetching payments: " + e.getMessage() + "\"}")
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
                        .entity("{\"error\": \"No payment found for the given booking ID.\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error while fetching payment: " + e.getMessage() + "\"}")
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
                        .entity("{\"message\": \"Payment successfully recorded.\"}")
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to record payment. Please check the data.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error while adding payment: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Update payment details (Admin Only)
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AdminRequired
    public Response updatePayment(@PathParam("id") int id, Payment payment) {
        try {
            payment.setId(id);  // Ensure ID is properly set
            boolean updated = paymentDAO.updatePayment(payment);
            if (updated) {
                return Response.ok("{\"message\": \"Payment updated successfully\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Payment not found or update failed.\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Update payment status (Mark as PAID or CANCELLED) (Admin Only)
    @PUT
    @Path("/{id}/status/{status}")
    @AdminRequired
    public Response updatePaymentStatus(@PathParam("id") int id, @PathParam("status") String status) {
        try {
            if (!status.equals("PAID") && !status.equals("CANCELLED")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Invalid status value. Allowed: PAID, CANCELLED.\"}")
                        .build();
            }

            boolean updated = paymentDAO.updatePaymentStatus(id, status);
            if (updated) {
                return Response.ok("{\"message\": \"Payment status updated successfully.\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Payment not found or status update failed.\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error while updating payment status: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{paymentId}")
    @AdminRequired
    public Response deletePayment(@PathParam("paymentId") int paymentId) {
        try {
            boolean deleted = paymentDAO.deletePayment(paymentId);
            if (deleted) {
                return Response.ok("{\"message\": \"Payment deleted successfully.\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Payment not found.\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error while deleting payment: " + e.getMessage() + "\"}")
                    .build();
        }
    }

}