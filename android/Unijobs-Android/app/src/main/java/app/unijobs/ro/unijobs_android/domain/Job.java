package app.unijobs.ro.unijobs_android.domain;

/**
 * Created by Maniga on 1/13/2018.
 */

public class Job {
    private String title;
    private String description;
    private Integer pay;

    public Job() {
    }

    public Job(String title, String description, Integer pay) {
        this.title = title;
        this.description = description;
        this.pay = pay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }
}
