package mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import de.hdodenhof.circleimageview.CircleImageView;
import com.example.androidminiproject.R;
import mvvm.model.Pet;
import mvvm.repository.PersistResponseCallback;
import mvvm.util.Common;
import mvvm.view.dialog.AppointmentActivity;
import mvvm.view_model.PetViewModel;
import mvvm.view_model.PetViewModelFactory;

public class PetActivity extends AppCompatActivity implements PersistResponseCallback {

    PetViewModel petViewModel;

    private Pet pet;
    TextView petname, name, species, gender, breed, age, weight;
    ImageView neutered, vaccinated;
    CircleImageView photo;
    Button appointment_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        petViewModel = ViewModelProviders.of(this, new PetViewModelFactory(this)).get(PetViewModel.class);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        pet = (Pet) bundle.getSerializable("pet");

        photo = findViewById(R.id.pet_photo);

        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(photo);
        downloadTask.execute(Common.BASE_IMAGE_URL + pet.getImageUrl());

        petname = findViewById(R.id.pet_name);
        name = findViewById(R.id.petName);
        species = findViewById(R.id.pet_species);
        gender = findViewById(R.id.pet_gender);
        breed = findViewById(R.id.pet_breed);
        age = findViewById(R.id.pet_age);
        weight = findViewById(R.id.pet_weight);
        neutered = findViewById(R.id.ic_neutered);
        vaccinated = findViewById(R.id.ic_vaccinated);
        appointment_btn = findViewById(R.id.appointment_btn);

        name.setText(pet.getName());
        species.setText("Species : " + pet.getSpecies());
        gender.setText("Gender : " + pet.getGender());
        breed.setText("Breed : " + pet.getBreed());
        age.setText("Age : " + String.valueOf(pet.getAge()) + " years");
        weight.setText("Weight : " + String.valueOf(pet.getWeight()) + " Kg");

        if(!pet.isNeutered())
            neutered.setColorFilter(PetActivity.this.getColor(R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
        if(!pet.isVaccinated())
            vaccinated.setColorFilter(PetActivity.this.getColor(R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);

        petname.setText(pet.getName());

        findViewById(R.id.edit_pet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent petFormIntent = new Intent(PetActivity.this, PetFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pet", pet);
                petFormIntent.putExtras(bundle);
                startActivity(petFormIntent);
            }
        });

        findViewById(R.id.remove_pet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petViewModel.removePet(pet);
            }
        });

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appointment_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent appointmentFormIntent = new Intent(PetActivity.this, AppointmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pet", pet);
                appointmentFormIntent.putExtras(bundle);
                startActivity(appointmentFormIntent);
            }
        });
    }

    @Override
    public void onAsyncCallFinished(Object result) {
        if((boolean) result)
        {
            finish();
        }
        else
            Toast.makeText(PetActivity.this, "An unexpected error occured !", Toast.LENGTH_SHORT).show();
    }

    // Load image from server url
    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageWithURLTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
