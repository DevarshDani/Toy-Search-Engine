//import java.text.DecimalFormat;
//import java.text.NumberFormat;
import java.io.*;
import java.util.*;

public class BooleanRetrieval {
	
	HashMap<String, Set<Integer>> invIndex;
	int [][] docs;
	HashSet<String> vocab;
	HashMap<Integer, String> map;  // int -> word
	HashMap<String, Integer> i_map; // inv word -> int map

	public BooleanRetrieval() throws Exception{
		// Initialize variables and Format the data using a pre-processing class and set up variables
		invIndex = new HashMap<String, Set<Integer>>();
		DatasetFormatter formater = new DatasetFormatter();
		formater.textCorpusFormatter("./all.txt");
		docs = formater.getDocs();
		vocab = formater.getVocab();
		map = formater.getVocabMap();
		i_map = formater.getInvMap();
	}

	void createPostingList(){
		Set<Integer> posting; // set of integers to alter the invIndex Value pair 
		//Initialze the inverted index with a SortedSet (so that the later additions become easy!)
		for(String s:vocab){
			invIndex.put(s, new TreeSet<Integer>());
		}
		//for each doc
		for(int i=0; i<docs.length; i++){
			//for each word of that doc
			for(int j=0; j<docs[i].length; j++){
				//Get the actual word in position j of doc i
				String w = map.get(docs[i][j]);
				// my code starts here.
				//posting = new HashSet<Integer>();
				/* TO-DO:
				Get the existing posting list for this word w and add the new doc in the list. 
				Keep in mind doc indices start from 1, we need to add 1 to the doc index , i
				 */
				posting = invIndex.get(w);
				posting.add(i+1);
				invIndex.put(w, posting);
				// my code ends here.
			}

		}
	}

	Set<Integer> intersection(Set<Integer> a, Set<Integer> b){
		/*
		First convert the posting lists from sorted set to something we 
		can iterate easily using an index. I choose to use ArrayList<Integer>.
		Once can also use other enumerable.
		 */
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
		ArrayList<Integer> PostingList_b = new ArrayList<Integer>(b);
		
		TreeSet<Integer> result = new TreeSet<Integer>();

		//Set indices to iterate two lists. I use i, j
		int i = 0;
		int j = 0;
		//int min = ((PostingList_a.size() >= PostingList_b.size()) ? PostingList_b.size() : PostingList_a.size());
			//my code starts here.
		while(i!=PostingList_a.size() && j!=PostingList_b.size())
		{
			//TO-DO: Implement the intersection algorithm here
			//my code starts here.
			if(PostingList_a.get(i).equals(PostingList_b.get(j)))
			{
				result.add(PostingList_a.get(i));
				i++;
				j++;
			}
			else if(PostingList_a.get(i) < PostingList_b.get(j))
				i++;
			else
				j++;
		}
			//my code ends here.
		return result;
	}

	Set <Integer> evaluateANDQuery(String a, String b){
		return intersection(invIndex.get(a), invIndex.get(b));
	}

	Set<Integer> union(Set<Integer> a, Set<Integer> b){
		/*
		 * IMP note: you are required to implement OR and cannot use Java Collections methods directly, e.g., .addAll whcih solves union in 1 line!
		 * TO-DO: Figure out how to perform union extending the posting list intersection method discussed in class?
		 */
		TreeSet<Integer> result = new TreeSet<Integer>();
		int i=0,j=0;
		// Implement Union here
		// my code starts here
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
		ArrayList<Integer> PostingList_b = new ArrayList<Integer>(b);
		while(i!=PostingList_a.size() || j!=PostingList_b.size())
		{
			if(i == PostingList_a.size())
			{	for(int k=j; k<PostingList_b.size(); k++)
					result.add(PostingList_b.get(k));
				break;
			}
			if(j == PostingList_b.size())
			{
				for(int k=i; k<PostingList_a.size(); k++)
					result.add(PostingList_a.get(k));
				break;
			}
			if(PostingList_a.get(i) == PostingList_b.get(j))
			{
				result.add(PostingList_a.get(i));
//				if(j < PostingList_b.size())
					j++;
//				if(i < PostingList_a.size())
					i++;
			}
			else 
			{
				result.add(PostingList_a.get(i));
				result.add(PostingList_b.get(j));
//				if(j < PostingList_b.size())
					j++;
//				if(i < PostingList_a.size())
					i++;
			}
		}
		// my code ends here
		return result;
	}

	Set <Integer> evaluateORQuery(String a, String b){
		return union(invIndex.get(a), invIndex.get(b));
	}
	
	Set<Integer> not(Set<Integer> a){
		TreeSet<Integer> result = new TreeSet<Integer>();
		/*
		 Hint:
		 NOT is very simple. I traverse the sorted posting list between i and i+1 index
		 and add the other (NOT) terms in this posting list between these two pointers
		 First convert the posting lists from sorted set to something we 
		 can iterate easily using an index. I choose to use ArrayList<Integer>.
		 Once can also use other enumerable.
		 */
		
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
		int total_docs = docs.length;
		int index = 0;
//		int doc_index;
		// TO-DO: Implement the not method using above idea or anything you find better!
		//my code starts here.
		while(index < PostingList_a.size())
		{
			if(index == 0)
			{
//				doc_index = PostingList_a.get(index) - 1;
				for(int j=1; j<PostingList_a.get(index); j++)
				{
					result.add(j);
				}
			}
			else if(index == PostingList_a.size()-1)
			{
//				doc_index = total_docs - PostingList_a.get(index);
				for(int j=PostingList_a.get(index-1) + 1; j<PostingList_a.get(index); j++)
				{
					result.add(j);
				}
				for(int j=PostingList_a.get(index) + 1; j<=total_docs; j++)
				{
					result.add(j);
				}
			}
			else	
			{
//				doc_index = PostingList_a.get(index) - PostingList_a.get(index - 1);
				for(int j=PostingList_a.get(index-1) + 1; j<PostingList_a.get(index); j++)
				{
					result.add(j);
				}
			}
			index++;
		}
		//my code ends here.
		return result;
	}

	Set <Integer> evaluateNOTQuery(String a){
		return not(invIndex.get(a));
	}
	
	Set <Integer> evaluateAND_NOTQuery(String a, String b){
		return intersection(invIndex.get(a), not(invIndex.get(b)));
	}
	public static void main(String[] args) throws Exception {
		
		//Initialize pointer to file
		BufferedWriter bw = null;
		
		//Initialize parameters
		BooleanRetrieval model = new BooleanRetrieval();

		//Generate posting lists
		model.createPostingList();

		//Print the posting lists from the inverted index
		
/*		System.out.println("\nPrinting posting list:");
		for(String s : model.invIndex.keySet()){
			System.out.println(s + " -> " + model.invIndex.get(s));	
		}
*/		
		// my code starts here.
		Set<Integer> i;
		String st = "";
		switch(args[0]) {
		case "PLIST" :
			//Write pointer to output file
			bw = new BufferedWriter(new FileWriter(args[2]));
			if(model.invIndex.containsKey(args[1]))
				st = args[1] + " -> " + model.invIndex.get(args[1]);
			else
				st = args[1] + " -> " + "null";
			bw.write(st);
			bw.close();
//			System.out.println("The input string does not support the min count of 5 or is not present in any document or is a stopword");
			break;
		case "AND" :
			bw = new BufferedWriter(new FileWriter(args[4]));
			if(args[0].equals(args[2]))
			{
				if(model.invIndex.containsKey(args[1]))
				{
					if(model.invIndex.containsKey(args[3]))
						{
							i = model.evaluateANDQuery(args[1], args[3]);
							st = args[1] + " " + args[2] + " " + args[3] + " -> " + i;
							bw.write(st);
							bw.close();
						}
					else
						{
							st = args[1] + " " + args[2] + " " + args[3] + " -> " + "null";
							bw.write(st);
							bw.close();
						}
				}
				else
				{
					st = args[1] + " " + args[2] + " " + args[3] + " -> " + "null";
					bw.write(st);
					bw.close();
				}
			}
			else
				System.out.println("Input does not have 'AND' operator in between the operands");
			break;
		case "OR" :
			bw = new BufferedWriter(new FileWriter(args[4]));
			if(args[0].equals(args[2]))
			{
				if(model.invIndex.containsKey(args[1]))
				{
					if(model.invIndex.containsKey(args[3]))
						{
							i = model.evaluateORQuery(args[1], args[3]);
							st = args[1] + " " + args[2] + " " + args[3] + " -> " + i;
							bw.write(st);
							bw.close();
						}
					else
						{
							st = args[1] + " " + args[2] + " " + args[3] + " -> " + model.invIndex.get(args[1]);
							bw.write(st);
							bw.close();
						}
				}
				else if(model.invIndex.containsKey(args[3]))
				{
					st = args[1] + " " + args[2] + " " + args[3] + " -> " + model.invIndex.get(args[3]);
					bw.write(st);
					bw.close();
				}
				else
				{
					st = args[1] + " " + args[2] + " " + args[3] + " -> " + "null";
					bw.write(st);
					bw.close();
				}
					
			}
			else
				System.out.println("Input does not have 'OR' operator in between operands");
			break;
		case "AND_NOT" :
			bw = new BufferedWriter(new FileWriter(args[4]));
			//StringTokenizer NOTtok = new StringTokenizer(args[3], "(");
			//StringTokenizer operand2tok = new StringTokenizer(args[4], ")");
			//String NOT = NOTtok.nextToken();
			//String operand2 = operand2tok.nextToken();
			if(args[0].equals(args[2]))
			{
				if(model.invIndex.containsKey(args[1]))
				{
					if(model.invIndex.containsKey(args[3]))
					{
						i = model.evaluateAND_NOTQuery(args[1], args[3]);
						st = args[1] + " " + "AND (NOT " + args[3] + ") -> " + i;
						bw.write(st);
						bw.close();
					}
					else
					{
						st = args[1] + " " + "AND (NOT " + args[3] + ") -> " + model.invIndex.get(args[1]);
						bw.write(st);
						bw.close();
					}
				}
				else
				{
					st = args[1] + " " + "AND (NOT " + args[3] + ") -> " + "null";
					bw.write(st);
					bw.close();
				}
			}
			else
				System.out.println("Input does not have Correct operations in between operands");
			break;
			default:
				System.out.println("Incorrect input in first argument....please run the program again with correct input parameter.");
			System.exit(0);
		}

		//Print test cases

/*		System.out.println();
		
		System.out.println("\nTesting AND queries \n");
		System.out.println("1) " + model.evaluateANDQuery("mouse", "keyboard"));
		System.out.println("2) " + model.evaluateANDQuery("mouse", "scrolling"));
		System.out.println("3) " + model.evaluateANDQuery("button", "keyboard"));
				
		System.out.println("\nTesting OR queries \n");
		System.out.println("4) " + model.evaluateORQuery("wifi", "scroll"));
		System.out.println("5) " + model.evaluateORQuery("youtube", "reported"));
		System.out.println("6) " + model.evaluateORQuery("errors", "report"));
		
		System.out.println("\nTesting AND_NOT queries \n");
		System.out.println("7) " + model.evaluateAND_NOTQuery("mouse", "scroll"));
		System.out.println("8) " + model.evaluateAND_NOTQuery("scroll", "mouse"));
		System.out.println("9) " + model.evaluateAND_NOTQuery("lenovo", "logitech")); */
	}

}