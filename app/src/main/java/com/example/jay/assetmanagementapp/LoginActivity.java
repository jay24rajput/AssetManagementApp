package com.example.jay.assetmanagementapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;




public class LoginActivity extends AppCompatActivity{

    int RC_SIGN_IN=200;
    private GoogleSignInClient mGoogleSignInClient;
    SignInButton SignInButton;

    public static String name;
    public static String email;
    public static Uri photo_url;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton=(SignInButton)findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    protected void onStart() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    String TAG="Error";
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"LOGIN SUCCESSFUL",Toast.LENGTH_LONG).show();


            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            if (acct != null) {
                  name = acct.getDisplayName();
                 email = acct.getEmail();
                 photo_url = acct.getPhotoUrl();
            }

            //Display current details
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Name",name );
            editor.putString("Email",email);
            editor.putString("Photo URL", String.valueOf(photo_url));
            editor.commit();



            Log.e("details",name+"\n"+email+"\n"+photo_url);
            Intent intent=new Intent(this,NavDrawer.class);
            startActivity(intent);


            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
}



