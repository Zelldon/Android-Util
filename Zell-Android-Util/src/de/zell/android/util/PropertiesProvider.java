/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.zell.android.util;


import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads properties from properties file 
 * if the property is not found it will try the system properties.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class PropertiesProvider {
  
  /**
   * The name of the property file.
   */
  private static String PROPERTY_FILE = "android.properties";
  
  
  //============================================================
  
  /**
   * The instance of the PropertiesProvider class.
   */
  private static final PropertiesProvider instance = new PropertiesProvider();
  
  /**
   * The key-value pairs named as properties.
   */
  private final Properties properties;

  /**
   * The default constructor to create a android properties object.
   */
  private PropertiesProvider() {
    properties = new Properties();
    init();
  }

  /**
   * Returns the instance of the PropertiesProvider.
   * 
   * @return                      the instance
   */
  public static PropertiesProvider getInstance() {
    return instance;
  }
  
  /**
   * Returns the value for the given composite property.
   * The property must contain a template which will be replaced
   * by the composite property value
   * 
   * @param property              the property
   * @param composite             the composite value
   * @return                      the value
   */
  public String getCompositeProperty(String property, String composite) {
    return getProperty(MessageFormat.format(property, composite));
  }
  
  /**
   * Returns an array of values for the given property.
   * 
   * @param property              the property
   * @return                      the values
   */
  public String[] getPropertyArray(String property) {
    String[] pArray = null;
    String p = getProperty(property);
    if (p == null) 
      p = System.getProperty(property);
    if (p != null) {
      if (p.contains(",")) {
        pArray = p.split(",");
      } else {
        pArray = new String[1];
        pArray[0] = p;
      }
    }
    return pArray;
  }
  
  /**
   * Returns the property value for the given property.
   * 
   * @param key                   the property
   * @return                      the value of the property
   */
  public String getProperty(String key) {
    if (properties.isEmpty())
      init();
    
    String p = properties.getProperty(key);
    return p == null ? System.getProperty(key) : p;
  }
  
  /**
   * Returns the property value for the given property.
   * 
   * @param key                   the property
   * @param defaultValue          the default value
   * @return                      the value of the property
   */
  public String getProperty(String key, String defaultValue) {
    if (properties.isEmpty())
      init();
    
    String p = properties.getProperty(key, defaultValue);
    
    return p == null ? System.getProperty(key, defaultValue) : p; 
  }
  
  
  /**
   * Initialized the PropertiesProvider object.
   * Reads the property file and saves the values into the Properties object.
   */
  private void init() {
    init(this.getClass());
  }
  
  /**
   * Initialized the PropertiesProvider object.
   * Reads the property file from class path 
   * and saves the values into the Properties object.
   * 
   * @param c the class which defines the class path
   */
  protected void init(Class c) { 
    try {
      //load a properties file from class path, inside static method
      InputStream stream = c.getResourceAsStream(PROPERTY_FILE);
      if (stream != null)
        properties.load(stream);

    } catch (IOException ex) {
      Logger.getLogger(PropertiesProvider.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
    }
  }
  
  public static String getPropertyFile() {
    return PROPERTY_FILE;
  }
  
  public static void setPropertyFile(Class c, String file) {
    PROPERTY_FILE = file;
    instance.init(c);
  }
}