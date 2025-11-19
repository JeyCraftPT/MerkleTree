# 🌳 Merkle Tree Implementation

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

A robust **Java implementation** of a Merkle Tree data structure. This project demonstrates how to maintain data integrity and verify the contents of large datasets using cryptographic hashing (SHA-256).

Developed as part of a university project at **UBI** (Universidade da Beira Interior).

---

## 📄 About The Project

A **Merkle Tree** (or Hash Tree) is a tree in which every leaf node is labelled with the cryptographic hash of a data block, and every non-leaf node is labelled with the cryptographic hash of the labels of its child nodes. 

This project allows you to:
* **Construct** a Merkle Tree from a list of data blocks (strings/files).
* **Calculate** the Root Hash (Master Hash).
* **Verify** if specific data belongs to the tree without revealing the entire dataset.
* **Detect** any changes or corruption in the data.

### 📂 Repository Structure

* `mktree/` - Source code for the Java implementation.
* `Relatorio.md` / `relatorio.pdf` - Detailed project report and theoretical documentation (Portuguese).
* `mktree.zip` - Compressed source files.

---

## 🚀 Getting Started

### Prerequisites

* **Java Development Kit (JDK)** 8 or higher.
* **IntelliJ IDEA** (recommended, as `.idea` files are included) or any Java IDE.

### Installation

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/JeyCraftPT/MerkelTree.git](https://github.com/JeyCraftPT/MerkelTree.git)
   ```
   
2. **Open the project:**
* Open IntelliJ IDEA.
* Select `File > Open` and navigate to the `MerkelTree` folder.

### Usage
1. Navigate to the source folder inside `mktree`.
2. Compile the Java files (or run via IDE):
```bash
javac Main.java
```
3. Run the main application to see the tree construction in action:
```bash
java Main
``` 
