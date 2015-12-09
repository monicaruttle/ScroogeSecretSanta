import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private ArrayList<Integer> alreadyHasSanta;
	int currentExport = 0;

	public static void main(String[] args) {
		ScroogeAlgorithm scr = new ScroogeAlgorithm();
		scr.createList();
		scr.matchUsers();
		scr.outputUsers();
	}
	
	public ScroogeAlgorithm() {
		numberOfPeople = 0;
		masterList = new ArrayList<Person>();
		exportList = new HashMap<Person, Person>();
		alreadyHasSanta = new ArrayList<Integer>();
	}
	
	public void createList() {
		String csvFile = "ScroogeRoughList.csv";
		BufferedReader br = null;
		String line = "";
		String comma = ",";
		int nameIndex = 0;
		int yearIndex = 0;
		int streamIndex = 0;
		int emailIndex = 0;
		int notesIndex = 0;
		int allergiesIndex = 0;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			
			//determine which column is which person attribute
			ArrayList<String> headers = new ArrayList<String>(Arrays.asList(br.readLine().split(comma)));
			nameIndex = headers.indexOf("Name");
			yearIndex = headers.indexOf("Year");
			streamIndex = headers.indexOf("Stream");
			emailIndex = headers.indexOf("Email");
			notesIndex = headers.indexOf("Notes");
			allergiesIndex = headers.indexOf("Allergies");
			
			while ((line = br.readLine()) != null) {
				String[] user = line.split(comma);
				
				//create a new person based on a row in the csv file
				Person p = new Person(user[nameIndex], Integer.parseInt(user[yearIndex]), user[emailIndex], user[streamIndex], user[allergiesIndex], user[notesIndex]);
				//place users from the csv file to a master list of users, with their name and the program/year identifer
				masterList.add(p);
				numberOfPeople++;
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
			
			while(true) {
				//look for a user at random and determine if they don't already have a santa, else find another
				int index = r.nextInt(numberOfPeople);
				Person selectedPerson = masterList.get(index);
				if (exportList.containsValue(selectedPerson)) {
					continue;
				}
				
				Person currentPerson = masterList.get(i);
				
				//ensure someone can't get paired with someone who was paired with them
				if (exportList.get(selectedPerson) != null)
					if (exportList.get(selectedPerson).equals(currentPerson))
						continue;
				//check to see if they are in both the same program and year. If not, pair them. If so, find another random student.
				if (masterList.get(i).year == masterList.get(index).year || masterList.get(i).program.equals(masterList.get(index).program))
					continue;
				else {
					//store the pairing, increase the list size and find another person
					exportList.put(currentPerson, selectedPerson);
					currentExport++;
					break;
				}
			}
			
			//check to see if the exported list is full, if so the pairing is complete
			if (currentExport == numberOfPeople)
				return;
		}
	}
	
	public void outputUsers() {
		try {
			PrintWriter writer = new PrintWriter("ScroogeMasterList.csv", "UTF-8");
			
			//export the final list in a csv file.
			writer.println("SANTA, EMAIL, SCROOGE, SCROOGE'S ALLERGIES, SCROOGE'S NOTES");
			
			for (Person p: exportList.keySet()) {
				Person export = exportList.get(p);
				writer.println(p.name + ", " + p.email + ", " + export.name + ", " + export.allergies + ", " + export.notes);
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
