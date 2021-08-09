package by.konovalchik.lesson20a.dao;


import by.konovalchik.lesson20a.entity.Address;

import java.sql.SQLException;
import java.util.List;

public interface AddressDAO {
    void addAddress(Address address) ;
    void updateAddressById(int id, Address address);
    void deleteAddressById(int id);
    Address findAddressById(int id);
    Address findAddress(String street, int home);
    List<Address> findAllAddresses();
}
