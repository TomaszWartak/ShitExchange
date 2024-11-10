package pl.dev4lazy.shit_exchange.model.reserve;

import java.io.Serializable;

public class ReserveItem implements Serializable {
    private Integer sapId; // Numer artykułu
    private Long eanCode; // Numer ean
    private Integer storeId; // konwersja z pierwszych 4 znaków "Sklep"
    private String storeName; // Sklep
    private String articleName;
    private Double value; // Rezerwa ostatni tydzień [wartość]
    private Double quantityToZeroReserve;
    private Double stockQuantity;
    private Double stockValue;
    private String sector; // Kategoria wiodąca
    private Double cogs12M;
    private Double rotation;
    private boolean excludedFromDonation;

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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getQuantityToZeroReserve() {
        return quantityToZeroReserve;
    }

    public void setQuantityToZeroReserve(Double quantityToZeroReserve) {
        this.quantityToZeroReserve = quantityToZeroReserve;
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

/*    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }*/

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public void setExclusionFromDonationState(boolean excludedFromDonation) {
        this.excludedFromDonation = excludedFromDonation;
    }

    public void setExcludedFromDonation() {
        setExclusionFromDonationState( true );
    }

    public void setIncludedForDonation() {
        setExclusionFromDonationState( false );
    }

    public boolean isExcludedFromDonation() {
        return excludedFromDonation;
    }

    public boolean isNotExcludedFromDonation() {
        return !isExcludedFromDonation();
    }

    public static class ReserveItemBuilder {
        private Integer sapId; // Numer artykułu
        private Long eanCode; // Numer ean
        private Integer storeId; // konwersja z pierwszych 4 znaków "Sklep"
        private String storeName; // Sklep
        private String articleName;
        private Double value; // Rezerwa ostatni tydzień [wartość]
        private Double quantityToZeroReserve;
        private Double stockQuantity;
        private Double stockValue;
        private String sector; // Kategoria wiodąca
        private Double cogs12M;
        private Double rotation;
        private boolean excludedFromDonation;

        public ReserveItemBuilder withSapId( Integer sapId ) {
            this.sapId = sapId;
            return this;
        }

        public ReserveItemBuilder withEanCode( Long eanCode ) {
            this.eanCode = eanCode;
            return this;
        }

        public ReserveItemBuilder withStoreId( Integer storeId ) {
            this.storeId = storeId;
            return this;
        }

        public ReserveItemBuilder withStoreName( String storeName ) {
            this.storeName = storeName;
            return this;
        }

        public ReserveItemBuilder withArticleName( String articleName ) {
            this.articleName = articleName;
            return this;
        }

        public ReserveItemBuilder withValue( Double value ) {
            this.value = value;
            return this;
        }

        public ReserveItemBuilder withQuantityToZeroReserve( Double quantityToZeroReserve ) {
            this.quantityToZeroReserve = quantityToZeroReserve;
            return this;
        }

        public ReserveItemBuilder withStockQuantity( Double stockQuantity ) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public ReserveItemBuilder withStockValue( Double stockValue ) {
            this.stockValue = stockValue;
            return this;
        }

        public ReserveItemBuilder withSector( String sector ) {
            this.sector = sector;
            return this;
        }

        public ReserveItemBuilder withCogs12M( Double cogs12M ) {
            this.cogs12M = cogs12M;
            return this;
        }

        public ReserveItemBuilder withRotation( Double rotation ) {
            this.rotation = rotation;
            return this;
        }

        public ReserveItemBuilder withExclusionFromDonation(boolean excludedFromDonation ) {
            this.excludedFromDonation = excludedFromDonation;
            return this;
        }

        public ReserveItem build() {
            ReserveItem reserveItem = new ReserveItem();
            reserveItem.setSapId( sapId );
            reserveItem.setEanCode( eanCode );
            reserveItem.setStoreId( storeId );
            reserveItem.setStoreName( storeName );
            reserveItem.setArticleName( articleName );
            reserveItem.setValue( value );
            reserveItem.setQuantityToZeroReserve( quantityToZeroReserve );
            reserveItem.setStockQuantity( stockQuantity );
            reserveItem.setStockValue( stockValue );
            reserveItem.setSector( sector );
            reserveItem.setCogs12M( cogs12M );
            reserveItem.setRotation( rotation );
            reserveItem.setExclusionFromDonationState(excludedFromDonation);
            return reserveItem;
        }
    }
}
/*
Sklep
Region
EAN
Numer artykułu
Artykuł podłączony
Nazwa
Rezerwa ostatni tydzień [wartość]
Rezerwa ostatni tydzień %	I
lość w rez. do oddania
Rezerwa poprzedni tydzień
Zmiana rezerwy tydzień do tygodnia
Zmiana rezerwy tydzień do tygodnia %
Marża %
COGS 12M
COGS ost. tydz.
Zapas
Zapas ilość
Status
SKU nowe
SKU out of range
Data nadania statusu out of range
Data usunięcia z planogramu
Przedział wiekowania
SKU w planogramie
Data zmiany statusu
Tytuł wyłączenia
Rezerwa przed wyłączeniami
Estymowana rez. +6M
6M do wycof. z nowości
Art. Out of range - 90 dni bez rezerwy
Data pierwszego zatowarowania
Unhealthy Stock Delisted Val
Unhealthy Stock Ranged Val
MOQ
Powód wzrostu rezerwy
Cena sprzedaży brutto
PQ
Sztuki potrzebne do zejścia z całej rezerwy
Zapas ekspozycja ilość
Kategoria wiodąca
Kategoria
Podkategoria
Podpodkategoria
Brick
Brick nazwa
Dostawca
Kod dostawcy
Footprint
RTM
 */