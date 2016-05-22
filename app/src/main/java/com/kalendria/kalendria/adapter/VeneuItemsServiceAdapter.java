package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.VeneuItemServiceHeader;
import com.kalendria.kalendria.model.VenueItemServiceChild;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;

import java.util.ArrayList;
import java.util.List;

public class VeneuItemsServiceAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<VeneuItemServiceHeader> groups;

    /*add to card start */
    private AlertDialog myDialog;
    private View alertView;
    TextView home_venu_name_txt_add, home_service_name_txt, home_service_price_txt, home_service_duration_txt, addtocart_txt;
    Button cross_image;
    Button home_service_book_btn;
    List<AddToCardVenueModel> addToCardSingletone;
    int positionBtn = 0;
    /*add to card start end */


    public VeneuItemsServiceAdapter(Context context, ArrayList<VeneuItemServiceHeader> expListItems) {
        this.context = context;
        this.groups = expListItems;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<VenueItemServiceChild> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @SuppressWarnings("static-access")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        // alertdialog start
        /*set the tag for book now button */
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflateralert = LayoutInflater.from(context);
        alertView = inflateralert.inflate(R.layout.booknow, null);
        builder.setView(alertView);


        home_venu_name_txt_add = (TextView) alertView.findViewById(R.id.home_venu_name_txt_add);
        home_service_name_txt = (TextView) alertView.findViewById(R.id.home_service_name_txt);
        home_service_price_txt = (TextView) alertView.findViewById(R.id.home_service_price_txt);
        home_service_duration_txt = (TextView) alertView.findViewById(R.id.home_service_duration_txt);
        addtocart_txt = (TextView) alertView.findViewById(R.id.addtocart_txt);
        cross_image = (Button) alertView.findViewById(R.id.cross_image_addto_card);
        home_service_book_btn = (Button) alertView.findViewById(R.id.home_service_book_btn);

        builder.setCancelable(true);
        myDialog = builder.create();


        TextView service_child, service_duration, service_discount, service_price;

        final VenueItemServiceChild child = (VenueItemServiceChild) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.venue_items_service_child, null);
        }

        service_child = (TextView) convertView.findViewById(R.id.service_child);
        service_duration = (TextView) convertView.findViewById(R.id.service_duration);
        service_discount = (TextView) convertView.findViewById(R.id.service_discount);
        service_price = (TextView) convertView.findViewById(R.id.service_price);

        service_child.setText(child.getName());

        if (!child.getDuration().equalsIgnoreCase("null")) {
            service_duration.setText(child.getDuration());
        }
        if (!child.getPrice().equalsIgnoreCase("null") && !child.getPrice().equalsIgnoreCase("0")) {
            service_discount.setText(child.getPrice() + "AED");
            service_discount.setPaintFlags(service_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (!child.getDiscount().equalsIgnoreCase("null") && !child.getDiscount().equalsIgnoreCase("0")) {

            service_price.setText(child.getDiscount() + "AED");
        }


        service_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                home_venu_name_txt_add.setText(child.getVenuename());
                home_service_name_txt.setText(child.getName());
                home_service_price_txt.setText(child.getPrice() + " " + "AED");
                home_service_duration_txt.setText(child.getDuration());
                myDialog.show();

/*vasanth you this for closing popup*/
                cross_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.cancel();
                    }
                });

                home_service_book_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "card addted successfully ", Toast.LENGTH_SHORT).show();
                        myDialog.cancel();

                    }
                });

                addtocart_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, "card addted successfully ", Toast.LENGTH_SHORT).show();
                        System.out.println("potionID card-->" + "" + positionBtn);

                        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();
                        boolean flag = false;
                        boolean serviceDublicateID = false;
                        int location = 0;


                        AddToCardVenueModel addToCardVenueModel = new AddToCardVenueModel();
                        ArrayList<AddToCardServiceModel> items = new ArrayList<AddToCardServiceModel>();
                        ArrayList<AddToCardServiceModel> service_checkDublicate = new ArrayList<AddToCardServiceModel>();

                        for (int i = 0; i < addToCardSingletone.size(); i++) {

                            if (child.getVenueid().equals(addToCardSingletone.get(i).getVenueID())) {
                                flag = true;
                                location = i;

                                service_checkDublicate = addToCardSingletone.get(location).getItems();
                                for (int j = 0; j < service_checkDublicate.size(); j++) {
                                    if (child.getId().equals(service_checkDublicate.get(j).getServiceId())) {
                                        serviceDublicateID = true;
                                    }
                                }
                            }
                        }

                        AddToCardServiceModel addToCardServiceModel = new AddToCardServiceModel();
                        addToCardServiceModel.setServiceId(child.getId());
                        addToCardServiceModel.setServiceName(child.getName());
                        addToCardServiceModel.setServicePrice(child.getPrice());
                        addToCardServiceModel.setServiceDiscount(child.getDiscount());
                        addToCardServiceModel.setServiceDuration(child.getDuration());

                        if (!flag) {
                            addToCardVenueModel.setVenueID(child.getVenueid());
                            addToCardVenueModel.setVenueName(child.getVenuename());
                            addToCardVenueModel.setVenuImage(child.getVenueImage());
                            addToCardVenueModel.setVenuImagethumb(child.getVenueImage());
                            addToCardVenueModel.setCity(child.getVenuecity());
                            addToCardVenueModel.setRegion(child.getVeneregiion());

                            items.add(addToCardServiceModel);
                            addToCardVenueModel.setItems(items);
                            addToCardSingletone.add(addToCardVenueModel);
                        } else {
                            addToCardVenueModel = addToCardSingletone.get(location);
                            items = addToCardVenueModel.getItems();
                            if (!serviceDublicateID) {
                                items.add(addToCardServiceModel);
                                addToCardVenueModel.setItems(items);
                                addToCardSingletone.set(location, addToCardVenueModel);
                            } else {
                                Toast.makeText(context, "This service have been already added in the cart", Toast.LENGTH_SHORT).show();
                                myDialog.cancel();
                            }

                        }


                        if (addToCardSingletone.size() <= 9) {
                            String sizeofcard = " " + addToCardSingletone.size();
                            ((VenueItem) context).dispatchInformations(sizeofcard);
                        } else {
                            ((VenueItem) context).dispatchInformations(String.valueOf(addToCardSingletone.size()));
                        }
                /*System.out.println("v--->"+""+	addToCardSingletone.size());
				for (AddToCardVenueModel addToCardVenueModel1:addToCardSingletone) {
					System.out.println("venueID--->"+""+	addToCardVenueModel1.getVenueID());
					System.out.println("venueID--->"+""+	addToCardVenueModel1.getVenueName());

					for (AddToCardServiceModel addToCardServiceModel1:addToCardVenueModel1.getItems()) {
						System.out.println("servericeName--->"+""+	addToCardServiceModel1.getServiceName());
					}
				}*/

                    }
                });


				/*System.out.println("Venui_id    "+child.getVenueid());
				System.out.println("Venui_name   "+child.getVenuename());
				System.out.println("Venui_image  "+child.getVenueImage());
				System.out.println("Venui_city    "+child.getVenuecity());
				System.out.println("Venui_region    "+child.getVeneregiion());

				System.out.println("service_name    "+child.getName());
				System.out.println("service_id    "+child.getId());
				System.out.println("service_price    "+child.getPrice());
				System.out.println("service_duration   "+child.getDuration());
				System.out.println("service_discount "+child.getDiscount());
*/
            }
        });

        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<VenueItemServiceChild> chList = groups.get(groupPosition).getItems();

        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }


    @SuppressWarnings("static-access")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        VeneuItemServiceHeader group = (VeneuItemServiceHeader) getGroup(groupPosition);


        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.venue_items_service_header, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.venu_service_header_txt);
        textView.setText(group.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

