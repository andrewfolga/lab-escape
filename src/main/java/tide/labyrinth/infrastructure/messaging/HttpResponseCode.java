package tide.labyrinth.infrastructure.messaging;

public enum HttpResponseCode {
    _200_OK(200), _404_NOT_FOUND(404), _500_INTERNAL_SERVER_ERROR(500);

    Integer httpCode;

    HttpResponseCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public Integer getHttpCode() {
        return httpCode;
    }
}
