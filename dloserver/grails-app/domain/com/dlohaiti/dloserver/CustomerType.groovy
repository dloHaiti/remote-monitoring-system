package com.dlohaiti.dloserver

class CustomerType {
    String name
    CustomerType parent

    static constraints = {
        name(blank: false, unique: true)
        parent(nullable: true)
    }

    @Override
    public String toString() {
        name
    }

    public boolean isSubType(){
        return parent != null
    }
}
