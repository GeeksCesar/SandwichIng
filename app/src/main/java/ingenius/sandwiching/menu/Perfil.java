package ingenius.sandwiching.menu;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import ingenius.sandwiching.LoginActivity;
import ingenius.sandwiching.R;
import ingenius.sandwiching.conection.WebServices;

import static android.content.Context.MODE_PRIVATE;


public class Perfil extends Fragment {

    View view;
    Context mContext;

    SharedPreferences preferences;

    private FirebaseAuth mAuth;

    RelativeLayout contenedorView;
    Button btnLogOutFacebook, btnSinLogin;
    TextView tvEmailUsuario, tvNameUsuario;
    String nameUsuario, emailUsuario;

    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.menu_perfil, container, false);

        mContext = getActivity();

        mAuth = FirebaseAuth.getInstance();

        contenedorView = view.findViewById(R.id.contenedorView);
        tvEmailUsuario = view.findViewById(R.id.tvEmailUsuario);
        tvNameUsuario = view.findViewById(R.id.tvNameUsuario);
        btnLogOutFacebook = view.findViewById(R.id.btnLogOutFacebook);
        btnSinLogin = view.findViewById(R.id.btnSinLogin);

        //Method
        getPreferencesUser();
        showInputSession();
        cerrarSession();


        return view;
    }

    private void getPreferencesUser() {
        preferences = mContext.getSharedPreferences(getString(R.string.pref_datos), MODE_PRIVATE);

        nameUsuario = preferences.getString(getString(R.string.pre_name), "");
        emailUsuario = preferences.getString(getString(R.string.pre_email), "");

        Log.d(WebServices.TAG, "name: " + nameUsuario);
        Log.d(WebServices.TAG, "email: " + emailUsuario);

        tvEmailUsuario.setText(emailUsuario);
        tvNameUsuario.setText(nameUsuario);
    }

    private void showInputSession() {
        if (AccessToken.getCurrentAccessToken() != null) {
            btnLogOutFacebook.setVisibility(View.VISIBLE);
            btnSinLogin.setVisibility(View.INVISIBLE);
            tvNameUsuario.setVisibility(View.VISIBLE);
            tvEmailUsuario.setVisibility(View.VISIBLE);
        } else {
            btnLogOutFacebook.setVisibility(View.INVISIBLE);
            btnSinLogin.setVisibility(View.VISIBLE);
            tvNameUsuario.setVisibility(View.INVISIBLE);
            tvEmailUsuario.setVisibility(View.INVISIBLE);
        }
    }

    private void screenLogin() {
        Intent intenLogin = new Intent(mContext, LoginActivity.class);
        preferences = mContext.getSharedPreferences(getString(R.string.pref_datos), Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        startActivity(intenLogin);
        getActivity().finish();
    }

    private void cerrarSession() {
        btnLogOutFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    screenLogin();
                }
            }
        });

        btnSinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenLogin();
            }
        });
    }

}
