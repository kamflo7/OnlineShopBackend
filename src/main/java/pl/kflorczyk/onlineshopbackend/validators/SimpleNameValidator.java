package pl.kflorczyk.onlineshopbackend.validators;

public class SimpleNameValidator {

    private Integer minLength, maxLength;

    public SimpleNameValidator(int minLength) {
        this.minLength = new Integer(minLength);
    }

    public SimpleNameValidator(int minLengthInclusive, int maxLengthInclusive) {
        this(minLengthInclusive);
        this.maxLength = maxLengthInclusive;
    }

    public boolean validate(String name) {
        if(name.length() < minLength)
            return false;

        if(maxLength != null && name.length() > maxLength)
            return false;

        return true;
    }

//    public static class Builder {
//        private Integer minLen, maxLen;
//
//        public Builder setMinLength(int minLen) {
//            this.minLen = new Integer(minLen);
//            return this;
//        }
//
//        public Builder setMaxLength(int maxLen) {
//            this.maxLen = new Integer(maxLen);
//            return this;
//        }
//
//        public SimpleNameValidator build
//    }
}
