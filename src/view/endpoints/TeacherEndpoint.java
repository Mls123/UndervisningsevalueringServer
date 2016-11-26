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
    public Response getCourseParticipation(@PathParam("courseId") String courseId) {

        Gson gson = new Gson();
        TeacherController teacherController = new TeacherController();

        String courseIdDecrypt = Digester.decrypt(courseId);

        int courseAttendants = 0;
        int courseIdDecrypt2 = Integer.valueOf(courseIdDecrypt);
        teacherController.getCourseParticipants(courseIdDecrypt2, courseAttendants);

        if (courseAttendants != 0) {
            return successResponse(200, courseAttendants);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }
    @GET
    @Consumes("applications/json")
    @Path("/averageLectureRating/{lectureId}")
    public Response getCalculateAverageRatingOnLecture(@PathParam("lectureId") String lectureId) {

        Gson gson = new Gson();
        TeacherController teacherController = new TeacherController();

        String decrypt = Digester.decrypt(lectureId);
        int toInt = Integer.valueOf(decrypt);

        double average = 0;
        teacherController.calculateAverageRatingOnLecture(toInt, average);

        if (average != 0) {
            return successResponse(200, average);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }
    @GET
    @Consumes("applications/json")
    @Path("/reviewParticipation/{lectureId}")
    public Response getCalculateReviewParticipation(@PathParam("lectureId") String lectureId) {

        Gson gson = new Gson();
        TeacherController teacherController = new TeacherController();

        String decrypt = Digester.decrypt(lectureId);
        int toInt = Integer.valueOf(decrypt);

        double reviewParticipation = 0;
        teacherController.calculateReviewParticipation(toInt, reviewParticipation);

        if (reviewParticipation != 0) {
            return successResponse(200, reviewParticipation);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }
    @GET
    @Consumes("applications/json")
    @Path("/averageCourseRating/{courseId}")
    public Response getCalculateAverageRatingOnCourse(@PathParam("courseId") String courseId) {

        Gson gson = new Gson();
        TeacherController teacherController = new TeacherController();
        String decrypt = Digester.decrypt(courseId);

        double average = 0;
        teacherController.calculateAverageRatingOnCourse(decrypt, average);

        if (average != 0) {
            return successResponse(200, average);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }

}
