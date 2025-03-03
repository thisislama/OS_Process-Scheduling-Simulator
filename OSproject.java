
import java.util.*;

class Process {
    int id, arrivalTime, burstTime, remainingTime, completionTime, waitingTime, turnaroundTime;

    Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class OSproject {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] processes = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for P" + (i + 1) + ": ");
            int arrival = sc.nextInt();
            System.out.print("Enter burst time for P" + (i + 1) + ": ");
            int burst = sc.nextInt();
            processes[i] = new Process(i + 1, arrival, burst);
        }

        System.out.print("Enter context switch time (CS): ");
        int contextSwitch = sc.nextInt();

        scheduleProcesses(processes, contextSwitch);
        sc.close();
    }

   public static void scheduleProcesses(Process[] processes, int CS) {
    int n = processes.length;
    int currentTime = 0, completed = 0;
    int totalTurnaround = 0, totalWaiting = 0;
    int totalIdleTime = 0;
    int contextSwitches = 0;
    Process lastExecuted = null;

    Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));
    List<String> ganttChart = new ArrayList<>();
    PriorityQueue<Process> pq = new PriorityQueue<>((p1, p2) -> {
        if (p1.remainingTime != p2.remainingTime)
            return Integer.compare(p1.remainingTime, p2.remainingTime);
        return Integer.compare(p1.id, p2.id); // FCFS
    });
    int index = 0;

    while (completed < n) {
        while (index < n && processes[index].arrivalTime <= currentTime) {
            pq.add(processes[index]);
            index++;
        }

        if (!pq.isEmpty()) {
            Process shortest = pq.poll();

            if (lastExecuted != null && lastExecuted != shortest) {
                ganttChart.add(currentTime + "-" + (currentTime + CS) + " CS");
                currentTime += CS;
                contextSwitches++;
            }

            int executionStart = currentTime;
            while (shortest.remainingTime > 0 &&
                   (pq.isEmpty() || pq.peek().remainingTime >= shortest.remainingTime)) {
                shortest.remainingTime--;
                currentTime++;
                while (index < n && processes[index].arrivalTime <= currentTime) {
                    pq.add(processes[index]);
                    index++;
                }
            }
            ganttChart.add(executionStart + "-" + currentTime + " P" + shortest.id);

            if (shortest.remainingTime > 0) {
                pq.add(shortest);
            } else {
                shortest.completionTime = currentTime;
                shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
                totalTurnaround += shortest.turnaroundTime;
                totalWaiting += shortest.waitingTime;
                completed++;
            }
            lastExecuted = shortest;
        } else {
            ganttChart.add(currentTime + "-" + (currentTime + 1) + " Idle");
            currentTime++;
            totalIdleTime++;
        }
    }

    printResults(ganttChart, totalTurnaround, totalWaiting, totalIdleTime, CS, contextSwitches, currentTime, n);
}


    public static void printResults(List<String> ganttChart, int totalTurnaround, int totalWaiting, int totalIdleTime, int CS, int contextSwitches, int totalTime, int n) {
        System.out.println("\nGantt Chart:");
        for (String event : ganttChart) {
            System.out.println(event);
        }

        double avgTAT = (double) totalTurnaround / n;
        double avgWT = (double) totalWaiting / n;
        double cpuUtilization = ((double) (totalTime - totalIdleTime - (contextSwitches * CS)) / totalTime) * 100;

        System.out.println("\nPerformance Metrics:");
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
        System.out.printf("Average Waiting Time: %.2f\n", avgWT);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
}
