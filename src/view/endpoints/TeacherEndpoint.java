package view.endpoints;

import com.google.gson.Gson;
import logic.StudentController;
import logic.TeacherController;
import security.Digester;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/teacher")
public class TeacherEndpoint extends UserEndpoint {

    @DELETE
    @Consumes("application/json")
    @Path("/review/{reviewId}")
    public Response deleteReview(@PathParam("reviewId") int reviewId) {
        Gson gson = new Gson();

        TeacherController teacherController = new TeacherController();

        boolean isDeleted = teacherController.softDeleteReview(reviewId);

        if (isDeleted) {
            String toJson = gson.toJson(Digester.encrypt(gson.toJson(isDeleted)));

            return successResponse(200, toJson);
        } else {
            return errorResponse(404, "Failed. Couldn't delete the chosen review.");
        }
    }

}
