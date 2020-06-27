package org.example;

public class DummyPojo {
    private String name;
    private String uuId;

    public DummyPojo() {
    }

    public DummyPojo(String name, String uuId) {
        this.name = name;
        this.uuId = uuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }
}
