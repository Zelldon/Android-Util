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
import android.view.View;
import android.widget.ProgressBar;
import de.zell.android.util.R;

/**
 * The abstract AsyncProgressTask class which defines some methods 
 * to show a progress bar or dialog if the async task is running.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 * @param <PARAM> the class for the parameter of the async task
 * @param <PROGRESS> the progress object of the async task
 * @param <RESULT> the class for the result of the async task
 */
public abstract class AsyncProgressTask<PARAM, PROGRESS, RESULT> 
        extends AsyncTask<PARAM, PROGRESS, RESULT>{
  
  
  /**
   * The progress bar which shows the current progress of the async task.
   */
  protected ProgressBar bar;
  
  /**
   * The progress dialog to show the user the progress.
   */
  protected ProgressDialog dialog;
  
  /**
   * The message which is shown by the dialog.
   */
  protected String dialogMessage;
  
  /**
   * The header which is shown by the dialog.
   */
  protected String dialogHeader;
  
  /**
   * The default ctor to create the AsyncProgressTask object.
   */
  public AsyncProgressTask() {
  }
  
  /**
   * Enables the progress bar with the given bar.
   *
   * @param bar the bar which should be used to show the progress
   */
  public void showProgress(ProgressBar bar) {
    this.bar = bar;
  }
  
  /**
   * Enables the progress dialog which should be shown if the async task is running.
   * 
   * @param dialog the dialog which should be used to show the progress
   */
  public void showProgress(ProgressDialog dialog) {
    this.dialog = dialog;
  }

  /**
   * Sets the message for the dialog which is shown for the progress.
   * 
   * @param dialogMessage the message which is shown
   */
  public void setDialogMessage(String dialogMessage) {
    this.dialogMessage = dialogMessage;
  }

  /**
   * Sets the header for the dialog which is shown for the progress.
   * 
   * @param dialogHeader the header which is shown
   */
  public void setDialogHeader(String dialogHeader) {
    this.dialogHeader = dialogHeader;
  }
  
  @Override
  protected void onPreExecute() {
    if (bar != null) {
      bar.setIndeterminate(true);
      bar.setVisibility(View.VISIBLE);
    }
    
    if (dialog != null) {
      dialog.setTitle(dialogHeader);
      dialog.setMessage(dialogMessage);
      dialog.show();
    }
    super.onPreExecute();
  }

  @Override
  protected void onPostExecute(RESULT result) {
    if (bar != null)
      bar.setVisibility(View.GONE);
    
    if (dialog != null)
      dialog.dismiss();
    
    super.onPostExecute(result);
  }
  
}
