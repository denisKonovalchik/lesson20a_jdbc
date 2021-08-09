package by.konovalchik.lesson20a.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private int id;
    private String login;
    private String password;
    private List<Address> addresses;
    private List<Telephone> telephones;

    public User(int id, String login, String password, List<Address> addresses, List<Telephone> telephones) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.addresses = addresses;
        this.telephones = telephones;
    }

    public User(String login, String password, List<Address> addresses, List<Telephone> telephones) {
        this.login = login;
        this.password = password;
        this.addresses = addresses;
        this.telephones = telephones;
    }

    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
        addresses = new ArrayList<>();
        telephones = new ArrayList<>();
    }


    public User(String login, String password) {
        this.login = login;
        this.password = password;
        addresses = new ArrayList<>();
        telephones = new ArrayList<>();
    }

    public User(int id) {
        this.id = id;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Telephone> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<Telephone> telephones) {
        this.telephones = telephones;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", addresses=" + addresses +
                ", telephones=" + telephones +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
