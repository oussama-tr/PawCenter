package mvvm.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidminiproject.R;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import mvvm.adapter.VetListAdapter;
import mvvm.model.Vet;
import mvvm.repository.PersistResponseCallback;
import mvvm.util.Common;
import mvvm.view_model.VetViewModel;
import mvvm.view_model.VetViewModelFactory;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class VetFragment extends Fragment implements PersistResponseCallback, VetFragmentCallback {


    private View view;
    private Context appContext;
    TextView select_photo;
    EditText firstname, lastname;
    CircleImageView img;
    MultipartBody.Part mfile;
    Vet vet;
    VetViewModel vetViewModel;

    private RecyclerView mListView;
    private List<Vet> mList = new ArrayList<>();
    private VetListAdapter mAdapter;


    public VetFragment(Context context) {
        this.appContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vet, container, false);
        vetViewModel = ViewModelProviders.of(this, new VetViewModelFactory(this)).get(VetViewModel.class);

        firstname = view.findViewById(R.id.vet_firstname);
        lastname = view.findViewById(R.id.vet_lastname);
        select_photo = view.findViewById(R.id.select_photo);
        img = view.findViewById(R.id.vet_image);

        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        mListView = (RecyclerView) view.findViewById(R.id.vet_listView);
        mListView.setLayoutManager(new LinearLayoutManager(appContext));
        mAdapter = new VetListAdapter(mList, appContext, this);
        mListView.setAdapter(mAdapter);

        vetViewModel.getVets().observe(this, new Observer<List<Vet>>() {
            @Override
            public void onChanged(List<Vet> vets) {
                mList.addAll(vets);
                mAdapter.notifyDataSetChanged();
            }
        });

        view.findViewById(R.id.form_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstname.getText().toString().equals("") || lastname.getText().toString().equals(""))
                    Toast.makeText(appContext, "Please verify form !", Toast.LENGTH_SHORT).show();
                else
                {
                    // update existing vet
                    if(vet != null)
                    {
                        vet.setFirstname(firstname.getText().toString());
                        vet.setLastname(lastname.getText().toString());
                        if (mfile != null) vetViewModel.editVetWithImage(mfile, vet);
                        else vetViewModel.editVetWithoutImage(vet);
                    }
                    else
                    {
                        // create new vet
                        if(mfile == null)   Toast.makeText(appContext, "Please select a photo !", Toast.LENGTH_SHORT).show();

                        else
                        {
                            Vet newVet = new Vet(firstname.getText().toString(), lastname.getText().toString());
                            vetViewModel.createVetWithImage(mfile, newVet);
                        }

                    }
                    // clear form after submitting
                    firstname.getText().clear();
                    lastname.getText().clear();
                    mfile = null;
                    DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(img);
                    downloadTask.execute(Common.BASE_IMAGE_URL + "avatar.png");
                }

            }
        });
        return view;
    }

    @Override
    public void onAsyncCallFinished(Object result) {
        if((boolean) result)
        {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            vetViewModel.getVets().observe(this, new Observer<List<Vet>>() {
                @Override
                public void onChanged(List<Vet> vets) {
                    mList.clear();
                    mList.addAll(vets);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        else
            Toast.makeText(appContext, "An unexpected error occured !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteVet(Vet vet) {
        vetViewModel.removePet(vet);
    }

    @Override
    public void editVet(Vet vet) {
        this.vet = vet;
        firstname.setText(vet.getFirstname());
        lastname.setText(vet.getLastname());
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(img);
        downloadTask.execute(Common.BASE_IMAGE_URL + vet.getImageUrl());
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
                    RequestBody filePart = RequestBody.create(MediaType.parse(appContext.getContentResolver().getType(photo)), file);
                    this.mfile = MultipartBody.Part.createFormData("image", file.getName(), filePart);

                    break;
            }
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = appContext.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
