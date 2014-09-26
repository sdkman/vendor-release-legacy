package net.gvmtool.release.response;

public class SuccessResponse {
    private String id;
    private String message;

    public SuccessResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
