package logic;

import security.Digester;
import shared.*;

import java.sql.Timestamp;
import java.util.ArrayList;

import service.DBWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    /**
     * Login metode der hasher passwordet en gang, og kalder på databasen og validere om brugeren findes.
     * @param cbs_email
     * @param password
     * @return
     */
    public UserDTO login(String cbs_email, String password) {

        UserDTO user = new UserDTO();
        Digester digester = new Digester();
        String securePW = digester.hashWithSalt(password);

        try {
            Map<String, String> params = new HashMap();
            params.put("cbs_mail", String.valueOf(cbs_email));
            params.put("password", String.valueOf(securePW));

            String[] attributes = new String[]{"id","type","cbs_mail"};
            ResultSet rs = DBWrapper.getRecords("user", attributes, params, null, 0);

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setType(rs.getString("type"));
                user.setCbsMail(rs.getString("cbs_mail"));
                System.out.println("User found ");
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("User not found");
        return null;
    }

    /**
     * Denne metode henter reviews ud fra et userId, det vil sige den user, brugeren er logget ind som.
     * @param userId
     * @return
     */
    public ArrayList<ReviewDTO> getReviewsWithUserId(int userId) {

        ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("user_id", String.valueOf(userId));
            params.put("is_deleted", "0");
            String[] attributes = new String[]{"id", "user_id", "rating", "comment"};

            ResultSet rs = DBWrapper.getRecords("review", attributes, params, null, 0);

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));

                reviews.add(review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getReviews");
        }
        return reviews;
    }

    /**
     * Denne metode henter reviews ud fra et lectureId
     * @param lectureId
     * @return
     */
    public ArrayList<ReviewDTO> getReviewsWithLectureId(int lectureId) {

        ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();

        try {
            Map<String, String> params = new HashMap();
            params.put("lecture_id", String.valueOf(lectureId));
            params.put("is_deleted", "0");
            String[] attributes = new String[]{"id", "lecture_id", "rating", "comment"};

            ResultSet rs = DBWrapper.getRecords("review", attributes, params, null, 0);

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setId(rs.getInt("id"));
                review.setLectureId(rs.getInt("lecture_id"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));

                reviews.add(review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getReviews");
        }
        return reviews;
    }

    /**
     * denne metode henter lectures, udfra coden på et kursus. for mig er det course_Id der kigges på.
     * fordi jeg har haft problemer med CBSparseren og Lectures i databasen.
     * @param code
     * @return
     */
    public ArrayList<LectureDTO> getLectures(String code) {

        ArrayList<LectureDTO> lectures = new ArrayList<LectureDTO>();

        try {
            Map<String, String> params = new HashMap();

            params.put("course_id", code);

            String[] attributes = new String[]{"type", "description", "id"};
            ResultSet rs = DBWrapper.getRecords("lecture", attributes, params, null, 0);

            while (rs.next()) {
                LectureDTO lecture = new LectureDTO();
                lecture.setLectureId(rs.getInt("id"));
                lecture.setType(rs.getString("type"));
                lecture.setDescription(rs.getString("description"));

                lectures.add(lecture);
            }


        }
        catch (SQLException e){
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getLecture");

        }
        return lectures;
    }

    /**
     *  Metode der softdeleter et review fra databasen - skal ind i AdminControlleren, da dette er moden for at slette et review uafhængigt af brugertype.
     */
    public boolean softDeleteReview(int userId, int reviewId) {
        boolean isSoftDeleted = true;

        try {
            Map<String, String> isDeleted = new HashMap();

            isDeleted.put("is_deleted", "1");

            Map<String, String> whereParams = new HashMap();
            whereParams.put("user_id", String.valueOf(userId));
            whereParams.put("id", String.valueOf(reviewId));

            DBWrapper.updateRecords("review", isDeleted, whereParams);
            return isSoftDeleted;

        } catch (SQLException e) {
            e.printStackTrace();
            isSoftDeleted = false;
        }
        return isSoftDeleted;
    }

    /**
     *  Metode der softdeleter et review fra databasen - skal ind i AdminControlleren, da dette er moden for at slette et review uafhængigt af brugertype.
     */
    public boolean softDeleteReviewMetode2(int userId, int reviewId) {
        boolean isSoftDeleted = true;

        try {
            Map<String, String> isDeleted = new HashMap();

            isDeleted.put("is_deleted", "1");

            Map<String, String> whereParams = new HashMap();

            if(userId != 0) {
                whereParams.put("user_id", String.valueOf(userId));
            }

            whereParams.put("id", String.valueOf(reviewId));

            DBWrapper.updateRecords("review", isDeleted, whereParams);
            return isSoftDeleted;

        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Softdelete kunne ikke slette review, SoftDeleteReview.");
            isSoftDeleted = false;
        }
        return isSoftDeleted;
    }

    /**
     * henter kurser ud fra et userId.
     * @param userId
     * @return
     */
    public ArrayList<CourseDTO> getCourses(int userId) {

        ArrayList<CourseDTO> courses = new ArrayList<CourseDTO>();

        try {
            Map<String, String> params = new HashMap();
            Map<String, String> joins = new HashMap();

            params.put("course_attendant.user_id", String.valueOf(userId));
            joins.put("course_attendant", "course_id");

            String[] attributes = new String[]{"name", "code", "course_id"};
            ResultSet rs = DBWrapper.getRecords("course", attributes, params, joins, 0);

            while (rs.next()) {
                CourseDTO course = new CourseDTO();
                course.setId(rs.getString("course_id"));
                course.setDisplaytext(rs.getString("name"));
                course.setCode(rs.getString("code"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getCourses");
        }
        return courses;
    }

    /**
     * henter studies udfra et shortname
     * @param shortname
     * @return
     */
    public ArrayList<StudyDTO> getStudies(String shortname) {

        ArrayList<StudyDTO> studies = new ArrayList<StudyDTO>();

        try {
            Map<String, String> params = new HashMap();

            params.put("shortname", String.valueOf(shortname));

            String[] attributes = new String[]{"name", "shortname", "id"};
            ResultSet rs = DBWrapper.getRecords("study", attributes, params, null, 0);

            while (rs.next()) {
                StudyDTO study = new StudyDTO();

                study.setName(rs.getString("name"));
                study.setShortname(rs.getString("shortname"));
                study.setId(rs.getInt("id"));
                studies.add(study);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getStudies");
        }
        return studies;
    }

    /**
     * henter lectures ud fra et courseId
     * @param courseId
     * @return
     */
    public ArrayList<LectureDTO> getLecturesFromCourseId(int courseId) {

        ArrayList<LectureDTO> lectures = new ArrayList<LectureDTO>();

        try {
            Map<String, String> params = new HashMap();

            params.put("course_id", String.valueOf(courseId));
            String[] attributes = new String[]{"description", "type", "id"};

            ResultSet rs = DBWrapper.getRecords("lecture", attributes, params, null, 0);

            while (rs.next()) {
                LectureDTO lecture = new LectureDTO();
                lecture.setLectureId(rs.getInt("id"));
                lecture.setType(rs.getString("type"));
                lecture.setDescription(rs.getString("description"));

                lectures.add(lecture);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            Logging.log(e,2,"Kunne ikke hente getLecture");


        }
        return lectures;
    }
}