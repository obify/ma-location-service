package com.mycompany.location.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestTemplateExceptionHandler {

    @ExceptionHandler({HttpServerErrorException.class, HttpClientErrorException.class, UnknownHttpStatusCodeException.class})
    //public ResponseEntity<String> restTemplateExceptionHandler(HttpServerErrorException httpServerErrorException, HttpClientErrorException httpClientErrorException/*, UnknownHttpStatusCodeException unknownHttpStatusCodeException, WebRequest req*/)
    public ResponseEntity<String> restTemplateExceptionHandler(RestClientResponseException restClientResponseException, HttpServletRequest req)
    {
        /*
        //System.out.println(req.getDescription(true));
        System.out.println(req.getRequestURL());

        //System.out.println(restClientResponseException.());

        System.out.println(restClientResponseException.getMessage());
        System.out.println(restClientResponseException.toString());
        //System.out.println(restClientResponseException.getStatusCode());
        System.out.println(restClientResponseException.getCause());
        System.out.println(restClientResponseException.getLocalizedMessage());
        System.out.println(restClientResponseException.getMostSpecificCause());
        System.out.println(restClientResponseException.getRawStatusCode());
        System.out.println(restClientResponseException.getResponseBodyAsString());
        */
        /*if(Integer.toString(restClientResponseException.getRawStatusCode()).startsWith("4"))
        {
            System.out.println("4xx HttpStatus Code "+Integer.toString(restClientResponseException.getRawStatusCode()));
        }
        if(Integer.toString(restClientResponseException.getRawStatusCode()).startsWith("5"))
        {
            System.out.println("5xx HttpStatus Code "+Integer.toString(restClientResponseException.getRawStatusCode()));
        }*/
        //return new ResponseEntity<>("The HttpStatus is 4xx or 5xx", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(restClientResponseException.getResponseBodyAsString(), HttpStatus.valueOf(restClientResponseException.getRawStatusCode()));
    }
}
