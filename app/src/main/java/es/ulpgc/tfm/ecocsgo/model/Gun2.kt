package es.ulpgc.tfm.ecocsgo.model

class Gun2 {

    var course: String = ""
    var name: String = ""
    var percentile: String = ""
    var grades: HashMap<String, String>? = null

    constructor() {

    }

    constructor(course: String, name: String, percentile: String, grades: HashMap<String, String>) {
        this.course = course
        this.name = name
        this.percentile = percentile
        this.grades = grades
    }
}