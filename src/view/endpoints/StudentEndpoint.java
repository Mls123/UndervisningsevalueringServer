package view.endpoints;

import com.google.gson.Gson;
import logic.StudentController;
import security.Digester;
import shared.ReviewDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Kasper on 19/10/2016.
 */

@Path("/api/student")
public class StudentEndpoint extends UserEndpoint {

    /**
     * Dette er en post metode (en tilføj metode) der tilføjer et review.
     * @param json
     * @return
     */
    @POST
    @Consumes("application/json")
    @Path("/review")
    public Response addReview(String json) {

        Gson gson = new Gson();
        ReviewDTO review = new Gson().fromJson(Digester.decrypt(json), ReviewDTO.class);

        StudentController studentCtrl = new StudentController();
        boolean isAdded = studentCtrl.addReview(review);

        if (isAdded) {
            String toJson = gson.toJson(gson.toJson(isAdded));
            return successResponse(200, toJson);

        } else {
            return errorResponse(404, "Failed. Couldn't get reviews.");
        }
    }

    /**
     * Dette er en delete metode der soft deleter et review udfra et reviewId
     * @param reviewId
     * @return
     */
    @DELETE
    @Consumes("application/json")
    @Path("/review/{reviewId}")
    public Response deleteReview(@PathParam("reviewId") String reviewId) {
        Gson gson = new Gson();

        StudentController studentCtrl = new StudentController();

        String reviewIdDecrypt = Digester.decrypt(reviewId);
        int review = Integer.valueOf(reviewIdDecrypt);

        boolean isDeleted = studentCtrl.softDeleteReview(review);

        if (isDeleted) {
            String toJson = gson.toJson(Digester.encrypt(gson.toJson(isDeleted)));

            return successResponse(200, toJson);
        } else {
            return errorResponse(404, "Failed. Couldn't delete the chosen review.");
        }
    }
}
