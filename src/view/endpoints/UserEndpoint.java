package view.endpoints;

import com.google.gson.Gson;
import logic.UserController;
import security.Digester;
import shared.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/api")
public class UserEndpoint {

    /**
     * En metode til at hente lektioner for et enkelt kursus i form af en JSON String.
     *
     * @param code Fagkoden på det kursus man ønsker at hente.
     * @return En JSON String
     */
    @GET
    @Consumes("applications/json")
    @Path("/lecture/{code}")
    public Response getLectures(@PathParam("code") String code) {
        Gson gson = new Gson();

        String decrypt = Digester.decrypt(code);

        UserController userCtrl = new UserController();
        ArrayList<LectureDTO> lectures = userCtrl.getLectures(decrypt);

        if (!lectures.isEmpty()) {
            return successResponse(200, lectures);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }

    /**
     * En metode til at hente de kurser en bruger er tilmeldt.
     *
     * @param userId Id'et på den bruger man ønsker at hente kurser for.
     * @return De givne kurser i form af en JSON String.
     */
    @GET
    @Consumes("applications/json")
    @Path("/course/{userId}")
    public Response getCourses(@PathParam("userId") String userId) {

        Gson gson = new Gson();
        UserController userCtrl = new UserController();

        String userIdDecrypt = Digester.decrypt(userId);
        int userIdDecrypt2 = Integer.valueOf(userIdDecrypt);

        ArrayList<CourseDTO> courses = userCtrl.getCourses(userIdDecrypt2);

        if (!courses.isEmpty()) {
            return successResponse(200, courses);
        } else {
            return errorResponse(404, "Failed. Couldn't get courses.");
        }
    }

    @GET
    @Consumes("applications/json")
    @Path("/reviews/{userId}")
    public Response getReviewsWithUserId(@PathParam("userId") String userId) {
        Gson gson = new Gson();

        String userIdDecrypt = Digester.decrypt(userId);
        int userIdDecrypt2 = Integer.valueOf(userIdDecrypt);

        UserController userCtrl = new UserController();
        ArrayList<ReviewDTO> reviews = userCtrl.getReviewsWithUserId(userIdDecrypt2);

            if (!reviews.isEmpty()) {
                return successResponse(200, reviews);
            } else {
                return errorResponse(404, "Failed. Couldn't get reviews.");
            }
    }

    @GET
    @Consumes("applications/json")
    @Path("/review/{lectureId}")
    public Response getReviewsWithLectureId(@PathParam("lectureId") String lectureId) {
        Gson gson = new Gson();

        String decrypt = Digester.decrypt(lectureId);
        int toInt = Integer.valueOf(decrypt);

        UserController userCtrl = new UserController();
        ArrayList<ReviewDTO> reviews = userCtrl.getReviewsWithLectureId(toInt);

        if (!reviews.isEmpty()) {
            return successResponse(200, reviews);
        } else {
            return errorResponse(404, "Failed. Couldn't get reviews.");
        }
    }

    @GET
    @Path("/study/{shortname}")
    public Response getStudy(@PathParam("shortname") String shortname) {

        Gson gson = new Gson();

        String shortNameDecrypt = Digester.decrypt(shortname);

        UserController userCtrl = new UserController();
        ArrayList<StudyDTO> studies = userCtrl.getStudies(shortNameDecrypt);

        if (!studies.isEmpty()) {
            return successResponse(200, studies);
        } else {
            return errorResponse(404, "Failed. Couldn't get studies.");
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/login")
    public Response login(String data) {

        Gson gson = new Gson();
        UserDTO user = new Gson().fromJson(Digester.decrypt(data), UserDTO.class);
        UserController userCtrl = new UserController();

        if (user != null) {
            UserDTO userdto = userCtrl.login(user.getCbsMail(), user.getPassword());
            return successResponse(200, userdto);
        } else {
            return errorResponse(401, "Couldn't login. Try again!");
        }
    }

    protected Response errorResponse(int status, String message) {

        return Response.status(status).entity(new Gson().toJson(Digester.encrypt("{\"message\": \"" + message + "\"}"))).build();
        //return Response.status(status).entity(new Gson().toJson("{\"message\": \"" + message + "\"}")).build());
    }

    protected Response successResponse(int status, Object data) {
        Gson gson = new Gson();

        //Denne er til og aktivere kryptering
        return Response.status(status).entity((Digester.encrypt(gson.toJson(data)))).build();

        //Denne er til og få Json udskrevet i browseren
        //return Response.status(status).entity(gson.toJson(data)).build();
    }
}
