# Abstract Factory Design Pattern

## 1. What is the Abstract Factory Pattern?

The **Abstract Factory Design Pattern** is a creational design pattern used to create families of related objects without specifying their concrete classes.

In simple words, it provides a factory that creates multiple related objects that are designed to work together.

It is useful when:
- We have multiple families of related objects.
- Objects belonging to the same family should work together.
- We need to support different platforms, themes, or configurations.
- We want to avoid creating objects directly using concrete classes.

**Simple example:** Imagine a desktop application that supports Windows and macOS.

Both platforms have their own:
- Button
- Checkbox
- Text field

A Windows application should use Windows buttons and Windows checkboxes. Similarly, a macOS application should use macOS components.

Abstract Factory helps us create the correct family of objects.

---

## 2. Understanding the Problem

Imagine we are building a cross-platform application.

We have two platforms:

- Windows
- macOS

Each platform has its own Button and Checkbox classes.

We might write:

```java
if (platform.equals("WINDOWS")) {
    button = new WindowsButton();
    checkbox = new WindowsCheckbox();
}
else if (platform.equals("MAC")) {
    button = new MacButton();
    checkbox = new MacCheckbox();
}
```

### Problems with this approach

1. **Too many conditions:** Every new platform adds more conditions.
2. **Tight coupling:** Client code directly depends on concrete classes.
3. **Mixing objects:** We might accidentally use a Windows button with a macOS checkbox.
4. **Difficult to maintain:** Adding new components requires modifying existing code.

**What we need:** A common factory that creates a complete family of related objects.

---

## 3. What is Abstract Factory?

Abstract Factory provides a common interface for creating multiple related objects.

Instead of creating each object separately, we create a factory for each family.

For example:

- `WindowsFactory` creates WindowsButton and WindowsCheckbox.
- `MacFactory` creates MacButton and MacCheckbox.

The client only talks to the factory interface. It does not need to know which concrete classes are being created.

### Class Diagram

```text
              Client
                 |
                 v
          AbstractFactory
          /             \
         v               v
   WindowsFactory     MacFactory
       |                  |
   -----------        -----------
   |         |        |         |
 Windows   Windows   Mac      Mac
 Button   Checkbox   Button  Checkbox
```

### Main Components

| Component | Responsibility |
|---|---|
| Abstract Factory | Defines methods to create related objects |
| Concrete Factory | Creates objects belonging to one family |
| Abstract Product | Common interface for a product type |
| Concrete Product | Actual implementation of a product |
| Client | Uses the factory and product interfaces |

---

## 4. Difference Between Factory Method and Abstract Factory

This is an important interview question.

| Factory Method | Abstract Factory |
|---|---|
| Creates one type of product through a factory method | Creates a family of related products |
| Uses creator subclasses | Uses factory implementations |
| Example: Email or SMS notification | Example: Windows button and Windows checkbox |
| Focuses on creating one product at a time | Focuses on creating compatible products together |

**Remember:**

- Factory Method = Create a particular type of object.
- Abstract Factory = Create a family of related objects.

Abstract Factory can also use Factory Methods internally to create its products.

---

## 5. Java Implementation: Windows and macOS UI

Let's implement the pattern step by step.

### Step 1: Create Abstract Product Interfaces

First, define common interfaces for all buttons and checkboxes.

**Button interface:**

```java
interface Button {
    void paint();
}
```

**Checkbox interface:**

```java
interface Checkbox {
    void check();
}
```

Both Windows and macOS components will implement these interfaces.

### Step 2: Create Concrete Products

**Windows components:**

```java
class WindowsButton implements Button {

    public void paint() {
        System.out.println("Windows Button");
    }
}

class WindowsCheckbox implements Checkbox {

    public void check() {
        System.out.println("Windows Checkbox");
    }
}
```

**macOS components:**

```java
class MacButton implements Button {

    public void paint() {
        System.out.println("Mac Button");
    }
}

class MacCheckbox implements Checkbox {

    public void check() {
        System.out.println("Mac Checkbox");
    }
}
```

### Step 3: Create the Abstract Factory

This interface defines methods for creating both products.

```java
interface GUIFactory {

    Button createButton();

    Checkbox createCheckbox();
}
```

Any factory implementing this interface must create both a Button and a Checkbox.

### Step 4: Create Concrete Factories

**Windows Factory:**

```java
class WindowsFactory implements GUIFactory {

    public Button createButton() {
        return new WindowsButton();
    }

    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}
```

**macOS Factory:**

```java
class MacFactory implements GUIFactory {

    public Button createButton() {
        return new MacButton();
    }

    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}
```

Notice that each factory creates objects belonging to its own platform.

### Step 5: Create the Client

The client receives a factory and uses it to create UI components.

```java
class Application {

    private Button button;
    private Checkbox checkbox;

    public Application(GUIFactory factory) {

        button = factory.createButton();

        checkbox = factory.createCheckbox();
    }

    public void render() {

        button.paint();

        checkbox.check();
    }
}
```

The Application does not know whether it is using Windows or macOS components.

It only knows the `Button` and `Checkbox` interfaces.

### Step 6: Main Class

```java
public class Main {

    public static void main(String[] args) {

        GUIFactory factory = new WindowsFactory();

        Application app = new Application(factory);

        app.render();
    }
}
```

**Output:**

```text
Windows Button
Windows Checkbox
```

If we change:

```java
GUIFactory factory = new MacFactory();
```

The output becomes:

```text
Mac Button
Mac Checkbox
```

We changed the factory, but we did not change the Application class.

### How does it work?

1. Client selects a factory.
2. Factory creates related objects.
3. Application receives those objects through common interfaces.
4. Application uses the objects without knowing their concrete classes.

**Important:** The factory ensures that all products created through it belong to the same family.

---

## 6. Practical Example: Notification System

Imagine a notification system supporting Email and SMS.

Each notification channel needs two related objects:

- Message: Creates the notification message.
- Sender: Sends the message.

An Email message should work with an Email sender. Similarly, an SMS message should work with an SMS sender.

### Class Diagram

```text
               NotificationFactory
                  /          \
                 /            \
           EmailFactory     SMSFactory
              |                |
          ---------         ---------
          |       |         |       |
        Email   Email      SMS     SMS
        Message Sender    Message  Sender
```

### Java Implementation

**Step 1: Abstract Product Interfaces**

```java
interface Message {
    String createMessage();
}

interface Sender {
    void send(String message);
}
```

**Step 2: Concrete Products**

```java
class EmailMessage implements Message {

    public String createMessage() {
        return "Email notification";
    }
}

class EmailSender implements Sender {

    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}
```

```java
class SMSMessage implements Message {

    public String createMessage() {
        return "SMS notification";
    }
}

class SMSSender implements Sender {

    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```

**Step 3: Abstract Factory**

```java
interface NotificationFactory {

    Message createMessage();

    Sender createSender();
}
```

**Step 4: Concrete Factories**

```java
class EmailFactory implements NotificationFactory {

    public Message createMessage() {
        return new EmailMessage();
    }

    public Sender createSender() {
        return new EmailSender();
    }
}
```

```java
class SMSFactory implements NotificationFactory {

    public Message createMessage() {
        return new SMSMessage();
    }

    public Sender createSender() {
        return new SMSSender();
    }
}
```

**Step 5: Client**

```java
class NotificationService {

    private Message message;
    private Sender sender;

    public NotificationService(NotificationFactory factory) {

        message = factory.createMessage();

        sender = factory.createSender();
    }

    public void notifyUser() {

        String content = message.createMessage();

        sender.send(content);
    }
}
```

**Step 6: Main Class**

```java
public class Main {

    public static void main(String[] args) {

        NotificationFactory factory = new EmailFactory();

        NotificationService service =
                new NotificationService(factory);

        service.notifyUser();
    }
}
```

**Output:**

```text
Sending Email: Email notification
```

To switch to SMS, simply change:

```java
NotificationFactory factory = new SMSFactory();
```

Now the SMS message and SMS sender will be created together.

---

## 7. Advantages

- **Consistency:** Related objects always belong to the same family.
- **Loose coupling:** Client depends on interfaces instead of concrete classes.
- **Easy to switch:** We can change the entire product family by changing the factory.
- **Easy to extend:** New families can be added with new factories and products.
- **Better maintenance:** Object creation logic stays separate from business logic.

## 8. Disadvantages

- **More classes:** Each family needs its own concrete factory and products.
- **Complex structure:** More interfaces and classes are required.
- **Difficult to add new product types:** If we add a new product, every factory must implement its creation method.

For example, adding a TextField means changing the factory interface and all existing concrete factories.

---

## 9. Important Interview Questions

**Q1. What is Abstract Factory Pattern?**

It is a creational pattern that provides an interface for creating families of related objects without specifying their concrete classes.

**Q2. Why do we use Abstract Factory?**

To create related objects together and ensure that they belong to the same family.

**Q3. What is the difference between Factory Method and Abstract Factory?**

Factory Method creates a particular product through an overridden method. Abstract Factory creates multiple related products through one factory interface.

**Q4. What is a Concrete Factory?**

A class that implements the Abstract Factory interface and creates a specific family of products.

**Q5. Can we add a new platform easily?**

Yes. We can create a new factory and its corresponding product classes without changing the existing client logic.

**Q6. What happens when we add a new product type?**

We must add a new method to the Abstract Factory interface and implement it in every concrete factory.

**Q7. Is Abstract Factory a creational or structural pattern?**

It is a **Creational Design Pattern** because it deals with creating objects.

---

## 10. Quick Revision

| Term | Meaning |
|---|---|
| Abstract Factory | Interface for creating related products |
| Concrete Factory | Creates a particular product family |
| Abstract Product | Common interface for a product |
| Concrete Product | Actual product implementation |
| Client | Uses the factory and product interfaces |

**One-line summary:**

Abstract Factory = One factory creates a family of related objects that are designed to work together.

**Remember:**
- Factory Method focuses on creating a product.
- Abstract Factory focuses on creating a family of products.
- Use Abstract Factory when related objects must remain compatible with each other.