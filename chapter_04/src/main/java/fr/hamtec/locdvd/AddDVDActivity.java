package fr.hamtec.locdvd;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddDVDActivity extends AppCompatActivity {
    EditText editTitreFilm;
    EditText editAnnee;
    EditText editResume;
    Button btnAddActeur;
    Button btnOk;
    LinearLayout addActeurLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddvd);

        editTitreFilm = findViewById(R.id.addDVD_titre);
        editAnnee = findViewById(R.id.addDVD_annee);
        editResume = findViewById(R.id.addDVD_resume);
        btnAddActeur = findViewById(R.id.addDVD_addActeur);
        btnOk = findViewById(R.id.addDVD_ok);
        addActeurLayout = (LinearLayout) findViewById(R.id.addDVD_addActeurLayout);

        btnAddActeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActeur(null);
            }
        });

        // Est-ce une rotation
        if (savedInstanceState != null){
            String acteurs[] = savedInstanceState.getStringArray("acteurs");
            for (String s : acteurs) {
                addActeur(s);
            }
        }else {
            addActeur(null);
        }
    }

    private void addActeur(String content) {
        EditText editNewActeur = new EditText(this);

        editNewActeur.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS);


        if (content != null)
            editNewActeur.setText(content);

        addActeurLayout.addView(editNewActeur);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {


        String acteurs[] = new String[addActeurLayout.getChildCount()];

        for (int i = 0; i < addActeurLayout.getChildCount(); i++) {
            View child = addActeurLayout.getChildAt(i);
            if (child instanceof EditText)
                acteurs[i] = ((EditText)child).getText().toString();
        }
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArray("acteurs", acteurs);
    }
}
