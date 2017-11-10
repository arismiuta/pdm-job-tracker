package app.unijobs.ro.unijobs.jobs;

/**
 * Created by Maniga on 11/9/2017.
 */

public class Job {
    private String title;
    private String description;
    private String pay;

    public Job(String title, String description, String pay) {
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

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
