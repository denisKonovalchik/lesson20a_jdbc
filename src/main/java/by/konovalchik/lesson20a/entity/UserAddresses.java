package by.konovalchik.lesson20a.entity;

import java.util.List;

public class UserAddresses {
    private int id;
    private List<Address> addresses;

    public UserAddresses(int id, List<Address> addresses) {
        this.id = id;
        this.addresses = addresses;
    }

    public UserAddresses() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
