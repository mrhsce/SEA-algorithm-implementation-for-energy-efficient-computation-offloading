# SEA: Suspension-Aware Energy-efficient Offloading Decision Maker

## Overview

This project implements the **SEA (Suspension-Aware Energy-optimization Algorithm)**, a sophisticated decision-making framework for computation offloading in resource-constrained embedded systems such as mobile devices. The framework addresses a critical challenge in mobile computing: **how to intelligently decide which computations should run locally versus remotely** to simultaneously achieve both **timing predictability** (bounded response times) and **energy efficiency**.

## What is Computation Offloading?

Computation offloading is a technique that selectively migrates computationally intensive tasks from a power-limited local device (e.g., smartphone) to powerful remote servers. This strategy allows mobile applications to:
- Execute resource-demanding operations more efficiently
- Reduce battery consumption on the device
- Maintain predictable response times for time-sensitive applications

**Challenge:** The offloading decision is non-trivial—while offloading can reduce energy consumption, it introduces communication delays and overhead costs. The goal is to optimize both energy consumption AND ensure bounded response times.

## Key Problem Addressed

This implementation is motivated by real-world applications like **mobile augmented reality (AR) systems** with SLAM (Simultaneous Localization and Mapping) algorithms that require:
- **Soft real-time constraints**: Bounded response times to ensure functional correctness
- **Energy efficiency**: Minimized battery drain on mobile devices
- **Multi-task support**: Handling multiple concurrent tasks without violating timing constraints

## The SEA Algorithm

The SEA algorithm makes intelligent offloading decisions by considering both timing and energy constraints. It operates in two distinct strategies:

### Strategy 1: SEA_S_Oblivion (Suspension-Oblivious)
- Focuses purely on ensuring timing predictability without explicitly modeling offloading-induced delays as suspensions
- Provides baseline temporal correctness guarantees

### Strategy 2: SEA_S_Aware (Suspension-Aware) ⭐ **Recommended**
A more sophisticated approach that:
1. **Models offloading delays as suspensions**: Treats communication and remote execution delays as task suspensions at the local level
2. **Filters impractical offloads**: Eliminates tasks where offloading overhead exceeds local execution time
3. **Prioritizes by efficiency**: Sorts tasks by the ratio of suspension time to task period (S_O/T) in descending order
4. **Ensures CPU constraints**: Iteratively assigns offloading decisions to satisfy CPU utilization bounds
5. **Optimizes energy**: For energy-aware mode, further improves energy consumption while maintaining timing guarantees

### Algorithm Steps (SEA_S_Aware):
```
1. Filter tasks with overhead > local execution time
2. Sort remaining tasks by suspension-to-period ratio (S_O/T)
3. Determine which tasks can be offloaded while respecting CPU constraints
4. If energy profile provided: Further optimize by reconsidering offloading decisions
   to minimize energy consumption without exceeding CPU limits
5. Output offloading decisions for each task
```

## Core Components

### Task.java
Represents individual tasks with three possible offloading states:
- `OFFLOADED` (1): Execute remotely
- `UNOFFLOADED` (0): Execute locally
- `UNDECIDED` (2): Not yet determined

Each task contains an `ApplicationProfile` defining its computational characteristics.

### ApplicationProfile.java
Encapsulates the performance characteristics of a task:
- **C_L**: Execution time of local-only phases (e.g., UI rendering, sensor access)
- **C_O**: Execution time of offloadable phases running locally
- **S_T**: Data transmission time to/from remote server
- **S_S**: Remote computation time
- **S_O**: Total offloading-induced delay (S_T + S_S)
- **C_OH**: Offloading overhead (encryption, setup, etc.)
- **T**: Task period (recurrence interval)

### EnergyProfile.java
Models device energy consumption rates:
- **P_O**: WiFi component power consumption (mW)
- **P_L**: CPU component power consumption (mW)
- **P_I**: Idle state power consumption (mW)

### SEA_S_Aware.java (Decision Maker)
The core algorithm implementation with two public methods:

**Method 1: Basic Decision (Timing Only)**
```java
decideOffloadingFor(ArrayList<Task> tasks, Integer cpuCount)
```
Makes offloading decisions based solely on timing constraints.

**Method 2: Energy-Aware Decision (Timing + Energy)**
```java
decideOffloadingFor(ArrayList<Task> tasks, EnergyProfile energyProfile, Integer cpuCount)
```
Makes offloading decisions optimizing both timing predictability and energy efficiency.

## Usage Example

```java
// Create a list of tasks with their application profiles
ArrayList<Task> taskList = new ArrayList<>();
taskList.add(new Task(1, new ApplicationProfile(1, 8, 0, 2, 5, 2, 1, 12)));
taskList.add(new Task(2, new ApplicationProfile(0, 5, 1, 1, 1, 0, 0, 12)));
taskList.add(new Task(3, new ApplicationProfile(1, 8, 0, 2, 4, 0, 0, 12)));

// Define the device's energy characteristics
EnergyProfile energyProfile = new EnergyProfile(10, 12, 1);  // P_O, P_L, P_I

// Get the decision maker (singleton)
SEA_S_Aware decisionMaker = SEA_S_Aware.getInstance();

// Make offloading decisions
// cpuCount = number of CPU cores available
decisionMaker.decideOffloadingFor(taskList, energyProfile, 2);

// Retrieve decisions
for (Task task : taskList) {
    int offloadStatus = task.getOffloadStatus();
    System.out.println("Task " + task.getId() + ": " + 
        (offloadStatus == Task.OFFLOADED ? "OFFLOAD" : "LOCAL"));
}
```

## Framework Architecture

```
┌─────────────────────────────────────────────────┐
│         Embedded Device (Smartphone)            │
├─────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────┐   │
│  │      SEA Decision Maker                   │   │
│  │  (Makes offloading decisions)             │   │
│  └──────────────────────────────────────────┘   │
│           ↓              ↓                       │
│  ┌──────────────┐  ┌──────────────┐            │
│  │Real-time     │  │Energy        │            │
│  │Scheduler     │  │Monitor       │            │
│  └──────────────┘  └──────────────┘            │
│           ↓                 ↓                    │
│  ┌─────────────────────────────────────────┐   │
│  │      Offloading Engine                   │   │
│  │  (Handles task transmission)             │   │
│  └─────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────┘
                       │ Network
                       ↓
┌─────────────────────────────────────────────────┐
│       Remote Server / Cloud Resource            │
│  (Executes offloaded task components)           │
└─────────────────────────────────────────────────┘
```

## Key Features

✅ **Dual Optimization**: Balances timing predictability and energy efficiency  
✅ **Soft Real-time Support**: Guarantees bounded response times for applications  
✅ **Multi-task Handling**: Makes decisions for multiple concurrent tasks  
✅ **Energy-Aware**: Minimizes battery drain while respecting timing constraints  
✅ **Efficient Algorithm**: O(n log n) complexity for practical deployment  
✅ **Realistic Overhead Modeling**: Accounts for communication and setup costs  

## Research Foundation

This project is based on the research paper:

**"An Energy-efficient Offloading Framework with Predictable Temporal Correctness"**
- Authors: Zheng Dong, Yuchuan Liu, Zhou Husheng, Xusheng Xiao, Yu Gu, Lingming Zhang, Cong Liu
- Venue: SEC '17 (2017 ACM SIGOPS Conference on Systems and Elastic Computing)
- DOI: 10.1145/3132211.3134448

The paper provides formal timing analysis, a 0-1 integer linear programming (ILP) solution for optimal offloading, and comprehensive experimental evaluation on Android platforms.

## Use Cases

- **Mobile Augmented Reality**: AR apps requiring low-latency image processing
- **Real-time Sensor Processing**: IoT devices with timing-sensitive computations
- **Mobile Gaming**: Graphics-intensive games with backend offloading
- **Edge Computing**: Any scenario requiring joint optimization of latency and energy

## Future Enhancements

- Implementation of SEA_S_Oblivion algorithm
- Integration with actual Android platforms
- Support for heterogeneous processors
- Dynamic re-evaluation of offloading decisions at runtime
- Machine learning-based prediction of network conditions
