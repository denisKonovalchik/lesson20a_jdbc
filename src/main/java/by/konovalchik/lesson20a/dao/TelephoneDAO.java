package by.konovalchik.lesson20a.dao;

import by.konovalchik.lesson20a.entity.Telephone;

import java.util.List;
import java.util.Optional;


public interface TelephoneDAO {
    void addTelephone(Telephone telephone);
    void updateTelephoneById(int id, Telephone telephone);
    void deleteTelephoneById(int id);
    Optional<Telephone> findById(int id);
    Optional <Telephone> findByNumber(int number);
    List<Telephone> findAllTelephones();

}
