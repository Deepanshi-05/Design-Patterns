# Factory Method Design Pattern

## 1. What is the Factory Method Pattern?

The **Factory Method Design Pattern** is a creational design pattern that allows subclasses to decide which object should be created.

Instead of creating objects directly using `new`, we define a common method for object creation and let subclasses implement it.

It is useful when:
- We have multiple types of objects.
- The exact object type is decided at runtime.
- We want to avoid large `if-else` or `switch` statements.
- We want to add new object types without changing existing core logic.

**Simple example:** Imagine an application that sends notifications through Email, SMS, and WhatsApp. Instead of creating all notification objects inside one service, we let separate creator classes handle their creation.

---

## 2. Understanding the Problem

Imagine we are building a notification system.

Initially, we only support Email notifications.

```java
class EmailNotification {
    public void send() {
        System.out.println("Sending Email");
    }
}
```

Now, we want to support SMS and WhatsApp notifications too.

We might write:

```java
class NotificationService {

    public void send(String type) {

        if (type.equals("EMAIL")) {
            EmailNotification email = new EmailNotification();
            email.send();
        }
        else if (type.equals("SMS")) {
            SMSNotification sms = new SMSNotification();
            sms.send();
        }
        else if (type.equals("WHATSAPP")) {
            WhatsAppNotification whatsapp =
                    new WhatsAppNotification();
            whatsapp.send();
        }
    }
}
```

### Problems with this approach

1. **Too many conditions:** Every new notification type adds another `if-else` block.
2. **Tight coupling:** The service directly depends on concrete notification classes.
3. **Hard to maintain:** Changes to object creation require modifying the service.
4. **Difficult to extend:** Adding new notification types keeps changing existing code.

We need a better way to manage object creation.

---

## 3. Simple Factory: First Solution

Before understanding Factory Method, let's look at Simple Factory.

In Simple Factory, we move all object creation logic into a separate factory class.

```java
class NotificationFactory {

    public static Notification createNotification(String type) {

        if (type.equals("EMAIL")) {
            return new EmailNotification();
        }
        else if (type.equals("SMS")) {
            return new SMSNotification();
        }

        return null;
    }
}
```

Now the service uses the factory:

```java
Notification notification =
        NotificationFactory.createNotification("EMAIL");

notification.send();
```

This makes the service cleaner because it no longer creates notification objects directly.

**But there is still one problem:** Every time we add a new notification type, we must modify the factory's `if-else` logic.

This is where Factory Method helps.

---

## 4. What is Factory Method?

Factory Method takes object creation one step further.

Instead of having one factory decide which object to create, we create separate creator classes.

Each creator decides which specific object it will create.

For example:

- EmailNotificationCreator creates EmailNotification.
- SMSNotificationCreator creates SMSNotification.
- WhatsAppNotificationCreator creates WhatsAppNotification.

All creators follow the same parent class or interface.

### Real-world example

Imagine a food delivery application.

Different restaurants prepare different food:

- Pizza restaurant prepares pizza.
- Burger restaurant prepares burgers.
- Sushi restaurant prepares sushi.

The delivery platform follows a common process, but each restaurant decides what it prepares.

Similarly, in Factory Method, the parent Creator defines the common workflow, while subclasses decide which object to create.

---

## 5. Class Diagram

```text
             <<interface>>
               Product
             + operation()
                  ^
                  |
        -----------------------
        |                     |
   EmailProduct           SMSProduct
        ^                     ^
        |                     |
  EmailCreator             SMSCreator
        |                     |
        ------- Creator -------
              + factoryMethod()
              + someOperation()
```

A more accurate way to understand the relationships:

```text
        <<abstract>>
          Creator
             |
       factoryMethod()
             |
      -----------------
      |               |
 EmailCreator     SMSCreator
      |               |
      v               v
 EmailProduct     SMSProduct

      All products implement
          Product interface
```

### Main Components

| Component | Responsibility |
|---|---|
| Product | Common interface for all objects |
| Concrete Product | Actual object being created |
| Creator | Declares the factory method and common workflow |
| Concrete Creator | Overrides the factory method to create a specific object |
| Client | Uses the Creator |

---

## 6. Java Implementation: Notification System

Let's implement Factory Method step by step.

### Step 1: Create the Product Interface

All notification classes will follow this interface.

```java
interface Notification {
    void send(String message);
}
```

### Step 2: Create Concrete Products

Each class implements the same interface but provides its own behavior.

```java
class EmailNotification implements Notification {

    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
```

```java
class SMSNotification implements Notification {

    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
```

```java
class WhatsAppNotification implements Notification {

    public void send(String message) {
        System.out.println("WhatsApp: " + message);
    }
}
```

### Step 3: Create the Abstract Creator

The Creator declares the factory method.

It also contains a common method that uses the created notification.

```java
abstract class NotificationCreator {

    // Factory Method
    public abstract Notification createNotification();

    // Common workflow
    public void send(String message) {

        Notification notification = createNotification();

        notification.send(message);
    }
}
```

**Important:**

- `createNotification()` is the Factory Method.
- `send()` is the common workflow.
- The parent class does not know which notification will be created.
- Subclasses decide which notification object to return.

### Step 4: Create Concrete Creators

Each subclass overrides the factory method.

**Email Creator:**

```java
class EmailNotificationCreator extends NotificationCreator {

    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}
```

**SMS Creator:**

```java
class SMSNotificationCreator extends NotificationCreator {

    @Override
    public Notification createNotification() {
        return new SMSNotification();
    }
}
```

**WhatsApp Creator:**

```java
class WhatsAppNotificationCreator extends NotificationCreator {

    @Override
    public Notification createNotification() {
        return new WhatsAppNotification();
    }
}
```

### Step 5: Client Code

```java
public class Main {

    public static void main(String[] args) {

        NotificationCreator email =
                new EmailNotificationCreator();

        email.send("Welcome!");

        NotificationCreator sms =
                new SMSNotificationCreator();

        sms.send("Your OTP is 1234");

        NotificationCreator whatsapp =
                new WhatsAppNotificationCreator();

        whatsapp.send("Hello!");
    }
}
```

### Output

```text
Email: Welcome!
SMS: Your OTP is 1234
WhatsApp: Hello!
```

### How does it work?

1. Client creates an `EmailNotificationCreator`.
2. Client calls `send()`.
3. The parent Creator calls `createNotification()`.
4. Java executes the overridden method in `EmailNotificationCreator`.
5. It creates an `EmailNotification` object.
6. The parent Creator calls `send()` on that object.

**Remember:** Parent class defines the workflow; child classes decide which object gets created.

---

## 7. Adding a New Notification Type

Suppose we now want to support Push notifications.

We only need to create two new classes.

**Product:**

```java
class PushNotification implements Notification {

    public void send(String message) {
        System.out.println("Push: " + message);
    }
}
```

**Creator:**

```java
class PushNotificationCreator extends NotificationCreator {

    @Override
    public Notification createNotification() {
        return new PushNotification();
    }
}
```

We don't need to modify the existing Email, SMS, or WhatsApp creator classes.

This follows the **Open/Closed Principle**:

- Open for extension: We can add new notification types.
- Closed for modification: Existing classes do not need to change.

The client still needs to select the appropriate creator, but the existing creation workflow remains unchanged.

---

## 8. Practical Example: Document Export System

Imagine an application that exports reports in different formats:

- PDF
- HTML
- CSV

Each format has its own way of generating a document.

We can use Factory Method to manage this.

### Class Diagram

```text
          Document
        + export()
             ^
      ----------------
      |       |      |
     PDF    HTML    CSV

       ExportCreator
       + createDocument()
       + export()

             ^
      -----------------
      |       |       |
   PdfCreator HtmlCreator CsvCreator
```

### Java Implementation

**Step 1: Product Interface**

```java
interface Document {
    void export();
}
```

**Step 2: Concrete Products**

```java
class PdfDocument implements Document {

    public void export() {
        System.out.println("Exporting PDF");
    }
}
```

```java
class HtmlDocument implements Document {

    public void export() {
        System.out.println("Exporting HTML");
    }
}
```

```java
class CsvDocument implements Document {

    public void export() {
        System.out.println("Exporting CSV");
    }
}
```

**Step 3: Abstract Creator**

```java
abstract class ExportCreator {

    public abstract Document createDocument();

    public void export() {
        Document document = createDocument();
        document.export();
    }
}
```

**Step 4: Concrete Creators**

```java
class PdfCreator extends ExportCreator {

    public Document createDocument() {
        return new PdfDocument();
    }
}
```

```java
class HtmlCreator extends ExportCreator {

    public Document createDocument() {
        return new HtmlDocument();
    }
}
```

```java
class CsvCreator extends ExportCreator {

    public Document createDocument() {
        return new CsvDocument();
    }
}
```

**Step 5: Client**

```java
public class Main {

    public static void main(String[] args) {

        ExportCreator pdf = new PdfCreator();
        pdf.export();

        ExportCreator html = new HtmlCreator();
        html.export();

        ExportCreator csv = new CsvCreator();
        csv.export();
    }
}
```

**Output:**

```text
Exporting PDF
Exporting HTML
Exporting CSV
```

Each creator decides which document object to create, while the common export workflow stays in the parent class.

---

## 9. Advantages

- **Loose coupling:** Client code depends on common interfaces instead of concrete product classes.
- **Easy extension:** New product types can be added using new creator classes.
- **Better maintenance:** Object creation logic is separated from business logic.
- **Open/Closed Principle:** Existing creator and product classes can remain unchanged when adding new types.

## 10. Disadvantages

- **More classes:** Every new product type usually requires a new concrete product and creator.
- **More complexity:** For a small application with only one or two object types, this pattern may be unnecessary.
- **Client selection:** The client still needs to decide which Concrete Creator to use.

---

## 11. Important Interview Questions

**Q1. What is Factory Method Design Pattern?**

It is a creational pattern where a parent class defines a method for creating objects, but subclasses decide which specific object to create.

**Q2. What is the difference between Simple Factory and Factory Method?**

| Simple Factory | Factory Method |
|---|---|
| One factory class creates objects | Separate creator subclasses create objects |
| Usually uses `if-else` or `switch` | Uses method overriding |
| Adding a type requires changing the factory | Adding a type usually requires a new creator |
| Not a formal GoF pattern | A GoF design pattern |

**Q3. What is the Factory Method?**

It is the method declared in the Creator class and overridden by subclasses to create specific products.

**Q4. What is the role of the Creator?**

It declares the factory method and can contain common business logic that uses the created object.

**Q5. How does Factory Method follow the Open/Closed Principle?**

We can add new concrete products and creators without modifying the existing creation workflow.

**Q6. Is Factory Method a creational or structural pattern?**

It is a **Creational Design Pattern** because it deals with object creation.

---

## 12. Quick Revision

| Term | Meaning |
|---|---|
| Product | Common interface |
| Concrete Product | Actual object |
| Creator | Declares the factory method |
| Concrete Creator | Creates a specific product |
| Factory Method | Method responsible for object creation |
| Client | Uses the Creator |

**One-line summary:**

Factory Method = Parent defines object creation method + Child classes decide which object to create.

**Remember:** Use Factory Method when you have multiple product types and want to extend object creation without continuously modifying the existing creation logic.