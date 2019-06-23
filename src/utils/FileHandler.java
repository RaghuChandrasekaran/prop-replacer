package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class FileHandler {

	public static Collection<File> getFileList(File directoryPath, String pattern) {
		Collection<File> fileList = FileUtils.listFiles(directoryPath, new RegexFileFilter(pattern),
				DirectoryFileFilter.DIRECTORY);
		return fileList;
	}

	public static void replaceFileString(String fileName, String oldString, String newString) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		String content = IOUtils.toString(fis, "Cp1252");
		content = content.replaceAll("\\b" + Pattern.quote(oldString) + "\\b", Matcher.quoteReplacement(newString));
		FileOutputStream fos = new FileOutputStream(fileName);
		IOUtils.write(content, new FileOutputStream(fileName), "Cp1252");
		fis.close();
		fos.close();
	}

	public static boolean findIfStringExists(String fileName, String searchPattern) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		String content = IOUtils.toString(fis, "Cp1252");
		fis.close();
		return content.contains(searchPattern);
	}

	public static void saveToFile(String fileName, List<String> list) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		IOUtils.writeLines(list, IOUtils.LINE_SEPARATOR_WINDOWS, fos);
		fos.close();
	}
}
