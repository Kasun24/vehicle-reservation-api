package com.example.controller;

import com.example.dao.VehicleDAO;
import com.example.model.Vehicle;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    // 🔹 Get All Vehicles (PUBLIC)
    @GET
    public Response getAllVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            return Response.ok(vehicles).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Get Vehicle by ID (PUBLIC)
    @GET
    @Path("/{id}")
    public Response getVehicleById(@PathParam("id") int id) {
        try {
            Vehicle vehicle = vehicleDAO.getVehicleById(id);
            if (vehicle != null) {
                return Response.ok(vehicle).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Vehicle not found\"}")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Add Vehicle (ADMIN ONLY)
    @POST
    @AdminRequired
    public Response addVehicle(Vehicle vehicle) {
        try {
            if (vehicleDAO.addVehicle(vehicle)) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"Vehicle added successfully\"}")
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to add vehicle\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Update Vehicle (ADMIN ONLY)
    @PUT
    @Path("/{id}")
    @AdminRequired
    public Response updateVehicle(@PathParam("id") int id, Vehicle vehicle) {
        try {
            vehicle.setId(id);
            if (vehicleDAO.updateVehicle(vehicle)) {
                return Response.ok("{\"message\": \"Vehicle updated successfully\"}").build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to update vehicle\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 🔹 Delete Vehicle (ADMIN ONLY)
    @DELETE
    @Path("/{id}")
    @AdminRequired
    public Response deleteVehicle(@PathParam("id") int id) {
        try {
            if (vehicleDAO.deleteVehicle(id)) {
                return Response.ok("{\"message\": \"Vehicle deleted successfully\"}").build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to delete vehicle\"}")
                    .build();
        } catch (Exception e) {
            // Check if the error message contains a foreign key constraint failure
            if (e.getMessage().contains("foreign key constraint fails")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Cannot delete vehicle: This vehicle is assigned to a booking. Remove the booking first.\"}")
                        .build();
            }

            // Handle other unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
