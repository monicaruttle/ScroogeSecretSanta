import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
	
	private int numberOfPlayers;
	private String masterList[][];
	private String exportList[][];
	private ArrayList<Integer> alreadyHasSanta;

	public static void main(String[] args) {
		ScroogeAlgorithm scr = new ScroogeAlgorithm(Integer.parseInt(args[0]));
		scr.createList();
		scr.matchUsers();
		scr.outputUsers();
	}
	
	public ScroogeAlgorithm(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
		masterList = new String[numberOfPlayers][3];
		exportList = new String[numberOfPlayers][4];
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
				
				//place users from the csv file to a master list of users, with their name and the program/year identifer
				masterList[i][0] = user[0];
				masterList[i][1] = user[2]+""+user[1];
				masterList[i][2] = user[3];
				
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
		int currentExport = 0;
		for(int i = 0; i < numberOfPlayers; i++) {
			
			//look for another user
			while(true) {
				//check again if the random user is already paired
				int index = r.nextInt(numberOfPlayers);
				if (alreadyHasSanta.contains(index)) {
					continue;
				}
				
				//obtain student information from current student and randomly selected student
				int masterYear = masterList[i][1].charAt(4);
				String masterProgram = masterList[i][1].substring(0, 3);
				int selectedYear = masterList[index][1].charAt(4);
				String selectedProgram = masterList[index][1].substring(0, 3);
				
				//check to see if they are in both the same program and year. If not, pair them. If so, find another random student.
				if (masterYear == selectedYear || masterProgram.equals(selectedProgram))
					continue;
				else {
					alreadyHasSanta.add(index);
					exportList[currentExport][0] = masterList[i][0];
					exportList[currentExport][1] = masterList[i][2];
					exportList[currentExport][2] = masterList[index][0];
					exportList[currentExport][3] = masterList[index][2];
					currentExport++;
					break;
				}
			}
			
			//check to see if the exported list is full
			if (exportList[numberOfPlayers-1][1] != null)
				return;
		}
	}
	
	public void outputUsers() {
		try {
			PrintWriter writer = new PrintWriter("ScroogeMasterList.csv", "UTF-8");
			
			for (int i = 0; i < numberOfPlayers; i++) {
				writer.println(exportList[i][0] + ", " + exportList[i][1] + ", " + exportList[i][2] + ", " + exportList[i][3]);
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
