package es.ulpgc.tfm.ecocsgo.model;

public class DefuseKit extends Equipment {
    private OriginEquipment origin;

    public DefuseKit(String key){
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
