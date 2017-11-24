package ingenius.sandwiching.conection;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import ingenius.sandwiching.R;

/**
 * Created by cesarlizcano on 17/11/17.
 */

public class checkConection {

    Snackbar snackbar ;
    Dialog dialog ;


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

    public boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }


}
