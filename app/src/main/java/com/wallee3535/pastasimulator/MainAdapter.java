package com.wallee3535.pastasimulator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Walter on 7/15/2016.
 * Adapter for displaying dishes
 */
public class MainAdapter extends ArrayAdapter<Dish> {

    private Context context;

    public MainAdapter(Context context, Dish[] dishes) {
        super(context, R.layout.custom_dish_row, dishes);
        this.context = context;
    }

    public MainAdapter(Context context, List<Dish> dishes) {
        super(context, R.layout.custom_dish_row, dishes);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_dish_row, parent, false);

        Dish singleDishItem = getItem(position);

        //get references to pic and textviews
        ImageView typeImageView = (ImageView) customView.findViewById(R.id.typeImageView);
        TextView typeTextView = (TextView) customView.findViewById(R.id.typeTextView);
        TextView weightTextView = (TextView) customView.findViewById(R.id.weightTextView);

        //set text and image
        Drawable drawable = getContext().getDrawable(singleDishItem.getMyDrawable());
        typeImageView.setImageDrawable(drawable);
        typeTextView.setText(singleDishItem.getType());
        weightTextView.setText(singleDishItem.getAmountUnit());

        return customView;
    }
}
