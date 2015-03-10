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

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a async GET requester. For given URL's the AsyncTask will execute
 * GET requests and returns the result of each request as JSONObject in given
 * PostExecuteJob doJob method call. Means to executed the AsyncTask, the caller
 * needs to pass into the task the URL's to request and a PostExecuteJob Object
 * which will be used to return the values or used if an exception appears. If
 * the Task was successful the doJob Method will be called of the
 * PostExecutedJob, if not the doException will be called.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class AsyncGETRequester extends AsyncProgressTask<GetRequestInfo, Void, List<JSONObject>> {

  /**
   * The content type of the objects which will be send.
   */
  private static final String CONTENT_TYPE = "application/json";
  /**
   * The error log message.
   */
  private static final String ERROR_LOG_MSG = "Oops, the HTTP-GET request failed! Statuscode: %d";
  /**
   * The exception log message.
   */
  private static final String EXECEPTION_LOG_MSG = "Exception!";
  /**
   * The job which will be executed after sending the objects.
   */
  private final PostExecuteJob job;
  /**
   * The credentials for basic authentication.
   */
  private UsernamePasswordCredentials credentials;
  /**
   * The string for the If-None-Match Http-header.
   */
  private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
  /**
   * The string for the Etag Http-header.
   */
  private static final String HEADER_ETAG = "Etag";

  /**
   * The gzip header value for the Content Encoding Header.
   */
  private static final String CONTENT_ENCODING_GZIP = "gzip";
  
  /**
   * The ctor of the AsyncJSONSender
   *
   * @param job the job which will be executed after sending the objects
   */
  public AsyncGETRequester(PostExecuteJob job) {
    this.job = job;
  }

  /**
   * The ctor of the AsyncJSONSender
   *
   * @param job the job which will be executed after sending the objects
   * @param credentials the user credentials to make the get request
   */
  public AsyncGETRequester(PostExecuteJob job, UsernamePasswordCredentials credentials) {
    this.job = job;
    this.credentials = credentials;
  }

  @Override
  protected List<JSONObject> doInBackground(GetRequestInfo... urls) {
    List<JSONObject> result = new ArrayList<JSONObject>();
    if (urls != null) {
      final int len = urls.length;
      for (int i = 0; i < len; i++) {
        String url = urls[i].getUrl();
        if (url != null) {
          HttpGet get = new HttpGet(url);
          String etag = urls[i].getEtag();
          if (etag != null && !etag.isEmpty()) {
            get.setHeader(HEADER_IF_NONE_MATCH, etag);
          }

          if (credentials != null) {
            get.addHeader(BasicScheme.authenticate(credentials, HTTP.UTF_8, false));
          }
          executeGetRequest(get, result);
        }
      }
    }
    return result;
  }

  private void executeGetRequest(HttpGet get, List<JSONObject> result) {
    HttpClient client = new DefaultHttpClient();
    try {
      HttpResponse response = client.execute(get);
      if (response == null) {
        job.doExeptionHandling(null);
      } else {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 400) {
          Log.e(AsyncGETRequester.class.getName(), String.format(ERROR_LOG_MSG, response.getStatusLine().getStatusCode()));
          job.doExeptionHandling(null);
        } else {
          if (statusCode != 304) {
            HttpEntity entity = response.getEntity();
            if (entity != null && entity.getContentType().getValue().contains(CONTENT_TYPE)) {
              try {
                String jsonStr = extractEntityContent(entity);
                JSONObject object = new JSONObject(jsonStr);
                result.add(object);
              } catch (JSONException ex) {
                Log.e(AsyncGETRequester.class.getName(), EXECEPTION_LOG_MSG, ex);
              }
            }
          }
          Header responseEtag = response.getFirstHeader(HEADER_ETAG);
          if (responseEtag != null) {
            String newEtag = responseEtag.getValue();
            Header ifNoneMatchHeader = get.getFirstHeader(HEADER_IF_NONE_MATCH);
            if (newEtag != null
                    && (ifNoneMatchHeader == null
                    || !newEtag.equalsIgnoreCase(ifNoneMatchHeader.getValue()))) {
              job.handleNewEtag(get.getURI().toString(), newEtag);
            }
          }
        }
      }
    } catch (IOException ex) {
      Log.e(AsyncGETRequester.class.getName(), EXECEPTION_LOG_MSG, ex);
      job.doExeptionHandling(ex);
    }
  }

  /**
   * Extracts the http entity and returns the content as string. If the entity
   * is encoded with gzip the zip will be decoded.
   *
   * @param entity the entity which will be extracted
   * @return the entity content as string
   */
  private String extractEntityContent(HttpEntity entity) {
    String content = "";
    GZIPInputStream zis = null;
    try {
      Header encoding = entity.getContentEncoding();
      if (encoding != null && encoding.getValue().contains(CONTENT_ENCODING_GZIP)) {
        byte str[] = new byte[1024];
        zis = new GZIPInputStream(new BufferedInputStream(entity.getContent()));
        StringBuilder builder = new StringBuilder();
        int decompressedSize;
        while ((decompressedSize = zis.read(str, 0, str.length)) != -1) {
          builder.append(new String(str, 0, decompressedSize, HTTP.UTF_8));
        }
        content = builder.toString();
      } else {
        content = EntityUtils.toString(entity, HTTP.UTF_8);
      }
    } catch (IOException ex) {
      Log.e(AsyncGETRequester.class.getName(), IOException.class.getName(), ex);
    } finally {
      try {
        if (zis != null) {
          zis.close();
        }
      } catch (IOException ex) {
        Log.e(AsyncGETRequester.class.getName(), IOException.class.getName(), ex);
      }
    }
    return content;
  }

  @Override
  protected void onPostExecute(List<JSONObject> result) {
    for (JSONObject json : result) {
      if (job != null) {
        job.doJob(json);
      }
    }
    super.onPostExecute(result);
  }

  /**
   * Represents an job which will be executed after sending the objects to the
   * web service.
   */
  public interface PostExecuteJob {

    /**
     * The job which will be done after the send was successful.
     *
     * @param response the result of the sending
     */
    public void doJob(JSONObject response);

    /**
     * The job which will be done after an error appears.
     *
     * @param t the throwable which cause the error
     */
    public void doExeptionHandling(Throwable t);

    /**
     * Method should handle the new etag.
     *
     * @param url the corresponding url
     * @param newEtag the new etag
     */
    public void handleNewEtag(String url, String newEtag);
  }
}
