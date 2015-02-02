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
package de.zell.android.util.async;

/**
 * Represents a key-value pair for an URL and an ETAG.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class GetRequestInfo {
  
  /**
   * The URL.
   */
  private String url;
  
  /**
   * The ETAG for the corresponding URL.
   */
  private String etag;

  /**
   * The ctor of the GetRequestInfo.
   * 
   * @param url   the URL
   * @param etag  the ETAG
   */
  public GetRequestInfo(String url, String etag) {
    this.url = url;
    this.etag = etag;
  }

  /**
   * Returns the URL.
   * 
   * @return    the URL
   */
  public String getUrl() {
    return url;
  }
  
  /**
   * Replaces the URL.
   * @param url     the URL
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Returns the ETAG for the corresponding URL.
   * @return      the ETAG
   */
  public String getEtag() {
    return etag;
  }

  /**
   * Replaces the ETAG for the corresponding URL.
   * @param etag    the ETAG
   */
  public void setEtag(String etag) {
    this.etag = etag;
  }
}
