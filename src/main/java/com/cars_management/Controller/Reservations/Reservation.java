package com.cars_management.Controller.Reservations;

import java.time.LocalDate;

public class Reservation {

    private Integer id;
    private String client;
    private String car;
    private int days;
    private double total;
    private LocalDate startDate;
    private LocalDate endDate;
    private double pricePerDay;

    public Reservation(String client, String car, int days, double total,
                       LocalDate startDate, LocalDate endDate, double pricePerDay) {

        this.client = client;
        this.car = car;
        this.days = days;
        this.total = total;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricePerDay = pricePerDay;
    }

    public Reservation(Integer id, String client, String car, int days, double total,
                       LocalDate startDate, LocalDate endDate, double pricePerDay) {

        this(client, car, days, total, startDate, endDate, pricePerDay);
        this.id = id;
    }

    // GETTERS - SETTERS
    public Integer getId() { return id; }
    public String getClient() { return client; }
    public String getCar() { return car; }
    public int getDays() { return days; }
    public double getTotal() { return total; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getPricePerDay() { return pricePerDay; }

    public void setId(Integer id) { this.id = id; }
}
