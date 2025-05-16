package ru.jordosi.workingwithreflection;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Простой DI-контейнер, который внедряет зависимости в пол, поля,
 * помеченные аннотацией {@link AutoInjectable}
 *
 * <p>Зависимости настраиваются через Properties, где ключ - это
 * имя интерфейса, а значение - имя класса реализации</p>
 */
public class Injector {
    private final Properties properties;

    /**
     * Создает инжектор с указанными настройками зависимостей
     * @param properties свойства, где ключ - интерфейс, значение - реализация
     * @throws IllegalArgumentException если properties == null
     */
    public Injector(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties cannot be null");
        }
        this.properties = properties;
    }

    /**
     * Внедряет зависимости в переданный объект
     * @param object объект, в который внедряются зависимости
     * @throws IllegalStateException если:
     * <ul>
     *     <li>поле с @AutoInjectable не является интерфейсом</li>
     *     <li>реализация не найдена в properties</li>
     *     <li>класс реализации не может быть создан</li>
     * </ul>
     * @throws IllegalArgumentException если object==null
     */
    public <T> void inject(T object) throws Exception {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();

                if (!fieldType.isInterface()) {
                    throw new IllegalStateException("Поле с @AutoInjectable должно быть интерфейсом: " +
                            field.getName() + " в классе " + clazz.getName());
                }

                String implementationClassName = properties.getProperty(fieldType.getName());
                if (implementationClassName == null) {
                    throw new IllegalStateException("Не найдена реализация для интерфейса " +
                            fieldType.getName() + " в properties");
                }
                Class <?> implementationClass = Class.forName(implementationClassName);
                Object implementationInstance = implementationClass.getDeclaredConstructor().newInstance();

                field.setAccessible(true);
                field.set(object, implementationInstance);
            }
        }
    }
}
