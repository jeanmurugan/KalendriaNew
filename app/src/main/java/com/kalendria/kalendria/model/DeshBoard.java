package com.kalendria.kalendria.model;

/**
 * Created by murugan on 01/03/16.
 */
public class DeshBoard {

    private String typeName;
    private String typeId;
    public int order; //added by magesh
    public String getType_image() {
        return type_image;
    }

    public void setType_image(String type_image) {
        this.type_image = type_image;
    }

    private String type_image;


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }



    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


}
