package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import models.Property;
import utils.ExcelHandler;
import utils.FileHandler;
import utils.PropertyHandler;

public class Application {
	public static void main(String[] args) {
		PropertyHandler propertyHandler=null;
		List<Property> excelValues = null;
		File projectRoot=new File("D:\\Tiaa Workspaces\\UD_Atom_July_Release");
		File propertyFile=new File("D:\\Tiaa Workspaces\\UD_Atom_July_Release\\Lumpsums\\src\\main\\resources\\appconfigs\\participantretirementtransactions\\lumpsum-application-messages.properties");
		String ftlPattern=".*.ftl";
		String javaPattern=".*.java";
		String excelPath="D:\\300.xlsx";
		Collection<File> ftlFileList=FileHandler.getFileList(projectRoot,ftlPattern);
		Collection<File> javaFileList=FileHandler.getFileList(projectRoot,javaPattern);
		
		try{
			//FileHandler.replaceFileString(propertyFile.getAbsolutePath(),",","\\,");
			propertyHandler=new PropertyHandler(propertyFile);
			excelValues = ExcelHandler.readBooksFromExcelFile(excelPath);
			//Set new properties
			addNewPropertyNames(propertyFile,propertyHandler,excelValues);
			//Set new properties in ftl
			//modifyFtlFiles(excelValues,ftlFileList);
			//Remove properties modified
			//removePropertyFromFile(propertyFile,excelValues);
			//Find if exists in java
			//findInJavaFiles(excelValues,javaFileList);
		}catch (org.apache.commons.configuration.ConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void findInJavaFiles(List<Property> excelValues, Collection<File> javaFileList) throws IOException{
		List<String> javaList=new ArrayList<String>();
		for(File file:javaFileList){
			System.out.println("Java file to be checked: "+file.getAbsolutePath());
			for(Property property:excelValues){
				String propertyKey=property.getKey();
				if(FileHandler.findIfStringExists(file.getAbsolutePath(), propertyKey)){
					javaList.add(file.getName()+":"+propertyKey);
				}
			}
		}
		FileHandler.saveToFile("D:\\javaList.txt", javaList);
	}
	
	public static void removePropertyFromFile(File propertyFile, List<Property> excelValues) throws IOException{
		for(Property property:excelValues){
			System.out.println("Property to be edited: "+property.getKey());
			FileHandler.replaceFileString(propertyFile.getAbsolutePath(), property.getKey()+" = ", property.getKey()+".iwc=");
			FileHandler.replaceFileString(propertyFile.getAbsolutePath(), property.getKey()+".ud=", "Delete");
		}
	}
	
	public static void addNewPropertyNames(File propertyFile, PropertyHandler propertyHandler, List<Property> excelValues) throws IOException, ConfigurationException{
		List<String> notContains=new ArrayList<String>();
		for(Property property:excelValues){
			String propertyToBeEdited=property.getKey();
			if(propertyHandler.containsProperty(propertyToBeEdited)){
				System.out.println("Property to be edited: "+propertyToBeEdited);
				propertyHandler.setProperty(propertyToBeEdited+".unifieddesktop", property.getUdProperty());
				propertyHandler.saveFile();
			}else{
				System.err.println(propertyToBeEdited);
				notContains.add(propertyToBeEdited);
			}
		}
		FileHandler.replaceFileString(propertyFile.getAbsolutePath(),"\\,",",");
		FileHandler.replaceFileString(propertyFile.getAbsolutePath(),"\\\"","\"");
		FileHandler.saveToFile("D:\\checkList.txt", notContains);
	}
	
	public static void modifyFtlFiles(List<Property> excelValues, Collection<File> ftlFileList) throws IOException{
		for(File file:ftlFileList){
			System.out.println("File going to be edited: "+file.getAbsolutePath());
			for(Property property:excelValues){
				String propertyToBeEdited=property.getKey();
				FileHandler.replaceFileString(file.getAbsolutePath(), propertyToBeEdited, propertyToBeEdited+".${atomUIContext.siteProfile.siteName}");
				FileHandler.replaceFileString(file.getAbsolutePath(), propertyToBeEdited+".ud", propertyToBeEdited+".${atomUIContext.siteProfile.siteName}");
			}
		}
	}
}
