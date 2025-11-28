package com.cars_management.Controller.Reservations;

public class Reservation {

    private Integer id;   // <-- ajouté
    private String client;
    private String car;
    private int days;
    private double total;

    // Constructeur utilisé pour créer une réservation depuis l'interface
    public Reservation(String client, String car, int days, double total) {
        this.client = client;
        this.car = car;
        this.days = days;
        this.total = total;
    }

    // Constructeur utilisé lorsque les données viennent de la BD
    public Reservation(Integer id, String client, String car, int days, double total) {
        this.id = id;
        this.client = client;
        this.car = car;
        this.days = days;
        this.total = total;
    }

    // === GETTERS ===
    public Integer getId() { return id; }
    public String getClient() { return client; }
    public String getCar() { return car; }
    public int getDays() { return days; }
    public double getTotal() { return total; }

    // === SETTERS ===
    public void setId(Integer id) { this.id = id; }
    public void setClient(String client) { this.client = client; }
    public void setCar(String car) { this.car = car; }
    public void setDays(int days) { this.days = days; }
    public void setTotal(double total) { this.total = total; }

}
