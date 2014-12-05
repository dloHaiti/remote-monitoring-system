package com.dlohaiti.dlokiosknew.client;

import java.util.ArrayList;
import java.util.List;

public class PostResponse {
    private List<String> errors;

    public PostResponse() {
        this.errors = new ArrayList<String>();
    }


    public PostResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }
}
