/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package de.zell.android.util.io;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
  
  
/**
 * Represents a file operator which enables to save a object into a file and 
 * read these object from the file.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class ObjectFileOperator {
  
  /**
   * The .obj file extension, for the object files.
   */
  public static final String FILE_EXTENSION = ".obj";
  
  /**
   * The user directory which will contain the subdirectories and files.
   */
  public static final String USER_DIR = System.getProperty("user.dir");
  
  /**
   * The file on which will be operated.
   */
  private File dir;
  
  /**
   * The constructor to create a file operator.
   * 
   * @param path                  the path of the directory from the file
   */
  public ObjectFileOperator(String path) {
    createDir(path);
  }
  
  /**
   * Creates the directory of the given path if not exits.
   * 
   * @param path                  the path of the directory
   */
  private void createDir(String path) {
    if(!path.startsWith("/")) {
      path = new StringBuilder(USER_DIR).append(File.separator)
                                                              .append(path)
                                                              .toString();
    }
    dir = new File(path);
    dir.mkdir();
  }
  
  /**
   * Returns a file object for the given file path.
   * 
   * @param name                  the path of the file
   * @return                      the corresponding file
   */
  public File getFile(String name) {
    String path = new StringBuilder(dir.getAbsolutePath())
                                    .append(File.separator)
                                    .append(name)
                                    .append(FILE_EXTENSION)
                                    .toString();
    File file = new File(path);
    return file;
  }
  
  /**
   * Writes the given object into the named file.
   * The object will be serialized into bytecode and saved in the given file.
   * 
   * @param o                     the object
   * @param fileName              the name of the file
   */
  public void writeObject(Object o, String fileName) {
    writeObject(o, getFile(fileName));
  }
  
  /**
   * Writes the given object into the file.
   * The object will be serialized into bytecode and saved in the given file.
   * 
   * @param o                     the object
   * @param file                  the file
   */
  public void writeObject(Object o, File file) {
    if (o != null && file != null) {
      try {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(o);
        out.close();
      } catch (IOException ex) {
        Logger.getLogger(ObjectFileOperator.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  /**
   * Reads the object from the named file.
   * The object will be deserialized from bytecode and created as new object.
   * 
   * @param fileName              the name of the file
   * @return                      the object
   */
  public Object readObject(String fileName) {
    return readObject(getFile(fileName));
  }
  
  /**
   * Reads the object from the file.
   * The object will be deserialized from bytecode and created as new object.
   * 
   * @param file                  the file
   * @return                      the object
   */
  public Object readObject(File file) {
    Object o = null;
    if (file == null || !file.exists() || !file.canRead() || !file.isFile()) 
      return null;
      
    try {
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
      o = in.readObject();
      in.close();
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(ObjectFileOperator.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ObjectFileOperator.class.getName()).log(Level.SEVERE, null, ex);
    }
    return o;
  } 
}