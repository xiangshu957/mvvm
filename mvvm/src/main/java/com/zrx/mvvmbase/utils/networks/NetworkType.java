package com.zrx.mvvmbase.utils.networks;

public enum NetworkType {
    NETWORK_WIFI("wifi"),
    NETWORK_4G("4G"),
    NETWORK_3G("3G"),
    NETWORK_2G("2G"),
    NETWORK_UNKNOWN("net_unknow"),
    NETWORK_NO("no_network");

    private String desc;

    NetworkType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
