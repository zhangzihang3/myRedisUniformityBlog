package com.zzh.data.enums;

public enum cacheKey {
    STOCKCACHEKEY("zzh");

    private String cacheKey;

    cacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
