package pl.dev4lazy.shit_exchange.model.listing;

import java.io.Serializable;

public class ListingItem implements Serializable {
    private Integer sapId; // SAP Id;
    private Integer storeId;
    private String storeName;
    private Double stockQuantity; //    Zapas ilość na dzień;
    private Double stockValue; //    Zapas wartość na dzień;
    private Double sales12MValue; //    Sprzedaż netto 12 m;
    private Double sales12MQuantity;
    private Double margin12M; //    Wskaźnik marży 12 m;
    private Double cogs12M; //
    private Double rotation;
    private String planoKind; // Typ plano;
    private String typeOfStokingUp; //    Typ zatowarow.
    private String ChoiceOfStokingUp; // Twój wybór zatowarowania

    public Integer getSapId() {
        return sapId;
    }

    public void setSapId(Integer sapId) {
        this.sapId = sapId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Double getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Double stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Double getStockValue() {
        return stockValue;
    }

    public void setStockValue(Double stockValue) {
        this.stockValue = stockValue;
    }

    public Double getSales12MValue() {
        return sales12MValue;
    }

    public void setSales12MValue(Double sales12MValue) {
        this.sales12MValue = sales12MValue;
    }

    public Double getSales12MQuantity() {
        return sales12MQuantity;
    }

    public void setSales12MQuantity(Double sales12MQuantity) {
        this.sales12MQuantity = sales12MQuantity;
    }

    public Double getMargin12M() {
        return margin12M;
    }

    public void setMargin12M(Double margin12M) {
        this.margin12M = margin12M;
    }

    public Double getCogs12M() {
        return cogs12M;
    }

    public void setCogs12M(Double cogs12M) {
        this.cogs12M = cogs12M;
    }

    public Double getRotation() {
        return rotation;
    }

    public void setRotation(Double rotation) {
        this.rotation = rotation;
    }

    public String getPlanoKind() {
        return planoKind;
    }

    public void setPlanoKind(String planoKind) {
        this.planoKind = planoKind;
    }

    public String getTypeOfStokingUp() {
        return typeOfStokingUp;
    }

    public void setTypeOfStokingUp(String typeOfStokingUp) {
        this.typeOfStokingUp = typeOfStokingUp;
    }

    public String getChoiceOfStokingUp() {
        return ChoiceOfStokingUp;
    }

    public void setChoiceOfStokingUp(String choiceOfStokingUp) {
        ChoiceOfStokingUp = choiceOfStokingUp;
    }

    public static class ListingItemBuilder {
        private Integer sapId;
        private Integer storeId;
        private String storeName;
        private Double stockQuantity;
        private Double stockValue;
        private Double sales12MValue;
        private Double sales12MQuantity;
        private Double margin12M;
        private Double cogs12M;
        private Double rotation;
        private String planoKind;
        private String typeOfStokingUp;
        private String choiceOfStokingUp; // Twój wybór zatowarowania

        public ListingItemBuilder withSapId(Integer sapId) {
            this.sapId = sapId;
            return this;
        }

        public ListingItemBuilder withStoreId(Integer storeId) {
            this.storeId = storeId;
            return this;

        }

        public ListingItemBuilder withStoreName(String storeName) {
            this.storeName = storeName;
            return this;

        }

        public ListingItemBuilder withStockQuantity(Double stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public ListingItemBuilder withStockValue(Double stockValue) {
            this.stockValue = stockValue;
            return this;
        }

        public ListingItemBuilder withSales12MValue(Double sales12MValue) {
            this.sales12MValue = sales12MValue;
            return this;
        }

        public ListingItemBuilder withSales12MQuantity(Double sales12MQuantity) {
            this.sales12MQuantity = sales12MQuantity;
            return this;
        }

        public ListingItemBuilder withMargin12M(Double margin12M) {
            this.margin12M = margin12M;
            return this;
        }

        public ListingItemBuilder withCogs12M(Double cogs12M) {
            this.cogs12M = cogs12M;
            return this;
        }

        public ListingItemBuilder withRotation(Double rotation) {
            this.rotation = rotation;
            return this;
        }

        public ListingItemBuilder withPlanoKind(String planoKind) {
            this.planoKind = planoKind;
            return this;
        }

        public ListingItemBuilder withTypeOfStokingUp(String typeOfStokingUp) {
            this.typeOfStokingUp = typeOfStokingUp;
            return this;
        }

        public ListingItemBuilder withChoiceOfStokingUp(String choiceOfStokingUp) {
            this.choiceOfStokingUp = choiceOfStokingUp;
            return this;
        }

        public ListingItem build() {
            ListingItem listingItem = new ListingItem();
            listingItem.setSapId(this.sapId);
            listingItem.setStoreId(this.storeId);
            listingItem.setStoreName(this.storeName);
            listingItem.setStockQuantity(this.stockQuantity);
            listingItem.setStockValue(this.stockValue);
            listingItem.setSales12MValue(this.sales12MValue);
            listingItem.setSales12MQuantity(this.sales12MQuantity);
            listingItem.setMargin12M(this.margin12M);
            listingItem.setCogs12M(this.cogs12M);
            listingItem.setRotation(this.rotation);
            listingItem.setPlanoKind(this.planoKind);
            listingItem.setTypeOfStokingUp(this.typeOfStokingUp);
            listingItem.setChoiceOfStokingUp(this.choiceOfStokingUp);
            return listingItem;
        }

        public void clearData() {
            sapId = null;
            storeId = null;
            storeName = null;
            stockQuantity = null;
            stockValue = null;
            sales12MValue = null;
            margin12M = null;
            cogs12M = null;
            rotation = null;
            planoKind = null;
            typeOfStokingUp = null;
            choiceOfStokingUp = null;
        }
    }

}
