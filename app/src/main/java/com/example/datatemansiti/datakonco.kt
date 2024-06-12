package com.example.datatemansiti

import androidx.constraintlayout.motion.widget.Key

class datakonco {
    var nama: String? = null
    var alamat: String? = null
    var no_hp: String? = null
    var key: String? = null

    constructor() {}

    constructor(nama: String?, alamat: String?, no_hp: String?) {
        this.nama = nama
        this.alamat = alamat
        this.no_hp = no_hp
    }

    constructor(nama: String?, alamat: String?, no_hp: String?, key: String?) {
        this.nama = nama
        this.alamat = alamat
        this.no_hp = no_hp
        this.key = key
}
}