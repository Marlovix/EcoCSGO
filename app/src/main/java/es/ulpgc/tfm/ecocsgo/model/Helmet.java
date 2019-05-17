package es.ulpgc.tfm.ecocsgo.model;

public class Helmet extends Equipment {
    private OriginEquipment origin;

    public Helmet(String key){
        super("utilities", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GEAR
        };
    }

    public OriginEquipment getOrigin() {
        return origin;
    }

    public void setOrigin(OriginEquipment origin) {
        this.origin = origin;
    }
}
