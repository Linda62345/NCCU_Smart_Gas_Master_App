package com.glotech.smartgasmasterapp;

public class OrderListItem {
    public String address,expectTime;

    public OrderListItem(String address, String expectTime) {
        this.address = address;
        this.expectTime = expectTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }
}
