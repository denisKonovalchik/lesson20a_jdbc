package by.konovalchik.lesson20a.dao;

import by.konovalchik.lesson20a.entity.Telephone;

import java.util.List;


public interface TelephoneDAO {
    void addTelephone(Telephone telephone);
    void updateTelephoneById(int id, Telephone telephone);
    void deleteTelephoneById(int id);
    Telephone findById(int id);
    Telephone findByNumber(int number);
    List<Telephone> findAllTelephones();

}
