package fr.hamtec.locdvd;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewDVDActivity extends AppCompatActivity {
    TextView txtTitreDVD;
    TextView txtAnneeDVD;
    TextView txtActeur1;
    TextView txtActeur2;
    TextView txtResumeFilm;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdvd);

        txtTitreDVD = findViewById(R.id.titreDVD);
        txtAnneeDVD = findViewById(R.id.anneeDVD);
        txtActeur1 = findViewById(R.id.acteur1);
        txtActeur2 = findViewById(R.id.acteur2);
        txtResumeFilm = findViewById(R.id.resumeFilm);
    }

    @Override
    protected void onResume() {
        super.onResume();

        txtTitreDVD.setText("Les vacances du petit Nicolas");
        txtAnneeDVD.setText(String.format(getString(R.string.annee_de_sortie), 2014));
        txtActeur1.setText("Valérie Lemercier");
        txtActeur2.setText("Kad Merad");
        String resume = "C'est la fin de l'année scolaire. Le moment tant " +
                "attendu des vacances est arrivé. Le petit Nicolas, " +
                "ses parents et Mémé prennent la route en direction " +
                "de la mer, et s'installent pour quelques temps " +
                "à l'Hôtel Beau-Rivage. Sur la plage, " +
                "Nicolas se fait vite de nouveaux copais";
        txtResumeFilm.setText(resume);

    }
}
