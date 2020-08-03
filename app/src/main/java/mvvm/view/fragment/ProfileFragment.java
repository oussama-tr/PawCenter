package mvvm.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.androidminiproject.R;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import mvvm.adapter.GridImageAdapter;
import mvvm.model.Pet;
import mvvm.model.User;
import mvvm.util.Common;
import mvvm.view.HomeActivity;
import mvvm.view.LoginActivity;
import mvvm.view.PetFormActivity;
import mvvm.view_model.PetViewModel;
import mvvm.view_model.PetViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private PetViewModel petViewModel;
    private List<Pet> mList = new ArrayList<>();
    private View view;
    private Context appContext;

    GridView gridView;
    TextView username, name, email, number;
    Button editProfile_btn;
    User user;
    FrameLayout frameLayout;
    CircleImageView img;
    GridImageAdapter adapter;

    public ProfileFragment(Context context, User u) {
        this.appContext = context;
        user = u;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        setupToolbar();

        petViewModel = ViewModelProviders.of(this, new PetViewModelFactory()).get(PetViewModel.class);

        username = view.findViewById(R.id.profile_username);
        name = view.findViewById(R.id.profile_fullName);
        email = view.findViewById(R.id.profile_email);
        number = view.findViewById(R.id.profile_number);
        editProfile_btn = view.findViewById(R.id.profile_edit_btn);
        gridView = (GridView) view.findViewById(R.id.gridView);
        frameLayout = view.findViewById(R.id.profile_frameLayout);

        username.setText(user.getUsername());
        name.setText(user.getFirstName() + " " + user.getLastName());
        email.setText(user.getEmail());
        number.setText("(+216) " + String.valueOf(user.getNumber()));

        view.findViewById(R.id.add_pet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent petFormIntent = new Intent(appContext, PetFormActivity.class);
                startActivity(petFormIntent);
            }
        });

        editProfile_btn.setVisibility(View.INVISIBLE);

        editProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: this is boring and has some work , follow the callback stuff like in pet ( async upload image --> get imageurl after upload ---> persist entity )
                //TODO: also manage updating this fragment after submiting ? how to do so without livedata ? hmmmm
               /* Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.home_activity));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
            }
        });

        view.findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = appContext.getApplicationContext().getSharedPreferences("UserPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent loginIntent = new Intent(appContext, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        // grid layout
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/3;
        gridView.setColumnWidth(imageWidth);
        adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, "", mList);
        gridView.setAdapter(adapter);

        img = (CircleImageView) view.findViewById(R.id.profile_profile_image);
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(img);
        downloadTask.execute(Common.BASE_IMAGE_URL + user.getImage_url());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mList.clear();
        adapter.notifyDataSetChanged();

        petViewModel.getUserPets(user.getId()).observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                mList.addAll(pets);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
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
