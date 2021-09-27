package by.konovalchik.lesson20a.dao;


import by.konovalchik.lesson20a.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressDAO {
    void addAddress(Address address) ;
    void updateAddressById(int id, Address address);
    void deleteAddressById(int id);
    Optional<Address> findAddressById(int id);
    Optional<Address> findAddress(String street, int home);
    List<Address> findAllAddresses();
}
