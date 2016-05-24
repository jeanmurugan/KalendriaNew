package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.AppointmentConfirmationModel;

import java.util.List;

@SuppressLint("InflateParams")
public class AppointmentConfirmationAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<AppointmentConfirmationModel> appointmentConfirmationList;


	public AppointmentConfirmationAdapter(Context context, List<AppointmentConfirmationModel> appointmentConfirmationList) {
		this.context = context;
		this.appointmentConfirmationList = appointmentConfirmationList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return appointmentConfirmationList.size();
	}

	@Override
	public Object getItem(int location) {
		return appointmentConfirmationList.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		Viewholder holder;
		if(convertView==null){
			holder=new Viewholder();
		
			convertView=inflater.inflate(R.layout.appointment_confirmation_row, parent, false);

			holder.appointment_service_name_tv = (TextView)convertView.findViewById(R.id.appointment_service_name_tv);
			holder.appointment_service_time_tv = (TextView)convertView.findViewById(R.id.appointment_service_time_tv);
			holder.appointment_duration_tv = (TextView)convertView.findViewById(R.id.appointment_duration_tv);
			holder.appointment_service_staff_name_tv = (TextView)convertView.findViewById(R.id.appointment_service_staff_name_tv);
			holder.appointment_service_price_tv= (TextView)convertView.findViewById(R.id.appointment_service_price_tv);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

			holder.appointment_service_name_tv.setText(appointmentConfirmationList.get(position).getServiceName());
			holder.appointment_service_time_tv.setText(appointmentConfirmationList.get(position).getStartTimeEndTime());
			holder.appointment_duration_tv.setText(appointmentConfirmationList.get(position).getDuritaion());
			holder.appointment_service_staff_name_tv.setText(appointmentConfirmationList.get(position).getStaffName());
			holder.appointment_service_price_tv.setText(appointmentConfirmationList.get(position).getServicePrice());

		return convertView;
	}
	
	static class Viewholder{


		TextView appointment_service_name_tv, appointment_service_time_tv, appointment_duration_tv, appointment_service_staff_name_tv, appointment_service_price_tv;
		
	}


}