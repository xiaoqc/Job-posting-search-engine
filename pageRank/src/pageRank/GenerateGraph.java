package pageRank;

import java.io.*;

//import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.*;
import java.util.Map.Entry;

public class GenerateGraph {
	public String input;
	public int size;
	private Map<String, ArrayList<Integer>> mp = new HashMap<String, ArrayList<Integer>>();
	private List<Set<Integer>> graph = new ArrayList<Set<Integer>>();
	private List<List<Integer>> result = new ArrayList<List<Integer>>();
	private String[] columns = {"longitude", "latitude", "company", "title"};
	
	public Map<String, Integer> token;
	public boolean parseTitle = false;
	
	//constructor, first parameter is the json file directory, second one is the size of files
	public GenerateGraph(String inputD, int size){
		this.input = inputD;
		this.size = size;
		for (int i = 0; i <size; i++){
			graph.add(new HashSet<Integer>());
			result.add(new ArrayList<Integer>());
		}
	}
	
	public void setTokenMap(Map<String, Integer> tmp){
		this.token = tmp;
		this.parseTitle = true;
	}
	
	public void run(){
		//iterative read json file
		System.out.println("Start generate graph");
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < size; i++){
			readFile(this.input + i + ".json", i);
			if (i % 10000 == 0){
				System.out.println("read " + i + " files");
			}
		}
		System.out.println("Read file takes " + (System.currentTimeMillis() - startTime) + " ms");
		invertedIndex();
		formatResult();
		System.out.println("Generate graph takes " + (System.currentTimeMillis() - startTime) + " ms in total");
	}
	
	public List<List<Integer>> getResultGraph(){
		return this.result;
	}
	
	//format result
	private void formatResult(){
		System.out.println("formatting result");
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < graph.size(); i++){
			graph.get(i).remove(i);
			result.get(i).addAll(graph.get(i));
			Collections.sort(result.get(i));
		}
		System.out.println("formatting result takes " + (System.currentTimeMillis() - startTime) + " ms");
	}
	
	//generate inverted index for each document
	private void invertedIndex(){
		System.out.println("generating inverted index");
		long startTime = System.currentTimeMillis();
		Iterator<Entry<String, ArrayList<Integer>>> it = mp.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, ArrayList<Integer>> et = (Entry<String, ArrayList<Integer>>) it.next();
			List<Integer> val = et.getValue();
			if (val.size() < 2){
				continue;
			}
			for (int id : val){
				graph.get(id).addAll(val);
			}
		}
		System.out.println("Compute inverted index takes " + (System.currentTimeMillis() - startTime) + " ms");
	}
	
	//read json file and add document id to map
	private void readFile(String path, int id){
		File f = new File(path);
		if (!f.isFile()){
			return;
		}
		try {
			InputStream input = new FileInputStream(f);
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
			line = reader.readLine();
			input.close();
			reader.close();
			
			JSONParser parser = new JSONParser(); 			
			JSONObject json = (JSONObject) parser.parse(line); 
			//JSONObject json = new JSONObject(line);


			String[] attr = new String[4];
			//read for chosen columns
			/*
			String tmp1 = json.get("longitude").toString();
			attr[0] = tmp1.substring(2, tmp1.length() - 2).trim();
			tmp1 = json.get("latitude").toString();
			attr[1] = tmp1.substring(2, tmp1.length() - 2).trim();
			tmp1 = json.get("company").toString();
			attr[2] = tmp1.substring(2, tmp1.length() - 2).trim();
			tmp1 = json.get("title").toString();
			attr[3] = tmp1.substring(2, tmp1.length() - 2).trim();
			*/
			for (int i = 0; i < this.columns.length; i++){
				attr[i] = json.get(this.columns[i]).toString();
				/*if (id == 162){
					System.out.println("attr" + i + ": " + attr[i]);
				}*/
			}
			
			
			//combine 2 out of 3 attributes to form 3 keys
			String[] key = new String[3];
			//link two attr as key
			if (!attr[0].equals("") && !attr[1].equals("") && !attr[2].equals("")){
				key[0] = attr[0] + "|" + attr[1] + "|" + attr[2];
			}
			if (!attr[0].equals("") && !attr[1].equals("") && !attr[3].equals("")){
				key[1] = attr[0] + "|" + attr[1] + "|" + attr[3];
			}
			if (!attr[2].equals("") && !attr[3].equals("")){
				key[2] = attr[2] + "|" + attr[3];
			}
			
			//ad document id to map based on key
			for (int i = 0; i < key.length; i++){
				if (key[i] == null){
					continue;
				}
				if (!mp.containsKey(key[i])){
					mp.put(key[i], new ArrayList<Integer>());
				}
				mp.get(key[i]).add(id);
				/*if (id == 163){
					System.out.println("key" + i + ": " + key[i]);
				}*/
			}
		} catch(Exception e){
			System.out.println("document " + id + ".json error");
			//e.printStacTrace();
		}
	}
	
	//tokenize title and find the top frequency words
}
