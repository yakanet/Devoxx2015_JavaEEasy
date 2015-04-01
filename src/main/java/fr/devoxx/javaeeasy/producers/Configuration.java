package fr.devoxx.javaeeasy.producers;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface Configuration {
    @Nonbinding
    String value();

    @Nonbinding
    String otherwise() default "";
}
