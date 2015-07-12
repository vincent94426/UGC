package cn.dezhisoft.cloud.mi.newugc.common.util.textdiff;

import java.util.Iterator;
import java.util.List;

public class FileDiffUtil {
	
	public static String StringDiff(String fromFile, String toFile) {
		String content = "";
		String[] aLines = read(fromFile);
		String[] bLines = read(toFile);
		List diffs = (new Diff(aLines, bLines)).diff();
		int lastEnd = 0;
		int delStart = 1;
		int delEnd = 1;
		int addStart = 1;
		int addEnd = 1;

		Iterator it = diffs.iterator();
		while (it.hasNext()) {
			Difference diff = (Difference) it.next();
			delStart = diff.getDeletedStart();
			delEnd = diff.getDeletedEnd();
			addStart = diff.getAddedStart();
			addEnd = diff.getAddedEnd();
			//String from = toString(delStart, delEnd);
			//String to = toString(addStart, addEnd);
			String type = delEnd != Difference.NONE
					&& addEnd != Difference.NONE ? "c"
					: (delEnd == Difference.NONE ? "a" : "d");
			content += printLines(lastEnd, addStart-1, bLines);
			/*if (type.equals("d")) {
				if (addEnd == -1)
					content += combination(lastEnd, addStart, bLines);
				else
					content += combination(lastEnd, addEnd, bLines);
				content += "<del>" + combination(delStart, delEnd, aLines)
						+ "</del>";
				if (addEnd == -1)
					lastEnd = addStart;
				else
					lastEnd = addEnd + 1;
			} else if (type.equals("a")) {
				content += combination(lastEnd, addStart - 1, bLines);
				content += "<ins>" + combination(addStart, addEnd, bLines)
						+ "</ins>";
				lastEnd = addEnd + 1;
			}
			//System.out.println(from + type + to);
*/			String left = null;
			String right = null;
			if (delEnd != Difference.NONE) {
				left = printLines(delStart, delEnd, aLines);
				lastEnd=addStart;
				/*if (addEnd != Difference.NONE) {
					System.out.println("---");
				}*/
			}

			// �����ʱ�Ĵ���?
			if (addEnd != Difference.NONE) {
				right = printLines(addStart, addEnd, bLines);
				lastEnd=addEnd+1;
			}
			
			if (type.equals("c")){
				content += new LineDiff().LineOperation(left, right);
			}else if (type.equals("d")){
				content += "<del>"+left+"</del>";
			}else if (type.equals("a")){
				content += "<ins>"+right+"</ins>";
			}
		}
		content += printLines(lastEnd, bLines.length-1, bLines);
		return content;
	}

	public FileDiffUtil() {
		// TODO Auto-generated constructor stub
	}

	protected static String printLines(int start, int end, String[] lines) {
		String content = new String();
		for (int lnum = start; lnum <= end; ++lnum) {
			content += lines[lnum] + "<br/>";
		}
		return content;
	}


	protected static String[] read(String fileName) {
		try {
			String[] aLines = fileName.split("\r\n");
			return aLines;// (String[])contents.toArray(new String[] {});
		} catch (Exception e) {
			//System.err.println("error reading " + fileName + ": " + e);
			//System.exit(1);
			return null;
		}
	}

	/*public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println(new FileDiff().StringDiff("c:\\test1.txt",
					"c:as\\tesr1eee2.txt"));
		} else {
			System.err
					.println("usage: org.incava.diffj.FileDiff from-file to-file");
		}
	}*/

}
