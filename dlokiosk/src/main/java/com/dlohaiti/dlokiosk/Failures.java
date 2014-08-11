package com.dlohaiti.dlokiosk;

import java.util.ArrayList;
import java.util.List;

public class Failures {
    private List<Failure> failures = new ArrayList<Failure>();

    public boolean isNotEmpty() {
        return !failures.isEmpty();
    }

    public void add(Failure failure) {
        failures.add(failure);
    }

    public Integer countFor(FailureKind kind) {
        int total = 0;
        for (Failure failure : failures) {
            if (failure.isFor(kind)) {
                total++;
            }
        }
        return total;
    }
}
