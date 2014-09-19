package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.exception.NoSalesChannelWithGivenIdException;

import java.util.ArrayList;
import java.util.List;

public class CustomerTypes extends ArrayList<CustomerType> {
    public CustomerTypes(List<CustomerType> customerTypes) {
        super(customerTypes);
    }

    public String getCustomerTypeNameById(String customerTypeId) {
        for(CustomerType ct: this){
            if(ct.getId()==Long.valueOf(customerTypeId))
                return ct.getName();
        }
        return "";
    }

    public String getCustomerTypeId(String typeName) {
        for(CustomerType ct: this){
            if(ct.getName().equalsIgnoreCase(typeName))
                return String.valueOf(ct.getId());
        }
        return "";
    }

    public List<String> getCustomerTypeNames() {
        List<String> names=new ArrayList<String>();
        for(CustomerType ct: this){
            names.add(ct.getName());
        }
        return names;
    }
}
