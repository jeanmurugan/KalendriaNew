package com.kalendria.kalendria.singleton;

import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.model.VeneuItemServiceHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murugan on 07/03/16.
 */
public class AddToCardSingleTone {

    private static AddToCardSingleTone addToCardSingleTone;

    public List<AddToCardVenueModel> addToCardArrayList;

    private AddToCardSingleTone() {
        addToCardArrayList = new ArrayList<AddToCardVenueModel>();

    }
    public static AddToCardSingleTone getInstance() {
        if (addToCardSingleTone == null) {
            addToCardSingleTone = new AddToCardSingleTone();
        }

        return addToCardSingleTone;
    }
    public List<AddToCardVenueModel> getParamList() {
        return addToCardArrayList;
    }

    public void setParamList(List<AddToCardVenueModel> paramList) {
        this.addToCardArrayList = paramList;
    }

}
