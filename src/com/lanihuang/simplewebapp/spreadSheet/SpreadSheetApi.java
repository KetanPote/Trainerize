package com.lanihuang.simplewebapp.spreadSheet;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes; 
import com.google.api.services.sheets.v4.model.*;
import com.lanihuang.simplewebapp.constants.Constant;
import com.lanihuang.simplewebapp.entity.Entity;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.*;

import javax.servlet.ServletContext;

public class SpreadSheetApi  
{
	private final String APPLICATION_NAME = "QuickStart";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/CreditData");
    private static final java.io.File Credit_DIR = new java.io.File(System.getProperty("user.home"), "/Credential");
    
    //private static final String DATA_STORE_DIR = "/CreditData";
    private final String LAST_LETTER = "N";
    private String spreadsheetId = "1AcD4rnZtjmtk26JAsogg5pN-HzAGBkK4meLIvfWwy90";
    private Set<String> keySet = getKeySets();
    private final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private final String CLIENT_SECRET_DIR = "/Alexander.json";
    private static FileDataStoreFactory dataStoreFactory;
    private static HttpTransport httpTransport;
    
    private Credential getCredentials(ServletContext  SC,String AuthCode[]) throws Exception 
    {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();        
    	
        System.out.println("********************************************");        
        System.out.println("0 : "  + DATA_STORE_DIR.getName());        
        System.out.println("1 : "  + DATA_STORE_DIR.getPath());
        System.out.println("2 : "  + DATA_STORE_DIR.getAbsolutePath());
        System.out.println("********************************************");
        
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        
  	  	InputStream in = new FileInputStream(Credit_DIR + "/credential.json");    	
  	  	GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( JSON_FACTORY,new InputStreamReader(in));    	
        
    	System.out.println(clientSecrets.getWeb().getRedirectUris().toString());
    	
		GoogleAuthorizationCodeFlow flow =
	            new GoogleAuthorizationCodeFlow.Builder(
	            		httpTransport, JSON_FACTORY,clientSecrets, SCOPES)
	                    .setDataStoreFactory(dataStoreFactory)
	                    .setApprovalPrompt("force")
	                    .setAccessType("offline")
	                    .build();        
		
        if(DATA_STORE_DIR.isDirectory())
        {			
    		if(DATA_STORE_DIR.list().length>0)
    		System.out.println("Data_Store_DIR Not Empty . . . !");    			
    		else
    		System.out.println("Unfortunately DIR is Empty . . . !");    				    			
    	}
        else
    	{    		
    		System.out.println("This is not a directory");    			
    	}        
        
       flow.newAuthorizationUrl().setScopes(SCOPES).setAccessType("offline").setApprovalPrompt("force").setClientId("938131041382-jtjhvm46iamtjeigf49m2k61nfjrn0pb.apps.googleusercontent.com")
       .setRedirectUri("http://september26.herokuapp.com/Callback");       
       
       Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
       
       return credential;
       // return credential;
    }
	

    private Sheets getSheets(ServletContext  SC,String AuthCode[])throws Exception 
    {
        final NetHttpTransport HTTP_TRANSPORT;

        try 
        {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(SC,AuthCode)).setApplicationName(APPLICATION_NAME).build();
            
            return service;
        } 
        catch (GeneralSecurityException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        // final String spreadsheetId = "1ovgCZSUyfl0Q42J8eDrbtD5G3NfXEsWLFz_5t5OunkM";
        // final String range = "A2:E";
        
        return null;
    }    
    
    public List<ValueRange> writeEntities(List<Entity> entityList,ServletContext  SC,String AuthCode[])throws Exception 
    {     
    	int columnsSize = getSpreadSheetSize(SC,AuthCode);
        List<ValueRange> List_VR = new ArrayList<ValueRange>();
        
        ValueRange VR = new ValueRange();
        
        if (columnsSize == 0) 
        {
            VR=writeHeaders(SC,AuthCode);
            List_VR=writeEntitiesToRaws(entityList, 2,SC,AuthCode);
        } 
        else 
        {
            List<String> lastdates = getColumnsByPosition(columnsSize+1,columnsSize+1,"C",SC,AuthCode);
         
            String lastdate = lastdates.get(0);
            
            if(lastdate.equals(entityList.get(0).getValue(Constant.DATE)))
            {
                HashMap<String,Integer> usersPosition = getUsersPosition(entityList.get(0).getValue(Constant.DATE),columnsSize,entityList.size(),SC,AuthCode);
                int raw = columnsSize + 2;
                
                for(int i=0;i<entityList.size();i++)
                {
                    Entity entity = entityList.get(i);
                    Integer position = usersPosition.get(entity.getValue(Constant.EMAIL));
     
                    if(position!=null)
                    {
                        List_VR.add(writeEntityToRaw(entity,position,SC,AuthCode));
                    }
                    else
                    {
                        List_VR.add(writeEntityToRaw(entity,raw,SC,AuthCode));
                        raw++;
                    }
                }
            }
            else
            {
                List_VR=writeEntitiesToRaws(entityList, columnsSize+2,SC,AuthCode);
            }
        }
        return List_VR;
    }
    private HashMap<String,Integer> getUsersPosition(String date, int size,int entitiesSize,ServletContext  SC,String AuthCode[])throws Exception
    {
        int start = size -entitiesSize;
         
        HashMap<String,Integer> usersPosition = new HashMap<>();
        
        if((entitiesSize*2)<=size)
        {
            start = size-entitiesSize*2;
        }

        List<String>  dates = getColumnsByPosition(start,size,"C",SC,AuthCode);
        List<String>  mails = getColumnsByPosition(start,size,"B",SC,AuthCode);
        
        for(int i=0;i<dates.size();i++)
        {
            if(!dates.get(i).equals("date")&& dates.get(i).equals(date))
            {
                usersPosition.put(mails.get(i),i+2);
            }
        }
        return usersPosition;
    }
    
    private List<ValueRange> writeEntitiesToRaws(List<Entity> entityList, int start,ServletContext  SC,String AuthCode[]) 
    {
    	List<ValueRange> List_VR = new ArrayList<ValueRange>();         
    	
        for (int i = 0; i < entityList.size(); i++) 
        {
            int rawCount = start + i;
            Entity entity = entityList.get(i);
            List<List<Object>> entityWriteData = new ArrayList<>();
            List<Object> entityDataRow = new ArrayList<>();
            
            for (String key : keySet) 
            {
                String value = entity.getValue(key);
            
                if (value == null) 
                {
                    value = "";
                }
                entityDataRow.add(value);
            }
            
            entityWriteData.add(entityDataRow);
            
            List_VR.add(writeData(entityWriteData, getRange("A", LAST_LETTER, rawCount,SC,AuthCode),SC,AuthCode));
        }
         
        return List_VR;
    }

    private ValueRange writeEntityToRaw(Entity entity, int position,ServletContext SC, String AuthCode[]) 
    {
        List<List<Object>> entityWriteData = new ArrayList<>();
        List<Object> entityDataRow = new ArrayList<>();
        
        for (String key : keySet) 
        {
            String value = entity.getValue(key);
            
            if (value == null) value = ""; 
            entityDataRow.add(value);
        }
        
        entityWriteData.add(entityDataRow); 
        
       return writeData(entityWriteData, getRange("A", LAST_LETTER, position,SC,AuthCode),SC,AuthCode);

    }
    private ValueRange writeHeaders(ServletContext  SC,String AuthCode[]) 
    {
        List<List<Object>> entityWriteData = new ArrayList<>();
        List<Object> entityDataRow = new ArrayList<>();
        
        for (String key : keySet) 
        {
            entityDataRow.add(key);
        }
        
        entityWriteData.add(entityDataRow);
        
        return writeData(entityWriteData, getRange("A", LAST_LETTER, 1,SC,AuthCode),SC,AuthCode);
    }

  public String getDateByPosition(int position,ServletContext SC,String Data[]) throws Exception
  {
//     .get(spreadsheetId, "A"+position-1+":A"+position)

    int nextPosition = position +1;
    
    
    try 
    {
    	ValueRange response = getSheets(SC,Data).spreadsheets()
              .values()
              .get(spreadsheetId, "C" + nextPosition + ":C" + nextPosition)
              .execute();
    	
      Collection<Object> objects = response.values();
      Iterator iterator = objects.iterator();
      Object object = null;
      while (iterator.hasNext()) 
      {
        object = iterator.next();
      }
      if (object != null) 
      {
        try 
        {
          ArrayList<String> strings = (ArrayList<String>) object;
          return strings.get(0);
        } 
        catch (Exception e) 
        {

        }
      }
    } 
    catch (IOException e) 
    {
      e.printStackTrace();
    }

    return null;

  }

    public List<String> getColumnsByPosition(int start,int end, String column,ServletContext  SC,String AuthCode[])throws Exception 
    {
//     .get(spreadsheetId, "A"+position-1+":A"+position)

        //  int nextPosition = position +1;
        List<String> values = new ArrayList<>();
        try 
        {
            ValueRange response = getSheets(SC,AuthCode).spreadsheets()
                    .values()
                    .get(spreadsheetId, column + start + ":"+column + end)
                    .execute();
            Collection<Object> objects = response.values();
            Iterator iterator = objects.iterator();
            Object object = null;
            while (iterator.hasNext()) 
            {
                object = iterator.next();
            }
            if (object != null) 
            {
                try 
                {
                    //ArrayList<String> strings = (ArrayList<String>) object;
                    ArrayList<List<String>> strings = (ArrayList<List<String>>) object;
                    //  return strings.get(0);
                    // values.addAll(strings);
                    for(int i=0;i<strings.size();i++)
                    {
                        values.addAll(strings.get(i));
                    }
                } 
                catch (Exception e) 
                {

                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        return values;

    }
    
    public int getSpreadSheetSize(ServletContext  SC,String AuthCode[]) throws Exception 
    {
        try 
        {
            
        	ValueRange response = getSheets(SC,AuthCode).spreadsheets().values().get(spreadsheetId, "A2:A1000000").execute();
            
            Collection<Object> objects = response.values();
            
            Iterator<Object> iterator = objects.iterator();
            Object object = null;
            
            while (iterator.hasNext()) 
            {
                object = iterator.next();
            }
            if (object != null) 
            {
                try 
                {
                    ArrayList<String> strings = (ArrayList<String>) object;
                    return strings.size();
                } 
                catch (Exception e) 
                {
                    return 0;
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        return 0;

    }
    

    private String getRange(String from, String to, int value,ServletContext  SC,String AuthCode[])
    {
        return from + value + ":" + to;
    }

    private ValueRange writeData(List<List<Object>> writeData, String range,ServletContext  SC,String AuthCode[]) 
    {
        // Build a new authorized API client service.
    	ValueRange body=null;
    	
        try 
        {        
        	/*
        	
        	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();  
            
            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(SC,AuthCode)) 
                    .setApplicationName(APPLICATION_NAME) 
                    .build();             
            
            */
        	
        	
        	body = new ValueRange().setValues(writeData);
            return body;
            
            /*
            UpdateValuesResponse result = 
                    service.spreadsheets().values().update(spreadsheetId, range, body)
                            .setValueInputOption("RAW")
                            .execute();
            */
            
            // System.out.printf("%d cells updated.", result.getUpdatedCells());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }

    /*
    private void writeBulkData(ServletContext  SC,String AuthCode[])
    {
        try 
        {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final String spreadsheetId = "1AcD4rnZtjmtk26JAsogg5pN-HzAGBkK4meLIvfWwy90";
            final String range = "Class Data!A2:E";
            
            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(SC,AuthCode))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            List<List<Object>> values = Arrays.asList(Arrays.asList());
            List<ValueRange> data = new ArrayList<ValueRange>();
            data.add(new ValueRange()
                    .setRange(range)
                    .setValues(values));
            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                    .setValueInputOption("RAW")
                    .setData(data);
            BatchUpdateValuesResponse result =
                    null;

            result = service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();

            // System.out.printf("%d cells updated.", result.getTotalUpdatedCells());
        } 
        catch (Exception e) 
        {

        }
    }
    */
    
    private Set<String> getKeySets() 
    {
        HashSet<String> keySet = new LinkedHashSet<>();
        keySet.add(Constant.NAME);
        keySet.add(Constant.EMAIL);
        keySet.add(Constant.DATE);
        keySet.add(Constant.WEIGHT);
        keySet.add(Constant.WAIST);
        keySet.add(Constant.TOTAL_CALORIES);
        keySet.add(Constant.GOAL_CALORIES);
        keySet.add(Constant.TOTAL_PROTEIN);
        keySet.add(Constant.GOAL_PROTEIN);
        keySet.add(Constant.TOTAL_FATS);
        keySet.add(Constant.GOAL_FAT);
        keySet.add(Constant.TOTAL_CARBOHYDRATES);
        keySet.add(Constant.GOAL_CARBOHYDRATES);
        keySet.add(Constant.WORKOUTCOMPLETE);
        return keySet;
    }
    
 }