package example.com.assignment.dto;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmObject;

public class ExclusionDto implements Serializable {
    private int optionId;
    private int filterId;
    private ArrayList<ExclusionDto> childExclusionList;

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public ArrayList<ExclusionDto> getChildExclusionList() {
        return childExclusionList;
    }

    public void setChildExclusionList(ArrayList<ExclusionDto> childExclusionList) {
        this.childExclusionList = childExclusionList;
    }
}
