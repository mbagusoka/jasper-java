package com.rnd.jasperjava.model;

public class AdjustmentData {

    private String containerId;
    private String productionDate;
    private String sku;
    private String batch;
    private int quantity;

    public String getContainerId() {
        return containerId;
    }

    public AdjustmentData setContainerId(String containerId) {
        this.containerId = containerId;
        return this;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public AdjustmentData setProductionDate(String productionDate) {
        this.productionDate = productionDate;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public AdjustmentData setSku(String sku) {
        this.sku = sku;
        return this;
    }

    public String getBatch() {
        return batch;
    }

    public AdjustmentData setBatch(String batch) {
        this.batch = batch;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public AdjustmentData setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
