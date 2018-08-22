package com.perimeterx.internals.cookie.cookieparsers;

import com.perimeterx.internals.cookie.RawCookieData;

public class MobileCookieHeaderParser extends HeaderParser{
    @Override
    protected String[] splitHeader(String header) {
        return header.split(",\\s?");
    }

    @Override
    protected RawCookieData createCookie(String cookie) {
        String [] splitCookie = cookie.split(":\\s?",2);
        RawCookieData rawCookieData = null;
        if (splitCookie.length == 2){
            String version = splitCookie[0];
            String standardVersion = putInCookieByVersionName(version);
            rawCookieData = new RawCookieData(standardVersion, splitCookie[1]);
        }
        else if(splitCookie.length == 1){
            rawCookieData = new RawCookieData("", splitCookie[0]);
        }
        return rawCookieData;
    }
}
