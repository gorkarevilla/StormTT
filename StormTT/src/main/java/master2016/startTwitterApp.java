/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


/**
 *
 * @author alvarofeal
 */
public class startTwitterApp {
    
    public static String mode;
    public static String apiKey;
    public static String apiSecret;
    public static String tokenValue;
    public static String tokenSecret;
    public static String kafkaBrokerURL;
    public static String folder;
    
    public void main(String[] args){
        
        if (args.length == 7) {
            mode = args[0];
            apiKey = args[1];
            apiSecret = args[2];
            tokenValue = args[3];
            tokenSecret = args[4];
            kafkaBrokerURL = args[5];
            folder = args[6];
        }
        else {
            printUsage();
            
        }
        if (mode.equals("1")){
            
            FileInputStream fstream = null;
            try{
                JSONParser parser = new JSONParser();
               // Open the file
                fstream = new FileInputStream(folder);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine;
                while ((strLine = br.readLine()) != null)   {
                    Object obj = parser.parse(strLine);
                    JSONObject tweet = (JSONObject)obj;
                    String lang = tweet.get("lang").toString();
                    
                    
                }
                
            }catch(FileNotFoundException ex){
                System.out.println("Your file couldn't be found");
                printUsage();
            } catch (IOException ex) {
                Logger.getLogger(startTwitterApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                System.out.println("The provided file should contain one tweet per line in JSON format");
                printUsage();
            } finally {
                try {
                    fstream.close();
                } catch (IOException ex) {
                    Logger.getLogger(startTwitterApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
         }
            
        }
    
    public void printUsage(){
        System.out.println("Usage of startTwitterApp:");
        System.out.println("./startTwitterApp.sh mode apiKey apiSecret tokenValue tokenSecret kafkaBrokerURL folder");
        System.exit(0);
    }
}
    

