package amazon.com.interview;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;


public class Solution {
	public class IpOccurrence {
		public String s;
		public int occurence;
		public boolean isInMinHeap;
		public IpOccurrence(String s, int occurence) {
			this.s = s;
			this.occurence = occurence;
			isInMinHeap = false;
		}
		
		// for print purpose
		public String toString() {
			return "IP:" + s + " hit rate:" + occurence;
		}
	}
	
	/**
	 * 
	 * @param ips a list of ip addresses
	 * @param k the top k common ips
	 * 
	 * 
	 */
	public void printTopKFrequentHitIps(ArrayList<String> ips, int k) {
		HashMap<String, IpOccurrence> ipOccurrenceMap = new HashMap<String, IpOccurrence>();
		PriorityQueue<IpOccurrence> minHeap = new PriorityQueue<IpOccurrence>(
				k, new Comparator<IpOccurrence>() {
					@Override
					public int compare(IpOccurrence io1,
							IpOccurrence io2) {
						return Integer.compare(io1.occurence, io2.occurence);
					}
				});

		for (String s : ips) {
			if (ipOccurrenceMap.containsKey(s)) {
				// exist
				// update minHeap
				// auto adjust minheap
				IpOccurrence so = ipOccurrenceMap.get(s);
				so.occurence++;
				// walk around to adjust minheap
				IpOccurrence tSo = minHeap.poll();
				minHeap.add(tSo);
				if (!so.isInMinHeap && so.occurence > minHeap.peek().occurence) {
					if (minHeap.size() == k) {
						minHeap.poll();
					}
					minHeap.add(so);
					so.isInMinHeap = true;
				}
			} else {
				// not exist in map
				IpOccurrence so = new IpOccurrence(s, 1);
				ipOccurrenceMap.put(s, so);
				if (minHeap.size() < k) {
					minHeap.add(so);
					so.isInMinHeap = true;
				}
			}
		}
		// print in descending order of hit rate
		Stack<IpOccurrence> stack = new Stack<IpOccurrence>();
		while (!minHeap.isEmpty()) {
			stack.push(minHeap.poll());
		}
		while (!stack.empty()) {
			System.out.println(stack.pop());
		}
	}
}
