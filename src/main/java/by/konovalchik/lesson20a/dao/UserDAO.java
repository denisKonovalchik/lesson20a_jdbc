package by.konovalchik.lesson20a.dao;


import by.konovalchik.lesson20a.entity.Address;
import by.konovalchik.lesson20a.entity.Telephone;
import by.konovalchik.lesson20a.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    void addUser(User user) throws SQLException;
    List<User> findAll();
    void deleteById(int id);
    void deleteByLogin(String login);
    User findById(int id);
    User findByLogin(String login);
    List<User> findByAddress(Address address);
    List <User> findByTelephone(Telephone telephone);
    boolean containUserById(int id);
    boolean containUserByLogin(String login);


}
