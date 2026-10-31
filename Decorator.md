# Decorator Design Pattern

## 1. What is the Decorator Design Pattern?

The **Decorator Design Pattern** is a structural design pattern that allows us to add new features or responsibilities to an existing object **without modifying its original class**.

In simple words:

We wrap an existing object inside another object to add extra functionality.

### Real-Life Example: Pizza

Imagine you order a plain pizza.

- Plain Pizza = ₹100
- Add Cheese = ₹50
- Add Cucumber = ₹60

Instead of creating separate classes for every pizza combination, we can add toppings dynamically using decorators.

This means we can create:
- Plain Pizza
- Plain Pizza + Cheese
- Plain Pizza + Cucumber
- Plain Pizza + Cheese + Cucumber

**Each decorator adds one extra feature to the original object.**

---

## 2. Why Do We Need Decorator Pattern?

Suppose we want to create different types of pizzas.

One approach is to create separate classes:

```text
PlainPizza
CheesePizza
CucumberPizza
CheeseCucumberPizza
```

But imagine adding more toppings:

- Onion
- Mushroom
- Olives
- Corn

We would need many more classes for every possible combination.

### Problems Without Decorator

1. **Class Explosion:** Too many classes are required for different combinations of features.
2. **Code Duplication:** Similar functionality gets repeated in multiple classes.
3. **Less Flexibility:** Adding or removing features at runtime becomes difficult.
4. **Difficult Maintenance:** Every new feature may require additional subclasses.

### Solution

Use the Decorator Pattern.

Instead of creating a new class for every combination, create one decorator class for each additional feature.

For example:

```text
PlainPizza
    |
CheeseDecorator
    |
CucumberDecorator
```

Now we can combine toppings as needed.

---

## 3. How Does Decorator Pattern Work?

There are four main components:

| Component | Responsibility |
|---|---|
| Component | Common interface implemented by the base object and decorators. |
| Concrete Component | Original object that provides basic functionality. |
| Abstract Decorator | Implements the same interface and stores a reference to another component. |
| Concrete Decorator | Adds a specific feature to the wrapped object. |

### Class Diagram

```text
             <<Interface>>
                  Pizza
             + getCost()
             + getDesc()
                   |
          -------------------
          |                 |
      PlainPizza      PizzaDecorator
                            |
                     + myPizza: Pizza
                            |
                  -------------------
                  |                 |
           CheeseDecorator   CucumberDecorator
```

**Important:** Both the original object and decorators implement the same interface.

This allows us to wrap a decorator inside another decorator.

---

## 4. Java Implementation – Pizza Example

### Step 1: Create Pizza Interface

```java
interface Pizza {

    int getCost();

    String getDesc();
}
```

**Explanation:**

- `getCost()` returns the pizza price.
- `getDesc()` returns the pizza description.

Every pizza and decorator must implement these methods.

### Step 2: Create Concrete Component

**PlainPizza.java**

```java
class PlainPizza implements Pizza {

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public String getDesc() {
        return "PLAIN PIZZA";
    }
}
```

**Explanation:**

This is our original pizza.

- Its cost is ₹100.
- Its description is PLAIN PIZZA.
- It does not contain any additional toppings.

This is the starting object that we will decorate.

### Step 3: Create Abstract Decorator

**PizzaDecorator.java**

```java
abstract class PizzaDecorator implements Pizza {

    protected Pizza myPizza;

    public PizzaDecorator(Pizza myPizza) {
        this.myPizza = myPizza;
    }
}
```

**Explanation:**

This is the most important part of the Decorator Pattern.

- `implements Pizza`: The decorator follows the same interface as the original pizza.
- `protected Pizza myPizza`: Stores the pizza object that we want to decorate.
- Constructor: Receives an existing Pizza object and stores it.

**Why do we use `Pizza` instead of `PlainPizza`?**

Because we want to wrap any object that implements the Pizza interface, including another decorator.

For example:

```java
new CheeseDecorator(new PlainPizza());
```

We can also do:

```java
new CucumberDecorator(
    new CheeseDecorator(new PlainPizza())
);
```

This is what makes the pattern flexible.

### Step 4: Create Concrete Decorators

Each decorator adds one specific topping and increases the cost.

**CheeseDecorator.java**

```java
class CheeseDecorator extends PizzaDecorator {

    public CheeseDecorator(Pizza myPizza) {
        super(myPizza);
    }

    @Override
    public int getCost() {
        return myPizza.getCost() + 50;
    }

    @Override
    public String getDesc() {
        return myPizza.getDesc() + " CHEESE";
    }
}
```

**Explanation:**

- Extends `PizzaDecorator`.
- `super(myPizza)` passes the existing pizza to the parent constructor.
- `myPizza.getCost() + 50` adds ₹50 to the existing pizza price.
- `myPizza.getDesc() + " CHEESE"` adds cheese to the existing description.

**CucumberDecorator.java**

```java
class CucumberDecorator extends PizzaDecorator {

    public CucumberDecorator(Pizza myPizza) {
        super(myPizza);
    }

    @Override
    public int getCost() {
        return myPizza.getCost() + 60;
    }

    @Override
    public String getDesc() {
        return myPizza.getDesc() + " CUCUMBER";
    }
}
```

**Explanation:**

It works exactly like CheeseDecorator, but adds:
- ₹60 to the existing price.
- CUCUMBER to the existing description.

### Step 5: Main Class

```java
public class Main {

    public static void main(String[] args) {

        Pizza myPizza = new PlainPizza();

        System.out.println(
            myPizza.getDesc() + " " + myPizza.getCost()
        );

        myPizza = new CheeseDecorator(myPizza);

        System.out.println(
            myPizza.getDesc() + " " + myPizza.getCost()
        );

        myPizza = new CucumberDecorator(myPizza);

        System.out.println(
            myPizza.getDesc() + " " + myPizza.getCost()
        );
    }
}
```

### Output

```text
PLAIN PIZZA 100
PLAIN PIZZA CHEESE 150
PLAIN PIZZA CHEESE CUCUMBER 210
```

---

## 5. Understand the Object Wrapping (Most Important)

Let's understand exactly what happens when decorators are applied.

### First: Create Plain Pizza

```java
Pizza myPizza = new PlainPizza();
```

```text
myPizza
   |
   v
PlainPizza
Cost = 100
```

Output:

```text
PLAIN PIZZA 100
```

### Second: Add Cheese

```java
myPizza = new CheeseDecorator(myPizza);
```

The existing PlainPizza object is passed to CheeseDecorator.

```text
myPizza
   |
   v
CheeseDecorator
   |
   v
PlainPizza
```

When `getCost()` is called:

```java
return myPizza.getCost() + 50;
```

Calculation:

```text
Plain Pizza = 100
Cheese      =  50
-----------------
Total       = 150
```

Output:

```text
PLAIN PIZZA CHEESE 150
```

### Third: Add Cucumber

```java
myPizza = new CucumberDecorator(myPizza);
```

Now the CheeseDecorator object is passed to CucumberDecorator.

```text
myPizza
   |
   v
CucumberDecorator
   |
   v
CheeseDecorator
   |
   v
PlainPizza
```

When `getCost()` is called:

```text
CucumberDecorator
       |
       v
CheeseDecorator
       |
       v
PlainPizza
```

Calculation:

```text
Plain Pizza = 100
Cheese      =  50
Cucumber    =  60
-----------------
Total       = 210
```

Output:

```text
PLAIN PIZZA CHEESE CUCUMBER 210
```

**Remember:** Each decorator adds its own functionality to the object it receives.

---

## 6. Why Do We Use `super(myPizza)`?

Consider this code:

```java
public CheeseDecorator(Pizza myPizza) {
    super(myPizza);
}
```

Here:

- `myPizza` is passed as an argument to the CheeseDecorator constructor.
- `super(myPizza)` calls the parent class constructor.
- The parent class stores the object in its `protected Pizza myPizza` variable.

Without storing this reference, the decorator would not know which existing pizza it needs to enhance.

## 7. Why Is `myPizza` of Type Pizza?

```java
protected Pizza myPizza;
```

We use the interface type instead of a specific class like `PlainPizza`.

Why?

Because the decorator should be able to wrap:

- PlainPizza
- CheeseDecorator
- CucumberDecorator
- Any other class implementing Pizza

This allows decorators to wrap other decorators.

**This is the main reason multiple decorators can be stacked together.**

---

## 8. Advantages of Decorator Pattern

1. **Flexible:** Add features dynamically at runtime.
2. **Reusable:** Each decorator can be reused with different objects.
3. **No Class Explosion:** No need to create separate classes for every combination.
4. **Easy Maintenance:** Each decorator handles one specific responsibility.
5. **Open/Closed Principle:** We can introduce new decorators without modifying existing classes.

## 9. Disadvantages of Decorator Pattern

1. **More Classes:** Each additional feature usually requires a separate decorator class.
2. **Complex Object Structure:** Multiple wrapping layers can make debugging difficult.
3. **Order Matters:** Changing the order of decorators can change the result.
4. **Difficult Object Management:** Many nested decorators can make object creation harder to understand.

---

## 10. When Should We Use Decorator Pattern?

Use it when:

- You want to add functionality to an object without modifying its original class.
- You need to add features dynamically.
- You want to combine multiple optional features.
- You want to avoid creating too many subclasses for different combinations.

**Real-world examples:**

- Pizza with different toppings.
- Coffee with milk, sugar, and cream.
- Text formatting with bold, italic, and underline.
- Java I/O streams, such as wrapping an input stream with buffering functionality.

---

## 11. Important Points for Interviews

- Decorator is a **Structural Design Pattern**.
- It adds functionality to an object without modifying its original class.
- It uses **object composition instead of relying only on inheritance**.
- Both the original object and decorators implement the same interface.
- A decorator contains a reference to another component of the same interface.
- Multiple decorators can be stacked together.
- Features can be added dynamically at runtime.
- Each decorator generally adds one specific responsibility.

### One-Line Interview Definition

**Decorator Design Pattern allows us to dynamically add new responsibilities to an object by wrapping it inside another object that implements the same interface.**

---

## 12. Quick Revision

| Question | Answer |
|---|---|
| Pattern Type | Structural |
| Main Purpose | Add functionality dynamically |
| Main Concept | Object wrapping |
| Uses | Composition |
| Common Interface | Implemented by component and decorators |
| Original Object | Concrete Component |
| Parent Decorator | Abstract Decorator |
| Additional Features | Concrete Decorators |
| Can decorators wrap other decorators? | Yes |
| Does it modify the original class? | No |

**Remember:** Decorator means *wrapping an existing object to add extra functionality without changing its original code*.