package shared;

/**
 * Dette er et DTO af study, med de attributter som DTOen kan indeholde.
 */
public class StudyDTO {

    private  int id;
    private  String name;
    private  String shortname;

    public StudyDTO (int id, String name, String shortname){
        this.id = id;
        this.shortname = shortname;
        this.name = name;
    }

    public StudyDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @Override
    public String toString() {
        return "StudyDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortname='" + shortname + '\'' +
                '}';
    }
}
