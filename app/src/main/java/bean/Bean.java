package bean;

/**
 * Created by Administrator on 2016/10/15.
 */

public class Bean {
    private String title;
    private String id;
    private String description;
    private String cover_url;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCover_url() {
        return cover_url;
    }
    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }
    public Bean() {
        super();
    }
    public Bean(String title, String id, String description, String cover_url) {
        super();
        this.title = title;
        this.id = id;
        this.description = description;
        this.cover_url = cover_url;
    }

}
