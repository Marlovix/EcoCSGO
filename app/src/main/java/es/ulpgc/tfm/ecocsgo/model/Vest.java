package es.ulpgc.tfm.ecocsgo.model;

public class Vest extends Equipment{
    private OriginEquipment origin;

    public Vest(String key){
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
