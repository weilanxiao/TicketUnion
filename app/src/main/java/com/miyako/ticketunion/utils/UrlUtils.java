package com.miyako.ticketunion.utils;

public class UrlUtils {

    public static String createCategoryContentUrl(int materialId, int page) {
        return "discovery" + "/" + materialId + "/" + page;
    }

    public static String coverPath(String path) {
        return "https://" + path;
    }
}
