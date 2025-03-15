package Support;

import java.util.Dictionary;

public class Response<T> {
    private String message;
    private int status;
    private T data;

    public Response(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public Response(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(message, 200, data);
    }

    public static <T> Response<T> make(String message, int status) {
        return new Response<>(message, status);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(message, 400);
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return status == 200;
    }

    public T getData() {
        return data;
    }
}