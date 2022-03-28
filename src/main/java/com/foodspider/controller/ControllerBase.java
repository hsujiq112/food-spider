package com.foodspider.controller;

import com.foodspider.model.response_model.EmptyResponse;
import com.foodspider.model.response_model.ResponseBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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


}
