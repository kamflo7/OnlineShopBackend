package pl.kflorczyk.onlineshopbackend.dto;

import java.util.List;

public class FeatureDefinitionDTO {
    private boolean multipleValues, filterable, visible;
    private String name;

    private List<String> newValues;

    public FeatureDefinitionDTO() {
    }

    public FeatureDefinitionDTO(boolean multipleValues, boolean filterable, boolean visible, String name, List<String> newValues) {
        this.multipleValues = multipleValues;
        this.filterable = filterable;
        this.visible = visible;
        this.name = name;
        this.newValues = newValues;
    }

    public FeatureDefinitionDTO(boolean multipleValues, boolean filterable, boolean visible, String name) {
        this.multipleValues = multipleValues;
        this.filterable = filterable;
        this.visible = visible;
        this.name = name;
    }

    public boolean isMultipleValues() {
        return multipleValues;
    }

    public void setMultipleValues(boolean multipleValues) {
        this.multipleValues = multipleValues;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNewValues() {
        return newValues;
    }

    public void setNewValues(List<String> newValues) {
        this.newValues = newValues;
    }
}
