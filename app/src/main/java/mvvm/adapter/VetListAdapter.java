package mvvm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidminiproject.R;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mvvm.model.Vet;
import mvvm.util.Common;
import mvvm.util.SquareImageView;
import mvvm.view.fragment.VetFragmentCallback;


public class VetListAdapter extends RecyclerView.Adapter<VetListAdapter.ViewHolder>{
    private List<Vet> listdata;
    private Context context;
    String BASE_URL = Common.BASE_IMAGE_URL;
    VetFragmentCallback vetFragmentCallback;

    public VetListAdapter(List<Vet> listdata, Context context, VetFragmentCallback vetFragmentCallback) {
        this.listdata = listdata;
        this.context = context;
        this.vetFragmentCallback = vetFragmentCallback;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_vet_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Vet myListData = listdata.get(position);
        holder.firstname.setText("Dr. " + myListData.getFirstname());
        holder.lastname.setText(myListData.getLastname());
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(holder.photo);
        downloadTask.execute(BASE_URL+myListData.getImageUrl());
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vetFragmentCallback.deleteVet(myListData);
            }
        });

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vetFragmentCallback.editVet(myListData);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstname, lastname;
        public ImageView delete_btn, edit_btn;
        public CircleImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.firstname = (TextView) itemView.findViewById(R.id.vet_firstname);
            this.lastname = (TextView) itemView.findViewById(R.id.vet_lastname);
            this.photo = (CircleImageView) itemView.findViewById(R.id.vet_photo);
            this.delete_btn = (ImageView) itemView.findViewById(R.id.remove_vet_btn);
            this.edit_btn = (ImageView) itemView.findViewById(R.id.edit_vet_btn);
        }
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
