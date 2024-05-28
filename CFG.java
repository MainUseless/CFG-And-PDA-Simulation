import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class CFG {

	private static String eps = "";
	private static ArrayList<HashMap<String, ArrayList<String>>> rules;

	public static boolean solveCFG(int ruleIdx, String target, String curr) {
		if (target.equals(curr))
			return true;
		
		int nonTermCnt = 0;
		int nonTermIdx = -1;

		for (int i = 0; i < curr.length(); ++i) {
			if (rules.get(ruleIdx).get(curr.charAt(i) + "") == null)
				continue;
			if(nonTermIdx == -1)
				nonTermIdx = i;
			nonTermCnt++;
		}

		if (nonTermIdx == -1)
			return false;
			
		if (curr.length() - nonTermCnt > target.length())
			return false;

		ArrayList<String> currRules = rules.get(ruleIdx).get(curr.charAt(nonTermIdx)+"");

		for (int i = 0; i < currRules.size(); ++i) {
			String tempCurr = curr;
			tempCurr = tempCurr.replaceFirst(tempCurr.charAt(nonTermIdx)+"", currRules.get(i));
			if(solveCFG(ruleIdx, target, tempCurr))
				return true;
		}

		return false;
	}

	public static void Q1(HashMap<String, ArrayList<String>> rule) {
		rule.put("S", new ArrayList<String>(Arrays.asList(eps, "aSbS", "bSaS")));
	}

	public static void Q2(HashMap<String, ArrayList<String>> rule) {
		rule.put("S", new ArrayList<String>(Arrays.asList(eps, "aSaSbS","bSaSaS","aSbSaS")));
	}

	public static void Q3(HashMap<String, ArrayList<String>> rule) {
		rule.put("S", new ArrayList<String>(Arrays.asList(eps, "aSa","bSb","a","b")));
	}

	public static void Q4(HashMap<String, ArrayList<String>> rule) {
		rule.put("S", new ArrayList<String>(Arrays.asList(eps, "aaXb")));
		rule.put("X", new ArrayList<String>(Arrays.asList(eps, "aaXb", "aaa")));
	}

	public static void main(String[] args) {

		try {
			rules = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				rules.add(new HashMap<String, ArrayList<String>>());
			}
			Q1(rules.get(0));
			Q2(rules.get(1));
			Q3(rules.get(2));
			Q4(rules.get(3));
			File inputFile = new File("input_cfg.txt");
			Scanner myReader = new Scanner(inputFile);
			FileWriter outputFile = new FileWriter("output_cfg_1.txt");

			while (myReader.hasNextLine()) {

				String problemNumber;

				while ((problemNumber = myReader.nextLine()).length() == 0);

				outputFile.write(problemNumber + "\n");
				int problemNumberInt = Integer.valueOf(problemNumber);
				String line;

				while (myReader.hasNextLine() && !((line = myReader.nextLine()).toLowerCase().contains("end"))) {
					line = solveCFG(problemNumberInt - 1, line, "S") ? "accepted" : "not accepted";
					outputFile.write(line + "\n");
				}

				outputFile.write("end\n");

			}

			outputFile.close();
			myReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("file not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}