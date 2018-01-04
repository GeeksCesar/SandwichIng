package ingenius.sandwiching.utils;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by CesarLiizcano on 10/12/2017.
 */

public class AlertMessage {

    Snackbar snackbar ;

    public void showSnackAlert(View view, String textAlert){

        snackbar = Snackbar.make(view, textAlert, Snackbar.LENGTH_SHORT);

        View viewSnackar = snackbar.getView();

        TextView mTextView = (TextView) viewSnackar.findViewById(android.support.design.R.id.snackbar_text);
        viewSnackar.setBackgroundColor(Color.parseColor("#0754A6"));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbar.show();
    }
}
