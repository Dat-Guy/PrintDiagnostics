package DatGuy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CVSFileReader {
	
	public static class FileReader {
		
		public static ArrayList ReadFile(String fileName, char DEFAULT_SPERATOR, char DEFAULT_QUOTE) throws FileNotFoundException {
			
			ArrayList<ArrayList<String>> file = new ArrayList<>();
			
			Scanner scanner = new Scanner(new File(fileName));
			
			while (scanner.hasNext()) {
				file.add(parseLine(scanner.nextLine(), DEFAULT_SPERATOR, DEFAULT_QUOTE));
			}
			
			return file;
			
		}
		
		private static ArrayList<String> parseLine(String cvsLine, char separators, char customQuote) {
			
			ArrayList<String> result = new ArrayList<>();
			
			//if empty, return!
			if (cvsLine == null || cvsLine.isEmpty()) {
				return result;
			}
			
			if (customQuote == ' ') {
				customQuote = '\"';
			}
			
			if (separators == ' ') {
				separators = ',';
			}
			
			StringBuffer curVal = new StringBuffer();
			boolean inQuotes = false;
			boolean startCollectChar = false;
			boolean doubleQuotesInColumn = false;
			
			char[] chars = cvsLine.toCharArray();
			
			for (char ch : chars) {
				
				if (inQuotes) {
					startCollectChar = true;
					if (ch == customQuote) {
						inQuotes = false;
						doubleQuotesInColumn = false;
					} else {
						
						//Fixed : allow "" in custom quote enclosed
						if (ch == '\"') {
							if (!doubleQuotesInColumn) {
								curVal.append(ch);
								doubleQuotesInColumn = true;
							}
						} else {
							curVal.append(ch);
						}
						
					}
				} else {
					if (ch == customQuote) {
						
						inQuotes = true;
						
						//Fixed : allow "" in empty quote enclosed
						if (chars[0] != '\"' && customQuote == '\"') {
							curVal.append('\"');
						}
						
						//double quotes in column will hit this!
						if (startCollectChar) {
							curVal.append('\"');
						}
						
					} else if (ch == separators) {
						
						result.add(curVal.toString());
						
						curVal = new StringBuffer();
						startCollectChar = false;
						
					} else if (ch == '\r') {
						//ignore LF characters
						continue;
					} else if (ch == '\n') {
						//the end, break!
						break;
					} else {
						curVal.append(ch);
					}
				}
				
			}
			
			result.add(curVal.toString());
			
			return result;
		}
		
	}
	
}