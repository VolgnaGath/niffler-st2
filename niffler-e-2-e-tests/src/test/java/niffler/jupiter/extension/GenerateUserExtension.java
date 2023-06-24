package niffler.jupiter.extension;

import com.github.javafaker.Faker;
import io.qameta.allure.AllureId;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.GenerateRandomUserEntity;
import org.junit.jupiter.api.extension.*;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class GenerateUserExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {
    public static ExtensionContext.Namespace GENERATE_USER_NAMESPACE = ExtensionContext.Namespace.create(GenerateUserExtension.class);

    private static Faker faker = new Faker();
    private NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();


    private static final String TEST_PWD = "12345";

    @Override
    public void beforeEach(ExtensionContext context) {
        final String testId = getTestId(context);
        Parameter[] testParameters = context.getRequiredTestMethod().getParameters();
        List<UserEntity> userEntityList = new ArrayList<>();
        for (Parameter parameter : testParameters) {
            GenerateRandomUserEntity randomUserEntity = parameter.getAnnotation(GenerateRandomUserEntity.class);
            UserEntity user = new UserEntity();
            if(randomUserEntity != null) {
                if(Objects.equals(randomUserEntity.username(), "")) {
                    user.setUsername(faker.name().username());
                } else {
                    user.setUsername(randomUserEntity.username());
                }
                if (Objects.equals(randomUserEntity.password(), "")) {
                    user.setPassword(TEST_PWD);
                } else {
                    user.setPassword(randomUserEntity.password());
                }
                user.setEnabled(randomUserEntity.enabled());
                user.setAccountNonExpired(randomUserEntity.accountNonExpired());
                user.setAccountNonLocked(randomUserEntity.accountNonLocked());
                user.setCredentialsNonExpired(randomUserEntity.credentialsNonExpired());
                user.setAuthorities(Arrays.stream(Authority.values()).map(
                        a -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setAuthority(a);
                            ae.setUser(user);
                            return ae;
                        }
                ).toList());
            }
            userEntityList.add(user);
            usersDAO.createUser(user);
        }
        context.getStore(GENERATE_USER_NAMESPACE).put(testId, userEntityList);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void afterEach(ExtensionContext context) {
        final String testId = getTestId(context);
        List<UserEntity> users = (List<UserEntity>) context.getStore(GENERATE_USER_NAMESPACE)
                .get(testId);

        for (UserEntity user : users) {
            usersDAO.removeUser(user);
        }
    }



    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserEntity.class);
    }
    @SuppressWarnings("unchecked")
    @Override
    public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        List<UserEntity> users = (List<UserEntity>) extensionContext.getStore(GENERATE_USER_NAMESPACE)
                .get(testId);

        return users.get(parameterContext.getIndex());
    }
    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}
