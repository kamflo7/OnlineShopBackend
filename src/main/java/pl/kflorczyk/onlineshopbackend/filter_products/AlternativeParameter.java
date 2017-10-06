package pl.kflorczyk.onlineshopbackend.filter_products;

public class AlternativeParameter implements FilterParameter {
    public String[] values;

    public AlternativeParameter(String[] values) {
        this.values = values;
    }

    public boolean contains(String str) {
        for(String s : values) {
            if(s.equals(str))
                return true;
        }

        return false;
    }
}
