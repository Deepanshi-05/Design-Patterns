# LLD: Introduction to Design Patterns

## 1. What Are Design Patterns?

A **design pattern** is a reusable, proven approach to solving a commonly occurring software design problem.

- It is **not ready-made code** that we copy and paste.
- It is a blueprint that we adapt to our requirements.
- It helps us write code that is easier to maintain, understand, and extend.

**Real-world analogy:** When assembling furniture, we follow a proven set of instructions instead of figuring out every step from scratch. Design patterns provide similar guidelines for designing software.

### Simple Java Example

Suppose an application supports different payment methods.

Without a good design, we might keep adding conditions:

```java
if (paymentType.equals("UPI")) {
    // Process UPI payment
} else if (paymentType.equals("CARD")) {
    // Process card payment
} else if (paymentType.equals("CASH")) {
    // Process cash payment
}
```

As payment methods increase, this code becomes harder to maintain.

A design pattern such as **Strategy** can help us separate each payment method into its own implementation.

The key idea: design patterns help us organize code when recurring design problems arise.

---

## 2. Why Do We Use Design Patterns?

### 1. Reusability
Reuse proven design approaches instead of reinventing solutions.

**Example:** Use the Builder pattern to construct objects with many optional fields.

### 2. Maintainability
Keep responsibilities separated so changes are easier to make.

**Example:** Separate email notification logic from task management logic.

### 3. Readability
Give familiar names to common design structures, making code easier for developers to understand.

**Example:** A class named `PaymentStrategy` suggests that it represents an interchangeable payment algorithm.

### 4. Flexibility
Make it easier to introduce new features without unnecessarily modifying existing code.

**Example:** Add a new payment method without rewriting the entire payment-processing service.

**Remember:** Design patterns are useful when they solve a real problem. Using them everywhere can make code unnecessarily complicated.

---

## 3. History of Design Patterns

- **1977:** Christopher Alexander introduced the idea of patterns in architecture through *A Pattern Language*.
- **1994:** Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides published *Design Patterns: Elements of Reusable Object-Oriented Software*.
- These four authors are known as the **Gang of Four (GoF)**.
- Their book introduced **23 classic design patterns**.

The patterns are not limited to Java. Their underlying ideas apply to many programming languages.

---

## 4. Three Categories of Design Patterns

The 23 classic GoF patterns are divided into three categories.

| Category | Main purpose | Simple question |
|---|---|---|
| Creational | Object creation | How should objects be created? |
| Structural | Object and class composition | How should objects fit together? |
| Behavioral | Communication and responsibilities | How should objects interact? |

### A. Creational Patterns

These patterns deal with creating objects while controlling or simplifying the creation process.

| Pattern | Purpose | Example / Analogy |
|---|---|---|
| Singleton | Ensure one instance of a class | One shared configuration manager |
| Factory Method | Let subclasses decide which object to create | A notification creator choosing an email or SMS notification |
| Abstract Factory | Create families of related objects | Creating matching Windows or macOS UI components |
| Builder | Construct complex objects step by step | Building a burger with optional ingredients |
| Prototype | Create objects by copying existing ones | Duplicating a configured document template |

**Easy way to remember:** Creational patterns focus on *how objects come into existence*.

### B. Structural Patterns

These patterns focus on how classes and objects are connected to build larger systems.

| Pattern | Purpose | Example / Analogy |
|---|---|---|
| Adapter | Make incompatible interfaces work together | A travel plug adapter |
| Bridge | Separate an abstraction from its implementation | A remote control working with different devices |
| Composite | Treat individual objects and groups uniformly | Files and folders |
| Decorator | Add responsibilities without changing the original class | Adding toppings to a pizza |
| Facade | Provide a simple interface to a complex subsystem | One dashboard controlling multiple services |
| Flyweight | Share common data to reduce memory usage | Sharing character formatting data in a text editor |
| Proxy | Control access to another object | A security guard controlling access to a building |

**Easy way to remember:** Structural patterns focus on *how objects fit together*.

### C. Behavioral Patterns

These patterns focus on communication between objects and how responsibilities or algorithms are organized.

| Pattern | Purpose | Example / Analogy |
|---|---|---|
| Chain of Responsibility | Pass a request through handlers | A support ticket moving through support levels |
| Command | Represent a request as an object | An undoable editor action |
| Interpreter | Evaluate expressions using defined grammar | A calculator evaluating an expression |
| Iterator | Traverse a collection without exposing its internals | Moving through a playlist |
| Mediator | Centralize communication between objects | A chat room coordinating messages |
| Memento | Save and restore an object's state | Restoring a previous document version |
| Observer | Notify subscribers when something changes | Subscribers receiving channel updates |
| State | Change behavior according to the current state | An order behaving differently when pending or shipped |
| Strategy | Switch between interchangeable algorithms | Choosing UPI, card, or cash payment processing |
| Template Method | Define the steps of an algorithm while allowing some steps to vary | A standard process with customizable steps |
| Visitor | Add operations to object structures without changing their classes | Running different reports on document elements |

**Easy way to remember:** Behavioral patterns focus on *how objects communicate and behave*.

---

## 5. Understanding One Pattern: Strategy

Let's understand how a design pattern improves code through a simple example.

### Problem

An application supports UPI and card payments. We want to add new payment methods without repeatedly modifying the main payment-processing logic.

### Solution

Define a common interface and provide separate implementations.

```java
interface PaymentStrategy {
    void pay(double amount);
}
```

Implement UPI payment:

```java
class UpiPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Paid using UPI: " + amount);
    }
}
```

Implement card payment:

```java
class CardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Paid using Card: " + amount);
    }
}
```

Use the selected strategy:

```java
class PaymentService {

    private final PaymentStrategy strategy;

    PaymentService(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    void processPayment(double amount) {
        strategy.pay(amount);
    }
}
```

Run the example:

```java
public class Main {

    public static void main(String[] args) {
        PaymentStrategy strategy = new UpiPayment();

        PaymentService service = new PaymentService(strategy);
        service.processPayment(500);
    }
}
```

Output:

```text
Paid using UPI: 500.0
```

### What did we achieve?

- Each payment method has its own implementation.
- `PaymentService` does not need to know the internal details of UPI or card payments.
- A new payment method can be added by implementing `PaymentStrategy`.

This is the **Strategy pattern**: define interchangeable algorithms behind a common interface.

---

## 6. Are Design Patterns Always Necessary?

No. Design patterns are tools, not compulsory rules.

Common criticisms include:

- **Over-engineering:** Adding too many interfaces and classes for a simple problem.
- **Unnecessary abstraction:** A straightforward solution may be easier to understand.
- **Language features:** Modern languages and frameworks already provide some pattern-like functionality.

### Best practices

1. Understand the problem before choosing a pattern.
2. Prefer the simplest design that meets your requirements.
3. Use patterns when they improve flexibility, maintainability, or clarity.
4. Avoid adding complexity just to say that you used a design pattern.

For example, a small program that only sends one kind of notification may not need a complicated factory hierarchy.

---

## 7. Quick Revision for Interviews

| Question | Short answer |
|---|---|
| What is a design pattern? | A reusable approach to a recurring software design problem. |
| Are design patterns complete code solutions? | No, they are adaptable design blueprints. |
| Who are the Gang of Four? | Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides. |
| How many classic GoF patterns are there? | 23. |
| What are the three categories? | Creational, Structural, and Behavioral. |
| Which pattern focuses on object creation? | Creational patterns. |
| Which pattern helps incompatible interfaces work together? | Adapter. |
| Which pattern adds behavior without modifying the original object? | Decorator. |
| Which pattern notifies subscribers about changes? | Observer. |
| Which pattern supports interchangeable algorithms? | Strategy. |
| Should every project use design patterns? | No. Use them when they solve an actual design problem. |

## Final Takeaway

**Design patterns provide a shared vocabulary and reusable approaches for solving recurring software design problems.**

For LLD, learn each pattern in this order:

1. Understand the problem.
2. Identify why a straightforward solution becomes difficult to maintain.
3. Understand the pattern's structure and responsibilities.
4. Implement it in Java.
5. Learn when to use it and when not to use it.
