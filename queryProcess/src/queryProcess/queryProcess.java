package HW2;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.json.*;

public class queryProcess {
	public static void main(String args[]){
		String[] parameter = new String[args.length];
		for (int i = 0; i < args.length; i++){
			parameter[i] = args[i];
		}
		queryProcess q = new queryProcess(parameter);
	}
	
	public queryProcess(String[] parameter){
//		parameter = new String[3];
//		parameter[0] = "1";
//		parameter[1] = "-75.575137";
//		parameter[2] = "6.235925";
		if (parameter.length == 0){
			System.out.println("no input found");		
			return;
		}
		int queryNumber = Integer.parseInt(parameter[0]);
		if (queryNumber == 1){
			query1(parameter);
		} else if (queryNumber == 2){
			query2(parameter);
		} else if (queryNumber == 3){
			query3(parameter);
		} else if (queryNumber == 4){
			query4(parameter);
		} else {
			System.out.println("wrong query number");
			return;
		}
	}
	
	public void query1(String[] para){
		double lat = 0, lon = 0;
		if (para.length < 3){
			System.out.println("lack parameters");
			return;
		}
		try {			
			lon = Double.parseDouble(para[1].trim());
			lat = Double.parseDouble(para[2].trim());
		} catch (Exception e){
			System.out.println("wrong geo location input");
			e.printStackTrace();
		}
		String query = "http://localhost:8983/solr/collection1/select?q=*%3A*&fq=longitude%3A%22" + lon + "%22&fq=latitude%3A+%22" + lat + "%22&wt=json&facet=true&facet.query=facet.sort%3Dcount%26facet.mincount%3D1&facet.field=jobtype&rows=0";
		try {
			URL url = new URL(query);
			BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			String rst = new String();
			while((line = buf.readLine()) != null){
				rst += line;	
			}
					
			JSONObject json = new JSONObject(rst);
			
			String type = json.getJSONObject("facet_counts").getJSONObject("facet_fields").getJSONArray("jobtype").get(0).toString();
			System.out.println("query result:");
			System.out.println("The potenial job type in the future is " + type);
			
		} catch (Exception e) {
			System.out.println("wrong query or bad return result");
			e.printStackTrace();
		}
	}
	
	public void query2(String[] para){
		String loc = "";
		if (para.length < 2){
			System.out.println("lack parameters");
			return;
		}
		loc = para[1];
		String query = "http://localhost:8983/solr/collection1/select?q=*%3A*&fq=jobLastDays%3A%5B0+TO+10000%5D&fq=location%3A" + loc + "&sort=jobLastDays+asc&rows=600&fl=jobLastDays&wt=json&indent=true&group=true&group.field=jobLastDays";
		
		try {
			URL url = new URL(query);
			BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			String rst = new String();
			while((line = buf.readLine()) != null){
				rst += line;	
			}
					
			JSONObject json = new JSONObject(rst);
			
			JSONArray ary = json.getJSONObject("grouped").getJSONObject("jobLastDays").getJSONArray("groups");
			List<String> groupVal = new ArrayList<String>();
			List<String> count = new ArrayList<String>();
			
			for (int i = 0; i < ary.length(); i++){
				JSONObject tmp = ary.getJSONObject(i);
				groupVal.add(tmp.get("groupValue").toString());
				count.add(tmp.getJSONObject("doclist").get("numFound").toString());
			}
			
			System.out.println("query result:");
			System.out.println("job last days" + "\t\t" + "number of job");
			for (int i = 0; i < groupVal.size(); i++){
				System.out.println(groupVal.get(i) + "\t\t\t\t" + count.get(i));
			}
			
		} catch (Exception e) {
			System.out.println("wrong query or bad return result");
			e.printStackTrace();
		}
	}
	
	public void query3(String[] para){
		String loc = "";
		if (para.length < 2){
			System.out.println("lack parameters");
			return;
		}
		loc = para[1];
		String query = "http://localhost:8983/solr/collection1/select?q=*%3A*&fq=location%3A" + loc + "&fq=!tag%3An%2Fa&fl=tag&wt=json&indent=true&group=true&group.field=tag";
	
		try {
			URL url = new URL(query);
			BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			String rst = new String();
			while((line = buf.readLine()) != null){
				rst += line;	
			}
					
			JSONObject json = new JSONObject(rst);			
			JSONArray ary = json.getJSONObject("grouped").getJSONObject("tag").getJSONArray("groups");
			
			int count = 0;
			String tag = "";
			for (int i = 0; i < ary.length(); i++){
				JSONObject tmp = ary.getJSONObject(i);
				int tmpCount = 0;
				try {
					tmpCount = Integer.parseInt(tmp.getJSONObject("doclist").get("numFound").toString());
				} catch(Exception e){
					
				}
				String tmpTag = tmp.get("groupValue").toString();
				if (tmpCount > count){
					tag = tmpTag;
				}
			}
			
			System.out.println("query result:");
			System.out.println("This city or zone is " + tag + " city or zone");
			
		} catch (Exception e) {
			System.out.println("wrong query or bad return result");
			e.printStackTrace();
		}
	}
	
	public void query4(String[] para){
		String query = "http://localhost:8983/solr/collection1/select?q=*%3A*&fq=postedDate%3A%5B2013-11-13+TO+2013-12-13%5D&sort=postedDate+asc&rows=100&fl=jobType&wt=json&indent=true&group=true&group.field=postedDate";
		try {
			URL url = new URL(query);
			BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			String rst = new String();
			while((line = buf.readLine()) != null){
				rst += line;	
			}
					
			JSONObject json = new JSONObject(rst);			
			JSONArray ary = json.getJSONObject("grouped").getJSONObject("postedDate").getJSONArray("groups");
			
			int[] count = new int[2];
			List<node> buf1 = new ArrayList<node>();
			
			for (int i = 0; i < ary.length(); i++){
				JSONObject tmp = ary.getJSONObject(i);
				String tmpDate = tmp.get("groupValue").toString().trim();
				String tmpType = tmp.getJSONObject("doclist").getJSONArray("docs").getJSONObject(0).get("jobType").toString();
				
				int val = -1;
				try {
					String[] s = tmpDate.split("-");
					val = Integer.parseInt(s[1]) * 30 + Integer.parseInt(s[2]);
				} catch (Exception e){
					
				}
				if (val == -1){
					continue;
				}
				buf1.add(new node(val, tmpDate, tmpType));
				
				if (tmpType.equals("full time")){
					count[0]++;
				} else if (tmpType.equals("part time")){
					count[1]++;
				}				
			}
			Comparator<node> comp = new Comparator<node>(){
				public int compare(node arg0, node arg1) {
					return arg0.val - arg1.val;
				}				
			};
			Collections.sort(buf1, comp);
			String type = (count[0] < count[1]) ? "part-time" : "full-time";
			System.out.println("query result:");
			System.out.println("The full-time and part-time distribution from 2013-11-13 to 2013-12-13 is");
			System.out.println("date" + "\t\t" + "job type");
			for (int i = 0; i < buf1.size(); i++){
				System.out.println(buf1.get(i).date + "\t\t" + buf1.get(i).type);
			}
			System.out.println("The trend of job type in South America is " + type);
			
		} catch (Exception e) {
			System.out.println("wrong query or bad return result");
			e.printStackTrace();
		}
	}
	
	public class node{
		String date;
		String type;
		int val;
		node(int a, String b, String c){
			val = a;
			date = b;
			type = c;
		}
	}
}

