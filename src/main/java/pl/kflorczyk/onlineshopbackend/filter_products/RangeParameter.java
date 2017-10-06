package pl.kflorczyk.onlineshopbackend.filter_products;

public class RangeParameter implements FilterParameter {
    public float min, max;

    public RangeParameter(float min, float max) {
        this.min = min;
        this.max = max;
    }
}
