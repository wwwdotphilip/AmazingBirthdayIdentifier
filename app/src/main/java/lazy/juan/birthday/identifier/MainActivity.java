package lazy.juan.birthday.identifier;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity {
    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    String currentDate = mMonth + "/" + mDay + "/" + mYear;
    ImageView iconDisplay;
    TextView textDisplay;
    Boolean isCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iconDisplay = (ImageView) findViewById(R.id.ivSmiley);
        textDisplay = (TextView) findViewById(R.id.tvMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View layout = inflater.inflate(R.layout.about, new LinearLayout(MainActivity.this), false);
                AlertDialog.Builder about = new AlertDialog.Builder(
                        MainActivity.this);
                about.setTitle("About");
                TextView textView = (TextView) layout
                        .findViewById(R.id.infoDetailText);
                textView.setText(R.string.about_content);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                about.setView(layout);
                about.setPositiveButton("Mail me", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL,
                                new String[]{"wwwdotphilip@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Birthday identifier");
                        i.putExtra(Intent.EXTRA_TEXT, "Hello, ");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            message("There are no email clients installed.");
                        }
                    }
                });
                about.setNegativeButton("Close", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                about.setNeutralButton("Rate this app", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                        } catch (Exception e) {
                            message("Google Play is not available in your device.");
                        }
                    }
                });
                AlertDialog displayInfo = about.create();
                displayInfo.show();
            default:
                break;
        }
        return true;
    }

    public void showDateDialog(View v) {
        isCanceled = false;
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (!isCanceled) {
                            System.out.println(monthOfYear + "/" + dayOfMonth + "/"
                                    + year);
                            compareDate(monthOfYear + "/" + dayOfMonth + "/"
                                    + year);
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Set date");
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCanceled = true;
                    }
                });
        dpd.setCancelable(false);
        dpd.show();
    }

    private void compareDate(String date) {
        if (date.equals(currentDate)) {
            iconDisplay.setImageResource(R.drawable.ic_cake);
            textDisplay.setText(R.string.success);
        } else {
            iconDisplay.setImageResource(R.drawable.ic_sad);
            textDisplay.setText(R.string.fail);
        }
    }

    private void message(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
