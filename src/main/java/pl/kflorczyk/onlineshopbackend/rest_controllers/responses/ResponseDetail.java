package pl.kflorczyk.onlineshopbackend.rest_controllers.responses;

public class ResponseDetail<T> extends Response {
    private T data = null;

    public ResponseDetail(T data) {
        super(Status.SUCCESS);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public ResponseDetail(Status status, String description) {
        super(status, description);
    }
}
