package org.zxcjaba.casino.api.Exceptions;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";
    ErrorAttributes errorAttributes;


    @RequestMapping(CustomErrorController.ERROR_PATH)
    public ResponseEntity<ErrorDTO> error(WebRequest request) {
        String error=new String("Unknown error");
        HttpStatus errorCode= HttpStatus.valueOf(500);
        enum HttpErrorStatus{
            BadRequest,
            NotFound,
            UnknownError;
        }
        HttpErrorStatus status=HttpErrorStatus.UnknownError;
        Map<String, Object> attributes = errorAttributes.
                getErrorAttributes(
                        request,
                        ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION,ErrorAttributeOptions.Include.MESSAGE));
        if(attributes.containsKey("exception")) {
            String exceptionName = (String) attributes.get("exception");
            String[] delim=exceptionName.split("\\.");
            error=delim[delim.length-1];
            switch (error){
                case "BadRequestException":
                    status=HttpErrorStatus.BadRequest;
                    error="BadRequest";
                    errorCode=HttpStatus.BAD_REQUEST;
                    break;
                case "NotFoundException":
                    status=HttpErrorStatus.NotFound;
                    error="NotFoundException";
                    errorCode=HttpStatus.NOT_FOUND;
                    break;
                default:
                    status=HttpErrorStatus.UnknownError;
            }



        }
        return ResponseEntity
                .status(errorCode.value())
                .body(ErrorDTO
                        .builder()
                        .error(String.valueOf(status))
                        .errorDescription((String) attributes.get("message"))
                        .build()
                );
    }
}