package ru.jordosi.workingwithreflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для пометки полей, которые должны быть автоматически инициализрованы
 *
 * <p>Поле должно быть интерфейсом, реализация которого указана в Properties</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoInjectable {
}
