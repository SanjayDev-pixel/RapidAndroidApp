package com.finance.app.persistence.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

@Entity
class TempModel : Serializable {

    /** The unique ID of the Area. */
    @PrimaryKey
    var localId: String = UUID.randomUUID().toString()

    var val1: Long = 0
    var val2: String = ""
    var val3: Boolean = false
}
