# Process Scheduling Simulator

### CSC227: Operating System  


## Programming Assignment 1  
### Process Scheduling Simulator  

### **Team Members**  
| ID          | Name                     |
|------------|-------------------------|
| 444201026  | Lama Faihan Albugami    |
| 444200487  | Amani Eid Alharbi       |
| 443204616  | Jumana Juma Shati       |

---

## Task Distribution Table
| Student Name        | Role |
|---------------------|------|
|Lama Faihan Albugami |      |
|                     |      |
|                     |      |

---

## Output

### **i) First Come, First Served (FCFS) Algorithm**
The program executes a CPU scheduling algorithm where the user inputs three processes with the following details:

- **P1**: Arrival Time = 0, Burst Time = 5  
- **P2**: Arrival Time = 1, Burst Time = 5  
- **P3**: Arrival Time = 1, Burst Time = 7  
- **Context Switch Time**: 1  

#### **Gantt Chart Execution:**
- **P1** starts execution first and runs until completion.
- **P2** starts after **P1** finishes and runs until completion.
- **P3** executes last and completes the schedule.
- This follows the **First Come, First Served (FCFS)** scheduling algorithm, as each process executes in the order of arrival without preemption.

---

### **ii) Preemptive Shortest Remaining Time First (SRTF) Algorithm**
This simulation demonstrates **Preemptive Shortest Job First (SRTF)** scheduling. The user provides:
- **Number of processes**
- **Arrival times**
- **Burst times**
- **Context switch time**

#### **Execution Behavior:**
- **P1** starts execution first.
- **P1** is **preempted** when **P2** arrives at time 2, as **P2** has a shorter burst time.
- After **P2** completes, **P1** resumes execution and runs until it finishes.

The Gantt Chart visually represents this preemptive behavior.

---

### **Notes**
- The implementation showcases two different scheduling algorithms.
- The Gantt Chart illustrates the execution order of processes in both FCFS and SRTF algorithms.

---

