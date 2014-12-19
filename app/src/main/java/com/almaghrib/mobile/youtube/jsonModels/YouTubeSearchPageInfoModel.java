package com.almaghrib.mobile.youtube.jsonModels;

public class YouTubeSearchPageInfoModel {
    String totalResults;
    String resultsPerPage;

    public YouTubeSearchPageInfoModel(String totalResults, String resultsPerPage) {
        this.totalResults = totalResults;
        this.resultsPerPage = resultsPerPage;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(String resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    @Override
    public String toString() {
        return "YouTubeSearchPageInfoModel{" +
                "totalResults='" + totalResults + '\'' +
                ", resultsPerPage='" + resultsPerPage + '\'' +
                '}';
    }
}
