package mvvm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidminiproject.R;

import java.io.InputStream;
import java.util.List;

import mvvm.model.Pet;
import mvvm.util.Common;
import mvvm.util.SquareImageView;
import mvvm.view.PetActivity;
import mvvm.view.PetFormActivity;

public class GridImageAdapter extends ArrayAdapter<Pet> {
    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private List<Pet> Pets;

    public GridImageAdapter(Context context, int layoutResource, String append, List<Pet> Pets) {
        super(context, layoutResource, Pets);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.layoutResource = layoutResource;
        mAppend = append;
        this.Pets = Pets;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*
        Viewholder build pattern (Similar to recyclerview)
         */
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.image = (SquareImageView) convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position).getImageUrl();
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(holder.image);
        downloadTask.execute(Common.BASE_IMAGE_URL+imgURL);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent petIntent = new Intent(mContext, PetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pet", getItem(position));
                petIntent.putExtras(bundle);
                mContext.startActivity(petIntent);
            }
        });
        return convertView;
    }

    private static class ViewHolder{
        SquareImageView image;
    }

    // Load image from server url
    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        SquareImageView bmImage;

        public DownloadImageWithURLTask(SquareImageView bmImage) {
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
