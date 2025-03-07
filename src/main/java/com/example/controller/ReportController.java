package com.example.controller;

import com.example.dao.ReportDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportController {
    private final ReportDAO reportDAO = new ReportDAO();

    // 🔹 Generate Booking Report
    @GET
    @Path("/bookings")
    public Response getBookingReport(
            @QueryParam("start_date") String startDate,
            @QueryParam("end_date") String endDate,
            @QueryParam("status") String status) {
        try {
            List<Map<String, Object>> report = reportDAO.getBookingReport(startDate, endDate, status);
            return Response.ok(report).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Generate Payment Report
    @GET
    @Path("/payments")
    public Response getPaymentReport(
            @QueryParam("start_date") String startDate,
            @QueryParam("end_date") String endDate,
            @QueryParam("status") String status) {
        try {
            List<Map<String, Object>> report = reportDAO.getPaymentReport(startDate, endDate, status);
            return Response.ok(report).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
