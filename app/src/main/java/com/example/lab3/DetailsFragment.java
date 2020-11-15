package com.example.lab3;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private int isSent;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID);
        isSent = dataFromActivity.getInt(ChatRoomActivity.ITEM_ISSENT);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.msgDetail);
        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));

        TextView idView = (TextView)result.findViewById(R.id.msgID);
        idView.setText("ID = "+id);


        CheckBox checkView = (CheckBox)result.findViewById(R.id.checkID);
        if(isSent == 1)
        {
            checkView.setChecked(true);
        }
        else{
            checkView.setChecked(false);
        }

        Button hideButton = (Button)result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }


}