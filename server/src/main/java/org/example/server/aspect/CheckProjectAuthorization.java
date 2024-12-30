package org.example.server.aspect;

import org.example.server.model.RoleEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckProjectAuthorization {
    RoleEnum[] roles() default {};
    boolean isNeedWriteAccess() default false;
}
