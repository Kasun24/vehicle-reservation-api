package com.example.controller;

import com.example.dao.DriverDAO;
import com.example.model.Driver;
import com.example.utils.AdminRequired;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverController {
    private final DriverDAO driverDAO = new DriverDAO();

    // 🔹 Get all drivers
    @GET
    public Response getAllDrivers() {
        List<Driver> drivers = driverDAO.getAllDrivers();
        return Response.ok(drivers).build();
    }

    // 🔹 Get driver by ID
    @GET
    @Path("/{id}")
    public Response getDriverById(@PathParam("id") int id) {
        Driver driver = driverDAO.getDriverById(id);
        if (driver != null) {
            return Response.ok(driver).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Driver not found\"}")
                    .build();
        }
    }

    // 🔹 Add a new driver (Admin Only)
    @POST
    @AdminRequired
    public Response addDriver(Driver driver) {
        if (driverDAO.addDriver(driver)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Driver added successfully\"}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to add driver\"}")
                .build();
    }

    // 🔹 Update driver details (Admin Only)
    @PUT
    @Path("/{id}")
    @AdminRequired
    public Response updateDriver(@PathParam("id") int id, Driver driver) {
        driver.setId(id);
        if (driverDAO.updateDriver(driver)) {
            return Response.ok("{\"message\": \"Driver updated successfully\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to update driver\"}")
                .build();
    }

    // 🔹 Delete a driver (Admin Only)
    @DELETE
    @Path("/{id}")
    @AdminRequired
    public Response deleteDriver(@PathParam("id") int id) {
        if (driverDAO.deleteDriver(id)) {
            return Response.ok("{\"message\": \"Driver deleted successfully\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Failed to delete driver\"}")
                .build();
    }
}
