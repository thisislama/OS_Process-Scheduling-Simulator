

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

public class SRTF_With_CS {
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
        boolean isSwitching = false;

        List<String> ganttChart = new ArrayList<>();

        while (completed < n) {
            Process shortest = null;
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (shortest == null || p.remainingTime < shortest.remainingTime ||
                            (p.remainingTime == shortest.remainingTime && p.arrivalTime < shortest.arrivalTime)) {
                        shortest = p;
                    }
                }
            }

            if (shortest != null) {
                if (isSwitching) {
                    ganttChart.add("CS");
                    currentTime += CS;
                    isSwitching = false;
                }

                ganttChart.add("P" + shortest.id);
                shortest.remainingTime--;
                currentTime++;

                if (shortest.remainingTime == 0) {
                    shortest.completionTime = currentTime;
                    shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;
                    shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;

                    totalTurnaround += shortest.turnaroundTime;
                    totalWaiting += shortest.waitingTime;
                    completed++;
                }

                // Check if the next process will cause a switch
                Process nextShortest = null;
                for (Process p : processes) {
                    if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                        if (nextShortest == null || p.remainingTime < nextShortest.remainingTime ||
                                (p.remainingTime == nextShortest.remainingTime && p.arrivalTime < nextShortest.arrivalTime)) {
                            nextShortest = p;
                        }
                    }
                }

                if (nextShortest != null && nextShortest != shortest) {
                    isSwitching = true;
                }
            } else {
                currentTime++;
            }
        }

        printResults(processes, ganttChart, totalTurnaround, totalWaiting, CS, currentTime);
    }

    public static void printResults(Process[] processes, List<String> ganttChart, int totalTurnaround, int totalWaiting, int CS, int totalTime) {
        System.out.println("\nGantt Chart:");
        for (String event : ganttChart) {
            System.out.print(event + " -> ");
        }
        System.out.println("\n");

        int n = processes.length;
        double avgTAT = (double) totalTurnaround / n;
        double avgWT = (double) totalWaiting / n;
        double cpuUtilization = (double) (totalTime - (CS * (ganttChart.size() / 2))) / totalTime * 100;

        System.out.println("Performance Metrics:");
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
        System.out.printf("Average Waiting Time: %.2f\n", avgWT);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
}
