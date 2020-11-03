package com.example.lab3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity{

    ListAdapter adaptor;
    private List<Message> elements = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        EditText chatText = (EditText)findViewById(R.id.chatText);
        ListView myList = (ListView)findViewById(R.id.myListView);

        loadDataFromDatabase(); //get any previously saved Contact objects

        adaptor = new ListAdapter();
        myList.setAdapter(adaptor);


        // set send message to elements
        Button sendBtn = (Button)findViewById(R.id.sendButton);
        sendBtn.setOnClickListener( click -> {
            String getText = chatText.getText().toString();

            // Add to database and get the new ID
            ContentValues newRowValues = new ContentValues();

            newRowValues.put(MyOpener.COL_MESSAGE, getText);
            newRowValues.put(MyOpener.COL_DIRECTION, 1);

            long newID = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Message m = new Message(getText, 1,newID);
            elements.add(m);
            chatText.setText("");
            adaptor.notifyDataSetChanged();
        });

        // set receive message to elements
        Button receiveBtn = (Button)findViewById(R.id.rcvButton);
        receiveBtn.setOnClickListener( click -> {

            String getText = chatText.getText().toString();

            // Add to database and get the new ID
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, getText);
            newRowValues.put(MyOpener.COL_DIRECTION, 0);

            long newID = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Message m = new Message(chatText.getText().toString(), 0,newID);
            elements.add(m);
            chatText.setText("");
            adaptor.notifyDataSetChanged();
        });




        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);

        myList.setOnItemClickListener( (parent, view, position, id) -> {

            Message selectedItem = elements.get(position);


            builder.setTitle(getResources().getString(R.string.alertMsg));
            builder.setMessage(getResources().getString(R.string.selectRow) + position +"\n"+getResources().getString(R.string.selectDbId) + getItemId(position))
                    .setPositiveButton(getResources().getString(R.string.alertYes),(click,b)-> {

                        deleteContact(selectedItem); //remove the selected message from database.
                        elements.remove(position); //also delete from list
                        adaptor.notifyDataSetChanged();

                    })
                    .setNegativeButton(getResources().getString(R.string.alertNo),null);
            AlertDialog alert = builder.create();
            alert.show();

        });
    }

    protected void deleteContact(Message msg)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(msg.getID())});
    }

    //inner class
    public class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public Message getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {

            return getItem(position).getID();
        }

        //return layout that will be positioned at the specified row in the list.do tis in step 9
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = getLayoutInflater();
            View thisRow = null;

            Message currentRow = getItem(position);
            if(currentRow.getType() == 1)
            {
                thisRow = inflater.inflate(R.layout.receive_layout, parent,false);
                TextView receiveView = thisRow.findViewById(R.id.receiveText);
                receiveView.setText(currentRow.getMessage());
            }
            else
            {
                thisRow = inflater.inflate(R.layout.send_layout, parent,false);
                TextView sendView = thisRow.findViewById(R.id.sendText);
                sendView.setText(currentRow.getMessage());
            }

//            if(elements.get(position).getType() == 1){  //if it's true
//                thisRow = inflater.inflate(R.layout.receive_layout, parent,false);
//                Message currentRow = getItem(position);
//
//                TextView receiveView = thisRow.findViewById(R.id.receiveText);
//                //receiveView.setText(elements.get(position).getMessage());
//                receiveView.setText(currentRow.getMessage());
//
//            }else{
//                thisRow = inflater.inflate(R.layout.send_layout, parent,false);
//                Message currentRow = getItem(position);
//
//                TextView sendView = thisRow.findViewById(R.id.sendText);
//                //sendView.setText(elements.get(position).getMessage());
//                sendView.setText(currentRow.getMessage());
//            }

            return thisRow;
        }
    }

    private void loadDataFromDatabase()
    {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //calls onCreate() if the table is not exists.

        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE,MyOpener.COL_DIRECTION};

        Cursor results = db.query(false,MyOpener.TABLE_NAME,columns,null,null,null,null,null,null);

        int messageIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int directionIndex = results.getColumnIndex(MyOpener.COL_DIRECTION);
        int idIndex = results.getColumnIndex(MyOpener.COL_ID);

        //loop over the results, return true if there is a next item
        while( results.moveToNext() ){

            String message = results.getString(messageIndex);
            int direction = results.getInt(directionIndex);
            long id = results.getLong(idIndex);

            printCursor(results, db.getVersion());

            //add the new Message to the arrayList
            elements.add(new Message(message,direction,id));
        }

    }

    //last week we returned (long) position. Now we return the object's database id that we get from line 71
    public long getItemId(int position)
    {
        return getItem(position).getID();
    }

    public Message getItem(int position) {
        return elements.get(position);
    }

    public void printCursor(Cursor c, int version){


        int messageIndex = c.getColumnIndex(MyOpener.COL_MESSAGE);
        int directionIndex = c.getColumnIndex(MyOpener.COL_DIRECTION);
        String message = c.getString(messageIndex);
        int direction = c.getInt(directionIndex);
        Log.d("Version number: ",String.valueOf(version));  //get version number
        Log.d("Number of column : ", String.valueOf(c.getColumnCount()));  //get column number
        Log.d("Name of the columns", String.valueOf("column name : "+c.getColumnName(messageIndex)+ " and :"+c.getColumnName(directionIndex)));  //get column names
        Log.d("Number of rows: ", String.valueOf(c.getPosition()));  //get number of rows
        Log.d("each row of result: ",message +" direction: "+ String.valueOf(direction)); //each row of result



        //db.getVersion();
    }
}



