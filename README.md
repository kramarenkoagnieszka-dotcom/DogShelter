# Dog Shelter Management System

A comprehensive, console-based application built with Java to simulate the operations of a dog shelter. This project is designed with clean code principles and a modular architecture, demonstrating the practical application of core Java concepts to solve a real-world business problem.

## 🎯 Project Goals & Learning Objectives

The primary objective of this project is to showcase proficiency in **Java Core** and software design. Key areas covered include:

* **Advanced Logic & Algorithms:** Implementing a matching engine to connect adopters with suitable dogs based on complex criteria.
* **Data Persistence:** Handling I/O operations and JSON serialization to maintain system state.
* **OOP Principles:** Practical application of Inheritance, Polymorphism, and Abstraction.
* **Robustness:** Implementing comprehensive exception handling for user input and file operations.
* **Testing:** Writing clean, expressive unit tests to ensure business logic reliability.

---

## ✨ Core Features

### Role-Based Access Control (RBAC)
The system provides distinct functionalities based on user roles:
* **Admin:** Manages users, reviews adoption applications, and monitors global finances.
* **Staff:** Manages the shelter population and records care-related expenses.
* **Adopter:** Browses dogs, completes lifestyle questionnaires, and tracks adoption status.
* **Donor:** Supports the shelter through financial contributions and views donation history.

### Intelligent Matching Service
The algorithm calculates a compatibility score between adopters and dogs.
* **Hard Requirements:** Enforces critical constraints (e.g., matching a dog that requires a garden only with adopters who have one).
* **Soft Scoring:** Analyzes energy levels, compatibility with other pets, and experience levels to recommend the best possible matches.

### Financial Management
* **Real-time Tracking:** Monitors all incoming donations and outgoing expenses to maintain a shelter balance.
* **Cost Analysis:** Expenses are linked to specific dogs, allowing for detailed tracking of investment per animal.

### Data Persistence
The entire application state—including users, dog profiles, applications, and financial records—is serialized to a `shelter_data.json` file on shutdown and reloaded on startup using the **Jackson** library.

---

## 🏗️ Project Architecture

The project is organized into modular packages to ensure separation of concerns:

| Package | Responsibility |
| :--- | :--- |
| **model** | Data classes (POJOs). |
| **service** | Core business logic: `UserService`, `Shelter`, `AdoptionService`, `FinancialService`, and `MatchingService`. |
| **persistence** | The `FileHandler` class, abstracting the logic for saving and loading the system state. |
| **ui** | Role-specific classes responsible for rendering the text-based console interface. |
| **test** | Unit tests for the service layer built with **JUnit 5** and **AssertJ**. |

---

## 🛠️ Technologies

* **Language:** Java 21
* **Build Tool:** Maven
* **Key Libraries:**
    * **Jackson:** For robust JSON serialization and deserialization.
    * **JUnit 5 & AssertJ:** For unit testing and assertions.
* **Version Control:** Git
* **IDE:** IntelliJ IDEA

---

## 🔐 Default Credentials

You can use the following accounts to test different role functionalities:

| Role | Username | Password |
| :--- | :--- | :--- |
| **Admin** | `admin` | `admin` |
| **Staff** | `anna_vet` | `staffpass1` |
| **Adopter** | `marek_88` | `userpass1` |
| **Donor** | `robert_king` | `donorpass1` |

---

## 🚀 Getting Started

### Prerequisites
* Java 21 or higher
* Maven 3.x

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/yourusername/dog-shelter-system.git](https://github.com/yourusername/dog-shelter-system.git)
