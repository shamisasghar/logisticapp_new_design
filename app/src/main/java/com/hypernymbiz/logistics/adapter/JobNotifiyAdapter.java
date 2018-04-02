package com.hypernymbiz.logistics.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.fragments.JobDetailsFragment;
import com.hypernymbiz.logistics.fragments.MaintenanceDetailFragment;
import com.hypernymbiz.logistics.model.IsViewed;
import com.hypernymbiz.logistics.model.JobInfo;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;

import java.util.List;

/**
 * Created by Metis on 23-Mar-18.
 */

public class JobNotifiyAdapter extends RecyclerView.Adapter<JobNotifiyAdapter.ViewHolder> {

    private List<JobInfo> jobInfo_s;
    private List<IsViewed> isVieweds;
    public Context context;

    public JobNotifiyAdapter(List<JobInfo> data, Context context) {
        this.jobInfo_s = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_notification, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(final JobNotifiyAdapter.ViewHolder holder, final int position) {




        holder.job.setText(jobInfo_s.get(position).getJobName());
        holder.notification_name.setText(Integer.toString(jobInfo_s.get(position).getNotificationId()));
        holder.create_time.setText(AppUtils.getTime(jobInfo_s.get(position).getCreatedTime()));
        holder.create_date.setText(jobInfo_s.get(position).getCreatedDatetime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int pos = holder.getAdapterPosition();
            Integer id = jobInfo_s.get(position).getJobId();


            @Override
            public void onClick(View v) {


                Toast.makeText(context, ""+id.toString(), Toast.LENGTH_SHORT).show();
                if(jobInfo_s.get(position).getJobType()==9)
                {

                    Intent intent = new Intent(context, FrameActivity.class);
                    intent.putExtra("jobid", Integer.toString(id));
                    intent.putExtra(Constants.FRAGMENT_NAME, MaintenanceDetailFragment.class.getName());
                    context.startActivity(intent);

                }
                else {
                    Intent intent = new Intent(context, FrameActivity.class);
                    intent.putExtra("jobid", Integer.toString(id));
                    intent.putExtra(Constants.FRAGMENT_NAME, JobDetailsFragment.class.getName());
//                    intent.putExtra(Constants.DATA, bundle);
                    context.startActivity(intent);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return jobInfo_s.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView job,notification_name,create_date,create_time;
        ImageView imageView;
        CardView cardView;
        int pos=getAdapterPosition();

        private ViewHolder(final View itemView) {
            super(itemView);

            job = (TextView) itemView.findViewById(R.id.jobname);
            notification_name = (TextView) itemView.findViewById(R.id.txt_notification_id);
            create_time = (TextView) itemView.findViewById(R.id.txt_job_time);
            create_date = (TextView) itemView.findViewById(R.id.txt_job_date);
//            imageView = (ImageView) itemView.findViewById(R.id.image);
            cardView = (CardView) itemView.findViewById(R.id.layout_cardview);


        }

    }
}