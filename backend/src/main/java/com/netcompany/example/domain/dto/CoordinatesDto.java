package com.netcompany.example.domain.dto;

/**
 * TODO
 *
 * @author Bjørn Golberg
 */
public class CoordinatesDto {

    private String id;
    private Double latitude;
    private Double longitude;

    public CoordinatesDto(final String company, final String id, final String latiude, final String longitude) {

        this.id = id;
        if (company.equals("soultrain")) {
            this.latitude = Double.parseDouble(latiude) - 3.28;
            this.longitude = Double.parseDouble(longitude) - 62.5268;
        } else if (company.equals("eastindia")) {
            this.latitude = Double.parseDouble(latiude) - 3.28;
            this.longitude = Double.parseDouble(longitude) - 62.5268;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

}
