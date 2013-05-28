package com.dlohaiti.dlokiosk.domain;

public interface Orderable {
    String getSku();
    Integer getQuantity();
    Money getPrice();
}
