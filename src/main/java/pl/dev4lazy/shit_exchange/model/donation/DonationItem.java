package pl.dev4lazy.shit_exchange.model.donation;

public class DonationItem {
    private Integer donorStoreId;
    private String donorStoreName;
    private Integer sapId;
    private Long eanCode;
    private String articleName;
    private Double donorReserveValue; // Rezerwa ostatni tydzień [wartość]
    private Double donorQuantityToZeroReserve;
    private Double donorStockQuantity;
    private Double donorStockValue;
    private String sector; // Kategoria wiodąca
    private Double donorCogs12M;
    private Integer takerStoreId;
    private String takerStoreName;
    private String takerPlanoKind; // Typ plano;
    private String takerTypeOfStokingUp; //    Typ zatowarow.
    private String takerChoiceOfStokingUp; // Twój wybór zatowarowania
    private Double takerStockQuantity;
    private Double takerStockValue;
    private Double takerSales12MValue; //    Sprzedaż netto 12 m;
    private Double takerSales12Quantity;
    private Double takerRotation;

    public Integer getDonorStoreId() {
        return donorStoreId;
    }

    public void setDonorStoreId(Integer donorStoreId) {
        this.donorStoreId = donorStoreId;
    }

    public String getDonorStoreName() {
        return donorStoreName;
    }

    public void setDonorStoreName(String donorStoreName) {
        this.donorStoreName = donorStoreName;
    }

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

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Double getDonorReserveValue() {
        return donorReserveValue;
    }

    public void setDonorReserveValue(Double donorReserveValue) {
        this.donorReserveValue = donorReserveValue;
    }

    public Double getDonorQuantityToZeroReserve() {
        return donorQuantityToZeroReserve;
    }

    public void setDonorQuantityToZeroReserve(Double donorQuantityToZeroReserve) {
        this.donorQuantityToZeroReserve = donorQuantityToZeroReserve;
    }

    public Double getDonorStockQuantity() {
        return donorStockQuantity;
    }

    public void setDonorStockQuantity(Double donorStockQuantity) {
        this.donorStockQuantity = donorStockQuantity;
    }

    public Double getDonorStockValue() {
        return donorStockValue;
    }

    public void setDonorStockValue(Double donorStockValue) {
        this.donorStockValue = donorStockValue;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Double getDonorCogs12M() {
        return donorCogs12M;
    }

    public void setDonorCogs12M(Double donorCogs12M) {
        this.donorCogs12M = donorCogs12M;
    }

    public Integer getTakerStoreId() {
        return takerStoreId;
    }

    public void setTakerStoreId(Integer takerStoreId) {
        this.takerStoreId = takerStoreId;
    }

    public String getTakerStoreName() {
        return takerStoreName;
    }

    public void setTakerStoreName(String takerStoreName) {
        this.takerStoreName = takerStoreName;
    }

    public String getTakerPlanoKind() {
        return takerPlanoKind;
    }

    public void setTakerPlanoKind(String takerPlanoKind) {
        this.takerPlanoKind = takerPlanoKind;
    }

    public String getTakerTypeOfStokingUp() {
        return takerTypeOfStokingUp;
    }

    public void setTakerTypeOfStokingUp(String takerTypeOfStokingUp) {
        this.takerTypeOfStokingUp = takerTypeOfStokingUp;
    }

    public String getTakerChoiceOfStokingUp() {
        return takerChoiceOfStokingUp;
    }

    public void setTakerChoiceOfStokingUp(String takerChoiceOfStokingUp) {
        this.takerChoiceOfStokingUp = takerChoiceOfStokingUp;
    }

    public Double getTakerStockQuantity() {
        return takerStockQuantity;
    }

    public void setTakerStockQuantity(Double takerStockQuantity) {
        this.takerStockQuantity = takerStockQuantity;
    }

    public Double getTakerStockValue() {
        return takerStockValue;
    }

    public void setTakerStockValue(Double takerStockValue) {
        this.takerStockValue = takerStockValue;
    }

    public Double getTakerSales12MValue() {
        return takerSales12MValue;
    }

    public void setTakerSales12MValue(Double takerSales12MValue) {
        this.takerSales12MValue = takerSales12MValue;
    }

    public Double getTakerSales12MQuantity() {
        return takerSales12Quantity;
    }

    public void setTakerSales12MQuantity(Double takerSales12MQuantity) {
        this.takerSales12Quantity = takerSales12MQuantity;
    }

    public Double getTakerRotation() {
        return takerRotation;
    }

    public void setTakerRotation(Double takerRotation) {
        this.takerRotation = takerRotation;
    }

    public static class DonationItemBuilder {
        private Integer donorStoreId;
        private String donorStoreName;
        private Integer sapId;
        private Long eanCode;
        private String articleName;
        private Double donorReserveValue; // Rezerwa ostatni tydzień [wartość]
        private Double donorQuantityToZeroReserve;
        private Double donorStockQuantity;
        private Double donorStockValue;
        private String sector; // Kategoria wiodąca
        private Double donorCogs12M;
        private Integer takerStoreId;
        private String takerStoreName;
        private String takerPlanoKind; // Typ plano;
        private String takerTypeOfStokingUp; //    Typ zatowarow.
        private String takerChoiceOfStokingUp; // Twój wybór zatowarowania
        private Double takerStockQuantity;
        private Double takerStockValue;
        private Double takerSales12MValue; //    Sprzedaż netto 12 m;
        private Double takerSales12MQuantity;
        private Double takerRotation;

        public DonationItemBuilder withDonorStoreId(Integer donorStoreId) {
            this.donorStoreId = donorStoreId;
            return this;
        }

        public DonationItemBuilder withDonorStoreName( String donorStoreName ) {
            this.donorStoreName = donorStoreName;
            return this;
        }

        public DonationItemBuilder withSapId( Integer sapId ) {
            this.sapId = sapId;
            return this;
        }

        public DonationItemBuilder withEanCode( Long eanCode ) {
            this.eanCode = eanCode;
            return this;
        }

        public DonationItemBuilder withArticleName( String articleName ) {
            this.articleName = articleName;
            return this;
        }

        public DonationItemBuilder withDonorReserveValue( Double donorReserveValue ) {
            this.donorReserveValue = donorReserveValue;
            return this;
        }

        public DonationItemBuilder withDonorQuantityToZeroReserve( Double donorQuantityToZeroReserve ) {
            this.donorQuantityToZeroReserve = donorQuantityToZeroReserve;
            return this;
        }

        public DonationItemBuilder withDonorStockQuantity( Double donorStockQuantity ) {
            this.donorStockQuantity = donorStockQuantity;
            return this;
        }

        public DonationItemBuilder withDonorStockValue(Double donorStockValue ) {
            this.donorStockValue = donorStockValue;
            return this;
        }

        public DonationItemBuilder withSector( String sector ) {
            this.sector = sector;
            return this;
        }

        public DonationItemBuilder withDonorCogs12M( Double donorCogs12M ) {
            this.donorCogs12M = donorCogs12M;
            return this;
        }

        public DonationItemBuilder withTakerStoreId( Integer takerStoreId ) {
            this.takerStoreId = takerStoreId;
            return this;
        }

        public DonationItemBuilder withTakerStoreName( String takerStoreName) {
            this.takerStoreName = takerStoreName;
            return this;
        }

        public DonationItemBuilder withTakerPlanoKind( String takerPlanoKind ) {
            this.takerPlanoKind = takerPlanoKind;
            return this;
        }

        public DonationItemBuilder withTakerTypeOfStokingUp( String takerTypeOfStokingUp ) {
            this.takerTypeOfStokingUp = takerTypeOfStokingUp;
            return this;
        }

        public DonationItemBuilder withTakerChoiceOfStokingUp( String takerChoiceOfStokingUp ) {
            this.takerChoiceOfStokingUp = takerChoiceOfStokingUp;
            return this;
        }

        public DonationItemBuilder withTakerStockQuantity( Double takerStockQuantity ) {
            this.takerStockQuantity = takerStockQuantity;
            return this;
        }

        public DonationItemBuilder withTakerStockValue(Double takerStockValue ) {
            this.takerStockValue = takerStockValue;
            return this;
        }

        public DonationItemBuilder withTakerSales12MValue( Double takerSales12MValue ) {
            this.takerSales12MValue = takerSales12MValue;
            return this;
        }

        public DonationItemBuilder withTakerSales12MQuantity( Double takerSales12MQuantity ) {
            this.takerSales12MQuantity = takerSales12MQuantity;
            return this;
        }


        public DonationItemBuilder withTakerRotation( Double takerRotation ) {
            this.takerRotation = takerRotation;
            return this;
        }

        public DonationItem build() {
            DonationItem donationItem = new DonationItem();
            donationItem.setDonorStoreId( donorStoreId );
            donationItem.setDonorStoreName( donorStoreName );
            donationItem.setSapId( sapId );
            donationItem.setEanCode( eanCode );
            donationItem.setArticleName( articleName );
            donationItem.setSector( sector );
            donationItem.setDonorReserveValue( donorReserveValue );
            donationItem.setDonorQuantityToZeroReserve( donorQuantityToZeroReserve );
            donationItem.setDonorStockQuantity( donorStockQuantity );
            donationItem.setDonorStockValue( donorStockValue );
            donationItem.setDonorCogs12M( donorCogs12M );
            donationItem.setTakerStoreId( takerStoreId );
            donationItem.setTakerStoreName( takerStoreName );
            donationItem.setTakerPlanoKind( takerPlanoKind );
            donationItem.setTakerTypeOfStokingUp( takerTypeOfStokingUp );
            donationItem.setTakerChoiceOfStokingUp( takerChoiceOfStokingUp );
            donationItem.setTakerStockQuantity( takerStockQuantity );
            donationItem.setTakerStockValue( takerStockValue );
            donationItem.setTakerSales12MValue( takerSales12MValue );
            donationItem.setTakerSales12MQuantity( takerSales12MQuantity );
            donationItem.setTakerRotation( takerRotation );
            return donationItem;
        }
    }
}
