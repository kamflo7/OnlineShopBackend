package pl.kflorczyk.onlineshopbackend.product_filters;

import org.springframework.data.domain.Sort;

public class ProductSortTranslator {
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String DELIMITER = "-";
    private static final String PRICE = "prc";

    private String sortRaw;

    public ProductSortTranslator(String sortRaw) {
        this.sortRaw = sortRaw;
    }

    public Sort translate() {
        if(sortRaw == null)
            return null;

        String[] parts = sortRaw.split(DELIMITER);
        if(parts.length == 2) {
            if(parts[0].equals(PRICE)) {
                Sort.Direction direction = parts[1].equals(ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
                Sort sort = new Sort(direction, "price");
                return sort;
            }
        }
        return null;
    }
}
