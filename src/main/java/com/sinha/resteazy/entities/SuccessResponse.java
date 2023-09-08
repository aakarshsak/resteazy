package com.sinha.resteazy.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    private String message;
    private String responseType;
    private Object data;

    public SuccessResponse(Object object, int status ) {
        this.data = object;
        this.responseType = object.getClass().toString();
        switch(status) {
            case 201 : message = "Successfully created..."; break;
            case 202: message = "Successfully deleted..."; break;
            default: message = "Something elsse....";
        }
    }
}
