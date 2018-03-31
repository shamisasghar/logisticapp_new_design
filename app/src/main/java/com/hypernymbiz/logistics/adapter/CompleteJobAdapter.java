package com.hypernymbiz.logistics.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.fragments.CompletedJobDetail;
import com.hypernymbiz.logistics.fragments.JobDetailsFragment;
import com.hypernymbiz.logistics.model.JobInfo_;
import com.hypernymbiz.logistics.model.Respone_Completed_job;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;

import java.util.List;

/**
 * Created by Metis on 28-Mar-18.
 */

public class CompleteJobAdapter extends RecyclerView.Adapter<CompleteJobAdapter.MyViewHolder> {
    private List<JobInfo_> jobInfo_s;
    Respone_Completed_job respone_completed_job;

    Context context;


    public CompleteJobAdapter(List<JobInfo_> jobInfo_s, Context context)
    {

        this.jobInfo_s=jobInfo_s;
        this.context = context;
    }

    @Override
    public CompleteJobAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete_job, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompleteJobAdapter.MyViewHolder holder,final int position) {
//        holder.jobid.setText(String.valueOf(jobInfo_s.get(position).getJobId()));
        holder.jobname.setText(jobInfo_s.get(position).getJob_name());
        holder.jobstatus.setText(jobInfo_s.get(position).getJob_status());
        holder.starttime.setText(AppUtils.getFormattedDate(jobInfo_s.get(position).getJob_start_time()) + " " + AppUtils.getTimedate(jobInfo_s.get(position).getJob_start_time()));
        holder.endtime.setText(AppUtils.getFormattedDate(jobInfo_s.get(position).getJob_end_time()) + " " + AppUtils.getTimedate(jobInfo_s.get(position).getJob_end_time()));

//        holder.endtime.setText(AppUtils.getTime(jobInfo_s.get(position).getJob_end_time()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = jobInfo_s.get(position).getJobId();

                Intent intent = new Intent(context, FrameActivity.class);
//                intent.putExtra("jobid",  Integer.toString(id));
                intent.putExtra(Constants.FRAGMENT_NAME, CompletedJobDetail.class.getName());
//                    intent.putExtra(Constants.DATA, bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobInfo_s.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jobid,jobname, jobstatus, starttime, endtime,compled_job;

        public MyViewHolder(View itemView) {
            super(itemView);

            jobname = (TextView) itemView.findViewById(R.id.txt_job_name);
//            jobid = (TextView) itemView.findViewById(R.id.txt_job_id);
            jobstatus = (TextView) itemView.findViewById(R.id.txt_job_status);
            starttime = (TextView) itemView.findViewById(R.id.txt_job_starttime);
            endtime = (TextView) itemView.findViewById(R.id.txt_job_endtime);
//            compled_job = (TextView) itemView.findViewById(R.id.txt_no_completed_job);

        }
    }
}