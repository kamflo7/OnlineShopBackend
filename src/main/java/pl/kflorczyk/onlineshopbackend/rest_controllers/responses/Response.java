package pl.kflorczyk.onlineshopbackend.rest_controllers.responses;

public class Response {
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAILURE = "failure";

    public enum Status {
        SUCCESS,
        FAILURE
    }

    private String status, description;

    public Response(Status status) {
        if(status == Status.SUCCESS)
            this.status = STATUS_SUCCESS;
        else if(status == Status.FAILURE)
            this.status = STATUS_FAILURE;
    }

    public Response(Status status, String description) {
        this(status);
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
