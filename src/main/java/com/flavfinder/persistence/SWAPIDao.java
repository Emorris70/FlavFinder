package com.flavfinder.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavfinder.Plant;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class SWAPIDao {

    public Plant getPlanet() {
        //
        Client client = ClientBuilder.newClient();
        //Gets the first plant
        // TODO read in the uri from properties file
        WebTarget target =
                client.target("https://swapi.info/api/planets/1");
        //
        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        //
        ObjectMapper mapper = new ObjectMapper();
        // Pass in the response and the class to map
        Plant plant = null;
        try {
            // TODO set up logging and write this to the log
            plant = mapper.readValue(response, Plant.class);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        return plant;
    }
}
