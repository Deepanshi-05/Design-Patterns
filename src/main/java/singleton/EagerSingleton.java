package singleton;

public class EagerSingleton {
    private static final EagerSingleton instance =
            new EagerSingleton();

    private EagerSingleton() {
        System.out.println("Object created");
    }

    public static EagerSingleton getInstance() {
        return instance;
    }
}
