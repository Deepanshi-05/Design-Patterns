# Composite Design Pattern

## 1. What is the Composite Design Pattern?

<blockquote>

🔵 **Definition**

The **Composite Design Pattern** is a structural design pattern that allows us to treat individual objects and groups of objects in the same way.

</blockquote>

In simple words:

It allows us to create a tree-like structure where a single object and a group of objects can be handled using the same interface.

### Real-Life Example: File Explorer

Think about your computer's file explorer.

- A file contains data.
- A folder contains files and other folders.
- A folder can contain another folder, which can contain more files.

For example:

```text
📁 Home
   |
   |--- 📁 Documents
   |       |
   |       |--- 📄 resume.pdf
   |       |--- 📄 notes.txt
   |
   |--- 📁 Pictures
           |
           |--- 🖼️ photo.jpg
```

We want to perform operations such as:

- `getSize()` – Get the size of a file or the total size of a folder.
- `printStructure()` – Display files and folders.
- `delete()` – Delete a file or an entire folder.

**Important:** We want to perform these operations without writing separate logic for files and folders in the client.

---

## 2. Why Do We Need Composite Pattern?

Imagine creating two separate classes:

- `File`
- `Folder`

A file has its own size, but a folder contains multiple files and folders.

Without Composite, we may need to check the type of every object before performing an operation.

For example:

```java
if (item instanceof File) {
    // Get file size
}
else if (item instanceof Folder) {
    // Calculate folder size
}
```

### Problems with this approach

🔴 **1. Repetitive Type Checking**

We need `instanceof` checks in multiple methods.

🔴 **2. No Common Interface**

Files and folders cannot be handled through one common abstraction.

🔴 **3. Difficult to Extend**

If we introduce a new type, such as a Shortcut, we may need to modify existing type-checking logic.

🔴 **4. Complicated Code**

Handling deeply nested folders becomes difficult.

### Solution

Introduce a common interface called `FileSystemItem`.

Both `File` and `Folder` will implement this interface.

Now the client can call the same methods without checking whether the object is a file or folder.

---

## 3. How Does Composite Pattern Work?

There are four main components:

| Component | Responsibility |
|---|---|
| Component | Common interface for all objects. |
| Leaf | Individual object that has no children. |
| Composite | Container that holds multiple components. |
| Client | Uses the common interface to work with objects. |

### Class Diagram

```text
               Client
                  |
                  v
          <<Interface>>
          FileSystemItem
          + getSize()
          + printStructure()
          + delete()
                  |
          -----------------
          |               |
          v               v
         File           Folder
        (Leaf)        (Composite)
                         |
                         |
                 List<FileSystemItem>
                         |
                   -------------
                   |           |
                  File       Folder
```

### Understand Each Component

**1. Component – FileSystemItem**

- It is the common interface.
- Declares methods that both files and folders must implement.
- Provides a common way to access different objects.

**2. Leaf – File**

- Represents an individual object.
- Does not contain other objects.
- Implements the interface directly.

Example: `resume.pdf`, `photo.jpg`.

**3. Composite – Folder**

- Represents a group of objects.
- Contains a list of `FileSystemItem` objects.
- Can contain files and other folders.
- Performs operations by calling the same methods on its children.

**4. Client**

- Interacts with the objects using the common interface.
- Does not need to know whether the object is a File or Folder.

---

## 4. How Does It Work? (Object Flow)

Consider this folder structure:

```text
📁 Home
   |
   |--- 📁 Documents
   |       |
   |       |--- 📄 readme.txt (5 KB)
   |       |--- 📄 data.csv (300 KB)
   |
   |--- 📁 Pictures
           |
           |--- 🖼️ photo.jpg (1500 KB)
```

Suppose we call:

```java
home.getSize();
```

What happens?

**Step 1:** Home receives the `getSize()` call.

**Step 2:** Home calls `getSize()` on its children.

**Step 3:** Documents calculates the size of its files.

**Step 4:** Pictures calculates the size of its files.

**Step 5:** Home adds the results received from both folders.

Calculation:

```text
Documents = 5 + 300 = 305 KB

Pictures = 1500 KB

Home = 305 + 1500

Total = 1805 KB
```

🟢 **Important:** A folder does not need to know whether its child is a File or another Folder. It simply calls `getSize()` on every child.

This is possible because both implement the same interface.

---

## 5. Java Implementation

Let's implement the Composite Pattern using the File Explorer example.

### Step 1: Create Component Interface

**FileSystemItem.java**

```java
interface FileSystemItem {

    int getSize();

    void printStructure(String indent);

    void delete();
}
```

Explanation:

- `getSize()` returns the size.
- `printStructure()` displays the hierarchy.
- `delete()` performs the delete operation.

Both File and Folder must implement these methods.

### Step 2: Create Leaf Class

**File.java**

```java
class File implements FileSystemItem {

    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void printStructure(String indent) {
        System.out.println(indent + name);
    }

    @Override
    public void delete() {
        System.out.println("Deleting file: " + name);
    }
}
```

Explanation:

- File implements the `FileSystemItem` interface.
- It stores its own name and size.
- `getSize()` simply returns its own size.
- It has no children.

This is called a **Leaf**.

### Step 3: Create Composite Class

**Folder.java**

```java
import java.util.ArrayList;
import java.util.List;

class Folder implements FileSystemItem {

    private String name;

    private List<FileSystemItem> children =
            new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public void addItem(FileSystemItem item) {
        children.add(item);
    }

    public void removeItem(FileSystemItem item) {
        children.remove(item);
    }

    @Override
    public int getSize() {

        int total = 0;

        for (FileSystemItem item : children) {
            total += item.getSize();
        }

        return total;
    }

    @Override
    public void printStructure(String indent) {

        System.out.println(indent + name);

        for (FileSystemItem item : children) {
            item.printStructure(indent + "  ");
        }
    }

    @Override
    public void delete() {

        for (FileSystemItem item : children) {
            item.delete();
        }

        System.out.println("Deleting folder: " + name);
    }
}
```

Explanation:

- Folder implements the same `FileSystemItem` interface.
- It stores a list of `FileSystemItem` objects.
- `addItem()` adds a file or another folder.
- `removeItem()` removes an item.
- `getSize()` calculates the total size of all children.
- `printStructure()` prints the folder and its contents.
- `delete()` deletes its children first, then the folder.

**Important line:**

```java
private List<FileSystemItem> children;
```

Why do we use `FileSystemItem` instead of `List<File>`?

Because a folder should be able to contain:

- Files
- Other folders

Both implement `FileSystemItem`, so we can store both in the same list.

### Step 4: Client Code

**Main.java**

```java
public class Main {

    public static void main(String[] args) {

        File file1 = new File("readme.txt", 5);
        File file2 = new File("data.csv", 300);
        File file3 = new File("photo.jpg", 1500);

        Folder documents = new Folder("Documents");
        documents.addItem(file1);
        documents.addItem(file2);

        Folder pictures = new Folder("Pictures");
        pictures.addItem(file3);

        Folder home = new Folder("Home");

        home.addItem(documents);
        home.addItem(pictures);

        home.printStructure("");

        System.out.println("Total Size: " + home.getSize() + " KB");

        // home.delete();
    }
}
```

### Output

```text
Home
  Documents
    readme.txt
    data.csv
  Pictures
    photo.jpg

Total Size: 1805 KB
```

**Notice:** We called `getSize()` on Home, but it automatically calculated the sizes of all nested files and folders.

We did not write separate logic in Main for files and folders.

---

## 6. Understand the Most Important Concept

<blockquote>

🟡 **Why does Folder store `List<FileSystemItem>` instead of `List<File>`?**

</blockquote>

Look at this code:

```java
private List<FileSystemItem> children;
```

Because:

- File implements FileSystemItem.
- Folder also implements FileSystemItem.

Therefore, both can be stored in the same list.

For example:

```java
Folder home = new Folder("Home");

home.addItem(new File("notes.txt", 100));

Folder documents = new Folder("Documents");

documents.addItem(new File("resume.pdf", 200));

home.addItem(documents);
```

Here, Home contains:

```text
📁 Home
   |
   |--- 📄 notes.txt
   |
   |--- 📁 Documents
           |
           |--- 📄 resume.pdf
```

This is called **Recursive Composition**.

A folder can contain another folder, and that folder can contain more folders.

There is no fixed nesting limit in the design.

---

## 7. Advantages of Composite Pattern

🟢 **1. Uniform Treatment**

Files and folders can be handled using the same interface.

🟢 **2. Less Type Checking**

No need for repeated `instanceof` checks.

🟢 **3. Easy to Extend**

New component types can be introduced by implementing the common interface.

🟢 **4. Supports Tree Structures**

Can represent deeply nested structures.

🟢 **5. Cleaner Client Code**

The client does not need to distinguish between individual objects and groups.

## 8. Disadvantages of Composite Pattern

🔴 **1. More Classes**

We need separate classes for different component types.

🔴 **2. Can Become Complex**

Large and deeply nested structures can be difficult to understand.

🔴 **3. Common Interface Limitations**

Sometimes certain operations make sense only for folders, such as `addItem()` and `removeItem()`, but not for individual files.

---

## 9. Another Real-Life Example: Organization Hierarchy

Imagine a company with the following structure:

```text
             👤 CEO
               |
        ----------------
        |              |
    👤 Manager      👤 Manager
        |              |
     --------        --------
     |      |        |      |
   👤 Dev  👤 Dev   👤 Dev  👤 Dev
```

Here:

- Employee is a Leaf.
- Manager is a Composite because it can contain employees and other managers.
- CEO is also a Composite.

We can define a common interface:

```java
interface OrgComponent {

    int getSalary();

    int getHeadcount();

    void printHierarchy();
}
```

Both Employee and Manager implement this interface.

For example:

- Calling `getSalary()` on an employee returns that employee's salary.
- Calling `getSalary()` on a manager returns the total salary of the manager and all members below them.
- Calling `getSalary()` on the CEO returns the total salary of the entire organization.

**The same method works at every level of the hierarchy.**

This is the power of the Composite Pattern.

---

## 10. When Should We Use Composite Pattern?

Use it when:

- You need to represent objects in a tree structure.
- One object can contain multiple objects of the same common type.
- You want to perform the same operations on individual objects and groups.
- You want to avoid writing separate logic for single objects and collections.

### Real-World Examples

| Example | Leaf | Composite |
|---|---|---|
| File Explorer | File | Folder |
| Organization | Employee | Manager |
| UI Components | Button | Panel |
| Shopping Cart | Product | Product Bundle |

---

## 11. Important Interview Points

🟡 **Remember these points:**

1. Composite is a **Structural Design Pattern**.
2. It represents objects in a tree-like structure.
3. It treats individual objects and groups uniformly.
4. Both Leaf and Composite implement the same interface.
5. Leaf objects do not contain children.
6. Composite objects contain a collection of components.
7. It uses recursion to perform operations on nested structures.
8. It reduces the need for `instanceof` checks.
9. It uses composition and polymorphism.
10. It is useful for part-whole hierarchies.

### One-Line Interview Definition

**Composite Design Pattern allows us to treat individual objects and groups of objects uniformly by organizing them into a tree-like structure using a common interface.**

---

## 12. Quick Revision

| Question | Answer |
|---|---|
| Pattern Type? | Structural |
| Main Purpose? | Handle single objects and groups uniformly |
| Main Structure? | Tree |
| Common Interface? | Component |
| Individual Object? | Leaf |
| Group of Objects? | Composite |
| Main Concept? | Recursion and composition |
| Common Example? | File and Folder |
| Main Benefit? | Uniform treatment without type checks |

---

## Final Remember Point

🟢 **Composite = Tree Structure + Common Interface + Uniform Treatment**

Think of a folder:

- A file is an individual object.
- A folder is a group of objects.
- Both follow the same interface.
- The folder performs operations on its children recursively.

**That is the Composite Design Pattern!**