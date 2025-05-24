**Available languages:**
- 🇷🇺 [Русский](README.md)
- 🇺🇸 [English](README_EN.md)

# 📚 Laboratory Work №1:
This project includes an implementation of a system according to the selected variant, as well as 
the integration of **GoF** design patterns.

## Task:
Develop an object-oriented model of the subject domain based on the given variants. Ensure the 
possibility of extending the model with new object types and behavior. To maintain loose coupling 
and system extensibility, apply **GoF** design patterns. You must use at least **two patterns from 
each group**: **creational**, **structural**, and **behavioral**. Create a **console application** 
in Java that implements the designed object-oriented model.

## Variant 8:
**Develop an object-oriented model of a telephone.**

A phone has the following attributes:\
```number```,\
```balance```.

The following operations are supported:\
```make a call```,\
```answer a call```,\
```end a call```,\
```recharge balance```.

The phone can be in the following states:\
```Waiting```,\
```Calling```,\
```In call```,\
```Blocked``` (negative balance).

**Implement an application demonstrating transitions between these states.**

## 📖 Explanation:
### Selected **creational** patterns:
**Multiton** (used in the ```Phone``` class) — to prevent creating multiple phones with the same number.

**Singleton** (used in the ```PhoneCallMediator``` class) — to ensure all calls are processed through 
a single controller.

**Builder** (used in the ```PhoneProxy``` class) — to simplify object construction using method chaining.

### Selected **structural** patterns:
**Facade** — to simplify interaction between objects.

**Proxy** (via the ```PhoneProxy``` class) — to separate error and exception handling logic.

### Selected **behavioral** patterns:
**Mediator** (via the ```PhoneCallMediator``` class) — to manage interactions between phones through 
a central interface.

**Chain of Responsibility** (used in the ```PhoneProxy``` class) — to delegate and simplify the error 
handling process and make it more scalable.
