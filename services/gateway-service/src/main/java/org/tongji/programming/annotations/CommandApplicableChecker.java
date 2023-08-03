package org.tongji.programming.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@FunctionalInterface
public interface CommandApplicableChecker {
    boolean handler(String command);
}
