
package singleton;

public class Main {
    public static void main(String[] args) {

        System.out.println("First call");
        LazySingleton obj1 = LazySingleton.getInstance();

        System.out.println("Second call");
        LazySingleton obj2 = LazySingleton.getInstance();

        System.out.println(obj1 == obj2);
    }
}
