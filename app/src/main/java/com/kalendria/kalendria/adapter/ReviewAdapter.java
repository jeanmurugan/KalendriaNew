package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.ReviewModel;
import com.kalendria.kalendria.utility.CircularNetworkImageView;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.squareup.picasso.Picasso;

import java.util.List;

@SuppressLint("InflateParams")
public class ReviewAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<ReviewModel> cafeList;
	ImageLoader imageLoader = KalendriaAppController.getInstance().getImageLoader();
	Activity activity = (Activity) context;


	public ReviewAdapter(Context context, List<ReviewModel> cafeList) {
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
		
			convertView=inflater.inflate(R.layout.review_row, parent, false);
			holder.review_username = (TextView) convertView.findViewById(R.id.review_username);
			holder.review_commants = (TextView) convertView.findViewById(R.id.review_commants);
			holder.review_responce_command = (TextView) convertView.findViewById(R.id.review_responce_command);
			holder.review_responce_username = (TextView) convertView.findViewById(R.id.review_responce_username);
			holder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

			holder.review_username.setText(cafeList.get(position).getReviewUserName());
			holder.review_commants.setText(cafeList.get(position).getReviewCommants());
			holder.review_responce_command.setText(cafeList.get(position).getReviewResponceUserName());
			holder.review_responce_username.setText(cafeList.get(position).getReviewResponceUserCommands());

		//holder.rating.setText(cafeList.get(position).getRating()+"/5");
		float numstars = Float.parseFloat(cafeList.get(position).getReviewRatting());
		holder.ratingBar.setRating(numstars);

		LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

		if(holder.ratingBar.getRating() > 0)
		{
			stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

		}
	
		if (imageLoader == null)
			imageLoader = KalendriaAppController.getInstance().getImageLoader();
		//final CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView.findViewById(R.id.thumbnail);
		//final NetworkImageView imgAvatar = (NetworkImageView)convertView.findViewById(R.id.thumbnail);
		//final CircularNetworkImageView review_responce_image = (CircularNetworkImageView) convertView.findViewById(R.id.review_responce_image);

		final ReviewModel cafeitems = cafeList.get(position);
		if( cafeitems.getReviewUserTampImage_url() != null && !"".equals(cafeitems.getReviewUserTampImage_url()) ){
			try {
				Picasso.with(context)
                        .load(cafeitems.getReviewUserTampImage_url())
                        // .memoryPolicy(MemoryPolicy.NO_CACHE )
                        // .networkPolicy(NetworkPolicy.NO_CACHE)
                        //.resize(720, 350)
                        // .error(R.drawable.login)
                        .placeholder(R.drawable.faverote_icon)
                        .noFade()
                        // .fit().centerCrop()
                        .into(holder.imageView );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		/*if(cafeitems.getReviewUserTampImage_url()!=null) {
			imageLoader.get(cafeitems.getReviewUserTampImage_url(), new ImageLoader.ImageListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e("TAG_image", "Image Load Error: " + error.getMessage());
				}

				@Override
				public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
					if (response.getBitmap() != null) {

						// load image into imageview

						imgAvatar.setImageBitmap(response.getBitmap());
					}
				}
			});
		}

		if(cafeitems.getReviewResponceUserTampImage_url()!=null) {
			imageLoader.get(cafeitems.getReviewUserTampImage_url(), new ImageLoader.ImageListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e("TAG_image", "Image Load Error: " + error.getMessage());
				}

				@Override
				public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
					if (response.getBitmap() != null) {

						// load image into imageview

						review_responce_image.setImageBitmap(response.getBitmap());
					}
				}
			});
		}*/




		return convertView;
	}
	
	static class Viewholder{
		TextView review_username ,review_commants,review_responce_command,review_responce_username;
		ImageView imageView;
		RatingBar ratingBar;

		
	}


}