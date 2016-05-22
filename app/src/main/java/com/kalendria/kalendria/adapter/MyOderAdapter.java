package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.FavorateModel;
import com.kalendria.kalendria.model.MyorderModel;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

@SuppressLint("InflateParams")
public class MyOderAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<MyorderModel> cafeList;


	public MyOderAdapter(Context context, List<MyorderModel> cafeList) {
		this.context = context;
		this.cafeList = cafeList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return cafeList.size();
	}

	@Override
	public Object getItem(int location) {
		return cafeList.get(location);
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
		
			convertView=inflater.inflate(R.layout.myorder_row, parent, false);



			holder.myOrder_VenueName = (TextView)convertView.findViewById(R.id.myorder_venu_name);
			holder.myOrder_ServiceName = (TextView)convertView.findViewById(R.id.myorder_service_name);
			holder.myOrder_Date = (TextView)convertView.findViewById(R.id.myorder_date);
			holder.myOrder_Time = (TextView)convertView.findViewById(R.id.myorder_time);
			holder.myOrder_Price_Point = (TextView)convertView.findViewById(R.id.myorder_price_or_points);
			holder.myOrder_Status = (TextView)convertView.findViewById(R.id.myorder_status);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

			holder.myOrder_VenueName.setText(cafeList.get(position).getMorderVenuName());
			holder.myOrder_ServiceName.setText(cafeList.get(position).getMorderServiceName());
			//holder.myOrder_Date.setText(cafeList.get(position).getMorderDate());
			holder.myOrder_Time.setText(cafeList.get(position).getMorderTime());
		if(!cafeList.get(position).getMorderPoints().equals("")){
			holder.myOrder_Price_Point.setText(cafeList.get(position).getMorderPoints());
		}
		if(!cafeList.get(position).getMorderPrice().equals("")){
			holder.myOrder_Price_Point.setText(cafeList.get(position).getMorderPrice());
		}
			//holder.myOrder_Status.setText(cafeList.get(position).getMorderStatus());

		return convertView;
	}
	
	static class Viewholder{


		TextView myOrder_Date, myOrder_Time, myOrder_VenueName, myOrder_ServiceName, myOrder_Price_Point, myOrder_Status;
		
	}


}