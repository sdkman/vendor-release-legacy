package net.gvmtool.release.response;

public class SuccessResponse {
    private String id;

    public SuccessResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
