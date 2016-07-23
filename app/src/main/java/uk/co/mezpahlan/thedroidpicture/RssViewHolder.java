package uk.co.mezpahlan.thedroidpicture;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mpahlan on 20/07/16.
 */
public class RssViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView image;
    private TextView description;

    public RssViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        image = (ImageView) itemView.findViewById(R.id.rss_image);
        description = (TextView) itemView.findViewById(R.id.rss_description);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked Position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getDescription() {
        return description;
    }
}
