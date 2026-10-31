# Prototype Design Pattern

## 1. What is the Prototype Design Pattern?

The **Prototype Design Pattern** is a creational design pattern that creates new objects by copying existing objects instead of creating them from scratch.

In simple words, instead of writing `new` and configuring an object every time, we create one fully configured object and make copies whenever needed.

**Example:** In a game, we create one pre-configured FlyingEnemy. Whenever the game needs another flying enemy, it clones the existing one.

### When should we use it?

- Creating objects is expensive or time-consuming.
- Objects require complex initialization.
- We need many similar objects with small differences.
- We want to avoid repeating object creation logic.

---

## 2. Why Do We Need Prototype?

Suppose we want to copy an existing object.

One approach is to create a new object and manually copy every field.

```java
Enemy enemy2 = new Enemy();

enemy2.setHealth(enemy1.getHealth());
enemy2.setSpeed(enemy1.getSpeed());
enemy2.setWeapon(enemy1.getWeapon());
```

But this approach has some problems.

### Problem 1: Encapsulation

Many fields are private. External code cannot directly access them to copy their values.

We would need getters and setters for every field, which makes copying complicated.

### Problem 2: Dependency on Concrete Classes

We need to know the exact class of the object before creating its copy.

This makes the code tightly coupled to specific classes.

### Problem 3: Working with Interfaces

Suppose we have a `Shape` interface and different classes implement it:

- Circle
- Rectangle
- Triangle

If our code only knows that an object is a `Shape`, it may not know which concrete class to instantiate when making a copy.

### Solution: Let the Object Clone Itself

The Prototype pattern solves these problems by allowing an object to create its own copy through a `clone()` method.

This means:
- The object handles its own copying.
- The client does not need to know its concrete class.
- The internal fields can remain encapsulated.
- Object creation becomes more flexible.

---

## 3. Shallow Copy vs Deep Copy

This is one of the most important concepts in the Prototype pattern.

### Shallow Copy

A shallow copy creates a new object, but its reference fields still point to the same objects as the original.

- Primitive fields (`int`, `double`, `boolean`) are copied by value.
- Immutable objects, such as `String`, can safely be shared.
- Mutable objects, such as `ArrayList`, are shared between the original and clone.

**Example:**

```java
class Employee {
    String name;
    List<String> skills;
}
```

If we create a shallow copy:

```java
Employee copy = original.clone();
```

Both objects may point to the same `skills` list.

Therefore:

```java
copy.skills.add("Java");
```

The original employee's skills list will also change.

<text>**Remember:** Two different objects can still share the same internal object.</text>

### Deep Copy

A deep copy creates a new object and also creates independent copies of its mutable reference fields.

For example:

```java
Employee copy = new Employee();

copy.name = original.name;
copy.skills = new ArrayList<>(original.skills);
```

Now both employees have separate lists.

Changing the clone will not affect the original.

| Shallow Copy | Deep Copy |
|---|---|
| Creates a new outer object | Creates a new outer object |
| Shares mutable reference objects | Copies mutable reference objects |
| Changes to shared objects affect both | Changes remain independent |
| Faster and simpler | Requires more copying |

**Important:** Deep copying becomes more complicated when nested objects contain other mutable objects. Those may also need to be copied.

---

## 4. Real-World Problem: Spawning Enemies in a Game

Imagine we are developing a 2D shooting game.

We have three types of enemies:

| Enemy | Health | Speed |
|---|---:|---:|
| BasicEnemy | 50 | 2 |
| ArmoredEnemy | 150 | 3 |
| FlyingEnemy | 100 | 5 |

Each enemy also has properties like:
- Armor
- Weapon
- Sprite
- Health
- Speed

### Without Prototype

Every time we create an enemy, we write its configuration again.

```java
Enemy enemy1 = new Enemy();
enemy1.setHealth(100);
enemy1.setSpeed(5);
enemy1.setWeapon("Missile");

Enemy enemy2 = new Enemy();
enemy2.setHealth(100);
enemy2.setSpeed(5);
enemy2.setWeapon("Missile");
```

Problems:
- Repeated code.
- Default values are scattered.
- Changing enemy settings requires changes in multiple places.
- More chances of mistakes.

### With Prototype

We create one configured enemy and clone it whenever needed.

```java
Enemy flyingPrototype = new Enemy(100, 5, "Missile");

Enemy enemy1 = flyingPrototype.clone();
Enemy enemy2 = flyingPrototype.clone();
```

Both enemies have the same initial configuration, but they are separate objects.

---

## 5. Structure of Prototype Pattern

The pattern has three required participants and one optional participant.

### 1. Prototype Interface

Declares the `clone()` method.

It defines the rule that every concrete prototype must be able to copy itself.

### 2. Concrete Prototype

Implements the `clone()` method.

It knows how to create a copy of itself and copy its fields.

### 3. Client

Requests a copy by calling `clone()`.

The client does not need to know how the object is internally created.

### 4. Prototype Registry (Optional)

Stores pre-configured prototype objects.

The client requests a prototype using a key, and the registry returns its clone.

### Simple Class Structure

```text
              Prototype
             + clone()
                  |
          -----------------
          |               |
     EnemyPrototype   ShapePrototype
          |
       Enemy
          |
        clone()

         Client
           |
    Prototype Registry
```

---

## 6. How Does It Work?

The workflow is simple:

1. **Create prototypes:** Prepare fully configured objects.
2. **Register prototypes:** Store them in a registry (optional).
3. **Request a clone:** The client requests an object using a key.
4. **Clone the object:** The prototype creates a new instance and copies its fields.
5. **Customize the clone:** The client can change the clone without modifying the original prototype.

Example:

```java
Enemy enemy = registry.get("flying");

enemy.setHealth(80);
```

The registry returns a copy of the flying enemy. Changing its health does not change the original prototype.

---

## 7. Implementing Prototype in Java

We will implement the game enemy example step by step.

### Step 1: Create the Prototype Interface

```java
public interface Prototype<T> {
    T clone();
}
```

This interface says that every class implementing it must provide a `clone()` method.

### Step 2: Create the Enemy Class

```java
import java.util.ArrayList;
import java.util.List;

public class Enemy implements Prototype<Enemy> {

    private int health;
    private int speed;
    private String weapon;
    private List<String> inventory;

    public Enemy(int health, int speed, String weapon) {
        this.health = health;
        this.speed = speed;
        this.weapon = weapon;
        this.inventory = new ArrayList<>();
    }

    // Copy constructor
    public Enemy(Enemy other) {
        this.health = other.health;
        this.speed = other.speed;
        this.weapon = other.weapon;

        // Deep copy of the mutable list
        this.inventory = new ArrayList<>(other.inventory);
    }

    @Override
    public Enemy clone() {
        return new Enemy(this);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void addItem(String item) {
        inventory.add(item);
    }

    public void showDetails() {
        System.out.println(
            "Health: " + health +
            ", Speed: " + speed +
            ", Weapon: " + weapon +
            ", Inventory: " + inventory
        );
    }
}
```

### Important Points

- `Enemy implements Prototype<Enemy>` means Enemy provides its own cloning logic.
- The copy constructor receives the original object.
- `new Enemy(this)` creates a new Enemy using the original object's values.
- `new ArrayList<>(other.inventory)` creates a separate list.
- Therefore, changing the clone's inventory does not affect the original.

**Why use a copy constructor?**

It keeps copying logic inside the class. The class itself knows which fields need to be copied and whether they require a shallow or deep copy.

### Step 3: Create the Prototype Registry

The registry stores configured enemy prototypes and returns clones.

```java
import java.util.HashMap;
import java.util.Map;

public class EnemyRegistry {

    private Map<String, Enemy> enemies = new HashMap<>();

    public void register(String type, Enemy enemy) {
        enemies.put(type, enemy);
    }

    public Enemy get(String type) {
        Enemy prototype = enemies.get(type);

        if (prototype == null) {
            throw new IllegalArgumentException("Unknown enemy type");
        }

        return prototype.clone();
    }
}
```

**Important:** The `get()` method returns a clone, not the original prototype.

If it returned the original object, the client could accidentally modify the stored prototype. Future enemies would then be created with incorrect values.

### Step 4: Client Code

```java
public class Main {

    public static void main(String[] args) {

        EnemyRegistry registry = new EnemyRegistry();

        // Create prototypes
        Enemy flying = new Enemy(100, 5, "Missile");
        flying.addItem("Rocket");

        Enemy armored = new Enemy(150, 3, "Cannon");
        armored.addItem("Shield");

        // Register prototypes
        registry.register("flying", flying);
        registry.register("armored", armored);

        // Get clones
        Enemy enemy1 = registry.get("flying");
        Enemy enemy2 = registry.get("flying");

        // Customize one clone
        enemy1.setHealth(80);
        enemy1.addItem("Extra Ammo");

        System.out.println("Original Prototype:");
        flying.showDetails();

        System.out.println("First Clone:");
        enemy1.showDetails();

        System.out.println("Second Clone:");
        enemy2.showDetails();
    }
}
```

### Expected Output

```text
Original Prototype:
Health: 100, Speed: 5, Weapon: Missile,
Inventory: [Rocket]

First Clone:
Health: 80, Speed: 5, Weapon: Missile,
Inventory: [Rocket, Extra Ammo]

Second Clone:
Health: 100, Speed: 5, Weapon: Missile,
Inventory: [Rocket]
```

Notice that:
- The original prototype remains unchanged.
- The first clone has its own health and inventory.
- The second clone still has the original default values.

This is the main benefit of Prototype.

---

## 8. Practical Example: Email Templates

Prototype can also be used in a bulk email system.

Imagine a company sends newsletters to different departments.

The basic email template contains:
- Subject
- Body
- Recipient list (`to`)
- CC list

Each department needs a slightly different email.

For example:
- HR adds an extra recipient.
- Marketing adds its department members.
- Engineering adds its own recipients.

Instead of creating every email from scratch, we create one base email template and clone it.

**Important:** The recipient list is mutable. If we use shallow copying, all email templates may share the same recipient lists.

We need a deep copy.

### Java Example

```java
import java.util.ArrayList;
import java.util.List;

class RecipientList {

    List<String> to = new ArrayList<>();
    List<String> cc = new ArrayList<>();

    RecipientList deepCopy() {
        RecipientList copy = new RecipientList();

        copy.to = new ArrayList<>(this.to);
        copy.cc = new ArrayList<>(this.cc);

        return copy;
    }
}

class EmailTemplate implements Prototype<EmailTemplate> {

    String subject;
    String body;
    RecipientList recipients;

    EmailTemplate(String subject, String body,
                  RecipientList recipients) {
        this.subject = subject;
        this.body = body;
        this.recipients = recipients;
    }

    @Override
    public EmailTemplate clone() {
        return new EmailTemplate(
            this.subject,
            this.body,
            this.recipients.deepCopy()
        );
    }
}
```

### Usage

```java
RecipientList baseRecipients = new RecipientList();
baseRecipients.to.add("company@example.com");

EmailTemplate base = new EmailTemplate(
    "Monthly Newsletter",
    "Here are the company updates.",
    baseRecipients
);

// Create department-specific copies
EmailTemplate hr = base.clone();
EmailTemplate marketing = base.clone();

// Customize each copy
hr.recipients.to.add("hr@example.com");
marketing.recipients.to.add("marketing@example.com");
```

Each email has its own recipient lists. Adding a recipient to one email does not affect the others or the original template.

---

## 9. Advantages of Prototype Pattern

1. **Reduces repeated code:** We clone existing objects instead of configuring new ones repeatedly.
2. **Faster object creation:** Useful when creating an object from scratch is expensive.
3. **Less dependency:** The client does not need to know the concrete class.
4. **Easy customization:** Clones can be modified independently.
5. **Centralized configuration:** A registry can manage commonly used prototypes.

## 10. Disadvantages of Prototype Pattern

1. **Deep copying can be difficult:** Nested objects may also need to be copied.
2. **Extra implementation:** Every prototype needs cloning logic.
3. **Circular references:** Objects that refer to each other can make deep copying complicated.
4. **Not always necessary:** For simple objects, normal constructors may be easier.

---

## 11. Prototype vs Factory Method vs Abstract Factory

| Pattern | Main Purpose |
|---|---|
| Prototype | Creates objects by cloning existing objects |
| Factory Method | Creates objects through a factory method, allowing subclasses to decide which type to create |
| Abstract Factory | Creates families of related objects |

**Easy way to remember:**

- Prototype = Copy an existing object.
- Factory Method = Let a method decide which object to create.
- Abstract Factory = Create a family of related objects.

---

## 12. Common Interview Questions

**Q1. What is the Prototype Design Pattern?**

It is a creational design pattern that creates new objects by cloning existing objects instead of creating them from scratch.

**Q2. Why do we use Prototype?**

When object creation is expensive or complex, or when we need many similar objects with small changes.

**Q3. What is the difference between shallow copy and deep copy?**

Shallow copy shares references to internal objects. Deep copy creates independent copies of mutable referenced objects.

**Q4. What is a Prototype Registry?**

It is an optional collection that stores pre-configured prototype objects and returns their clones when requested.

**Q5. Does Prototype always require a registry?**

No. A client can directly call `clone()` on a prototype. The registry is optional.

**Q6. Why should the registry return a clone instead of the original object?**

So that clients cannot accidentally modify the stored prototype and affect future clones.

**Q7. What is a copy constructor?**

A constructor that accepts another object of the same class and copies its field values.

**Q8. Does Java's `Object.clone()` automatically perform deep copying?**

No. Java's default `Object.clone()` performs a shallow copy. Deep copying requires additional logic.

---

## 13. Quick Revision

- Prototype is a **creational design pattern**.
- It creates new objects by copying existing objects.
- The object provides its own `clone()` method.
- It avoids repeated object initialization.
- A copy constructor is one way to implement cloning.
- Shallow copy shares references to internal objects.
- Deep copy creates independent copies of mutable objects.
- Prototype Registry is optional and stores pre-configured objects.
- The registry should return clones, not original prototypes.

**Most important point:** Prototype is useful when you already have a configured object and want to create more objects with the same initial configuration without repeating the creation process.