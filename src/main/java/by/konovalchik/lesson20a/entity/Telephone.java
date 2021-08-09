package by.konovalchik.lesson20a.entity;

import java.util.Objects;

public class Telephone {
    private int id;
    private int number;

    public Telephone(int id, int number) {
        this.id = id;
        this.number = number;
    }

    public Telephone(int id) {
        this.id = id;
    }

    public Telephone() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telephone telephone = (Telephone) o;
        return id == telephone.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "id=" + id +
                ", number=" + number +
                '}';
    }
}
