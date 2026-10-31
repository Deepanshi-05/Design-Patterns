# Facade Design Pattern

## 1. What is the Facade Design Pattern?

The **Facade Design Pattern** is a structural design pattern that provides a **simple interface to a complex system**.

It hides the internal complexity of multiple classes and provides one common entry point to perform a task.

In simple words:

Instead of calling multiple classes and their methods separately, we create one class (Facade) that handles all those operations internally.

### Real-Life Example: Hotel Reception

Imagine you are staying in a hotel.

You need:
- Room cleaning
- Food service
- Laundry service

Without a facade, you would have to contact each department separately.

But with a hotel reception (Facade), you make one request, and the reception coordinates with all the departments.

**Hotel Reception = Facade**

**Hotel Departments = Subsystems**

**You = Client**

---

## 2. Why Do We Need the Facade Pattern?

Suppose an application contains multiple classes that work together to complete one task.

Without a facade, the client must:
- Know about every class.
- Call multiple methods.
- Remember the correct order of execution.
- Handle errors from different classes.

This makes the code complicated and difficult to maintain.

### Problems Without Facade

1. **High Complexity:** Client code becomes lengthy because it handles multiple operations.
2. **Tight Coupling:** Client directly depends on many classes.
3. **Poor Maintainability:** Changes in subsystem classes may require changes in multiple clients.
4. **Difficult Testing:** We need to test and mock multiple dependencies.

**Solution:** Introduce a Facade class that handles these operations internally.

---

## 3. How Does the Facade Pattern Work?

There are three main parts:

| Component | Responsibility |
|---|---|
| Client | Requests an operation. |
| Facade | Coordinates different subsystem classes. |
| Subsystems | Perform the actual work. |

### Without Facade

```text
          Client
            |
     -----------------
     |       |       |
  Class A  Class B  Class C
```

The client communicates with every class directly.

### With Facade

```text
          Client
            |
          Facade
            |
     -----------------
     |       |       |
  Class A  Class B  Class C
```

Now the client communicates only with the Facade.

**Important:** The Facade does not perform all the actual work itself. It delegates the work to the appropriate subsystem classes.

---

## 4. Java Example: Home Theater System

Imagine we have a home theater system.

To watch a movie, we need to:

1. Turn on the projector.
2. Turn on the amplifier.
3. Turn on the DVD player.
4. Start the movie.

Each operation is handled by a different class.

### Step 1: Create Subsystem Classes

**Projector.java**

```java
public class Projector {

    public void on() {
        System.out.println("Projector is ON");
    }
}
```

**Amplifier.java**

```java
public class Amplifier {

    public void on() {
        System.out.println("Amplifier is ON");
    }
}
```

**DVDPlayer.java**

```java
public class DVDPlayer {

    public void play() {
        System.out.println("Movie started");
    }
}
```

These classes perform their own specific tasks.

### Step 2: Create Facade Class

**HomeTheaterFacade.java**

```java
public class HomeTheaterFacade {

    private Projector projector;
    private Amplifier amplifier;
    private DVDPlayer dvdPlayer;

    public HomeTheaterFacade(
        Projector projector,
        Amplifier amplifier,
        DVDPlayer dvdPlayer
    ) {
        this.projector = projector;
        this.amplifier = amplifier;
        this.dvdPlayer = dvdPlayer;
    }

    public void watchMovie() {

        projector.on();

        amplifier.on();

        dvdPlayer.play();
    }
}
```

**What is happening here?**

- The Facade contains references to all subsystem objects.
- It calls their methods in the correct order.
- The client does not need to know the internal steps.

### Step 3: Client Class

**Main.java**

```java
public class Main {

    public static void main(String[] args) {

        Projector projector = new Projector();

        Amplifier amplifier = new Amplifier();

        DVDPlayer dvdPlayer = new DVDPlayer();

        HomeTheaterFacade homeTheater =
            new HomeTheaterFacade(
                projector,
                amplifier,
                dvdPlayer
            );

        homeTheater.watchMovie();
    }
}
```

### Output

```text
Projector is ON
Amplifier is ON
Movie started
```

### Understand the Difference

**Without Facade:**

The client would have to write:

```java
projector.on();
amplifier.on();
dvdPlayer.play();
```

**With Facade:**

The client only writes:

```java
homeTheater.watchMovie();
```

That's the main idea of the Facade Pattern!

---

## 5. Advantages of Facade Pattern

1. **Simple Code:** Client interacts with one class instead of multiple classes.
2. **Loose Coupling:** Client does not directly depend on all subsystem classes.
3. **Easy Maintenance:** Internal implementation can change without affecting the client, as long as the Facade interface remains compatible.
4. **Code Reusability:** Multiple clients can use the same Facade.
5. **Better Readability:** Complex operations are hidden behind meaningful methods.

---

## 6. Disadvantages of Facade Pattern

1. **Extra Class:** We need to create and maintain an additional Facade class.
2. **Facade Can Become Large:** If too many responsibilities are added, the Facade itself can become complicated.
3. **Less Direct Control:** Clients using only the Facade may not have direct access to every subsystem operation.

---

## 7. When Should We Use Facade Pattern?

Use it when:

- A system contains multiple classes that work together.
- A client needs a simple way to perform a complex operation.
- You want to hide internal implementation details.
- You want to reduce direct dependencies between the client and subsystems.

**Example:** A payment system where one operation needs to validate payment, process the transaction, and generate a receipt.

A `PaymentFacade` could provide one method:

```java
paymentFacade.makePayment();
```

Internally, it coordinates all three operations.

---

## 8. Important Points to Remember

- Facade is a **Structural Design Pattern**.
- It provides a **single, simplified interface** to a complex subsystem.
- It hides complexity; it does not remove the subsystem classes.
- The Facade coordinates the work but delegates actual operations to other classes.
- The client communicates with the Facade instead of directly communicating with multiple subsystems.
- Subsystem classes can still be used independently if needed.
- The Facade reduces coupling and improves maintainability.

### One-Line Interview Definition

**Facade Design Pattern provides a simplified interface to a complex subsystem by hiding its internal operations and allowing clients to interact through a single entry point.**

---

## 9. Quick Revision

| Question | Answer |
|---|---|
| Pattern Type? | Structural |
| Main Purpose? | Simplify complex systems |
| Main Component? | Facade class |
| What does Facade do? | Coordinates subsystem classes |
| Does it perform all operations itself? | No, it delegates them |
| Main Benefit? | Reduces complexity and coupling |
| Real-Life Example? | Hotel reception or Home Theater |

**Remember:** Facade means *one simple entry point for many complex operations*.