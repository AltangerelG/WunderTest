package io.github.altangerelg.wundertest.models;

import com.google.android.gms.maps.model.LatLng;

public class Car {
    private String address;
    private LatLng coordinates;
    private String engineType;
    private String exterior;
    private int fuel;
    private String interior;
    private String name;
    private String vin;


    public Car(String address,  LatLng coordinates, String engineType, String exterior, int fuel,
               String interior, String name, String vin) {
        this.address = address;
        this.coordinates = coordinates;
        this.engineType = engineType;
        this.exterior = exterior;
        this.fuel = fuel;
        this.interior = interior;
        this.name = name;
        this.vin = vin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getExterior() {
        return exterior;
    }

    public void setExterior(String exterior) {
        this.exterior = exterior;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) { this.interior = interior; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) { this.vin = vin; }
}
