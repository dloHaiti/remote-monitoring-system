package com.dlohaiti.dlokiosknew.domain;

import com.dlohaiti.dlokiosknew.DeliveryType;

import java.util.Date;

public class Delivery {
    private final Integer id;
    private final int quantity;
    private final DeliveryType type;
    private final String agentName;
    private final Date createdDate;

    public Delivery(int quantity, DeliveryType type, Date createdDate, String agentName) {
        this(null, quantity, type, createdDate, agentName);
    }

    public Delivery(Integer id, Integer quantity, DeliveryType type, Date createdDate, String agentName) {
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        this.createdDate = createdDate;
        this.agentName = agentName;
    }

    public Integer getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public DeliveryType getType() {
        return type;
    }

    public String getAgentName() {
        return agentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Delivery delivery = (Delivery) o;

        if (quantity != delivery.quantity) return false;
        if (agentName != null ? !agentName.equals(delivery.agentName) : delivery.agentName != null) return false;
        if (createdDate != null ? !createdDate.equals(delivery.createdDate) : delivery.createdDate != null)
            return false;
        if (id != null ? !id.equals(delivery.id) : delivery.id != null) return false;
        if (type != delivery.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + quantity;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (agentName != null ? agentName.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", type=" + type +
                ", agentName='" + agentName + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
