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
package de.zell.android.util.device;

import de.zell.android.util.JSONElement;
import java.io.Serializable;


/**
 * The device entity which contains useful informations about the user device. 
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class Device implements Serializable {

  /**
   * The uuid of the device.
   */
  @JSONElement(name = "uuid")
  private String uuid;
  
  /**
   * The name of the device.
   */
  @JSONElement(name= "name")
  private String name;
  
  /**
   * The device model.
   */
  @JSONElement(name = "model")
  private String model;
  
  /**
   * The os which runs on the device.
   */
  @JSONElement(name = "os")
  private String os;
  
  /**
   * The version of the device os.
   */
  @JSONElement(name = "osVersion")
  private String osVersion;
  
  /**
   * The current device locale.
   */
  @JSONElement(name = "locale")
  private String locale;
  
  /**
   * The carrier which is used by the device.
   */
  @JSONElement(name = "carrier")
  private String carrier;
  
  /**
   * The resolution of the device.
   */
  @JSONElement(name = "resolution")
  private String resolution;

  public Device() {
  }

  public Device(String uuid) {
    this.uuid = uuid;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getOsVersion() {
    return osVersion;
  }

  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getCarrier() {
    return carrier;
  }

  public void setCarrier(String carrier) {
    this.carrier = carrier;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
