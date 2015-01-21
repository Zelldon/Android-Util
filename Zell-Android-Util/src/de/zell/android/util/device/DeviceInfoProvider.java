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

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import de.zell.android.util.R;

/**
 * Represents the device information provider which reads
 * all useful information from the device and provide them in a device
 * entity object.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class DeviceInfoProvider {
  
  /**
   * The device entity which contains the useful information.
   */
  private static Device device;
 
  /**
   * Returns the device informations as device entity object.
   * 
   * @param c   the context of an activity
   * @return    the device entity
   */
  public static Device getDeviceInformation(Context c) {
    if (device == null) {
      Device d = new Device(getUUID(c));
      d.setLocale(getLocale(c));
      d.setResolution(getResolutionAsString(c));
      d.setCarrier(getCarrier(c));
      d.setOs(c.getString(R.string.log_device_os));
      d.setOsVersion(Build.VERSION.RELEASE);
      d.setModel(Build.MODEL);
      device = d;
    }
    
    return device;
  }
  
  /**
   * Returns the uuid of the device.
   * 
   * @param c   the context of an activity
   * @return    the uuid
   */
  private static String getUUID(Context c) {
    return Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
  }
  
  /**
   * Returns the current device locale.
   * 
   * @param c   the context of an activity
   * @return    the device locale
   */
  private static String getLocale(Context c) {
    return c.getResources().getConfiguration().locale.getCountry();
  }
  
  /**
   * Returns the carrier which is used with the device.
   * 
   * @param c   the context of an activity
   * @return    the carrier
   */
  private static String getCarrier(Context c) {
    return ((TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE))
                    .getNetworkOperatorName();
  }
  
  /**
   * Returns the resolution of the device.
   * 
   * @param c   the context of an activity
   * @return    the resolution
   */
  private static String getResolutionAsString(Context c) {
    Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE))
                                  .getDefaultDisplay();
    Point point = new Point();
    try {
        display.getSize(point);
    } catch (java.lang.NoSuchMethodError ignore) { // Older device
        point.x = display.getWidth();
        point.y = display.getHeight();
    }
    return String.format(c.getString(R.string.log_device_res), point.x, point.y);
  }
}
