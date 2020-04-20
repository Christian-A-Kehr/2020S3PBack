package utils;

import java.io.IOException;
import java.util.Properties;

public class Settings {

    public static final String PROPERTY_FILE = "config.properties";
    private static Properties props = null;

    private static Properties loadProperties() {
        Properties allProps = new Properties();
        try {
            allProps.load(Settings.class.getClassLoader().getResourceAsStream(PROPERTY_FILE));
            for (Object key : allProps.keySet()) {
                allProps.setProperty((String) key, allProps.getProperty((String) key).trim());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not load properies for :" + PROPERTY_FILE);
        }
        return allProps;
    }
    
    /**
     * Returns the property value for the given key or null if it does not exist
     * Key/value must be defined in "config.properties"
     * @param key
     * @return Property value for the given key
     */
    public static String getPropertyValue(String key){
        intializeProperties();
        return props.getProperty(key);
    }
   
    /**
     * Utility method that builds the DEV-connection string using the property values: db.server , db.port and db.testdatabase
     * @return 
     *  a connection string formatted like this: "jdbc:mysql://localhost:3307/startcode"
     */
    public static String getDEV_DBConnection(){
        intializeProperties();
        return String.format("jdbc:mysql://%s:%s/%s",props.getProperty("db.server"),props.getProperty("db.port"),props.getProperty("db.database"));
    }
    
    /**
     * Utility method that builds the TEST-connection string using the property values: db.server , db.port and db.database
     * @return 
     *  a connection string formatted like this: "jdbc:mysql://localhost:3307/startcode_test"
     */
    public static String getTEST_DBConnection(){
        intializeProperties();
        String server = props.getProperty("dbtest.server") != null ? props.getProperty("dbtest.server") : props.getProperty("db.server");
        String port = props.getProperty("dbtest.port") != null ? props.getProperty("dbtest.port") : props.getProperty("db.port");
        return String.format("jdbc:mysql://%s:%s/%s",server,port,props.getProperty("dbtest.database"));
    }

     private static void intializeProperties() {
        if(props == null){
            props = loadProperties();
        }
    }
    
//    //Simple manual test
//    public static void main(String[] args) {
//       System.out.println(getPropertyValue("db.port"));
//       System.out.println(getDEV_DBConnection());
//       System.out.println(getTEST_DBConnection());
//       System.out.println(getPropertyValue("i.dont.exist"));
//    }
}
