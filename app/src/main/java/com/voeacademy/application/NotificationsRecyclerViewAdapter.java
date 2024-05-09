package com.voeacademy.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.voeacademy.application.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PaymentNotification> notifications;
    private String userID;
    public NotificationsRecyclerViewAdapter(Context context, ArrayList<PaymentNotification> notifications, String userID) {
        this.context = context;
        this.notifications = notifications;
        this.userID = userID;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String teacher = notifications.get(position).getTeacherName();
        String no_of_class = notifications.get(position).getNo_of_classes();
        String subject = notifications.get(position).getSubject();
        String selected_class = notifications.get(position).getSelectedClass();
        String paymentId = notifications.get(position).getPaymentID();
        String docID = notifications.get(position).getDocID();
        String timestamp = notifications.get(position).getTimeStamp().toDate().toLocaleString();
        String amount = notifications.get(position).getAmount();
        String conform = notifications.get(position).getClassStatus();
        final boolean[] markRead = {notifications.get(position).getCheck_read()};
        if(!markRead[0]){
            holder.cardView.setBackgroundResource(R.color.unread_message);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore voe_db_firestore = FirebaseFirestore.getInstance();
                    DocumentReference docRef = voe_db_firestore.collection("ClassBooking").document(docID);
                    docRef.update("check_read", true);
                    holder.cardView.setBackgroundResource(R.color.white);                }
            });
        }

        holder.paymentId.setText(paymentId);
        holder.teacherName.setText(teacher);
        holder.no_of_classes.setText(no_of_class);
        holder.selectedSubject.setText(subject);
        holder.selectedClass.setText(selected_class);
        holder.amount.setText(amount);
        holder.timestamp.setText(timestamp.toString());
        holder.conform.setText(conform);
    }

    private void updateFirestore() {
//        FirebaseFirestore voe_db_firestore = FirebaseFirestore.getInstance();
//        DocumentReference docRef = voe_db_firestore.collection("ClassBooking").document(paymentId);
//        docRef.update("check_read", true);
//        System.out.println("payment ID : "+paymentId);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName, no_of_classes , selectedClass , selectedSubject , paymentId , timestamp,amount,conform;
        LinearLayout cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacher_name);
            no_of_classes = itemView.findViewById(R.id.no_of_classes);
            selectedClass = itemView.findViewById(R.id.selected_class);
            selectedSubject = itemView.findViewById(R.id.selected_subject);
            paymentId = itemView.findViewById(R.id.payment_id);
            timestamp = itemView.findViewById(R.id.timestamp);
            amount = itemView.findViewById(R.id.amount);
            cardView = itemView.findViewById(R.id.notification_cardview);
            conform = itemView.findViewById(R.id.conform);
        }
    }
}
