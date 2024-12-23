package app.demo3;

public abstract class Action {
    private String date;
    private String type;
    private String description;

    public Action(String date, String type, String description) {
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
}
