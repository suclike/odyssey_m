package org.odyssey.views;

import android.content.Context;
import android.widget.TextView;

import org.odyssey.R;

public class AlbumTracksListViewItem extends GenericListItem {

    public AlbumTracksListViewItem(Context context, String number, String title, String information, String duration) {
        super(context);

        mTitleView.setText(title);
        mNumberView.setText(number);
        mInformationView.setText(information);
        mDurationView.setText(duration);
    }

    @Override
    TextView provideTitleView() {
        return (TextView) this.findViewById(R.id.item_album_tracks_title);
    }

    @Override
    TextView provideNumberView() {
        return (TextView) this.findViewById(R.id.item_album_tracks_number);
    }

    @Override
    TextView provideInformationView() {
        return (TextView) this.findViewById(R.id.item_album_tracks_additional_information);
    }

    @Override
    TextView provideDurationView() {
        return (TextView) this.findViewById(R.id.item_album_tracks_duration);
    }

    @Override
    int provideLayout() {
        return R.layout.listview_item_album_tracks;
    }
}