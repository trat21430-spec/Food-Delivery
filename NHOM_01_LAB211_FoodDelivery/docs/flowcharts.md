# FLOWCHARTS: FOOD DELIVERY NETWORK SIMULATION WORKFLOWS

## 1. Order & Driver Dispatch Flowchart
```mermaid
graph TD
    A[Customer Initiates Order] --> B{Check MenuItem Stock}
    B -- Stock <= 0 --> C[Reject Order - Oversell Exception]
    B -- Stock > 0 --> D[Deduct Stock in MenuItemRepository]
    D --> E[Create Pending Order in orders.csv]
    E --> F[Calculate Haversine Distance to find Nearest Driver]
    F --> G{Is Driver AVAILABLE?}
    G -- No --> H[Wait / Retry Dispatch]
    G -- Yes --> I[Assign Driver in OrderRepository]
    I --> J[Mark Driver BUSY in DriverRepository]
    J --> K[Order Confirmed & Delivery Route Logged]
```

---

## 2. Race Condition Prevention Mechanics Flowchart
```mermaid
graph TD
    Start[Concurrency Operation] --> LockChoice{Lock Mechanism Selected?}
    LockChoice -- NO_LOCK --> NL[Direct CSV Read/Write - Widen Race Window]
    NL --> NLErr[Data Race Occurs: Double Assign / Oversell / Driver Overload]

    LockChoice -- SYNCHRONIZED --> Sync[Acquire Java Memory Object Lock per Resource ID]
    Sync --> SyncExec[Atomic Read-Check-Write Execution]
    SyncExec --> SyncPass[0% Errors]

    LockChoice -- OPTIMISTIC --> Opt[Read Entity & Record Expected Version]
    Opt --> OptCheck{Compare Current Version == Expected Version}
    OptCheck -- Mismatch --> OptFail[Throw OptimisticLockException - Retry Operation]
    OptCheck -- Match --> OptSuccess[Increment Version & Save CSV - 0% Errors]

    LockChoice -- FILE_LOCK --> FLock[Acquire OS FileChannel Lock on target CSV]
    FLock --> FLExec[Read-Write CSV exclusively]
    FLExec --> FLPass[0% Errors - High Throughput Penalty]
```

---

## 3. Simulator & Post-Verification Flowchart
```mermaid
graph TD
    SInit[Initialize Simulator] --> SSetup[Setup CountDownLatch startLatch & finishLatch]
    SSetup --> STH[Spawn N Threads: CustomerThread & DispatchThread]
    STH --> SWait[Threads block at startLatch.await]
    SWait --> SGo[startLatch.countDown - Simultaneous Execution]
    SGo --> SExec[Execute Concurrent Orders across ThreadPool]
    SExec --> SFinish[finishLatch.await & Shutdown ThreadPool]
    SFinish --> SVerify[Run Direct CSV Post-Verification Algorithms]
    SVerify --> V1[detectDoubleAssignments]
    SVerify --> V2[detectOversellItems]
    SVerify --> V3[detectDriverOverload]
    V1 & V2 & V3 --> SReport[Generate 8-Column Comparison Report]
```
