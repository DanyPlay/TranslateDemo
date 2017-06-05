package translate;

public class Format {
	protected static String formatJson(String oldJson) {
        int i = 0;
	    String space = "  ";
	    String formatJson = "";
	    int indentCount = 0;
	    Boolean isStr = false;
	    String currChar = "";
	
	    for (i = 0; i < oldJson.length(); i++) {
		    currChar = oldJson.substring(i, i+1);
		    switch (currChar) {
			    case "{":
			    case "[":
				    if (!isStr) {
					    indentCount++;
					    formatJson += currChar + appendJson(space, indentCount);
				    } else {
					    formatJson += currChar;
				    }
				    break;
			    case "}":
			    case "]":
				    if (!isStr) {
					    indentCount--;
					    formatJson += appendJson(space, indentCount) + currChar;
				    } else {
					    formatJson += currChar;
				    }
				    break;
			    case ",":
				    if (!isStr) {
					    formatJson += "," + appendJson(space, indentCount);
				    } else {
					    formatJson += currChar;
				    }
				    break;
			    case ":":
				    if (!isStr) {
					    formatJson += ": ";
				    } else {
					    formatJson += currChar;
				    }
				    break;
			    case " ":
			    case "\n":
			    case "\t":
				    if (isStr) {
					    formatJson += currChar;
				    }
				    break;
			    case "\"":
				    if (i > 0 && !oldJson.substring(i - 1, i).equals("\\")) {
					    isStr = !isStr;
				    }
				    formatJson += currChar;
				    break;
			    default:
				    formatJson += currChar;
				    break;
		    }
	    }
	    return formatJson;
    }
    
    private static String appendJson(String str, int count) {
        //String retStr = "\n";
        String retStr = System.getProperty("line.separator", "\n");
        for (int i = 0; i < count; i++) {
            retStr += str;
        }
        return retStr;
    }
}
