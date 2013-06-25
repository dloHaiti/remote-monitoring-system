package com.dlohaiti.dlokiosk;

import java.util.List;

public class Failure {
    private FailureKind failureKind;
    private List<String> messages;

    Failure(FailureKind failureKind, List<String> messages) {
        this.failureKind = failureKind;
        this.messages = messages;
    }

    public boolean isFor(FailureKind kind) {
        return kind == failureKind;
    }
}
