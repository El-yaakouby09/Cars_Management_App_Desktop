package com.cars_management.Controller.Cars;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Car {

    private Integer id;
    private String matricule;        // رقم اللوحة
    private String marqueModel;      // marque + model
    private Integer annee;
    private String status;           // "Available", "maintenance", "not available"
    private BigDecimal prixDay;      // prix par jour
    private LocalDate dateMaintenance;

    public Car() {}

    public Car(Integer id, String matricule, String marqueModel, String status, BigDecimal prixDay, LocalDate dateMaintenance) {
        this.id = id;
        this.matricule = matricule;
        this.marqueModel = marqueModel;
        this.annee = null;
        this.status = status;
        this.prixDay = prixDay;
        this.dateMaintenance = dateMaintenance;
    }

    public Car(String matricule, String marqueModel, String status, BigDecimal prixDay, LocalDate dateMaintenance) {
        this(null, matricule, marqueModel, status, prixDay, dateMaintenance);
    }

    // Compatibility constructor used by older controllers: id, marque, modele, annee, prix
    public Car(int id, String marque, String modele, int annee, double prix) {
        this.id = id;
        this.matricule = null;
        this.marqueModel = (marque == null ? "" : marque) + (modele == null ? "" : " " + modele);
        this.annee = annee;
        this.status = "Available";
        this.prixDay = BigDecimal.valueOf(prix);
        this.dateMaintenance = null;
    }

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getMarqueModel() { return marqueModel; }
    public void setMarqueModel(String marqueModel) { this.marqueModel = marqueModel; }

    // Backwards-compatible accessors used by existing controller code
    public String getMarque() {
        if (marqueModel == null) return "";
        String[] parts = marqueModel.split(" ", 2);
        return parts.length > 0 ? parts[0] : "";
    }

    public void setMarque(String marque) {
        String modele = getModele();
        this.marqueModel = (marque == null ? "" : marque) + (modele.isEmpty() ? "" : " " + modele);
    }

    public String getModele() {
        if (marqueModel == null) return "";
        String[] parts = marqueModel.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    public void setModele(String modele) {
        String marque = getMarque();
        this.marqueModel = (marque == null ? "" : marque) + (modele == null ? "" : " " + modele);
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getPrixDay() { return prixDay; }
    public void setPrixDay(BigDecimal prixDay) { this.prixDay = prixDay; }

    public Double getPrix() { return prixDay == null ? 0.0 : prixDay.doubleValue(); }

    public void setPrix(Double prix) { this.prixDay = prix == null ? null : BigDecimal.valueOf(prix); }

    public LocalDate getDateMaintenance() { return dateMaintenance; }
    public void setDateMaintenance(LocalDate dateMaintenance) { this.dateMaintenance = dateMaintenance; }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", matricule='" + matricule + '\'' +
                ", marqueModel='" + marqueModel + '\'' +
                ", status='" + status + '\'' +
                ", prixDay=" + prixDay +
                ", dateMaintenance=" + dateMaintenance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return Objects.equals(matricule, car.matricule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricule);
    }
}
