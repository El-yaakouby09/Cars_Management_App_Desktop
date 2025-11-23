package com.cars_management.Controller.Contracts;

import java.time.LocalDate;

public class Contract {
    private Integer id;
    private String clientName;
    private String clientCNI; // CNI/Passport
    private String clientPhone;
    private String clientAddress;
    private LocalDate driverLicenseDate;
    
    // Car information at rental time
    private String carBrand;
    private String carModel;
    private String carPlate; // Numéro d'immatriculation
    
    // Rental dates
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private Integer rentalDays;
    
    // Price
    private Double pricePerDay;
    private Double totalPrice;

    // Default constructor
    public Contract() {}

    // Full constructor
    public Contract(Integer id, String clientName, String clientCNI, String clientPhone, 
                    String clientAddress, LocalDate driverLicenseDate, 
                    String carBrand, String carModel, String carPlate,
                    LocalDate rentalStartDate, LocalDate rentalEndDate, 
                    Integer rentalDays, Double pricePerDay, Double totalPrice) {
        this.id = id;
        this.clientName = clientName;
        this.clientCNI = clientCNI;
        this.clientPhone = clientPhone;
        this.clientAddress = clientAddress;
        this.driverLicenseDate = driverLicenseDate;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carPlate = carPlate;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.rentalDays = rentalDays;
        this.pricePerDay = pricePerDay;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCNI() {
        return clientCNI;
    }

    public void setClientCNI(String clientCNI) {
        this.clientCNI = clientCNI;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public LocalDate getDriverLicenseDate() {
        return driverLicenseDate;
    }

    public void setDriverLicenseDate(LocalDate driverLicenseDate) {
        this.driverLicenseDate = driverLicenseDate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public LocalDate getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDate rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDate getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(LocalDate rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public Integer getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(Integer rentalDays) {
        this.rentalDays = rentalDays;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", clientCNI='" + clientCNI + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carPlate='" + carPlate + '\'' +
                ", rentalStartDate=" + rentalStartDate +
                ", rentalEndDate=" + rentalEndDate +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
