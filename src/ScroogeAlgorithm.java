import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 
 * @author Monica Ruttle
 * @version 1
 * 
 * Algorithm which takes in a list of students for a Scrooge Secret Santa and 
 * matches up people so they are either in the same year and a different program, a different year
 * and the same program, or a different year and a different program.
 *
 */
public class ScroogeAlgorithm {
	
	private int numberOfPeople;
	private ArrayList<Person> masterList;
	private HashMap<Person, Person> exportList;
	//private String exportList[][];
	private ArrayList<Integer> alreadyHasSanta;
	int currentExport = 0;

	public static void main(String[] args) {
		ScroogeAlgorithm scr = new ScroogeAlgorithm(Integer.parseInt(args[0]));
		scr.createList();
		scr.matchUsers();
		scr.outputUsers();
	}
	
	public ScroogeAlgorithm(int numberOfPlayers) {
		this.numberOfPeople = numberOfPlayers;
		masterList = new ArrayList<Person>();
		exportList = new HashMap<Person, Person>();
		alreadyHasSanta = new ArrayList<Integer>();
	}
	
	public void createList() {
		String csvFile = "ScroogeRoughList.csv";
		BufferedReader br = null;
		String line = "";
		String comma = ",";
		int i = 0;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] user = line.split(comma);
				
				
				Person p = new Person(user[0], Integer.parseInt(user[1]), user[3], user[2]);
				//place users from the csv file to a master list of users, with their name and the program/year identifer
				masterList.add(p);
				i++;

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public void matchUsers() {
		Random r = new Random();
		for(int i = 0; i < numberOfPeople; i++) {
			
			//look for another user
			while(true) {
				//check again if the random user is already paired
				int index = r.nextInt(numberOfPeople);
				Person selectedPerson = masterList.get(index);
				if (exportList.containsValue(selectedPerson)) {
					continue;
				}
				
				Person currentPerson = masterList.get(i);
				
				if (exportList.get(selectedPerson) != null)
					if (exportList.get(selectedPerson).equals(currentPerson))
						continue;
				//check to see if they are in both the same program and year. If not, pair them. If so, find another random student.
				if (masterList.get(i).year == masterList.get(index).year || masterList.get(i).program == masterList.get(index).program)
					continue;
				else {
					exportList.put(currentPerson, selectedPerson);
					currentExport++;
					break;
				}
			}
			
			//check to see if the exported list is full
			if (currentExport == numberOfPeople)
				return;
		}
	}
	
	public void outputUsers() {
		try {
			PrintWriter writer = new PrintWriter("ScroogeMasterList.csv", "UTF-8");
			
			
			for (Person p: exportList.keySet()) {
				writer.println(p.name + ", " + p.email + ", " + exportList.get(p).name + ", " + exportList.get(p).email);
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
