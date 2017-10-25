package pl.kflorczyk.onlineshopbackend.dto;

import java.util.List;
import java.util.Map;

public class FeatureDefinitionDTOEditable {
    private boolean multipleValues, filterable, visible;
    private String name;

    private Map<Long, String> values;
    private List<String> newValues;

    private boolean forceUpdate;

    public FeatureDefinitionDTOEditable() {
    }

    public FeatureDefinitionDTOEditable(boolean multipleValues, boolean filterable, boolean visible, String name, boolean forceUpdate, Map<Long, String> values) {
        this.multipleValues = multipleValues;
        this.filterable = filterable;
        this.visible = visible;
        this.name = name;
        this.forceUpdate = forceUpdate;
        this.values = values;
    }

    public FeatureDefinitionDTOEditable(boolean multipleValues, boolean filterable, boolean visible, String name, boolean forceUpdate) {
        this.multipleValues = multipleValues;
        this.filterable = filterable;
        this.visible = visible;
        this.name = name;
        this.forceUpdate = forceUpdate;
    }

    public List<String> getNewValues() {
        return newValues;
    }

    public void setNewValues(List<String> newValues) {
        this.newValues = newValues;
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

    public Map<Long, String> getValues() {
        return values;
    }

    public void setValues(Map<Long, String> values) {
        this.values = values;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}
