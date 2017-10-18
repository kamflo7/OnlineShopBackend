package pl.kflorczyk.onlineshopbackend.rest_controllers.responses;

public class Response<T> {
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILURE = "failure";

    private String status, description;
    private T data;

    public Response(String status) {
        this.status = status;
    }

    public Response(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public Response(T data) {
        this.status = STATUS_SUCCESS;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public T getData() {
        return data;
    }
}
