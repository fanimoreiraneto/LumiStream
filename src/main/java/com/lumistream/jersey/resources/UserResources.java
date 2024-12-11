package com.lumistream.jersey.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.lumistream.jersey.user.User;
import com.lumistream.jersey.user.UserSupervisor;

import com.google.firebase.auth.FirebaseAuth;


@Path("user")

public class UserResources {

    private final Integer APP1 = 1;
    private static String URL = "jdbc:sqlite:/home/fanineto1/LumiStream/src/main/databases/user.db";
    //private static String URL = "jdbc:sqlite:/src/main/databases/user.db";

    @Path("/addUser")
    @POST
    public Response addUser(User user) {
        User.addUser(user.getUsername(), user.getPassword(), URL);
        return Response.ok("{\"status\":\"User added successfully\"}").build();
    }

/*  @Path("/login")
    @POST
    public void login(@PathParam("username") String username, @PathParam("pass") String userpass) {

        UserSupervisor.loginUser(username, userpass, APP1, URL);
    }
*/


    @Path("/logout")
    @POST
    public Response logout(User user) {
        UserSupervisor.logoutUser(user.getUsername(), user.getPassword(), APP1);
        return Response.ok("{\"status\":\"User logged out successfully\"}").build();
    }

    @Path("/delete")
    @POST
    public Response deleteUser(User user) {
        User.deleteUser(user.getUsername(), URL);
        return Response.ok("{\"status\":\"User logged out successfully\"}").build();
    } 


    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        try {
            // validate user credentials
           // UserSupervisor supervisor = UserSupervisor.getInstance();
            boolean isValid = User.Authenticate(user.getUsername(), user.getPassword(), URL);

            if (isValid) {
                // Generate a Firebase custom token
                String customToken = FirebaseAuth.getInstance().createCustomToken(user.getUsername());

                // return token 
                return Response.ok(new TokenResponse(customToken)).build();
            } else {
                // Invalid 
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error during login").build();
        }
    }
}

