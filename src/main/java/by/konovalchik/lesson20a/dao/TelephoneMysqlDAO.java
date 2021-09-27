package by.konovalchik.lesson20a.dao;


import by.konovalchik.lesson20a.connection.MysqlConnection;
import by.konovalchik.lesson20a.entity.Telephone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<Telephone> findById(int id) {
         Optional<Telephone> telephone = Optional.empty();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT * FROM users_db.telephones WHERE id_telephone = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                telephone = Optional.of(new Telephone(
                        resultSet.getInt("id_telephone"),
                        resultSet.getInt("number")
                ));
            }
            return telephone;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return telephone;
    }

    @Override
    public Optional<Telephone> findByNumber(int number) {
        Optional<Telephone> telephone = Optional.empty();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT * FROM users_db.telephones WHERE number = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, number);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                telephone = Optional.of(new Telephone(
                        resultSet.getInt("id_telephone"),
                        resultSet.getInt("number")
                ));
            }
            return telephone;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return telephone;
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
