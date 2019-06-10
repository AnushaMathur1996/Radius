package example.com.assignment.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class FacilitiesDto implements Serializable {

    private String facilityName;
    private int facilityId;

    private int optionId;
    private String optionName;
    private String iconName;

    private int selectedPosition = -1;
    private String selectedOption;

    private ArrayList<FacilitiesDto> optionsList;

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public ArrayList<FacilitiesDto> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(ArrayList<FacilitiesDto> optionsList) {
        this.optionsList = optionsList;
    }

    @Override
    public String toString() {
        return optionName;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
