# Multithreaded Web Server and Client with Cache Implementations in Java

This repository contains various implementations of Java servers and clients using socket programming. The servers demonstrate handling requests in different ways: single-threaded, multi-threaded, and with a thread pool. The **ThreadPool** Server is designed to handle high-load scenarios and can be tested using JMeter. Additionally, a caching mechanism is implemented to optimize performance by storing and reusing responses for repeated client requests, reducing server processing time and improving efficiency.

---

## Table of Contents

1. [Project Overview](#project-overview)  
2. [Prerequisites](#prerequisites)  
3. [SingleThreaded Server and Client](#singlethreaded-server-and-client)  
4. [MultiThreaded Server and Client](#multithreaded-server-and-client)  
5. [ThreadPool Server with JMeter](#threadpool-server-with-jmeter)  

---

## Project Overview

- **SingleThreaded Server**: Handles one client at a time.  
- **MultiThreaded Server**: Handles multiple clients concurrently by spawning new threads for each connection.  
- **ThreadPool Server**: Uses a fixed thread pool to efficiently handle a large number of concurrent requests.

---

## Prerequisites

1. **Java Development Kit (JDK)** installed (version 8 or higher).  
2. **JMeter** installed (for testing the ThreadPool server).  
3. **Command Prompt (CMD)** or any terminal to compile and run Java programs.  

---

## SingleThreaded Server and Client

### What It Does

The **SingleThreaded Server** listens on a specific port and handles one client connection at a time. The **Client** connects to the server and sends a message. The server responds with a greeting message.

### Steps to Run

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/multithreaded-webserver.git
   cd multithreaded-webserver
   ```

   Alternatively, download the ZIP file and extract it.

2. **Navigate to the SingleThreaded Directory**

   ```bash
   cd SingleThreaded
   ```

3. **Compile the Server**

   ```bash
   javac SingleThreadedServer.java
   ```

4. **Run the Server**

   ```bash
   java SingleThreadedServer
   ```

   The server will start listening on port 8010.

5. **Open a New CMD and Navigate to the SingleThreaded Directory**

   ```bash
   cd SingleThreaded
   ```

6. **Compile the Client**

   ```bash
   javac SingleThreadedClient.java
   ```

7. **Run the Client**

   ```bash
   java SingleThreadedClient
   ```

8. **Check the Connection**

   - The client sends a message to the server.  
   - The server responds with a greeting message, which is displayed on the client.

---

## MultiThreaded Server and Client

### What It Does

The **MultiThreaded Server** can handle multiple client connections concurrently by creating a new thread for each client. The **Client** connects to the server and sends a message. The server responds with a greeting message.

### Steps to Run

1. **Navigate to the MultiThreaded Directory**

   ```bash
   cd ../MultiThreaded
   ```

2. **Compile the Server**

   ```bash
   javac MultiThreadedServer.java
   ```

3. **Run the Server**

   ```bash
   java MultiThreadedServer
   ```

   The server will start listening on port 8020.

4. **Open a New CMD and Navigate to the MultiThreaded Directory**

   ```bash
   cd ../MultiThreaded
   ```

5. **Compile the Client**

   ```bash
   javac MultiThreadedClient.java
   ```

6. **Run Multiple Clients**

   You can run multiple clients by executing the following command multiple times or by running the client in a loop:

   ```bash
   java MultiThreadedClient
   ```

7. **Check the Connection**

   - Each client sends a message to the server.  
   - The server handles each client concurrently and responds to each one.

---

## ThreadPool Server with JMeter

### What It Does

The **ThreadPool Server** uses a fixed thread pool to handle multiple client connections efficiently. This server is ideal for handling high-load scenarios. Instead of a Java client, we use **JMeter** to simulate a large number of concurrent requests.

### Steps to Run

1. **Navigate to the ThreadPool Directory**

   ```bash
   cd ../ThreadPool
   ```

2. **Compile the Server**

   ```bash
   javac ThreadPoolServer.java
   ```

3. **Run the Server**

   ```bash
   java ThreadPoolServer
   ```

   The server will start listening on port 8010.

4. **Test with JMeter**

   - **Create a JMeter Test Plan**:
     1. Open **JMeter**.
     2. Add a **Thread Group** with the desired number of threads (e.g., 300).
     3. Add a **TCP Sampler** and set the following:
        - **Server Name**: `localhost`  
        - **Port Number**: `8010`  
        - **Text to Send**: Any message (e.g., "Hello from JMeter")  
     4. Add a **Listener** (e.g., **View Results Tree**) to see the responses.
   - **Run the Test Plan**.
   
5. **Check the Results**

   - Verify the responses in the JMeter Listener.  
   - The server should handle multiple requests concurrently and respond to each one.

---

## Conclusion

This project demonstrates different concurrency models in Java socket programming:

1. **Single-threaded** for simplicity.  
2. **Multi-threaded** for concurrent clients.  
3. **Thread pool** for efficient, scalable request handling under high load.

Recruiters can easily test and understand the code by following the steps provided.

---

## Owner

This project is maintained by **Junaid Shaik**.  
GitHub: [https://github.com/junaid-shaikk](https://github.com/junaid-shaikk)

---

### Thank you for checking out my project! 🚀
