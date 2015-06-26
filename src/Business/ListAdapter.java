package Business;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.ro3ab.*;

import java.util.List;

import Entities.Episode;


public class ListAdapter extends ArrayAdapter<Episode> {
    public Activity a;
    public List<Episode> eps_list;
    public Episode eps = new Episode();

    public ListAdapter(Activity a, List<Episode> eps_list) {
        super(a, R.drawable.listview_item, eps_list);
        this.a = (Activity) a;
        this.eps_list = eps_list;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        LayoutInflater inflater = a.getLayoutInflater();
        View rowView = inflater.inflate(R.drawable.listview_item, null, true);
        eps = eps_list.get(position);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.itemimgview);
        final ProgressBar imgprogress = (ProgressBar) rowView.findViewById(R.id.imgprogress);
        txtTitle.setText(eps.Name);
        Glide.with(a)
                .load(eps.Picture)

                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        imgprogress.setVisibility(View.GONE);

                        super.onResourceReady(drawable, anim);
                    }
                });

        //image  DisplayImage(resturent.imgUrl, imageView);

        return rowView;
    }
}
