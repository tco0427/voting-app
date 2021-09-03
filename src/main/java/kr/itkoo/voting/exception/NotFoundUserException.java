package kr.itkoo.voting.exception;

import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import lombok.Getter;

@Getter
public class NotFoundUserException extends RuntimeException{
    private int code = StatusCode.NOT_FOUND;
    private String message= ResponseMessage.NOT_FOUND_USER;

    public NotFoundUserException() {}

    public NotFoundUserException(int code, String message){
        super(message);
        this.code = code;
    }
    public NotFoundUserException(String message){
        super(message);
    }

    public NotFoundUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundUserException(Throwable cause) {
        super(cause);
    }
}
