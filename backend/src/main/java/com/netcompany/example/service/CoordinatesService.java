package com.netcompany.example.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.netcompany.example.domain.dto.CoordinatesDto;

/**
 * TODO
 *
 * @author Bjørn Golberg
 */
@Service
public class CoordinatesService {

    private static final String SERVER_URL = "https://railradar.railyatri.in/coa/user_train_location.json";
    private final RestTemplate rest;
    private final HttpHeaders headers;
    private final Gson gson;

    @Autowired
    public CoordinatesService(final Gson gson) {
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
        this.gson = gson;
    }

    public List<CoordinatesDto> getCoordinates(final String company) {
        final JsonObject allCoordinates = requestCoordinates();
        return getAmountOfCoordinates(allCoordinates).stream().map(coord -> new CoordinatesDto(company,
                                                                                               coord.get(0).getAsString(),
                                                                                               coord.get(26).getAsString(),
                                                                                               coord.get(27).getAsString()))
                .collect(Collectors.toList());
    }

    private JsonObject requestCoordinates() {
        final HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        final ResponseEntity<String> responseEntity = rest.exchange(SERVER_URL, HttpMethod.GET, requestEntity, String.class);

        return this.gson.fromJson(responseEntity.getBody(), JsonArray.class).get(0).getAsJsonObject();
    }

    private List<JsonArray> getAmountOfCoordinates(final JsonObject allCoords) {
        final Iterator<Map.Entry<String, JsonElement>> coordsIterator = allCoords.entrySet().iterator();
        final List<JsonArray> newCoordList = new ArrayList<>();

        Integer i = 0;
        while(coordsIterator.hasNext()) {
            if (i > 10) break;
            final Map.Entry<String, JsonElement> coordsEntry = coordsIterator.next();
            final JsonArray test = coordsEntry.getValue().getAsJsonArray();
            if (test.size() > 0) {
                newCoordList.add(test.get(0).getAsJsonArray());
                i++;
            }
        }

        return newCoordList;
    }

}
