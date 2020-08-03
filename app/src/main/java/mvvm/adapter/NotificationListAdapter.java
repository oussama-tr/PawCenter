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
import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mvvm.model.Notification;
import mvvm.util.Common;
import mvvm.view.fragment.VetFragmentCallback;


public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder>{
    private List<Notification> listdata;
    private Context context;
    String BASE_URL = Common.BASE_IMAGE_URL;

    public NotificationListAdapter(List<Notification> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_notification_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Notification myListData = listdata.get(position);
        holder.content.setText(myListData.getContent());
        try {
            holder.date.setText(Common.getElapsedTime(myListData.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(myListData.getPet() == null && !myListData.isForAdmin()) {
            holder.photo.setImageResource(R.drawable.logo);
            holder.photo.setFillColor(context.getResources().getColor(R.color.light_cyan));
        }
        else
        {
            DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(holder.photo);
            downloadTask.execute(BASE_URL+myListData.getUser().getImage_url());
        }


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content, date;
        public CircleImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.tv_notif_date);
            this.content = (TextView) itemView.findViewById(R.id.tv_notif_text);
            this.photo = (CircleImageView) itemView.findViewById(R.id.notif_photo);
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
