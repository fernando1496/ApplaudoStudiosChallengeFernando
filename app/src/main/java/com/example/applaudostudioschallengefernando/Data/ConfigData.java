package com.example.applaudostudioschallengefernando.Data;

public class ConfigData {

    //URL to get categories by Id
    public String sUrlGetKitsuCategories(int iCategoryId) {
        String sURL = String.format("https://kitsu.io/api/edge/categories/%1$d", iCategoryId);
        return sURL;
    }



    //URL to get animes by categorid
    public String sUrlGetKitsuAnimesByCategories(int iCategoryId, int iCurrentOffset) {
        String sURL = String.format("https://kitsu.io/api/edge/categories/%1$d/anime?page[limit]=20&page[offset]=%2$d", iCategoryId, iCurrentOffset);
        return sURL;
    }

}