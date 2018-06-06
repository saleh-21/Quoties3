package com.goldenkey.quoties;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by sohel97 on 3/4/18.
 */

public class quoteAdapter extends RecyclerView.Adapter<quoteAdapter.quoteViewHolder> {

    LayoutInflater inflater; //takes an xml file (R.layout.) and inflates it to an object of type View!
    ArrayList<Quote> data;
    Context context;
    User s;
    TextView noQuote;
    int remove;

    public quoteAdapter(Context context, ArrayList<Quote> data, int remove, TextView noQuote) {
        this.inflater = LayoutInflater.from(context);
        this.remove = remove;
        this.context = context;
        this.data = data;
        this.noQuote = noQuote;
    }

    @Override
    public quoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.quote_item, parent, false);
        return new quoteViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(final quoteViewHolder holder, final int position) {
        final int position1 = position;
        final quoteViewHolder holder1 = holder;
        final Quote quoteItem = data.get(position);
        holder.tvPublishTime.setText(quoteItem.date.toString().substring(0,quoteItem.date.toString().length()-18));
        holder.tvQuoteContent.setText("“" + quoteItem.text + "”");
        holder.username.setText(quoteItem.user);
        holder.numOfLikes.setText(String.valueOf(quoteItem.likes));
        holder.authorName.setText("―" + quoteItem.author);
        if(s.admin == 0 && !quoteItem.user.equals(s.fullName)) {
            holder.close.setVisibility(View.GONE);
        }
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position1);
                notifyItemRemoved(position1);
                notifyItemChanged(position1);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Quotes");
                mDatabase.child(quoteItem.quoteid).removeValue();
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("profile", quoteItem.uid);
                context.startActivity(i);
            }
        });
        holder.profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("profile", quoteItem.uid);
                context.startActivity(i);
            }
        });
        if(quoteItem.likedUser.contains(s.fullName)){
            holder1.heartbtn.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_heart_red));
        }
        if(quoteItem.staredUser.contains(s.fullName)){
            holder1.star.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_star));
        }
        holder.heartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Quotes");
                if(!quoteItem.likedUser.contains(s.fullName)){
                    holder1.heartbtn.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_heart_red));
                    holder1.numOfLikes.setText(String.valueOf(Integer.parseInt(holder1.numOfLikes.getText().toString())+1));
                    quoteItem.likedUser.add(s.fullName);
                    quoteItem.likes++;
                    mDatabase.child(quoteItem.quoteid).child("likes").setValue(quoteItem.likes);
                    mDatabase.child(quoteItem.quoteid).child("likedUser").setValue(quoteItem.likedUser);
                    holder1.heartbtn.clearFocus();
                }
                else if(quoteItem.likedUser.contains(s.fullName)){
                    holder1.heartbtn.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_heart_white));
                    holder1.numOfLikes.setText(String.valueOf(Integer.parseInt(holder1.numOfLikes.getText().toString())-1));
                    quoteItem.likes--;
                    mDatabase.child(quoteItem.quoteid).child("likes").setValue(quoteItem.likes);
                    quoteItem.likedUser.remove(s.fullName);
                    mDatabase.child(quoteItem.quoteid).child("likedUser").setValue(quoteItem.likedUser);
                    holder1.heartbtn.clearFocus();
                }
            }
        });
        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Quotes");
                if(!quoteItem.staredUser.contains(s.fullName)){
                    holder1.star.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_star));
                    quoteItem.staredUser.add(s.fullName);
                    mDatabase.child(quoteItem.quoteid).child("staredUser").setValue(quoteItem.staredUser);
                    return;
                }
                else{
                    holder1.star.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_star_empty));
                    quoteItem.staredUser.remove(s.fullName);
                    mDatabase.child(quoteItem.quoteid).child("staredUser").setValue(quoteItem.staredUser);
                    if(remove == 1) {
                        data.remove(position1);
                        notifyItemRemoved(position1);
                        notifyItemChanged(position1);
                        if(data.size() == 0) {
                            noQuote.setVisibility(View.VISIBLE);
                        }
                    }
                    return;
                }
            }
        });
    }

    void addItem(Quote q, User s, String quoteid) {
        this.s = s;
        if(!data.contains(q)) {
            q.quoteid = quoteid;
            data.add(0, q);
            notifyItemInserted(0);
        }
    }


    //implement the abstract methods.

    //inner class ViewHolder:
    //find the views by id inside the constructor:
    class quoteViewHolder extends RecyclerView.ViewHolder {
        //fields: tvTitle, tvSummary, ivArticle.

        TextView tvPublishTime,username, tvQuoteContent,numOfLikes, authorName;
        ImageView profile_pic;
        Button heartbtn;
        ImageView star;
        ImageView close;
        int i=0;


        public quoteViewHolder(View itemView) {
            super(itemView);
            //Find View By Id:
            username= itemView.findViewById(R.id.username);
            numOfLikes=itemView.findViewById(R.id.numOfLikes);
            tvPublishTime = itemView.findViewById(R.id.tvPublishTime);
            tvQuoteContent = itemView.findViewById(R.id.tvQuoteContent);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            heartbtn= itemView.findViewById(R.id.heart);
            authorName = itemView.findViewById(R.id.authorName);
            star = itemView.findViewById(R.id.ivStar);
            close = itemView.findViewById(R.id.ivClose);
        }
    }

}
