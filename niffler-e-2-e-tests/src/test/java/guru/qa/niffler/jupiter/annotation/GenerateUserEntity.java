package guru.qa.niffler.jupiter.annotation;


import guru.qa.niffler.db.dao.NifflerDaoType;
import guru.qa.niffler.jupiter.extension.GenerateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(GenerateUserExtension.class)
public @interface GenerateUserEntity {

    NifflerDaoType userDao() default NifflerDaoType.HIBERNATE;

    String username() default "";


    String password() default "";

    boolean enabled() default true;


    boolean accountNonExpired() default true;

    boolean accountNonLocked() default true;

    boolean credentialsNonExpired() default true;



}
