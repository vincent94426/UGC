package cn.dezhisoft.cloud.mi.newugc.common.util.textdiff;

import java.util.*;

public class LineDiff {
	public static String LineOperation(String fromFile, String toFile) {
		String content = "";
		String[] aLines = read(fromFile);
		String[] bLines = read(toFile);
		List diffs = (new Diff(aLines, bLines)).diff();
		int lastEnd=0;
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
			String left = null;
			String right = null;
			if (delEnd != Difference.NONE) {
				left = printLines(delStart, delEnd, aLines);
				lastEnd=addStart;
				/*if (addEnd != Difference.NONE) {
					System.out.println("---");
				}*/
			}

			// ������ʱ�Ĵ���
			if (addEnd != Difference.NONE) {
				right = printLines(addStart, addEnd,  bLines);
				lastEnd=addEnd+1;
			}
			
			if (type.equals("c")){
				content += "<del>"+left+"</del>"+ "<ins>"+right+"</ins>";
			}else if (type.equals("d")){
				content += "<del>"+left+"</del>";
			}else if (type.equals("a")){
				content += "<ins>"+right+"</ins>";
			}
		}
		content+=printLines(lastEnd,bLines.length-1,bLines);
		return content;
	}


	protected static String printLines(int start, int end, String[] lines) {
		String content = new String();
		for (int lnum = start; lnum <= end; ++lnum) {
			content += lines[lnum];
		}
		return content;
	}
	
	protected static String[] read(String fileName) {
		try {
			int count = fileName.length();
			List contents = new ArrayList();
			for (int i = 0; i < count; i++) {
				contents.add(fileName.charAt(i) + "");
			}
			return (String[]) contents.toArray(new String[] {});
		} catch (Exception e) {
			return null;
		}
	}

}
