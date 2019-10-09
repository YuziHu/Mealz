package com.example.mealz.Models;

import java.util.List;


public class ResponseModel {

    private String q;
    private List<HitsModel> hits;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public List<HitsModel> getHits() {
        return hits;
    }

    public void setHits(List<HitsModel> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "Response{" +
                "q='" + q + '\'' +
                ", hits=" + hits +
                '}';
    }
}


