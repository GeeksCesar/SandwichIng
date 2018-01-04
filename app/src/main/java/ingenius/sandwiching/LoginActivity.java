package ingenius.sandwiching;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ingenius.sandwiching.conection.WebServices;
import ingenius.sandwiching.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    Button btnLogFacebook ;

   SharedPreferences preferences;
   SharedPreferences.Editor editor;

   String idPhone ;

    //VOLLEY
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String URl = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        btnLogFacebook = findViewById(R.id.btnLogFacebook);

        getTokenPhone();

        btnLogFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
            }
        });

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(WebServices.TAG, "facebook:onSuccess: "+loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(WebServices.TAG, "facebook:onCancel ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(WebServices.TAG, "facebook:onError: "+error.getMessage());
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(WebServices.TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else {
                    Log.w(WebServices.TAG, "signInWithCredential:failure", task.getException());
                    updateUI(null);
                }
            }
        });
    }


    private void updateUI(FirebaseUser currentUser) {

        preferences = getSharedPreferences(getString(R.string.pref_datos), Context.MODE_PRIVATE);
        editor = preferences.edit();

        editor.putString(getString(R.string.pre_email), currentUser.getEmail());
        editor.putString(getString(R.string.pre_name), currentUser.getDisplayName());

        editor.commit();

        setUsuario(currentUser.getDisplayName(), currentUser.getEmail());

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    public String keyHasher() {
        PackageInfo info;
        String keyHasher = null;
        try {
            info = getPackageManager().getPackageInfo("ingenius.sandwiching", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHasher = new String(Base64.encode(md.digest(), 0));
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyHasher;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            updateUI(currentUser);
        }
    }


    private void getTokenPhone(){
        if (SharedPrefManager.getInstance(this).getToken() != null){
            idPhone = SharedPrefManager.getInstance(this).getToken();
            Log.d(WebServices.TAG , "token: "+idPhone);
        }else {
            Log.d(WebServices.TAG , "token no generado ");
        }
    }

    private void setUsuario(final String name, final String email){
        getTokenPhone();

        requestQueue = Volley.newRequestQueue(this);

        URl = WebServices.REGISTRAR_USUARIO ;

        stringRequest = new StringRequest(Request.Method.POST, URl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String res = object.getString("message");
                    Log.d(WebServices.TAG, "response: "+res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            Log.d(WebServices.TAG, "error: " + volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params  = new HashMap<String, String>();

                params.put("name", name);
                params.put("email", email);
                params.put("token", idPhone);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
