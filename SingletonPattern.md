# LLD Notes: Singleton Design Pattern

## 1. What is Singleton?

**Singleton is a creational design pattern that ensures a class has only one instance and provides a common access point to it.**

**Real-world analogy:** Imagine an office where all employees send print jobs through one print manager instead of creating a separate manager for every employee.

**Remember:** One shared instance, multiple users.

## 2. Three Important Components

- **Private constructor:** Prevents other classes from creating objects directly using `new`.
- **Static instance:** Stores the shared object at the class level.
- **Static `getInstance()` method:** Gives callers access to the shared object.

---

## 3. Types of Singleton Implementation

### A. Eager Initialization

The object is created as soon as the class is initialized, even if nobody has requested it yet.

**Real-world analogy:** A shop prepares its opening counter before customers arrive.

```java
class EagerSingleton {

    private static final EagerSingleton INSTANCE =
            new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}
```

**How it works:**
- The instance is created during class initialization.
- Every call to `getInstance()` returns the same instance.
- Java's class initialization mechanism makes this approach thread-safe.

**Advantages:** Simple and thread-safe.

**Disadvantage:** The object is created even if it is never used.

**Use when:** The object is inexpensive to create and will almost certainly be needed.

### B. Lazy Initialization (Not Thread-Safe)

The object is created only when `getInstance()` is called for the first time.

**Real-world analogy:** A restaurant prepares a special dish only after someone orders it.

```java
class LazySingleton {

    private static LazySingleton instance;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }

        return instance;
    }
}
```

**How it works:**
1. Initially, `instance` is `null`.
2. The first call creates the object.
3. Later calls return the existing object.

**Advantage:** Avoids creating an unused object.

**Disadvantage:** Not thread-safe. Two threads can both see `instance == null` and create separate objects.

**Use when:** You understand the initialization trade-off, but don't use this implementation as-is in a multithreaded application.

### C. Thread-Safe Lazy Initialization Using `synchronized`

We can fix the previous implementation by allowing only one thread at a time to execute the method.

```java
class ThreadSafeSingleton {

    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {}

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }

        return instance;
    }
}
```

**How it works:**
- A thread acquires the object's class-level lock before executing the static synchronized method.
- Other threads must wait until the lock is released.
- Only one thread can perform the initialization at a time.

**Advantage:** Lazy initialization with thread safety.

**Disadvantage:** Every call acquires the lock, even after the object exists.

**Use when:** You want a simple synchronized implementation and the extra locking overhead is acceptable.

### D. Double-Checked Locking

This approach reduces synchronization overhead by checking the instance twice.

```java
class DoubleCheckedSingleton {

    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {}

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }

        return instance;
    }
}
```

**Why two checks?**

- **First check:** If the object already exists, return it without locking.
- **Second check:** Once a thread obtains the lock, check again because another thread may have created the object while it was waiting.

**Why `volatile`?**

It ensures that threads see the properly initialized instance and prevents unsafe reordering of its initialization.

**Advantages:** Lazy initialization, thread safety, and no locking on the normal path after initialization.

**Disadvantage:** More complicated than other implementations and easier to get wrong.

### E. Bill Pugh Singleton (Initialization-on-Demand Holder)

This approach uses a static inner class to create the instance only when it is first requested.

```java
class BillPughSingleton {

    private BillPughSingleton() {}

    private static class Holder {
        private static final BillPughSingleton INSTANCE =
                new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

**How it works:**
- The `Holder` class is not initialized until `getInstance()` accesses it.
- Java initializes the holder's static field safely.
- No explicit `synchronized` or `volatile` is required.

**Advantages:** Lazy initialization, thread safety, and good performance.

**Disadvantage:** Uses an extra inner class, so the implementation may initially look unfamiliar.

**Remember:** The holder class is the key to this approach.

### F. Enum Singleton

Java enums provide a concise way to implement a Singleton.

```java
enum EnumSingleton {
    INSTANCE;

    public void showMessage() {
        System.out.println("Shared instance");
    }
}
```

Usage:

```java
EnumSingleton s1 = EnumSingleton.INSTANCE;
EnumSingleton s2 = EnumSingleton.INSTANCE;

System.out.println(s1 == s2); // true

s1.showMessage();
```

**Why is it useful?**

- Enum constants are initialized safely by the JVM.
- Serialization preserves the enum constant's identity.
- Reflection cannot create additional enum instances through ordinary reflective constructor invocation.

**Limitation:** An enum cannot extend another class.

**Remember:** For a straightforward Java Singleton, Enum is one of the safest options. Bill Pugh is also a strong choice when you want lazy initialization using a normal class.

---

## 4. Quick Comparison

| Implementation | Lazy? | Thread-safe? |
|---|---|---|
| Eager initialization | No | Yes |
| Basic lazy initialization | Yes | No |
| Synchronized method | Yes | Yes |
| Double-checked locking | Yes | Yes, with `volatile` |
| Bill Pugh holder | Yes | Yes |
| Enum Singleton | On enum initialization | Yes |

## 5. Verify That Both References Are the Same

```java
EagerSingleton s1 = EagerSingleton.getInstance();
EagerSingleton s2 = EagerSingleton.getInstance();

System.out.println(s1 == s2); // true
```

The `==` operator checks whether both variables refer to the same object.

Compare this with:

```java
MyClass a = new MyClass();
MyClass b = new MyClass();
```

If `MyClass` permits public construction, these statements create two separate objects. A correctly implemented Singleton prevents ordinary callers from doing this.

## 6. Advantages and Disadvantages

**Advantages**
- Provides one shared instance within its intended scope.
- Avoids unnecessary duplicate objects.
- Gives a common access point to a shared resource.

**Disadvantages**
- Introduces global state.
- Can make unit testing harder.
- Can tightly couple classes to the Singleton.
- Requires care when multiple threads access the instance.

## 7. Important LLD and Spring Boot Note

In Spring Boot, a bean is singleton-scoped by default **within a particular Spring application context**.

```java
@Service
class TaskService {
    // Spring manages the bean instance
}
```

You generally don't need to manually implement the Singleton pattern for ordinary Spring-managed services.

Also, one Java Singleton instance does not automatically mean one instance across multiple JVMs or servers.

## 8. Interview Revision

- **Eager:** Create early.
- **Lazy:** Create when first requested.
- **Synchronized:** Simple thread-safe lazy creation.
- **Double-checked locking:** Avoid locking after initialization; requires `volatile`.
- **Bill Pugh:** Lazy and thread-safe through an inner holder class.
- **Enum:** Concise, robust Java-specific Singleton.

**Final memory trick:** Eager means *ready beforehand*; lazy means *created when needed*. Thread safety ensures concurrent requests don't accidentally create multiple instances.
