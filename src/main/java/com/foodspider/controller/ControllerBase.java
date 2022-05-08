package com.foodspider.controller;

import com.foodspider.model.response_model.EmptyResponse;
import com.foodspider.model.response_model.ResponseBase;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;

public abstract class ControllerBase {

    public ResponseEntity<ResponseBase> createOKResponse(ResponseBase body) {
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public ResponseEntity<ResponseBase> createErrorResponse(HttpStatus status, String error) {
        return ResponseEntity.status(status).body(new EmptyResponse(){{
            isError = true;
            errorMessage = error;
        }});
    }

    public ResponseEntity<ResponseBase> createEmptyResponse() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new EmptyResponse());
    }

    public ResponseEntity<byte[]> createBinaryResponse(byte[] binary, String contentType, String fileName) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData(fileName, fileName);
        return new ResponseEntity<>(binary, headers, HttpStatus.OK);
    }

    static final Logger LOGGER = LoggerFactory.getLogger(ControllerBase.class);


}
