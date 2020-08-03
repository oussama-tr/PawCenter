package mvvm.view.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.androidminiproject.R;
import java.io.InputStream;
import de.hdodenhof.circleimageview.CircleImageView;
import mvvm.model.Pet;
import mvvm.util.Common;

public class PetDialog extends AppCompatActivity {

    private Pet pet;
    TextView name, species, gender, breed, age, weight;
    ImageView neutered, vaccinated;
    CircleImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pet);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        pet = (Pet) bundle.getSerializable("pet");

        photo = findViewById(R.id.pet_photo);

        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(photo);
        downloadTask.execute(Common.BASE_IMAGE_URL + pet.getImageUrl());

        name = findViewById(R.id.petName);
        species = findViewById(R.id.pet_species);
        gender = findViewById(R.id.pet_gender);
        breed = findViewById(R.id.pet_breed);
        age = findViewById(R.id.pet_age);
        weight = findViewById(R.id.pet_weight);
        neutered = findViewById(R.id.ic_neutered);
        vaccinated = findViewById(R.id.ic_vaccinated);

        name.setText(pet.getName());
        species.setText("Species : " + pet.getSpecies());
        gender.setText("Gender : " + pet.getGender());
        breed.setText("Breed : " + pet.getBreed());
        age.setText("Age : " + String.valueOf(pet.getAge()) + " years");
        weight.setText("Weight : " + String.valueOf(pet.getWeight()) + " Kg");

        if(!pet.isNeutered())
            neutered.setColorFilter(PetDialog.this.getColor(R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
        if(!pet.isVaccinated())
            vaccinated.setColorFilter(PetDialog.this.getColor(R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
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
