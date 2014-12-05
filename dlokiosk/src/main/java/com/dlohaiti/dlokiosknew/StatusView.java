package com.dlohaiti.dlokiosknew;

public interface StatusView {
    void showProgressBar();

    void dismissProgressBar();

    void refreshStatus(Boolean result);
}
