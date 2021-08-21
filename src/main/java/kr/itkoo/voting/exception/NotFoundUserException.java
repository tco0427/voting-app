package kr.itkoo.voting.exception;

import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import lombok.Getter;

@Getter
public class NotFoundUserException extends Exception{
    private int code = StatusCode.NOT_FOUND;
    private String message= ResponseMessage.NOT_FOUND_USER;

    public NotFoundUserException() {}

    public NotFoundUserException(int code, String message){
        super(message);
        this.code = code;
    }
}
