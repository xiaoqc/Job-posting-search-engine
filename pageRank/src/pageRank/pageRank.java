package pageRank;

import java.util.List;

public class pageRank {
	private GenerateGraph G;
	private List<List<Integer>> graph;
	
	public static void main(String[] args){
		pageRank rank = new pageRank(args);
	}
	
	public pageRank(String[] args){
		//String inputD = args[0];
		//int size = Integer.parseInt(args[1]);
		String inputD = "C:\\Users\\chen\\Desktop\\½Ì²ÄÃÇ\\2014 fall\\572\\assignment2\\testJson\\";
		int size = 71827;//276269
		this.G = new GenerateGraph(inputD, size);
		run();
	}
	
	public void run(){
		int count = 0;
		this.graph = this.generateGraph();
		for (int i = 0; i < this.graph.size(); i++){
			if (graph.get(i).size() > 10){
				count++;
			}
		}
		System.out.println(count + " documents with the number of linked doc bigger than 10");
	}
	
	//generate graph based on json's chosen columns.
	private List<List<Integer>> generateGraph(){
		this.G.run();
		return this.G.getResultGraph();
	}
}
