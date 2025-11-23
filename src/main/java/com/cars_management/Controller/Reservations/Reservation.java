package com.cars_management.Controller.Reservations;

public class Reservation {

    private String client;
    private String car;
    private int days;
    private double total;

    public Reservation(String client, String car, int days, double total) {
        this.client = client;
        this.car = car;
        this.days = days;
        this.total = total;
    }

    public String getClient() { return client; }
    public String getCar() { return car; }
    public int getDays() { return days; }
    public double getTotal() { return total; }

}
