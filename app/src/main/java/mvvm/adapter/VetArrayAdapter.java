package mvvm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidminiproject.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mvvm.model.Vet;
import mvvm.util.Common;
import mvvm.util.SquareImageView;
import mvvm.view.dialog.AppointmentDialogCallback;

public class VetArrayAdapter extends ArrayAdapter<Vet> {

    private Context mContext;
    private List<Vet> vetList = new ArrayList<>();
    AppointmentDialogCallback appointmentDialogCallback;

    public VetArrayAdapter(@NonNull Context context, int resource, @NonNull List<Vet> objects, AppointmentDialogCallback appointmentDialogCallback) {
        super(context, resource, objects);
        mContext = context;
        vetList = objects;
        this.appointmentDialogCallback = appointmentDialogCallback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.layout_vet_spinneritem,parent,false);

        Vet currentVet = vetList.get(position);

        CircleImageView image = (CircleImageView)listItem.findViewById(R.id.vet_photo);
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(image);
        downloadTask.execute(Common.BASE_IMAGE_URL+ currentVet.getImageUrl());


        TextView firstname = (TextView) listItem.findViewById(R.id.vet_firstname);
        firstname.setText("Dr. " + currentVet.getFirstname());

        TextView lastname = (TextView) listItem.findViewById(R.id.vet_lastname);
        lastname.setText(currentVet.getLastname());

        appointmentDialogCallback.onVetSelected(currentVet);

        return listItem;
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
