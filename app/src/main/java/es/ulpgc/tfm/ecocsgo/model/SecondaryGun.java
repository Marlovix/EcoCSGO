package es.ulpgc.tfm.ecocsgo.model;

public class SecondaryGun extends Gun {
    public SecondaryGun(String key){
        super(key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.PISTOL
        };
    }
}
