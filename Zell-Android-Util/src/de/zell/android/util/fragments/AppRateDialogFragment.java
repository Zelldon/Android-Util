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
package de.zell.android.util.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import de.zell.android.util.R;

/**
 * Represents the rating dialog which delegates the user to the app in the app store.
 * 
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class AppRateDialogFragment extends AutoCloseDialogFragment {
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View v = inflater.inflate(R.layout.alert_app_rate, null);
    setOnClickListenerToRateButton(v);
    setOnClickListenerToLaterButton(v);
    return builder.setView(v).create();
  }

  /**
   * Set the onClickListener to the rating button.
   * If the button was clicked the intent for the market are created and started.
   * Also the rating is saved in the preferences.
   * 
   * @param root the root view which contains the button
   */
  private void setOnClickListenerToRateButton(View root) {
    Button btn = (Button) root.findViewById(R.id.alert_app_rate_btn);
    btn.setOnClickListener(new View.OnClickListener() {

      public void onClick(View arg0) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String packageName = getActivity().getPackageName();
        String uri = String.format(getString(R.string.alert_app_rate_uri), packageName);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(packageName, MODE_PRIVATE).edit();
        editor.putBoolean(packageName, true).commit();
      }
    });
  }
  
  /**
   * Set the onClickListener to the later button.
   * After the later button is clicked the rate dialog will be dismissed.
   * 
   * @param root the root view which contains the button
   */
  private void setOnClickListenerToLaterButton(View root) {
    Button btn = (Button) root.findViewById(R.id.alert_app_rate_later_btn);
    btn.setOnClickListener(new View.OnClickListener() {

      public void onClick(View arg0) {
        dismiss();
      }
    });
  }
  
  /**
   * Returns the name of the Fragment.
   * 
   * @return the name of the fragment
   */
  public String getName() {
    return AppRateDialogFragment.class.getName();
  }
}
