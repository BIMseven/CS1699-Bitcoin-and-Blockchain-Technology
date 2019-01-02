# CS1699 Deliverable 2: Mining
[cs1699]

Benjamin Miller
BIMseven
https://github.com/BIMseven?tab=repositories
	Mine.java maximizes transaction fees by following the ‘Greedy Algorithm” outline in “Different Approaches to Solve the 0/1 Knapsack Problem”(http://www.micsymposium.org/mics_2005/papers/paper102.pdf). It reads in a file of transactions, evaluates the weight (number of transaction inputs) and transaction fee, and stores them in a Java TreeMap. When building a candidate block the program iterates through the list of transactions, in the order of highest to lowest value-weight ratio, adding each transaction to a block whose weight + transaction <= 16 (the maximum weight of a block).


