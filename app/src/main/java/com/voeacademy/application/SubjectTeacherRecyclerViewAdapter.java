package com.voeacademy.application;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.voeacademy.application.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class SubjectTeacherRecyclerViewAdapter extends RecyclerView.Adapter<SubjectTeacherRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SubjectTeacher> subjectTeachersList;
    String sub;

    public SubjectTeacherRecyclerViewAdapter(Context context, ArrayList<SubjectTeacher> subjectTeachersList , String sub) {
        this.context = context;
        this.subjectTeachersList = subjectTeachersList;
        this.sub = sub;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_teacher_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String teacherID = subjectTeachersList.get(position).getTeacherID();
        String imageUrl = subjectTeachersList.get(position).getImageUrl();
        String name = subjectTeachersList.get(position).getName();
        String rating = subjectTeachersList.get(position).getRating();
        List<String> subjectList = subjectTeachersList.get(position).getSubjectList();
//        pos = holder.getAdapterPosition();

        holder.teacherName.setText(name);
        holder.teacherRating.setText("Rating : "+rating+"/5");
        Glide.with(context).load(imageUrl).into(holder.teacherProfilePic);


        holder.bookClassBtn.setOnClickListener(view -> {
            Intent i = new Intent(context, BookingPage.class);
            i.putExtra("subject",sub);
            i.putExtra("teacher id",teacherID);

//            Toast.makeText(context, "Teacher_id : "+ teacherID, Toast.LENGTH_SHORT).show();


            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        holder.teacherProfileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Teacher_Profile_Details.class);
                i.putExtra("subject",sub);
                i.putExtra("teacherID",teacherID);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectTeachersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView teacherName, teacherRating, teacherProfileDetail;
        ImageView teacherProfilePic;
        Button bookClassBtn;
        FlexboxLayout subjectFlexLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacher_name);
            teacherRating = itemView.findViewById(R.id.teacher_rating);
            teacherProfileDetail = itemView.findViewById(R.id.tecaher_profile_detail);
            teacherProfilePic = itemView.findViewById(R.id.teacher_profile_pic);
            bookClassBtn = itemView.findViewById(R.id.pay_button);
            subjectFlexLayout = itemView.findViewById(R.id.subjectFlexLayout);
//            final View view = itemView.getLayoutInflater().inflate(R.layout.flex_box_item_layout, null);

        }
    }


}
