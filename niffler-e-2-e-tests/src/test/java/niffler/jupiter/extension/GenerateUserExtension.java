package niffler.jupiter.extension;

import com.github.javafaker.Faker;
import io.qameta.allure.AllureId;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.GenerateUserEntity;
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
    private NifflerUsersDAO usersDAO;


    private static final String TEST_PWD = "12345";

    @Override
    public void beforeEach(ExtensionContext context) {

        List<UserEntity> userEntityList = new ArrayList<>();
        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> p.isAnnotationPresent(GenerateUserEntity.class))
                .filter(p -> p.getType().isAssignableFrom(UserEntity.class))
                .toList();
        for (Parameter parameter : parameters) {

            GenerateUserEntity annotation = parameter.getAnnotation(GenerateUserEntity.class);
            usersDAO = annotation.userDao().getDao();
            UserEntity user = new UserEntity();
            user.setUsername("".equals(annotation.username()) ? faker.name().username() : annotation.username());
            user.setPassword("".equals(annotation.password()) ? TEST_PWD : annotation.password());
            user.setEnabled(annotation.enabled());
            user.setAccountNonExpired(annotation.accountNonExpired());
            user.setAccountNonLocked(annotation.accountNonLocked());
            user.setCredentialsNonExpired(annotation.credentialsNonExpired());
            user.setAuthorities(Arrays.stream(Authority.values()).map(
                    a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        ae.setUser(user);
                        return ae;
                    }
            ).toList());
            usersDAO.createUser(user);
            userEntityList.add(user);
        }
        context.getStore(GENERATE_USER_NAMESPACE).put(getTestId(context), userEntityList);
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
