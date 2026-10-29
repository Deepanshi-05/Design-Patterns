# Adapter Design Pattern

## 1. What is the Adapter Pattern?

The **Adapter Pattern** allows two classes to work together when their methods do not match.

Think of a travel plug adapter: your plug may not fit a wall socket, so the adapter connects them without changing the plug or the socket.

In Java, an adapter is a class that takes the methods our application expects and connects them to the methods an existing class provides.

## 2. Why Do We Need It?

Imagine your application expects a payment method called `processPayment()`, but an old payment library provides `executeTransaction()`.

- Your application already uses `processPayment()`.
- The old library works, but its method names and return values are different.
- You may not be able to change the old library because it belongs to another company.

Instead of rewriting your application or changing the library, create an **adapter** between them.

## 3. Java Example: Adapting an Old Payment System

### Step 1: The interface our application expects

This is the method shape that our application wants to use.

```java
interface PaymentProcessor {
    void processPayment(double amount);
    boolean isPaymentSuccessful();
    String getTransactionId();
}
```

### Step 2: The existing payment class

Suppose this class comes from an old system or a third-party library. Its methods are different, so we do not change it.

```java
class LegacyPaymentGateway {
    private long referenceNumber;
    private boolean successful;

    public void executeTransaction(double amount) {
        System.out.println("Processing payment: " + amount);
        referenceNumber = 12345L;
        successful = true;
    }

    public boolean checkStatus(long reference) {
        return successful && reference == referenceNumber;
    }

    public long getReferenceNumber() {
        return referenceNumber;
    }
}
```

Notice the differences:

| Our interface expects | Existing class provides |
|---|---|
| `processPayment(amount)` | `executeTransaction(amount)` |
| `isPaymentSuccessful()` | `checkStatus(reference)` |
| `getTransactionId()` returns `String` | `getReferenceNumber()` returns `long` |

The classes do similar work, but their methods do not match.

### Step 3: Create the adapter

The adapter implements the interface our application expects. Inside it, we call the existing class's methods.

```java
class PaymentAdapter implements PaymentProcessor {
    private final LegacyPaymentGateway gateway;

    public PaymentAdapter(LegacyPaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void processPayment(double amount) {
        gateway.executeTransaction(amount);
    }

    @Override
    public boolean isPaymentSuccessful() {
        long reference = gateway.getReferenceNumber();
        return gateway.checkStatus(reference);
    }

    @Override
    public String getTransactionId() {
        return "LEGACY_TXN_" + gateway.getReferenceNumber();
    }
}
```

What the adapter does:
- Changes the method call from `processPayment()` to `executeTransaction()`.
- Gets the reference number and passes it to `checkStatus()`.
- Converts the numeric reference into a `String` transaction ID.

### Step 4: Use it in the application

```java
public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor =
                new PaymentAdapter(new LegacyPaymentGateway());

        processor.processPayment(500.0);

        System.out.println("Successful: "
                + processor.isPaymentSuccessful());
        System.out.println("Transaction ID: "
                + processor.getTransactionId());
    }
}
```

**Output:**

```text
Processing payment: 500.0
Successful: true
Transaction ID: LEGACY_TXN_12345
```

### What happened?

The application calls only the methods in `PaymentProcessor`. The adapter translates those calls into methods understood by `LegacyPaymentGateway`.

The application does not need to know the old class's method names or details.

## 4. The Four Main Parts

| Part | Meaning | In our example |
|---|---|---|
| **Target** | The interface our application expects | `PaymentProcessor` |
| **Adaptee** | The existing class whose methods do not match | `LegacyPaymentGateway` |
| **Adapter** | Connects the two and translates method calls | `PaymentAdapter` |
| **Client** | The code that uses the interface | `Main` or a payment service |

A useful way to remember it: **Client → Target interface → Adapter → Existing class.**

## 5. How the Pattern Works

1. The client calls a method it already knows, such as `processPayment()`.
2. The adapter receives the call because it implements `PaymentProcessor`.
3. The adapter calls the matching method on the old class.
4. If needed, the adapter converts the result into the format the application expects.
5. The client receives the result through its familiar interface.

An adapter can translate method names, pass extra parameters, or convert return types. It does not have to do all of these; it depends on what is different.

## 6. Types of Adapter

### Object Adapter — commonly used in Java

The adapter **contains an object** of the existing class and calls its methods.

```java
class PaymentAdapter implements PaymentProcessor {
    private final LegacyPaymentGateway gateway;

    public PaymentAdapter(LegacyPaymentGateway gateway) {
        this.gateway = gateway;
    }

    // Translate interface calls here
}
```

This approach is flexible and is the one used in our example.

### Class Adapter

A class adapter uses inheritance to adapt a class. Java classes can extend only one class, so Java code commonly uses the object-adapter approach instead.

## 7. When Should You Use It?

Use the Adapter Pattern when:
- Connecting your application to an old system or a third-party library.
- The existing class does useful work, but its interface does not match yours.
- You want to reuse the existing code without modifying it.
- You want the rest of your application to keep using one familiar interface.

For example, a media player might use adapters to make different audio/video libraries work through the same `MediaPlayer` interface.

## 8. Important Points to Remember

- **An adapter connects mismatched interfaces.** It is not mainly for adding new business features.
- **It helps reuse existing code.** The old class can stay unchanged.
- **The client depends on the expected interface.** It does not need to call the old class directly.
- **The adapter may translate data too.** For example, it can turn a `long` reference number into a `String`.
- **Use it when there is a real mismatch.** Do not add an adapter when the classes already work together cleanly.

## Quick Revision

**In one sentence:** The Adapter Pattern acts as a translator, allowing code that expects one interface to work with a class that provides a different interface.

**Remember the roles:**
- Target = what the client expects
- Adaptee = existing class
- Adapter = translator
- Client = code using the target interface
