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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import de.zell.android.util.R;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The AsyncJSONSender sends to an given URL via POST
 * some JSONObjects.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class AsyncJSONSender extends AsyncTask<JSONObject, Void, List<JSONObject>> {
  
  /**
   * The content type of the objects which will be send.
   */
  private final String CONTENT_TYPE = "application/json; charset=utf-8";
  
  
  /**
   * The content type header (key).
   */
  private static final String CONTENT_TYPE_KEY = "Content-Type";
  
  /**
   * The error log message.
   */
  private static final String ERROR_LOG_MSG = "JSON sending failed!\nStatuscode %d";
  
  /**
   * The url of the web service.
   */
  private final String url;
  
  /**
   * The job which will be executed after sending the objects.
   */
  private final PostExecuteJob job;
  
  /**
   * The progress dialog to show the user the progress.
   */
  private ProgressDialog progress;
  
  /**
   * The context of the activity which called the AsyncJSONSender.
   */
  private Context context;
  
  /**
   * The ctor of the AsyncJSONSender
   * @param url     the url of the web service
   * @param job     the job which will be executed after sending the objects 
   */
  public AsyncJSONSender(String url, PostExecuteJob job) {
    this.url = url;
    this.job = job;
  }
  
  
  /**
   * The ctor of the AsyncJSONSender with context to show a progess dialog
   * @param url     the url of the web service
   * @param job     the job which will be executed after sending the objects 
   * @param context to create a progress dialog
   */
  public AsyncJSONSender(String url, PostExecuteJob job, Context context) {
    this.url = url;
    this.job = job;
    this.progress = new ProgressDialog(context);
  }

  @Override
  protected void onPreExecute() {
    if (progress != null) {
      progress.setMessage(context.getString(R.string.progress));
      progress.show();
    }
    super.onPreExecute();
  }
  
  
  
  @Override
  protected List<JSONObject> doInBackground(JSONObject ... arg0) {
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(url);
    post.addHeader(CONTENT_TYPE_KEY, CONTENT_TYPE);
    List<JSONObject> result = new ArrayList<JSONObject>();
    if (arg0 != null) {
      for (int i = 0; i < arg0.length; i++) {
        try {
          JSONObject json = arg0[i];
          post.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));
          HttpResponse response = client.execute(post);
          if (response == null || response.getStatusLine().getStatusCode() >= 400) {
            Log.e(AsyncJSONSender.class.getName(), String.format(ERROR_LOG_MSG, response.getStatusLine().getStatusCode()));
            job.doExeptionHandling(null);
          } else {
            HttpEntity entity = response.getEntity();
            if (entity != null && entity.getContentType().getValue().equals(CONTENT_TYPE)) {
              try {
                JSONObject object = new JSONObject(EntityUtils.toString(entity));
                result.add(object);
              } catch (JSONException ex) {
                Log.e(AsyncJSONSender.class.getName(), "JSONObject creation failed" ,ex);
              }
            }
          }
        } catch (ClientProtocolException ex) {
          Log.e(AsyncJSONSender.class.getName(), "Exception" ,ex);
          job.doExeptionHandling(ex);
        } catch (UnsupportedEncodingException ex) {
          Log.e(AsyncJSONSender.class.getName(), "Exception" ,ex);
          job.doExeptionHandling(ex);
        } catch (IOException ex) {
          Log.e(AsyncJSONSender.class.getName(), "Exception" ,ex);
          job.doExeptionHandling(ex);
        } 
      }
    }
    return result;
  }

  @Override
  protected void onPostExecute(List<JSONObject> result) {
    for (JSONObject json : result) {
      Log.d(AsyncJSONSender.class.getName(), json.toString());
      if (job != null) {
        job.doJob(json);
      }
    }
    if (progress != null)
      progress.dismiss();
    
    
    job.doFinalJob();
    super.onPostExecute(result);
  }
  
  /**
   * Represents an job which will be executed after sending the objects to
   * the web service.
   */
  public interface PostExecuteJob {
    
    /**
     * The job which will be done after the send was successful.
     * 
     * @param jsonResult    the result of the sending
     */
    public void doJob(JSONObject jsonResult);
    
    /**
     * The job which will be done after an error appears.
     * 
     * @param t       the throwable which cause the error
     */
    public void doExeptionHandling(Throwable t);
    
    /**
     * These job/method is called no matter if an exception or error appears
     * or the task was successfully. 
     * Can be used to add some other stuff like clean up or something else.
     * 
     */
    public void doFinalJob();
  }
  
}
