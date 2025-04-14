/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package os2a;

import java.util.*;

/**
 *
 * @author 96655
 */

class MemoryBlock {
    int blockSize;
    int startAddress;
    int endAddress;
    boolean isAllocated;
    String processId;
    int internalFragmentation;

    MemoryBlock(int blockSize, int startAddress) {
        this.blockSize = blockSize;
        this.startAddress = startAddress;
        this.endAddress = startAddress + blockSize - 1;
        this.isAllocated = false;
        this.processId = "Null";
        this.internalFragmentation = 0;
    }
}

class MemoryManager {
    List<MemoryBlock> blocks;
    int allocationStrategy; // 1 = first-fit, 2 = best-fit, 3 = worst-fit

    MemoryManager(List<Integer> sizes, int allocationStrategy) {
        this.blocks = new ArrayList<>();
        this.allocationStrategy = allocationStrategy;
        int address = 0;
        for (int size : sizes) {
            blocks.add(new MemoryBlock(size, address));
            address += size;
        }
    }

    void allocate(String pid, int size) {
        MemoryBlock selected = null;
        for (MemoryBlock block : blocks) {
            if (!block.isAllocated && block.blockSize >= size) {
                if (selected == null ||
                    (allocationStrategy == 1) ||
                    (allocationStrategy == 2 && block.blockSize < selected.blockSize) ||
                    (allocationStrategy == 3 && block.blockSize > selected.blockSize)) {
                    selected = block;
                    if (allocationStrategy == 1) break;
                }
            }
        }
        if (selected == null) {
            System.out.println("No suitable block found. Request rejected.");
            return;
        }
        selected.isAllocated = true;
        selected.processId = pid;
        selected.internalFragmentation = selected.blockSize - size;
        System.out.println(pid + " allocated at address: " + selected.startAddress + ", internal fragmentation: " + selected.internalFragmentation);
    }

    void deallocate(String pid) {
        for (MemoryBlock block : blocks) {
            if (block.isAllocated && block.processId.equals(pid)) {
                block.isAllocated = false;
                block.processId = "Null";
                block.internalFragmentation = 0;
                System.out.println("Process " + pid + " deallocated.");
                return;
            }
        }
        System.out.println("Process ID not found.");
    }

    void report() {
        System.out.println("=================================================================");
        System.out.printf("%-8s %-10s %-12s %-10s %-12s %-10s\n", "Block#", "Size", "Start-End", "Status", "ProcessID", "Fragment");
        System.out.println("=================================================================");
        for (int i = 0; i < blocks.size(); i++) {
            MemoryBlock block = blocks.get(i);
            String status = block.isAllocated ? "Allocated" : "Free";
            System.out.printf("%-8d %-10d %-12s %-10s %-12s %-10d\n",
                    i,
                    block.blockSize,
                    block.startAddress + "-" + block.endAddress,
                    status,
                    block.processId,
                    block.internalFragmentation);
        }
    }
}

public class Os2A {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter total number of blocks: ");
        int m = scanner.nextInt();

        List<Integer> sizes = new ArrayList<>();
        System.out.println("Enter the size of each block in KB:");
        for (int i = 0; i < m; i++) {
            sizes.add(scanner.nextInt());
        }

        System.out.print("Enter allocation strategy (1=First-Fit, 2=Best-Fit, 3=Worst-Fit): ");
        int strategy = scanner.nextInt();

        MemoryManager manager = new MemoryManager(sizes, strategy);
System.out.println("Memory blocks are created…");
System.out.println("Memory blocks:");
System.out.println("================================================");
System.out.printf("%-8s %-8s %-15s %-8s\n", "Block#", "Size", "Start-End", "Status");
System.out.println("================================================");
for (int i = 0; i < sizes.size(); i++) {
    int start = manager.blocks.get(i).startAddress;
    int end = manager.blocks.get(i).endAddress;
    int size = manager.blocks.get(i).blockSize;
    System.out.printf("Block%-3d %-8d %-15s %-8s\n", i, size, start + "-" + end, "free");
}

        while (true) {
            System.out.println("\n1) Allocate memory\n2) Deallocate memory\n3) Print report\n4) Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter process ID and size: ");
                    String pid = scanner.next();
                    int size = scanner.nextInt();
                    manager.allocate(pid, size);
                    break;
                case 2:
                    System.out.print("Enter process ID to deallocate: ");
                    String pidDealloc = scanner.next();
                    manager.deallocate(pidDealloc);
                    break;
                case 3:
                    manager.report();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

    
