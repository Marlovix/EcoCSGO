package es.ulpgc.tfm.ecocsgo

class Weapon(val name: String, val price: Int, val reward: Int) {
    override fun toString(): String {
        return "$name --> Price: $price Reward: $reward"
    }
}