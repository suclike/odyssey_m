package org.odyssey.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.odyssey.R;

public class ArtistsGridViewItem extends GenericGridItem{

    public ArtistsGridViewItem(Context context, String title, String imageURL, ViewGroup.LayoutParams layoutParams) {
        super(context, imageURL, layoutParams);

        mTitleView.setText(title);
    }

    @Override
    TextView provideTitleView() {
        return (TextView) this.findViewById(R.id.item_artists_title);
    }

    @Override
    ImageView provideImageView() {
        return (ImageView) this.findViewById(R.id.item_artists_cover_image);
    }

    @Override
    ViewSwitcher provideViewSwitcher() {
        return (ViewSwitcher) this.findViewById(R.id.item_artists_view_switcher);
    }

    @Override
    int provideLayout() {
        return R.layout.gridview_item_artists;
    }
}
