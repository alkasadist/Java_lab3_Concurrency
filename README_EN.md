**Available languages:**
- 🇷🇺 [Русский](README.md)
- 🇺🇸 [English](README_EN.md)

# 📚 Lab Work №3:
This lab involves implementing a multithreaded server-side interaction system based on the system from
[Lab Work №1](https://github.com/alkasadist/Java_lab1_Patterns)

## 🧩 Task:
You need to upgrade the system developed in Lab Work 1 using your knowledge of Concurrency.
You must solve a classic multithreading programming problem:  
**client-server interaction**:
- Organize the operation of request generators, request queue, and request handlers.
- Requests must be processed in a multithreaded mode. You should avoid data races and all the related issues.

## 📖 Explanation:
### A new class `Request` has been added
It contains all the necessary information for successful processing:
1. Informational fields: `type`, `fromPhone`, `toNumber`.
2. A flag `success` indicating successful request execution.
3. A synchronization counter: `done`.

### The `PhoneCallMediator` class has been redesigned
All client interactions with `PhoneCallMediator` now occur via the `submitRequest` method. 
It places the request into a `requestQueue`, which is constantly read by server threads 
(in the `processRequests` method). These threads take requests from the queue and process them.

All call validations have been moved to `PhoneCallMediator`, since previously, some checks 
executed locally in `PhoneProxy` could become outdated by the time the request was processed 
on the server, due to non-atomicity.

### A request generator has been added to `Main`
With the `phoneCount` and `requestCount` variables, you can configure the number of active phones 
and the number of requests sent to each (each request is handled in a separate thread).
