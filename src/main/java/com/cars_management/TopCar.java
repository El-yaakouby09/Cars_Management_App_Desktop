package com.cars_management;

public class TopCar {

    private String marque;
    private String model;

    public TopCar() {}

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getFullName() {
        return marque + " " + model;
    }
}
