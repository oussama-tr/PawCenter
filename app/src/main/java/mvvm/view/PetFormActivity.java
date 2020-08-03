package mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.androidminiproject.R;
import java.io.File;
import java.io.InputStream;
import de.hdodenhof.circleimageview.CircleImageView;
import mvvm.model.Pet;
import mvvm.repository.PersistResponseCallback;
import mvvm.util.Common;
import mvvm.util.Session;
import mvvm.view_model.PetViewModel;
import mvvm.view_model.PetViewModelFactory;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PetFormActivity extends AppCompatActivity implements PersistResponseCallback {

    PetViewModel petViewModel;
    Pet pet;

    String[] SPECIES = new String[] {"dog", "cat", "bird", "reptile", "rodent"};
    String[] GENDERS = new String[] {"male", "female"};

    MultipartBody.Part mfile;
    CircleImageView img;
    TextView form_btn, select_photo, form_title;
    EditText name, breed, age, weight;
    ToggleButton neutered, vaccinated;
    AutoCompleteTextView species_dropdown, gender_dropdown;

    ArrayAdapter<String> speciesAdapter, genderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);

        petViewModel = ViewModelProviders.of(this, new PetViewModelFactory(this)).get(PetViewModel.class);

        speciesAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, SPECIES);
        genderAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, GENDERS);

        species_dropdown = findViewById(R.id.species_dropdown);
        species_dropdown.setAdapter(speciesAdapter);

        gender_dropdown = findViewById(R.id.gender_dropdown);
        gender_dropdown.setAdapter(genderAdapter);

        form_btn = findViewById(R.id.form_btn);
        form_title = findViewById(R.id.pet_form_title);
        select_photo = findViewById(R.id.select_photo);
        img = findViewById(R.id.pet_photo);
        name = findViewById(R.id.pet_name);
        breed = findViewById(R.id.pet_breed);
        age = findViewById(R.id.pet_age);
        weight = findViewById(R.id.pet_weight);
        neutered = findViewById(R.id.pet_neutered);
        vaccinated = findViewById(R.id.pet_vaccinated);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            this.pet = (Pet) bundle.getSerializable("pet");
            form_title.setText("Edit your pet");
            fillForm();
        }

        findViewById(R.id.backArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        form_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isFormValid())
               {
                   if(pet != null)
                   {
                       // update existing pet

                       pet.setName(name.getText().toString());
                       pet.setSpecies(species_dropdown.getText().toString());
                       pet.setGender(gender_dropdown.getText().toString());
                       pet.setBreed(breed.getText().toString());
                       pet.setAge(Integer.valueOf(age.getText().toString()));
                       pet.setWeight(Float.valueOf(weight.getText().toString()));
                       pet.setNeutered(neutered.isChecked());
                       pet.setVaccinated(vaccinated.isChecked());
                       if(mfile != null) petViewModel.editPetWithImage(mfile, pet);
                       else petViewModel.editPetWithoutImage(pet);
                   }
                   else
                   {
                       // create a new pet
                       Pet newPet = new Pet(name.getText().toString(), species_dropdown.getText().toString(), gender_dropdown.getText().toString(),
                               breed.getText().toString(), Integer.valueOf(age.getText().toString()), Float.valueOf(weight.getText().toString()),
                               neutered.isChecked(), vaccinated.isChecked(), Session.getInstance().getUser());
                       if(mfile != null) petViewModel.createPetWithImage(mfile, newPet);
                       else petViewModel.createPetWithoutImage(newPet);
                   }

               }
            }
        });

    }

    private void fillForm()
    {
        name.setText(this.pet.getName());
        species_dropdown.setText(this.pet.getSpecies());
        gender_dropdown.setText(this.pet.getGender());
        breed.setText(this.pet.getBreed());
        age.setText(String.valueOf(this.pet.getAge()));
        weight.setText(String.valueOf(this.pet.getWeight()));
        neutered.setChecked(this.pet.isNeutered());
        vaccinated.setChecked(this.pet.isVaccinated());
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(img);
        downloadTask.execute(Common.BASE_IMAGE_URL + this.pet.getImageUrl());
    }

    private boolean isFormValid()
    {
        if(!Common.isNameValid(name.getText().toString().trim())){
            Toast.makeText(PetFormActivity.this, "Please enter a valid name for your pet !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(species_dropdown.getText().toString().trim().equals("")){
            Toast.makeText(PetFormActivity.this, "Please provide your pet species !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(gender_dropdown.getText().toString().trim().equals("")){
            Toast.makeText(PetFormActivity.this, "Please provide your pet gender !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Common.isNameValid(breed.getText().toString().trim())){
            Toast.makeText(PetFormActivity.this, "Please provide your pet breed !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(age.getText().toString().trim().equals("")){
            Toast.makeText(PetFormActivity.this, "Please provide your pet age !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(weight.getText().toString().trim().equals("")){
            Toast.makeText(PetFormActivity.this, "Please provide your pet weight !", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:
                    //data.getData returns the content URI for the selected Image
                    Uri photo = data.getData();
                    img.setImageURI(photo);

                    //Create a file object using file path
                    File file = new File(getPathFromURI(photo));
                    RequestBody filePart = RequestBody.create(MediaType.parse(getContentResolver().getType(photo)), file);
                    this.mfile = MultipartBody.Part.createFormData("image", file.getName(), filePart);

                    break;
            }
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onAsyncCallFinished(Object result) {
        if((Pet) result != null){
            finish();
            Intent petIntent = new Intent(PetFormActivity.this, PetActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("pet", (Pet) result);
            petIntent.putExtras(bundle);
            startActivity(petIntent);
        }
        else
            Toast.makeText(PetFormActivity.this, "An unexpected error occured !", Toast.LENGTH_SHORT).show();
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
