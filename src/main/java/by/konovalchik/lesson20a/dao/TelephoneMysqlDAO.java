package by.konovalchik.lesson20a.dao;


import by.konovalchik.lesson20a.connection.MysqlConnection;
import by.konovalchik.lesson20a.entity.Address;
import by.konovalchik.lesson20a.entity.Telephone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelephoneMysqlDAO implements TelephoneDAO {

    @Override
    public void addTelephone(Telephone telephone) {
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "INSERT INTO users_db.telephones VALUES ( NULL, ? ) ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, telephone.getNumber());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTelephoneById(int id, Telephone telephone) {
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "UPDATE users_db.telephones SET  number = ? WHERE id_telephone = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, telephone.getNumber());
            statement.setInt(2, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTelephoneById(int id) {
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "DELETE FROM users_db.telephones WHERE id_telephone = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Telephone findById(int id) {
        Telephone telephone  = new Telephone();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT * FROM users_db.telephones WHERE id_telephone = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                telephone = new Telephone(
                        resultSet.getInt("id_telephone"),
                        resultSet.getInt("number")
                );
            }
            return telephone;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Telephone();
    }

    @Override
    public Telephone findByNumber(int number) {
        Telephone telephone  = new Telephone();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT * FROM users_db.telephones WHERE number = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, number);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                telephone = new Telephone(
                        resultSet.getInt("id_telephone"),
                        resultSet.getInt("number")
                );
            }
            return telephone;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Telephone();
    }

    @Override
    public List<Telephone> findAllTelephones() {
        List<Telephone> telephones = new ArrayList<>();
        try (Connection connection = MysqlConnection.getConnection()) {
            String sql = "SELECT * FROM users_db.telephones ";
            Statement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Telephone telephone = new Telephone(
                        resultSet.getInt("id_telephone"),
                        resultSet.getInt("number")
                );
                telephones.add(telephone);
            }
            return telephones;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
