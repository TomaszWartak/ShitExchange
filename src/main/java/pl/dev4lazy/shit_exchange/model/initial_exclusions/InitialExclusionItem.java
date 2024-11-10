package pl.dev4lazy.shit_exchange.model.initial_exclusions;

import java.io.Serializable;

public class InitialExclusionItem implements Serializable {
    private Integer sapId; // Numer artykułu
    private Long eanCode; // Numer ean
    private Integer storeId; // konwersja z pierwszych 4 znaków "Sklep"

    public Integer getSapId() {
        return sapId;
    }

    public void setSapId(Integer sapId) {
        this.sapId = sapId;
    }

    public Long getEanCode() {
        return eanCode;
    }

    public void setEanCode(Long eanCode) {
        this.eanCode = eanCode;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public static class InitialExclusionItemBuilder {
        private Integer sapId; // Numer artykułu
        private Long eanCode; // Numer ean
        private Integer storeId; // konwersja z pierwszych 4 znaków "Sklep"

        public InitialExclusionItemBuilder withSapId(Integer sapId ) {
            this.sapId = sapId;
            return this;
        }

        public InitialExclusionItemBuilder withEanCode(Long eanCode ) {
            this.eanCode = eanCode;
            return this;
        }

        public InitialExclusionItemBuilder withStoreId(Integer storeId ) {
            this.storeId = storeId;
            return this;
        }

        public InitialExclusionItem build() {
            InitialExclusionItem reserveItem = new InitialExclusionItem();
            reserveItem.setSapId( sapId );
            reserveItem.setEanCode( eanCode );
            reserveItem.setStoreId( storeId );
            return reserveItem;
        }
    }
}
