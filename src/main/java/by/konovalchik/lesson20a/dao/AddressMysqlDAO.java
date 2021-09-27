package by.konovalchik.lesson20a.dao;

import by.konovalchik.lesson20a.connection.MysqlConnection;
import by.konovalchik.lesson20a.entity.Address;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AddressMysqlDAO implements AddressDAO{


    @Override
    public void addAddress(Address address) {
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = " INSERT INTO users_db.addresses VALUES (NULL, ?, ? ) ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, address.getStreet());
            statement.setInt(2, address.getHome());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAddressById(int id, Address address) {
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "UPDATE users_db.addresses SET street = ?, home = ? WHERE id_address = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, address.getStreet());
            statement.setInt(2, address.getHome());
            statement.setInt(3, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteAddressById(int id) {
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "DELETE FROM users_db.addresses WHERE id_address = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Address> findAddressById(int id) {
        Optional<Address> address = Optional.empty();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT * FROM users_db.addresses WHERE id_address = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                address = Optional.of(new Address(
                        resultSet.getInt("id_address"),
                        resultSet.getString("street"),
                        resultSet.getInt("home")
                ));
            }
            return address;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public Optional<Address> findAddress(String street, int home) {
        Optional<Address> address = Optional.empty();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT * FROM users_db.addresses WHERE street = ? AND home = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, street);
            statement.setInt(2, home);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                address = Optional.of(new Address(
                        resultSet.getInt("id_address"),
                        resultSet.getString("street"),
                        resultSet.getInt("home")
                ));
            }
            return address;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }


    @Override
    public List<Address> findAllAddresses() {
        List<Address> addresses = new ArrayList<>();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT * FROM users_db.addresses ";
            Statement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                Address address = new Address(
                        resultSet.getInt("id_address"),
                        resultSet.getString("street"),
                        resultSet.getInt("home")
                );
                addresses.add(address);
            }
            return addresses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
