# Builder Design Pattern

## 1. What is the Builder Pattern?

The **Builder Design Pattern** is a creational design pattern used to create complex objects step by step.

It is useful when:
- An object has many fields.
- Some fields are required, while others are optional.
- We want to avoid constructors with too many parameters.
- We want to create objects that cannot be modified after construction (immutable objects).

**Simple example:** Imagine creating an HTTP request. It needs a URL, method, headers, body, and timeout. But not every request needs all these fields.

Instead of passing everything through a constructor, we can use a Builder to set only the fields we need.

---

## 2. Understanding the Problem

Suppose we have an `HttpRequest` class:

```java
class HttpRequest {
    String url;
    String method;
    String body;
    int timeout;
}
```

We can create objects using constructors:

```java
HttpRequest request = new HttpRequest(
    "https://api.com",
    "POST",
    null,
    30
);
```

As the number of fields increases, constructors become difficult to manage.

### Problems with this approach

1. **Difficult to read:** We may not understand what each argument represents.
2. **Too many parameters:** Constructors become long.
3. **Null values:** We have to pass `null` for optional fields.
4. **Hard to maintain:** Adding more fields means changing constructors.

This problem is called the **Telescoping Constructor Problem**.

---

## 3. How Does the Builder Pattern Solve It?

Instead of passing all values through a constructor, we create a separate Builder.

We configure the object using individual methods and finally call `build()`.

### Before: Constructor

```java
HttpRequest request =
    new HttpRequest(url, "POST", headers, body, 30);
```

### After: Builder

```java
HttpRequest request = new HttpRequest.Builder(url)
        .method("POST")
        .body("Hello")
        .timeout(60)
        .build();
```

**Notice:**
- Each method clearly tells us which value we are setting.
- We only set the fields we need.
- `build()` creates the final object.

This is called a **Fluent Interface** because methods return the Builder itself, allowing method chaining.

---

## 4. Class Diagram

```text
       Client
          |
          v
       Builder
          |
          | build()
          v
       Product
     (HttpRequest)
```

### Main Components

| Component | Responsibility |
|---|---|
| Product | Final object we want to create |
| Builder | Provides methods to configure the object |
| Concrete Builder | Implements the building process |
| Director | Optional class that defines reusable building steps |
| Client | Uses the Builder to create the object |

In simple implementations, the Builder is often written as a static nested class inside the Product class. A separate Builder interface and Director are not always necessary.

---

## 5. Java Implementation: HttpRequest

Let's create an HTTP request using the Builder pattern.

### Step 1: Create the Product and Builder

```java
import java.util.HashMap;
import java.util.Map;

class HttpRequest {

    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final int timeout;

    // Private constructor
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    // Static nested Builder class
    public static class Builder {

        private final String url;
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private int timeout = 30;

        public Builder(String url) {
            this.url = url;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public void showRequest() {
        System.out.println("URL: " + url);
        System.out.println("Method: " + method);
        System.out.println("Headers: " + headers);
        System.out.println("Body: " + body);
        System.out.println("Timeout: " + timeout);
    }
}
```

### Step 2: Use the Builder

```java
public class Main {
    public static void main(String[] args) {

        HttpRequest request = new HttpRequest.Builder(
                "https://api.example.com/users"
        )
        .method("POST")
        .addHeader("Content-Type", "application/json")
        .body("{\"name\":\"Deepanshi\"}")
        .timeout(60)
        .build();

        request.showRequest();
    }
}
```

### Output

```text
URL: https://api.example.com/users
Method: POST
Headers: {Content-Type=application/json}
Body: {"name":"Deepanshi"}
Timeout: 60
```

### Important Points to Understand

**1. Why is the constructor private?**

```java
private HttpRequest(Builder builder)
```

It prevents clients from directly creating an `HttpRequest` using its constructor. They must use the Builder.

**2. Why do Builder methods return `this`?**

```java
public Builder method(String method) {
    this.method = method;
    return this;
}
```

`this` returns the current Builder object.

This allows us to chain methods:

```java
builder.method("POST")
       .body("Hello")
       .timeout(60);
```

**3. Why is `build()` used?**

```java
public HttpRequest build() {
    return new HttpRequest(this);
}
```

It creates the final object using the values configured in the Builder.

**4. Why use `final` in HttpRequest?**

The final fields cannot be reassigned after construction. This helps make the final object immutable.

---

## 6. What is a Director?

A Director is an optional class that controls the steps used to build an object.

Imagine that many parts of your application need to create the same type of HTTP request.

Instead of repeating the same Builder configuration everywhere, we can create a Director with predefined methods.

For example:

```java
class HttpRequestDirector {

    public HttpRequest buildGetRequest(String url) {
        return new HttpRequest.Builder(url)
                .method("GET")
                .timeout(30)
                .build();
    }

    public HttpRequest buildPostRequest(
            String url, String token, String body) {

        return new HttpRequest.Builder(url)
                .method("POST")
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .body(body)
                .timeout(60)
                .build();
    }
}
```

**Using the Director:**

```java
HttpRequestDirector director = new HttpRequestDirector();

HttpRequest request = director.buildPostRequest(
        "https://api.example.com",
        "my-token",
        "{\"id\":1}"
);
```

### When do we need a Director?

- When multiple places need the same object configuration.
- When construction steps are complicated.
- When we want reusable predefined configurations.

**Remember:** Director is optional. For most simple Builder implementations, the client can call Builder methods directly.

---

## 7. Practical Example: SQL Query Builder

Imagine we want to construct SQL queries step by step.

Instead of writing a complete SQL query manually, we can use Builder methods.

```java
class QueryBuilder {

    private String table;
    private String condition;
    private String order;

    public QueryBuilder from(String table) {
        this.table = table;
        return this;
    }

    public QueryBuilder where(String condition) {
        this.condition = condition;
        return this;
    }

    public QueryBuilder orderBy(String order) {
        this.order = order;
        return this;
    }

    public String build() {
        String query = "SELECT * FROM " + table;

        if (condition != null) {
            query += " WHERE " + condition;
        }

        if (order != null) {
            query += " ORDER BY " + order;
        }

        return query;
    }
}
```

**Client code:**

```java
public class Main {
    public static void main(String[] args) {

        String query = new QueryBuilder()
                .from("employees")
                .where("salary > 50000")
                .orderBy("name")
                .build();

        System.out.println(query);
    }
}
```

**Output:**

```sql
SELECT * FROM employees
WHERE salary > 50000
ORDER BY name
```

Here, every method adds one part of the query. Finally, `build()` combines everything into a complete SQL query.

*Note: This simplified example is for understanding the Builder pattern. In real applications, SQL values should be parameterized rather than directly concatenated.*

---

## 8. Advantages

- **Readable code:** Each method clearly identifies the field being configured.
- **No long constructors:** Avoids constructors with too many parameters.
- **Flexible:** We can set only the fields we need.
- **Immutable objects:** The final object can be protected from modification.
- **Easy to maintain:** Adding optional fields usually requires adding a Builder method.

## 9. Disadvantages

- **More code:** We need to create an additional Builder class.
- **Extra complexity:** Not necessary for objects with only two or three simple fields.
- **More objects:** A Builder object is created before the final object.

---

## 10. Important Interview Questions

**Q1. What is the Builder Design Pattern?**

It is a creational design pattern that creates complex objects step by step.

**Q2. Why do we use the Builder Pattern?**

To avoid long constructors, improve readability, and handle optional fields easily.

**Q3. What is the Telescoping Constructor Problem?**

It occurs when we create multiple overloaded constructors to support different combinations of optional parameters.

**Q4. Why does a Builder method return `this`?**

To support method chaining, also known as a Fluent Interface.

**Q5. What is the purpose of `build()`?**

It creates and returns the final object using the values stored in the Builder.

**Q6. What is a Director?**

An optional class that defines reusable steps for creating objects.

**Q7. Is the Builder Pattern a creational or structural pattern?**

It is a **Creational Design Pattern** because it deals with object creation.

---

## 11. Quick Revision

| Term | Meaning |
|---|---|
| Builder | Helps create an object step by step |
| Product | Final object |
| `build()` | Creates the final object |
| `this` | Returns the current Builder |
| Fluent Interface | Allows method chaining |
| Director | Provides predefined construction steps |
| Immutable Object | Object whose state cannot be changed after construction |

**One-line summary:**

Builder Pattern = Create complex objects step by step without using long constructors.

**Remember:** Use Builder when an object has many optional fields or complex construction logic. For simple objects, a normal constructor is usually enough.