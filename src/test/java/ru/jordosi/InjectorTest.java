package ru.jordosi;

import org.junit.jupiter.api.Test;
import ru.jordosi.workingwithreflection.AutoInjectable;
import static org.junit.jupiter.api.Assertions.*;
import ru.jordosi.workingwithreflection.Injector;

import java.util.Properties;

/**
 * Юнит-тест для проеерки работоспособности инъекций
 */
public class InjectorTest {
    /**
     * Тестовый интерфейс
     */
    interface TestService {
        String greet();
    }

    /**
     * Реализация тестового интерфейса
     */
    public static class TestServiceImpl implements TestService {
        @Override
        public String greet() {
            return "Hello DI";
        }
    }

    /**
     * Класс для тестирования инъекции
     */
    static class Client {
        @AutoInjectable
        TestService testService;
    }

    /**
     * Тест инъекции зависимости
     */
    @Test
    void shouldInjectDependency() throws Exception {
        Properties props = new Properties();
        props.setProperty("ru.jordosi.InjectorTest$TestService", "ru.jordosi.InjectorTest$TestServiceImpl");

        Injector injector = new Injector(props);

        Client client = new Client();

        injector.inject(client);

        assertNotNull(client.testService);

        assertEquals("Hello DI", client.testService.greet());
    }

    /**
     * Тест вброса исключения, когда поле не является интерфейсом
     */
    @Test
    void shouldThrowIfFieldNotInterface() throws Exception {
        class InvalidClient {
            @AutoInjectable
            String notAnInterface;
        }

        Injector injector = new Injector(new Properties());
        InvalidClient invalidClient = new InvalidClient();
        assertThrows(IllegalStateException.class, () -> injector.inject(invalidClient));
    }

    /**
     * Тест вброса исключения, когда реализация не найдена
     */
    @Test
    void shouldThrowIfImplementationNotFound() throws Exception {
        Properties props = new Properties();
        Injector injector = new Injector(props);

        Client client = new Client();
        assertThrows(IllegalStateException.class, () -> injector.inject(client));
    }

    /**
     * Тест вброса исключения, когда объект равен null
     */
    @Test
    void shouldThrowIfObjectIsNull() throws Exception {
        Injector injector = new Injector(new Properties());
        assertThrows(NullPointerException.class, () -> injector.inject(null));
    }

    /**
     * Тест вброса исключения, когда properties==null
     */
    @Test
    void shouldThrowIfPropertiesAreNull() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new Injector(null));
    }
}
