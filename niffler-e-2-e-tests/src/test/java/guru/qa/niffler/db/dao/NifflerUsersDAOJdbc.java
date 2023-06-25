package guru.qa.niffler.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;


public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

    private static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);

    @Override
    public int createUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement insertUserSt = conn.prepareStatement("INSERT INTO users "
                    + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                    + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement insertAuthoritySt = conn.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {
                insertUserSt.setString(1, user.getUsername());
                insertUserSt.setString(2, pe.encode(user.getPassword()));
                insertUserSt.setBoolean(3, user.getEnabled());
                insertUserSt.setBoolean(4, user.getAccountNonExpired());
                insertUserSt.setBoolean(5, user.getAccountNonLocked());
                insertUserSt.setBoolean(6, user.getCredentialsNonExpired());
                executeUpdate = insertUserSt.executeUpdate();

                final UUID finalUserId;

                try (ResultSet generatedKeys = insertUserSt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        finalUserId = UUID.fromString(generatedKeys.getString(1));
                        user.setId(finalUserId);
                    } else {
                        throw new SQLException("Creating user failed, no ID present");
                    }
                }

                for (AuthorityEntity authority : user.getAuthorities()) {
                    insertAuthoritySt.setObject(1, finalUserId);
                    insertAuthoritySt.setString(2, authority.getAuthority().name());
                    insertAuthoritySt.addBatch();
                    insertAuthoritySt.clearParameters();
                }
                insertAuthoritySt.executeBatch();
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }

    @Override
    public String getUserId(String userName) {
        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            st.setString(1, userName);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                throw new IllegalArgumentException("Can`t find user by given username: " + userName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity getUser(UserEntity user) {
        UserEntity userInfo = new UserEntity();
        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
             PreparedStatement stAuthorities = conn.prepareStatement("SELECT * FROM authorities WHERE user_id = ?")) {
            st.setString(1, user.getUsername());
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                userInfo.setId((UUID) resultSet.getObject(1));
                userInfo.setUsername(resultSet.getString(2));
                userInfo.setEnabled(resultSet.getBoolean(4));
                userInfo.setAccountNonExpired(resultSet.getBoolean(5));
                userInfo.setAccountNonLocked(resultSet.getBoolean(6));
                userInfo.setCredentialsNonExpired(resultSet.getBoolean(7));
                List<AuthorityEntity> entityList = new ArrayList<>();
                stAuthorities.setObject(1, userInfo.getId());
                ResultSet result = stAuthorities.executeQuery();
                while (result.next()) {
                    AuthorityEntity auInfo = new AuthorityEntity();
                    auInfo.setId((UUID) result.getObject("id"));
                    auInfo.setAuthority(Authority.valueOf(result.getString(3)));
                    entityList.add(auInfo);
                }
                userInfo.setAuthorities(entityList);
                return userInfo;
            } else {
                throw new IllegalArgumentException("Can`t find user by given username: " + user.getUsername());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int updateUser(UserEntity user) throws RuntimeException {
        int executeUpdate;

        try (Connection conn = ds.getConnection();
             PreparedStatement updateUserSt = conn.prepareStatement("UPDATE users SET username = ?, password = ?, enabled = ?, account_non_expired = ?," +
                     " account_non_locked = ?, credentials_non_expired = ? WHERE id = ?")) {
            updateUserSt.setString(1, user.getUsername());
            updateUserSt.setString(2, pe.encode(user.getPassword()));
            updateUserSt.setBoolean(3, user.getEnabled());
            updateUserSt.setBoolean(4, user.getCredentialsNonExpired());
            updateUserSt.setBoolean(5, user.getAccountNonLocked());
            updateUserSt.setBoolean(6, user.getCredentialsNonExpired());
            updateUserSt.setObject(7, user.getId());
            executeUpdate = updateUserSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }


    @Override
    public int removeUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement deleteUserSt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
                 PreparedStatement deleteAuthoritySt = conn.prepareStatement(
                         "DELETE FROM authorities WHERE user_id = ?")) {
                deleteUserSt.setObject(1, user.getId());
                deleteAuthoritySt.setObject(1, user.getId());

                deleteAuthoritySt.executeUpdate();
                executeUpdate = deleteUserSt.executeUpdate();

            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }
}