package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalBusinessResponse {
    private String status;

    @JsonProperty("data")
    private List<BusinessItem> data;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<BusinessItem> getData() { return data; }
    public void setData(List<BusinessItem> data) { this.data = data; }
}
