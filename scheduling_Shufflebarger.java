package scheduling_Shufflebarger;
/*****************************************
Course Number: CS4345
Semester/Year: Spring 2022
Assignment 3
Author: Ethan Shufflebarger
*****************************************/


import java.util.ArrayList;
import java.util.Scanner;

public class scheduling_Shufflebarger {
	
	static ArrayList<process> processes = new ArrayList<process>();
	static ArrayList<process> SJF = new ArrayList<>();
	static ArrayList<process> priority = new ArrayList<>();
	static int avgWaitTimeSJF = 0;
	static int avgWaitTimePriority = 0;
	static int avgWaitTimeRoundRobin = 0;
	
	public static void main(String[] args) {
		
		//Generates a list of processes and prints it
		createProcessList();
		printProcesses();
		
		//Prompts user to enter a new process and prints an updated list
		createUserProccess();
		printProcesses();
		
		//Prints headed for our sorting lists
		System.out.println("\n	Scheduling Algorithm	|	Process ID	|	Priority	|	Burst-length	 |	Total waiting time");
		
		//Sorts list with shortest job first algorithm and prints it ordered by ID
		sjfSorting();
		orderSJFByID();
		
		//Sorts list with priority sorting algorithm and prints it ordered by ID
		prioritySorting();
		orderPriorityByID();
		
		//Sorts list with round robin sorting algorithm and prints it ordered by ID
		roundRobinSorting();
		orderRoundRobinByID();
		
		//Prints a list of each sorting algorithm ordered from least to greatest average waiting time
		printQuickestAlgo();
	}
	
	//Creates a random number 1 - 10 and assigns it to the process's ID, unless it is already used
	public static int createID() {
		int ID = (int) ((Math.random() * (11 - 1)) + 1);
	    for (int i = 0; i < processes.size(); i++) {
			while (processes.get(i).getID() == ID) {
				i = 0;
				ID = (int) ((Math.random() * (11 - 1)) + 1);
			}
		}
		return ID;
	}
	
	//Generates random number between 1 - 10 and assigns it to the process's priority
	public static int createPriority() {
		return (int) ((Math.random() * (11 - 1)) + 1);
	}
	
	//Generates random number between 20 - 100 and assigns it to the process's burst length
	public static int createBurstLength() {
		return (int) ((Math.random() * (20 - 101)) + 101);
	}
	
	//Creates a random process for insertion
	public static process createProcess() {
		process pro = new process();
		
		pro.id = createID();
		pro.priority = createPriority();
		pro.burstLength = createBurstLength();
		pro.robinRounds = 0;
		pro.completionStatus = false;
		
		return pro;
	}
	
	//Uses createProcess() to generate a list of length 5 of random processes
	public static void createProcessList() {
		for (int i = 0; i < 5; i++) {
			processes.add(createProcess());
		}	
	}
	
	//Prompts user for information which is used to create a process, which is inserted into the main processes
	public static void createUserProccess() {
		
		//Creates scanner and prompts user for integer for ID
		@SuppressWarnings("resource")
		Scanner myObj = new Scanner(System.in);
	    System.out.print("\nEnter a new ID that is not already in use between 1 and 10: ");

	    int ID = myObj.nextInt();
	    
	    //Makes sure ID is between 1 - 10
	    if (ID < 1 || ID > 10) {
	    	System.out.print("Number is not between 1 and 10! Enter again: ");
	    	ID = myObj.nextInt();
	    }
	    
	    //Makes sure ID is unique
	    for (int i = 0; i < processes.size(); i++) {
			while (processes.get(i).getID() == ID) {
				System.out.print("ID already in use! Enter again: ");
				i = 0;
				ID = myObj.nextInt();
			}
		}
	    
	    //Prompts user for an integer for priority
	    System.out.print("Enter a priority between 1 and 10 for your process: ");
	    int priority = myObj.nextInt();
	    
	    //Makes sure entered integer is within 1 - 10
	    while (priority < 1 || priority > 10) {
	    	System.out.print("Number is not between 1 and 10! Enter again: ");
	    	priority = myObj.nextInt();
	    }
	    
	    //Prompts user for an integer for burst length
	    System.out.print("Enter a burst length between between 20 and 100 for your process: ");
	    int burstLength = myObj.nextInt();
	    
	    //Makes sure entered integer is within 20 - 100
	    while (burstLength < 20 || burstLength > 100) {
	    	System.out.print("Number is not between 20 and 100! Enter again: ");
	    	burstLength = myObj.nextInt();
	    }
	    
	    System.out.print("\n");
	    
	    //Creates a process using input numbers
	    process pro = new process();
		
		pro.id = ID;
		pro.priority = priority;
		pro.burstLength = burstLength;
		pro.robinRounds = 0;
		pro.completionStatus = false;
		
		//Inserts process into list
		processes.add(pro);
	    
	}
	
	//Sorts processes using shortest job first
	public static void sjfSorting() {
		
		//Creates variables for finding lowest burst length and its index, as well as for storing wait times
		int lowestBurstLength = 101;
		int idxLowestBurstLength = 0;
		int waitTime = 0;
		
		//Creates a copy of the list used for sorting
		ArrayList<process> arr = new ArrayList<process>();
		arr.addAll(processes);
		
		//Finds smallest burst time in the array
		for (int i = 0; i < 6; i++ ) {
			for (int j = 0; j < arr.size(); j++) {
				if (arr.get(j).getBurstLength() < lowestBurstLength) {
					lowestBurstLength = arr.get(j).getBurstLength();
					idxLowestBurstLength = j;
				}
			}
			SJF.add(arr.get(idxLowestBurstLength));
			arr.remove(idxLowestBurstLength);
			
			//Calculates wait times
			waitTime += SJF.get(i).getBurstLength();
			SJF.get(i).totalWaitTime = waitTime;
			avgWaitTimeSJF += waitTime;
			
			//Sets loop up to find next smallest burst time
			lowestBurstLength = 101;
			idxLowestBurstLength = 0;
		}
		//Calculates average wait time
		avgWaitTimeSJF = avgWaitTimeSJF / 6;
	}
	
	//Sorts processes using priority sorting
	public static void prioritySorting() {
		//Creates variables for finding lowest burst length and its index, as well as for storing wait times
		int lowestPriority = 11;
		int idxlowestPriority = 0;
		int waitTime = 0;
		
		//Creates a copy of the list used for sorting
		ArrayList<process> arr = new ArrayList<process>();
		arr.addAll(processes);
		
		//Finds smallest priority in the array
		for (int i = 0; i < 6; i++ ) {
			for (int j = 0; j < arr.size(); j++) {
				if (arr.get(j).getPriority() < lowestPriority) {
					lowestPriority = arr.get(j).getPriority();
					idxlowestPriority = j;
				}
			}
			
			//Inserts smallest priority of that iteration into array
			priority.add(arr.get(idxlowestPriority));
			arr.remove(idxlowestPriority);
			
			//Calculates wait times
			waitTime += priority.get(i).getBurstLength();
			priority.get(i).totalWaitTime = waitTime;
			avgWaitTimePriority += waitTime;
						
			//Sets loop up to find next smallest burst time
			lowestPriority = 11;
			idxlowestPriority = 0;
		}	
		//Calculates average wait time
		avgWaitTimePriority = avgWaitTimePriority / 6;
	}
	
	//Sorts processes using shortest job first
	public static void roundRobinSorting() {
		int waitTime = 0;
		ArrayList<process> arr = new ArrayList<process>();
		arr.addAll(priority);
		
		for (int j = 0; j < 6; j++) {
			//Determines if process can be finished in this cycle, if not, moves on while incrementing the wait time
			for (int i = 0; i < priority.size(); i++) {
				if ((priority.get(i).getBurstLength() - (25 * priority.get(i).getRobinRounds())) > 25 && priority.get(i).getCompletionStatus() == false) { 
					waitTime += 25;
					priority.get(i).robinRounds += 1;
				}
				//Determines process can be completed this cycle, updating wait time and total wait time appropriately
				else if (priority.get(i).getCompletionStatus() == false) {
					waitTime += (priority.get(i).getBurstLength() - (25 * priority.get(i).getRobinRounds()));
					avgWaitTimeRoundRobin += waitTime;
					
					priority.get(i).totalWaitTime = waitTime;
					priority.get(i).completionStatus = true;
				}
				else {
				}
			}		
		}
		avgWaitTimeRoundRobin = avgWaitTimeRoundRobin / 6;
	}
	
	//Prints SJF array in order of ID
	public static void orderSJFByID() {
		ArrayList<process> arr = new ArrayList<process>();
		arr.addAll(SJF);
		int lowestID = 11;
		int idxLowestID = 0;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < arr.size(); j++) {
				if (arr.get(j).getID() < lowestID) {
					lowestID = arr.get(j).getID();
					idxLowestID = j;
				}
			}
			System.out.println("	         SJF      	|" + arr.get(idxLowestID).ToString() + "		 |	       " + arr.get(idxLowestID).getTotalWaitTime());
			arr.remove(idxLowestID);
			lowestID = 11;
			idxLowestID = 0;
		}
	}
	
	//Prints priority array in order of ID
	public static void orderPriorityByID() {
		ArrayList<process> arr2 = new ArrayList<>();
		arr2.addAll(priority);
		int lowestID = 11;
		int idxLowestID = 0;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < arr2.size(); j++) {
				if (arr2.get(j).getID() < lowestID) {
					lowestID = arr2.get(j).getID();
					idxLowestID = j;
				}
			}
			System.out.println("	      Priority    	|" + arr2.get(idxLowestID).ToString() + "		 |	       " + arr2.get(idxLowestID).getTotalWaitTime());
			arr2.remove(idxLowestID);
			lowestID = 11;
			idxLowestID = 0;
		}
	}
	
	//Prints resorted priority array in order of ID
	public static void orderRoundRobinByID() {
		ArrayList<process> arr = new ArrayList<>();
		arr.addAll(priority);
		int lowestID = 11;
		int idxLowestID = 0;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < arr.size(); j++) {
				if (arr.get(j).getID() < lowestID) {
					lowestID = arr.get(j).getID();
					idxLowestID = j;
				}
			}
			System.out.println("             Round Robin    	|" + arr.get(idxLowestID).ToString() + "		 |	       " + arr.get(idxLowestID).getTotalWaitTime());
			arr.remove(idxLowestID);
			lowestID = 11;
			idxLowestID = 0;
		}
	}
	
	//Prints each algorithm in order of shortest average run time
	public static void printQuickestAlgo() {
		System.out.println("\n	Scheduling Algorithm	|	Average waiting time");
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(avgWaitTimeSJF);
		arr.add(avgWaitTimePriority);
		arr.add(avgWaitTimeRoundRobin);
		
		int lowestTime = 6000;
		int idxLowestTime = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < arr.size(); j++) {
				if (arr.get(j) < lowestTime ) {
					lowestTime = arr.get(j);
					idxLowestTime = j;
				}
			}
			if (lowestTime == avgWaitTimeSJF) {
				System.out.println("	         SJF	        |	       " + avgWaitTimeSJF);
				arr.remove(idxLowestTime);
			}
			else if (lowestTime == avgWaitTimePriority) {
				System.out.println("	      Priority		|	       " + avgWaitTimePriority);
				arr.remove(idxLowestTime);
			}
			else {
				System.out.println("	     Round Robin	|	       " + avgWaitTimeRoundRobin);
				arr.remove(idxLowestTime);
			}
			lowestTime = 600;
			idxLowestTime = 0;
		}
	}
	
	//Helper method for easy process printing
	public static void printProcesses() {
		System.out.println("	Process ID	|	Priority	|	Burst-length");
		for (int i = 0; i < processes.size(); i++) {
			System.out.println(processes.get(i).ToString());
		}
	}
	
	//Process object made for easier data storage
	static class process {
		
		int id;
		int priority;
		int burstLength;
		int totalWaitTime;
		int robinRounds;
		boolean completionStatus;
		
		public int getID() {
			return id;
		}
		
		public int getPriority() {
			return priority;
		}
		
		public int getBurstLength() {
			return burstLength;
		}
		
		public int getTotalWaitTime() {
			return totalWaitTime;
		}
		
		public int getRobinRounds() {
			return robinRounds;
		}
		
		public boolean getCompletionStatus() {
			return completionStatus;
		}
		
		public String ToString() {
			String output = "	    " + getID() + "	  	|    	   " + getPriority() + "		|	     " + getBurstLength();
			return output;
		}
	}
}
