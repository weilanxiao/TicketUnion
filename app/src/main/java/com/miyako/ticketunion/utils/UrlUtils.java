package com.miyako.ticketunion.utils;

public class UrlUtils {

    public static String createCategoryContentUrl(int materialId, int page) {
        return "discovery" + "/" + materialId + "/" + page;
    }

    public static String coverPath(String path) {
        if(path.startsWith("http") || path.startsWith("https")) {
            return path;
        } else {
            return "https:" + path;
        }
    }

    public static String getTicketUrl(String url) {
        if(url.startsWith("http") || url.startsWith("https")) {
            return url;
        } else {
            return "https:" + url;
        }
    }
}
