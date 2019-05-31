package cn.neuedu.his.config;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler
    public void processException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        logger.error("[NotExpectError]  " + ex.toString());
    }

}
