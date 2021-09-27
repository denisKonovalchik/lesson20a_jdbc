package by.konovalchik.lesson20a.dao;


import by.konovalchik.lesson20a.connection.MysqlConnection;
import by.konovalchik.lesson20a.entity.Address;
import by.konovalchik.lesson20a.entity.Telephone;
import by.konovalchik.lesson20a.entity.User;
import by.konovalchik.lesson20a.entity.UserAddresses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMysqlDAO implements UserDAO {

    public void addUser(User user) {
        Connection connection = null;
        try {
            connection = MysqlConnection.getConnection();
            connection.setAutoCommit(false);
            String sql1 = "INSERT INTO users VALUES (NULL, ?, ?) ";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, user.getLogin());
            statement1.setString(2, user.getPassword());
            statement1.execute();

            String sql2 = "SELECT id_user FROM users WHERE login = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, user.getLogin());
            ResultSet resultSet1 = statement2.executeQuery();
            resultSet1.next();
            int userId = resultSet1.getInt("id_user");

            String sqlCheck1 = "SET foreign_key_checks = 0 ";
            Statement statementCheck1 = connection.prepareStatement(sqlCheck1);
            statementCheck1.execute(sqlCheck1);

            for(Address address: user.getAddresses()) {
                String sql3 = "INSERT INTO users_addresses VALUES (?, ?)";
                PreparedStatement statement3 = connection.prepareStatement(sql3);
                statement3.setInt(1, userId);
                statement3.setInt(2, address.getId());
                statement3.execute();
            }

            for(Telephone telephone: user.getTelephones()) {
                String sql4 = "INSERT INTO users_telephones VALUES (?, ?)";
                PreparedStatement statement4 = connection.prepareStatement(sql4);
                statement4.setInt(1, userId);
                statement4.setInt(2, telephone.getId());
                statement4.execute();
            }

            String sqlCheck2 = "SET foreign_key_checks = 1 ";
            Statement statementCheck2 = connection.prepareStatement(sqlCheck1);
            statementCheck2.execute(sqlCheck2);

            connection.commit();
        } catch(SQLException throwable) {
            try {
                if(connection !=null) connection.rollback();
                throwable.printStackTrace();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } finally {
            try {
                if(connection != null)
                connection.setAutoCommit(true);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try(Connection connection = MysqlConnection.getConnection()) {
            String sql1 = "SELECT u.id_user, u.login, u.password,ua.id_user, a.id_address, a.street, a.home, t.id_telephone, t.number " +
                          "FROM users u " +
                          "LEFT JOIN users_addresses ua ON u.id_user = ua.id_user " +
                          "LEFT JOIN addresses a ON ua.id_addr = a.id_address " +
                          "LEFT JOIN users_telephones ut ON u.id_user = ut.id_user " +
                          "LEFT JOIN telephones t ON ut.id_tel = t.id_telephone";

            PreparedStatement statement1 = connection.prepareStatement(sql1);
            ResultSet resultSet = statement1.executeQuery();
            while(resultSet.next()){
              User  user = new User (
                        resultSet.getInt("id_user"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));

                if(!users.contains(user)) {
                    user.getAddresses().add(new Address(
                            resultSet.getInt("id_address"),
                            resultSet.getString("street"),
                            resultSet.getInt("home")));


                    user.getTelephones().add(new Telephone(
                            resultSet.getInt("id_telephone"),
                            resultSet.getInt("number")));

                    users.add(user);

                } else{
                    Address address = new Address(
                            resultSet.getInt("id_address"),
                            resultSet.getString("street"),
                            resultSet.getInt("home")
                    );
                    if(!users.get(users.indexOf(user)).getAddresses().contains(address))
                        users.get(users.indexOf(user)).getAddresses().add(address);

                    Telephone telephone = new Telephone(
                            resultSet.getInt("id_telephone"),
                            resultSet.getInt("number")
                    );
                    if(!users.get(users.indexOf(user)).getTelephones().contains(telephone))
                        users.get(users.indexOf(user)).getTelephones().add(telephone);
                }
            }
           return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public void deleteById(int id) {
        List<Address> addresses = new ArrayList<>();
        List<Telephone> telephones = new ArrayList<>();
        int idUser = 0;
        int idTel = 0;
        int idAddr = 0;

       Connection connection = null;
       try {
           connection = MysqlConnection.getConnection();
           connection.setAutoCommit(false);
           String sql1 = "SELECT u.id_user, a.id_address, a.street, a.home, t.id_telephone, t.number " +
                                             "FROM users u "+
                                             "LEFT JOIN users_addresses ua ON u.id_user = ua.id_user " +
                                             "LEFT JOIN addresses a ON ua.id_addr = a.id_address " +
                                             "LEFT JOIN users_telephones ut ON u.id_user = ut.id_user " +
                                             "LEFT JOIN telephones t ON ut.id_tel = t.id_telephone " +
                                             "WHERE u.id_user = ? ";
           PreparedStatement statement1 = connection.prepareStatement(sql1);
           statement1.setInt(1,id);
           ResultSet resultSet1 = statement1.executeQuery();

           while(resultSet1.next()) {
               idUser = resultSet1.getInt("id_user");
               Address address = new Address(
                       resultSet1.getInt("id_address"),
                       resultSet1.getString("street"),
                       resultSet1.getInt("home")
               );
               if(!addresses.contains(address)){addresses.add(address);}

               Telephone telephone = new Telephone(
                       resultSet1.getInt("id_telephone"),
                       resultSet1.getInt("number")
               );
               if(!telephones.contains(telephone)){telephones.add(telephone);}
           }

           String sql2 = "DELETE FROM users WHERE id_user = ?";
           PreparedStatement statement2 = connection.prepareStatement(sql2);
           statement2.setInt(1,idUser);
           statement2.execute();

           String sql3 = "DELETE FROM users_addresses WHERE id_user = ?";
           PreparedStatement statement3 = connection.prepareStatement(sql3);
           statement3.setInt(1,idUser);
           statement3.execute();

           String sql4 = "DELETE FROM users_telephones WHERE id_user = ?";
           PreparedStatement statement4 = connection.prepareStatement(sql4);
           statement4.setInt(1,idUser);
           statement4.execute();

           for(Telephone value : telephones) {
               int idTelResp = 0;
               idTel = value.getId();
               String sql5 = "SELECT * FROM users_telephones WHERE id_tel = ?";
               PreparedStatement statement5 = connection.prepareStatement(sql5);
               statement5.setInt(1, idTel);
               ResultSet resultSet5 = statement5.executeQuery();
               while(resultSet5.next()) {
                   idTelResp = resultSet5.getInt("id_tel");
               }

               if(idTelResp == 0) {
                   String sql6 = "DELETE FROM users_db.telephones t WHERE t.id_telephone = ? ";
                   PreparedStatement statement6 = connection.prepareStatement(sql6);
                   statement6.setInt(1, idTel);
                   statement6.execute();
               }
           }

           for(Address value : addresses) {
               int idAddrResp = 0;
               idAddr = value.getId();
               String sql7 = "SELECT * FROM users_addresses WHERE id_addr = ?";
               PreparedStatement statement7 = connection.prepareStatement(sql7);
               statement7.setInt(1, idAddr);
               ResultSet resultSet7 = statement7.executeQuery();
               while(resultSet7.next()) {
                   idAddrResp = resultSet7.getInt("id_addr");
               }

               if(idAddrResp == 0) {
                   String sql8 = "DELETE FROM users_db.addresses a WHERE a.id_address = ? ";
                   PreparedStatement statement8 = connection.prepareStatement(sql8);
                   statement8.setInt(1, idAddr);
                   statement8.execute();
               }
           }
           connection.commit();
       } catch(SQLException throwable) {
           try {
               if(connection !=null) connection.rollback();
               throwable.printStackTrace();
           }catch (SQLException e){
               e.printStackTrace();
           }
       } finally {
           try {
               if(connection != null)
                   connection.setAutoCommit(true);
           }catch(SQLException e){
               e.printStackTrace();
           }
       }
    }


    @Override
    public void deleteByLogin(String login) {
        Connection connection = null;
        List<Address> addresses = new ArrayList<>();
        List<Telephone> telephones = new ArrayList<>();
        int idUser = 0;
        int idTel = 0;
        int idAddr = 0;

        try {
            connection = MysqlConnection.getConnection();
            connection.setAutoCommit(false);
            String sql = "SELECT u.id_user FROM users u WHERE u.login = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,login);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) idUser = resultSet.getInt("id_user");

            String sql1 = "SELECT u.id_user, a.id_address, a.street, a.home, t.id_telephone, t.number " +
                    "FROM users u "+
                    "LEFT JOIN users_addresses ua ON u.id_user = ua.id_user " +
                    "LEFT JOIN addresses a ON ua.id_addr = a.id_address " +
                    "LEFT JOIN users_telephones ut ON u.id_user = ut.id_user " +
                    "LEFT JOIN telephones t ON ut.id_tel = t.id_telephone " +
                    "WHERE u.id_user = ? ";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1,idUser);
            ResultSet resultSet1 = statement1.executeQuery();

            while(resultSet1.next()) {
                idUser = resultSet1.getInt("id_user");
                Address address = new Address(
                        resultSet1.getInt("id_address"),
                        resultSet1.getString("street"),
                        resultSet1.getInt("home")
                );
                if(!addresses.contains(address)){addresses.add(address);}

                Telephone telephone = new Telephone(
                        resultSet1.getInt("id_telephone"),
                        resultSet1.getInt("number")
                );
                if(!telephones.contains(telephone)){telephones.add(telephone);}
            }

            String sql2 = "DELETE FROM users WHERE id_user = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1,idUser);
            statement2.execute();

            String sql3 = "DELETE FROM users_addresses WHERE id_user = ?";
            PreparedStatement statement3 = connection.prepareStatement(sql3);
            statement3.setInt(1,idUser);
            statement3.execute();

            String sql4 = "DELETE FROM users_telephones WHERE id_user = ?";
            PreparedStatement statement4 = connection.prepareStatement(sql4);
            statement4.setInt(1,idUser);
            statement4.execute();

            for(Telephone value : telephones) {
                int idTelResp = 0;
                idTel = value.getId();
                String sql5 = "SELECT * FROM users_telephones WHERE id_tel = ?";
                PreparedStatement statement5 = connection.prepareStatement(sql5);
                statement5.setInt(1, idTel);
                ResultSet resultSet5 = statement5.executeQuery();
                while (resultSet5.next()) {
                    idTelResp = resultSet5.getInt("id_tel");
                }

                if(idTelResp == 0) {
                    String sql6 = "DELETE FROM users_db.telephones t WHERE t.id_telephone = ? ";
                    PreparedStatement statement6 = connection.prepareStatement(sql6);
                    statement6.setInt(1, idTel);
                    statement6.execute();
                }
            }

            for(Address value : addresses) {
                int idAddrResp = 0;
                idAddr = value.getId();
                String sql7 = "SELECT * FROM users_addresses WHERE id_addr = ?";
                PreparedStatement statement7 = connection.prepareStatement(sql7);
                statement7.setInt(1, idAddr);
                ResultSet resultSet7 = statement7.executeQuery();
                while (resultSet7.next()) {
                    idAddrResp = resultSet7.getInt("id_addr");
                }

                if(idAddrResp == 0) {
                    String sql8 = "DELETE FROM users_db.addresses a WHERE a.id_address = ? ";
                    PreparedStatement statement8 = connection.prepareStatement(sql8);
                    statement8.setInt(1, idAddr);
                    statement8.execute();
                }
            }

            connection.commit();
        } catch(SQLException throwable) {
            try {
                if(connection !=null) connection.rollback();
                throwable.printStackTrace();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } finally {
            try {
                if(connection != null)
                    connection.setAutoCommit(true);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public User findById(int id) {
        List <User> users = new ArrayList<>();
        try(Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT u.id_user, u.login, u.password,ua.id_user, a.id_address, a.street, a.home, t.id_telephone, t.number " +
                    "FROM users u " +
                    "LEFT JOIN users_addresses ua ON u.id_user = ua.id_user " +
                    "LEFT JOIN addresses a ON ua.id_addr = a.id_address " +
                    "LEFT JOIN users_telephones ut ON u.id_user = ut.id_user " +
                    "LEFT JOIN telephones t ON ut.id_tel = t.id_telephone " +
                    "WHERE u.id_user = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                User  user = new User (
                        resultSet.getInt("id_user"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));

                if(users.isEmpty()) {
                    user.getAddresses().add(new Address(
                            resultSet.getInt("id_address"),
                            resultSet.getString("street"),
                            resultSet.getInt("home")));

                    user.getTelephones().add(new Telephone(
                            resultSet.getInt("id_telephone"),
                            resultSet.getInt("number")));

                    users.add(user);

                } else{
                    Address address = new Address(
                            resultSet.getInt("id_address"),
                            resultSet.getString("street"),
                            resultSet.getInt("home")
                    );
                    if (!users.get(0).getAddresses().contains(address))
                        users.get(0).getAddresses().add(address);

                    Telephone telephone = new Telephone(
                            resultSet.getInt("id_telephone"),
                            resultSet.getInt("number")
                    );
                    if (!users.get(0).getTelephones().contains(telephone))
                        users.get(0).getTelephones().add(telephone);
                }
            }
            return users.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users.get(0);
    }

    @Override
    public User findByLogin(String login) {
        int idUser = 0;
        List <User> users = new ArrayList<>();
        try(Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT u.id_user FROM users u WHERE u.login = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,login);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {idUser = resultSet.getInt("id_user");}

            String sql1 = "SELECT u.id_user, u.login, u.password,ua.id_user, a.id_address, a.street, a.home, t.id_telephone, t.number " +
                    "FROM users u " +
                    "LEFT JOIN users_addresses ua ON u.id_user = ua.id_user " +
                    "LEFT JOIN addresses a ON ua.id_addr = a.id_address " +
                    "LEFT JOIN users_telephones ut ON u.id_user = ut.id_user " +
                    "LEFT JOIN telephones t ON ut.id_tel = t.id_telephone " +
                    "WHERE u.id_user = ? ";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1,idUser);
            ResultSet resultSet1 = statement1.executeQuery();
            while(resultSet1.next()){
                User  user = new User (
                        resultSet1.getInt("id_user"),
                        resultSet1.getString("login"),
                        resultSet1.getString("password"));

                if(users.isEmpty()) {
                    user.getAddresses().add(new Address(
                            resultSet1.getInt("id_address"),
                            resultSet1.getString("street"),
                            resultSet1.getInt("home")));

                    user.getTelephones().add(new Telephone(
                            resultSet1.getInt("id_telephone"),
                            resultSet1.getInt("number")));

                    users.add(user);

                } else{
                    Address address = new Address(
                            resultSet1.getInt("id_address"),
                            resultSet1.getString("street"),
                            resultSet1.getInt("home")
                    );
                    if (!users.get(0).getAddresses().contains(address))
                        users.get(0).getAddresses().add(address);

                    Telephone telephone = new Telephone(
                            resultSet1.getInt("id_telephone"),
                            resultSet1.getInt("number")
                    );
                    if (!users.get(0).getTelephones().contains(telephone))
                        users.get(0).getTelephones().add(telephone);
                }
            }

            return users.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users.get(0);

    }

    @Override
    public List<User> findByAddress(Address address) {
        int idAddr = 0;
        List<User> users = new ArrayList<>();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT a.id_address FROM addresses a WHERE street = ? AND home = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, address.getStreet());
            statement.setInt(2,address.getHome());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                idAddr = resultSet.getInt("id_address");
            }
            String sql1 = "SELECT u.id_user, u.login, u.password,ua.id_user, a.id_address, a.street, a.home, t.id_telephone, t.number " +
                    "FROM users u " +
                    "LEFT JOIN users_addresses ua ON u.id_user = ua.id_user " +
                    "LEFT JOIN addresses a ON ua.id_addr = a.id_address " +
                    "LEFT JOIN users_telephones ut ON u.id_user = ut.id_user " +
                    "LEFT JOIN telephones t ON ut.id_tel = t.id_telephone " +
                    "WHERE ua.id_addr = ? ";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1,idAddr);
            ResultSet resultSet1 = statement1.executeQuery();
            while(resultSet1.next()) {
                User user = new User(
                        resultSet1.getInt("id_user"),
                        resultSet1.getString("login"),
                        resultSet1.getString("password"));

                if (!users.contains(user)) {
                    user.getAddresses().add(new Address(
                            resultSet1.getInt("id_address"),
                            resultSet1.getString("street"),
                            resultSet1.getInt("home")));


                    user.getTelephones().add(new Telephone(
                            resultSet1.getInt("id_telephone"),
                            resultSet1.getInt("number")));

                    users.add(user);

                } else {
                    Address address1 = new Address(
                            resultSet1.getInt("id_address"),
                            resultSet1.getString("street"),
                            resultSet1.getInt("home")
                    );
                    if (!users.get(users.indexOf(user)).getAddresses().contains(address1))
                        users.get(users.indexOf(user)).getAddresses().add(address1);

                    Telephone telephone = new Telephone(
                            resultSet1.getInt("id_telephone"),
                            resultSet1.getInt("number")
                    );
                    if (!users.get(users.indexOf(user)).getTelephones().contains(telephone))
                        users.get(users.indexOf(user)).getTelephones().add(telephone);
                }
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public List<User> findByTelephone(Telephone telephone) {
        int idTel = 0;
        List<User> users = new ArrayList<>();
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT t.id_telephone FROM telephones t WHERE number = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, telephone.getNumber());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                idTel = resultSet.getInt("id_telephone");
            }

            String sql1 = "SELECT u.id_user, u.login, u.password,ua.id_user, a.id_address, a.street, a.home, t.id_telephone, t.number " +
                    "FROM users u " +
                    "LEFT JOIN users_addresses ua ON u.id_user = ua.id_user " +
                    "LEFT JOIN addresses a ON ua.id_addr = a.id_address " +
                    "LEFT JOIN users_telephones ut ON u.id_user = ut.id_user " +
                    "LEFT JOIN telephones t ON ut.id_tel = t.id_telephone " +
                    "WHERE ut.id_tel = ? ";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1,idTel);
            ResultSet resultSet1 = statement1.executeQuery();
            while(resultSet1.next()) {
                User user = new User(
                        resultSet1.getInt("id_user"),
                        resultSet1.getString("login"),
                        resultSet1.getString("password"));

                if (!users.contains(user)) {
                    user.getAddresses().add(new Address(
                            resultSet1.getInt("id_address"),
                            resultSet1.getString("street"),
                            resultSet1.getInt("home")));


                    user.getTelephones().add(new Telephone(
                            resultSet1.getInt("id_telephone"),
                            resultSet1.getInt("number")));

                    users.add(user);

                } else {
                    Address address1 = new Address(
                            resultSet1.getInt("id_address"),
                            resultSet1.getString("street"),
                            resultSet1.getInt("home")
                    );
                    if (!users.get(users.indexOf(user)).getAddresses().contains(address1))
                        users.get(users.indexOf(user)).getAddresses().add(address1);

                    Telephone telephone1 = new Telephone(
                            resultSet1.getInt("id_telephone"),
                            resultSet1.getInt("number")
                    );
                    if (!users.get(users.indexOf(user)).getTelephones().contains(telephone1))
                        users.get(users.indexOf(user)).getTelephones().add(telephone1);
                }
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean containUserById(int id) {
        int idUser = 0;
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT u.id_user FROM users u WHERE id_user = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                idUser = resultSet.getInt("id_user");
                    if(idUser == id){
                    return true;
                   }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean containUserByLogin(String login) {
        String loginUser;
        try (Connection connection = MysqlConnection.getConnection()){
            String sql = "SELECT u.login FROM users u WHERE login = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,login);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                loginUser = resultSet.getString("login");
                if(login.contains(loginUser)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
