package by.konovalchik.lesson20a.entity;

import java.util.Objects;

public class Address {
    private int id;
    private String street;
    private int home;

    public Address(int id, String street, int home) {
        this.id = id;
        this.street = street;
        this.home = home;
    }

    public Address(String street, int home) {
        this.street = street;
        this.home = home;
    }

    public Address(int id) {
        this.id = id;
    }

    public Address() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", home=" + home +
                '}';
    }
}
