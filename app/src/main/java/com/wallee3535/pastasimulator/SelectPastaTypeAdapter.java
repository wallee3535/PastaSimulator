package com.wallee3535.pastasimulator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Walter on 7/16/2016.
 * Adapter for display pasta types
 */
public class SelectPastaTypeAdapter extends ArrayAdapter<String> {

    public SelectPastaTypeAdapter(Context context, ArrayList<String> pastaTypes) {
        super(context, R.layout.select_pasta_type, pastaTypes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.pasta_type_row,parent, false);

        //set text for pasta type
        String pastaTypeItem = getItem(position);
        TextView typeTextView = (TextView) customView.findViewById(R.id.typeTextView);
        typeTextView.setText(pastaTypeItem);

        //set image view for the pasta type
        ImageView typeImageView = (ImageView) customView.findViewById(R.id.imageView);
        int drawableId=-1;
        try {
            Class res = R.drawable.class;
            Field field = res.getField(pastaTypeItem);
            drawableId = field.getInt(null);
            Drawable drawable = getContext().getDrawable(drawableId);
            typeImageView.setImageDrawable(drawable);
        }
        catch (Exception e) {
            Log.e(Globals.TAG, "Failure to get drawable id.", e);
        }
        return customView;
    }
}
