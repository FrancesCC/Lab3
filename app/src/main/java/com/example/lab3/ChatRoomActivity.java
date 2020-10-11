package com.example.lab3;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity{

    ListAdapter adaptor;
    private List<Message> elements = new ArrayList<Message>();
    Message m = new Message();

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EditText chatText = (EditText)findViewById(R.id.chatText);

        // set send message to elements
        Button sendBtn = (Button)findViewById(R.id.sendButton);
        sendBtn.setOnClickListener( click -> {
            m = new Message(chatText.getText().toString(), true);
            elements.add(m);
            chatText.setText("");
            adaptor.notifyDataSetChanged();
        });

        // set receive message to elements
        Button receiveBtn = (Button)findViewById(R.id.rcvButton);
        receiveBtn.setOnClickListener( click -> {
            m = new Message(chatText.getText().toString(), false);
            elements.add(m);
            chatText.setText("");
            adaptor.notifyDataSetChanged();
        });


        ListView myList = (ListView)findViewById(R.id.myListView);
        myList.setAdapter(adaptor = new ListAdapter());
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
        String str = getResources().getString(R.string.alertYes);

        myList.setOnItemClickListener( (parent, view, position, id) -> {

            builder.setTitle(getResources().getString(R.string.alertMsg));
            builder.setMessage(getResources().getString(R.string.selectRow) + position +"\n"+getResources().getString(R.string.selectDbId) + position)
                    .setPositiveButton(getResources().getString(R.string.alertYes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            elements.remove(position);
                            adaptor.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.alertNo),null);
            AlertDialog alert = builder.create();
            alert.show();

        });
    }



    //inner class
    public class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public Object getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //return layout that will be positioned at the specified row in the list.do tis in step 9
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View thisRow = null;

            if(elements.get(position).getType()){  //if it's true
                thisRow = inflater.inflate(R.layout.receive_layout, parent,false);
                TextView receiveView = thisRow.findViewById(R.id.receiveText);
                receiveView.setText(elements.get(position).getMessage());
            }else{
                thisRow = inflater.inflate(R.layout.send_layout, parent,false);
                TextView sendView = thisRow.findViewById(R.id.sendText);
                sendView.setText(elements.get(position).getMessage());
            }

            return thisRow;
        }
    }
}



