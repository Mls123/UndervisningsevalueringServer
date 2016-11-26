package view.endpoints;

import com.google.gson.Gson;
import logic.StudentController;
import logic.TeacherController;
import logic.UserController;
import security.Digester;
import shared.CourseDTO;
import shared.LectureDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

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

    @GET
    @Consumes("applications/json")
    @Path("/courseParticipation/{courseId}")
    public Response getCourseParticipation(@PathParam("courseId") int courseId) {
        Gson gson = new Gson();
        TeacherController teacherController = new TeacherController();

        String courseIdDecrypt = Digester.decrypt(String.valueOf(courseId));

        int courseAttendants = 0;
        int courseIdDecrypt2 = Integer.valueOf(courseIdDecrypt);
        teacherController.getCourseParticipants(courseIdDecrypt2, courseAttendants);

        if (courseAttendants != 0) {
            return successResponse(200, courseAttendants);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }

}
