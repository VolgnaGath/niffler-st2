package guru.qa.niffler.db.dao;

import java.util.*;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {

  private final TransactionTemplate transactionTemplate;
  private final JdbcTemplate jdbcTemplate;

  public NifflerUsersDAOSpringJdbc() {
    DataSourceTransactionManager transactionManager = new JdbcTransactionManager(
            DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));
    this.transactionTemplate = new TransactionTemplate(transactionManager);
    this.jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
  }

  @Override
  public int createUser(UserEntity user) {
    transactionTemplate.execute(st -> {
      SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
      Map<String, Object> newUser = new HashMap<>();
      newUser.put("username", user.getUsername());
      newUser.put("password", pe.encode(user.getPassword()));
      newUser.put("enabled", user.getEnabled());
      newUser.put("account_non_expired", user.getAccountNonExpired());
      newUser.put("account_non_locked", user.getAccountNonLocked());
      newUser.put("credentials_non_expired", user.getCredentialsNonExpired());

      KeyHolder keyHolder = simpleJdbcInsert.withTableName("users")
              .usingColumns("username", "password", "enabled", "account_non_expired", "account_non_locked", "credentials_non_expired")
              .usingGeneratedKeyColumns("id")
              .withoutTableColumnMetaDataAccess()
              .executeAndReturnKeyHolder(newUser);
      UUID id = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
      user.setId(id);

      user.getAuthorities().forEach(a ->
              jdbcTemplate.update("INSERT INTO authorities (user_id, authority) VALUES (?, ?)",
                      user.getId(), a.getAuthority().name()));
      return 1;
    });
    return 1;
  }

  @Override
  public String getUserId(String userName) {
    return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
            rs -> {
              return rs.getString(1);
            },
            userName
    );
  }

  @Override
  public int removeUser(UserEntity user) {
    transactionTemplate.execute(st -> {
      jdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
      return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
    });
    return 1;
  }

  @Override
  public int updateUser(UserEntity user) {
    return jdbcTemplate.update("UPDATE users SET username = ?, password = ?, enabled = ?, account_non_expired = ?," +
                    " account_non_locked = ?, credentials_non_expired = ? WHERE username = ?",
            user.getUsername(),
            user.getPassword(),
            user.getEnabled(),
            user.getAccountNonExpired(),
            user.getAccountNonLocked(),
            user.getCredentialsNonExpired(),
            user.getUsername());
  }

  @Override
  public UserEntity getUser(UserEntity user) {
    UserEntity userInfo = new UserEntity();
    return transactionTemplate.execute(st -> {
      jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
              rs -> {
                userInfo.setId((UUID) rs.getObject("id"));
                userInfo.setUsername(rs.getString("username"));
                userInfo.setPassword(rs.getString("password"));
                userInfo.setEnabled(rs.getBoolean("enabled"));
                userInfo.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                userInfo.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                userInfo.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
              },
              user.getUsername()
      );
      List<AuthorityEntity> entityList = new ArrayList<>();
      jdbcTemplate.query("SELECT * FROM authorities WHERE user_id= ?",
              rs -> {
                while (rs.next()) {
                  AuthorityEntity auInfo = new AuthorityEntity();
                  auInfo.setId((UUID) rs.getObject("id"));
                  auInfo.setAuthority(Authority.valueOf(rs.getString("authority")));
                  entityList.add(auInfo);
                }
              },
              userInfo.getId()
      );
      userInfo.setAuthorities(entityList);
      return userInfo;
    });
  }
}
