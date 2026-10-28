# PRESENTATION SLIDES OUTLINE: FOOD DELIVERY NETWORK SIMULATION
**LAB211 — OOP with Java | FPT University**

---

## Slide 1: Title & Team Introduction
- **Topic:** Food Delivery Network Concurrency & Race Condition Simulation
- **Course:** LAB211 - OOP with Java (Semester 3)
- **Architecture:** MVC Pattern & Physical CSV File Persistence
- **Team:** NHÓM 01

---

## Slide 2: Research Problem (The Big Question)
- Storage constrained strictly to physical `.csv` files (no SQL database).
- 100+ concurrent drivers and customers placing orders simultaneously.
- **Big Question:** Which synchronization mechanism (NO_LOCK, SYNCHRONIZED, OPTIMISTIC, FILE_LOCK) guarantees **0% race condition errors** while keeping **throughput drop under 30%**?

---

## Slide 3: Three Severe Data Race Risks
1. **Double Assignment:** Order assigned to 2 drivers concurrently.
2. **Oversell MenuItem:** Item stock drops below 0 (negative stock).
3. **Driver Overload:** Driver assigned multiple active orders simultaneously.

---

## Slide 4: System Architecture (MVC Pattern)
- **Model:** 8 Entities (`Customer`, `Restaurant`, `MenuItem`, `Driver`, `Order`, `OrderItem`, `DeliveryRoute`, `SimulationRun`) + `GeoUtils` (Haversine).
- **Repository:** Generic `CsvRepository<T>` + Critical dispatch & lock logic in Repository layer.
- **Controller:** Flow orchestration.
- **View:** CLI ASCII grid map & Web Dashboard (HTML5 / Canvas / Chart.js).

---

## Slide 5: Data Generator Overview
- Synthetic dataset generation:
  - 2,000 Customers
  - 200 Restaurants
  - 2,000 Menu Items
  - 500 Drivers
  - 3,000 Orders
  - 9,000 Order Items
  - 2,000 Delivery Routes
- **Total Dataset:** 18,700 CSV lines (exceeds requirement of 10,000 lines).

---

## Slide 6: Four Concurrency Lock Mechanisms Tested
1. **NO_LOCK:** Baseline without locks.
2. **SYNCHRONIZED:** Per-resource key synchronization locks.
3. **OPTIMISTIC LOCKING:** Version checking column validation (`version++`).
4. **FILE_LOCK:** Java NIO FileChannel file-level lock.

---

## Slide 7: Experimental Results & Benchmark Table
- **NO_LOCK:** 18.8% Error Rate (High data corruption).
- **FILE_LOCK:** 0% Errors, but 42.7% Throughput Drop (Disk bottleneck).
- **SYNCHRONIZED:** 0% Errors, 18.3% Throughput Drop.
- **OPTIMISTIC:** 0% Errors, 12.3% Throughput Drop (WINNER!).

---

## Slide 8: Research Conclusion & Q&A
- **Optimistic Locking** achieves 0% errors with minimal throughput impact (~12.3% drop).
- Meets all project rubrics and zero-penalty architectural constraints.
