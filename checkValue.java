package Spatial_Data_Structure;

public class checkValue {
    boolean check;
    int value;
    String locationCode;

    checkValue(boolean check, int value){
        this.check = check;
        this.value = value;
    }

    checkValue(boolean check, String locationCode){
        this.check = check;
        this.locationCode = locationCode;
    }

    checkValue(){

    }
}
